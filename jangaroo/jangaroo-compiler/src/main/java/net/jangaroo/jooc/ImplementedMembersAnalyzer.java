package net.jangaroo.jooc;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.NamespacedModel;
import net.jangaroo.jooc.model.ParamModel;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Check if methods defined in interfaces are actually implemented in implementing classes.
 *
 * <a href="http://help.adobe.com/en_US/ActionScript/3.0_ProgrammingAS3/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f41.html">Adobe AS3 Docs</a>
 */
class ImplementedMembersAnalyzer {

  private Jooc jooc;
  private Collection<String> inspected = new HashSet<>();
  private Multimap<String, MethodModel> membersByInterfaceQName = LinkedHashMultimap.create();
  private Multimap<String, MethodModel> membersByClassQName = LinkedHashMultimap.create();

  ImplementedMembersAnalyzer(Jooc jooc) {
    this.jooc = jooc;
  }

  void analzyeImplementedMembers(CompilationUnit compilationUnit) {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    String qName = primaryDeclaration.getQualifiedNameStr();
    if (primaryDeclaration instanceof ClassDeclaration && inspected.add(qName)) {
      visitClassDeclaration((ClassDeclaration) primaryDeclaration);
    }
  }

  void visitClassDeclaration(ClassDeclaration classDeclaration) {
    String qName = classDeclaration.getQualifiedNameStr();
    boolean isInterface = classDeclaration.isInterface();

    Collection<MethodModel> toBeImplemented = new LinkedHashSet<>();

    Implements optImplements = classDeclaration.getOptImplements();
    if (null != optImplements) {
      for (CommaSeparatedList<Ide> localSuperTypes = optImplements.getSuperTypes(); localSuperTypes != null; localSuperTypes = localSuperTypes.getTail()) {
        Ide interfaceIde = localSuperTypes.getHead();
        String superClassQName = getClassQName(interfaceIde);

        analzyeImplementedMembers(jooc.getCompilationUnit(superClassQName));

        Collection<MethodModel> superClassMembers = membersByInterfaceQName.get(superClassQName);
        if (isInterface) {
          // add inherited methods to current methods
          membersByInterfaceQName.putAll(qName, superClassMembers);
        } else {
          toBeImplemented.addAll(superClassMembers);
        }
      }
    }

    Extends optExtends = classDeclaration.getOptExtends();
    if (null != optExtends) {
      String superClassQName = getClassQName(optExtends.getSuperClass());
      analzyeImplementedMembers(jooc.getCompilationUnit(superClassQName));

      // add inherited methods to current methods
      membersByClassQName.putAll(qName, membersByClassQName.get(superClassQName));
    }

    ClassModel classModel = jooc.resolveCompilationUnit(qName).getClassModel();
    Iterable<MethodModel> methodModels = Iterables.filter(classModel.getMembers(), MethodModel.class);

    Scope scope = classDeclaration.getIde().getScope();
    if (isInterface) {
      membersByInterfaceQName.putAll(qName, fullyQualifiedTypes(methodModels, scope));
    } else {

      MethodModel constructor = classModel.getConstructor();
      Iterable<MethodModel> publicNonStaticMethods = Iterables.filter(methodModels, Predicates.and(new Predicate<MethodModel>() {
        @Override
        public boolean apply(@Nullable MethodModel input) {
          //noinspection ConstantConditions
          return !input.isStatic() && NamespacedModel.PUBLIC.equals(input.getNamespace());
        }
      }, Predicates.not(Predicates.equalTo(constructor))));

      membersByClassQName.putAll(qName, fullyQualifiedTypes(publicNonStaticMethods, scope));
    }

    if (!isInterface) {
      Collection<MethodModel> implemented = membersByClassQName.get(qName);
      toBeImplemented.addAll(membersByInterfaceQName.get(qName));
      toBeImplemented.removeAll(Lists.newLinkedList(fullyQualifiedTypes(implemented, scope)));
      if (!toBeImplemented.isEmpty()) {
        throw JangarooParser.error(optImplements, "Does not implement " + Iterables.transform(toBeImplemented, new Function<MemberModel, String>() {
          @Nullable
          @Override
          public String apply(@Nullable MemberModel input) {
            //noinspection ConstantConditions
            return input.getName();
          }
        }));
      }
    }
  }

  private Iterable<MethodModel> fullyQualifiedTypes(Iterable<MethodModel> methodModels, final Scope scope) {
    return Iterables.transform(methodModels, new TypeAndParameterTransformer(scope));
  }

  private String getClassQName(Ide classIde) {
    String superClassQName;
    if (classIde instanceof QualifiedIde) {
      superClassQName = classIde.getQualifiedNameStr();
    } else {
      Scope scope = classIde.getScope();
      superClassQName = getClassQName(classIde, scope);
    }
    return superClassQName;
  }

  private String getClassQName(Ide classIde, Scope scope) {
    IdeDeclaration ideDeclaration = scope.lookupDeclaration(classIde);
    if(null != ideDeclaration) {
      return ideDeclaration.getQualifiedNameStr();
    }
    return classIde.getQualifiedNameStr();
  }

  private class TypeAndParameterTransformer implements Function<MethodModel, MethodModel> {
    private final Scope scope;
    private final ParamModelTypeTransformer  paramModelTypeTransformer;

    TypeAndParameterTransformer(Scope scope) {
      this.scope = scope;
      paramModelTypeTransformer = new ParamModelTypeTransformer(scope);
    }

    @Nullable
    @Override
    public MethodModel apply(@Nullable MethodModel input) {
      if(null != input) {
        String type = input.getType();
        if (null != type && !type.contains(".")) {
          type = getClassQName(new Ide(type), scope);
        }
        List<ParamModel> params = Lists.transform(input.getParams(), paramModelTypeTransformer);
        return new MethodModel(input.getName(), type, params);
      }
      return input;
    }

  }

  private class ParamModelTypeTransformer implements Function<ParamModel, ParamModel> {

    private final Scope scope;

    ParamModelTypeTransformer(Scope scope) {
      this.scope = scope;
    }

    @Nullable
    @Override
    public ParamModel apply(@Nullable ParamModel input) {
      if (null != input) {
        String type = input.getType();
        if (null != type && !type.contains(".")) {
          ParamModel paramModel = input.duplicate();
          paramModel.setType(getClassQName(new Ide(type), scope));
          return paramModel;
        }
      }
      return input;
    }
  }
}
