package net.jangaroo.jooc;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Check if methods defined in interfaces are actually implemented in implementing classes.
 *
 * <a href="http://help.adobe.com/en_US/ActionScript/3.0_ProgrammingAS3/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f41.html">Adobe AS3 Docs</a>
 */
class ImplementedMembersAnalyzer {

  private Jooc jooc;
  private Collection<String> inspected = new HashSet<>();
  private Multimap<String, FunctionDeclaration> membersByInterfaceQName = LinkedHashMultimap.create();
  private Multimap<String, FunctionDeclaration> membersByClassQName = LinkedHashMultimap.create();

  ImplementedMembersAnalyzer(Jooc jooc) {
    this.jooc = jooc;
  }

  void analyzeImplementedMembers(CompilationUnit compilationUnit) {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    String qName = primaryDeclaration.getQualifiedNameStr();
    if (primaryDeclaration instanceof ClassDeclaration && inspected.add(qName)) {
      visitClassDeclaration((ClassDeclaration) primaryDeclaration);
    }
  }

  void visitClassDeclaration(ClassDeclaration classDeclaration) {
    String qName = classDeclaration.getQualifiedNameStr();
    boolean isInterface = classDeclaration.isInterface();

    Collection<TypedIdeDeclaration> toBeImplemented = new LinkedHashSet<>();

    Implements optImplements = classDeclaration.getOptImplements();
    if (null != optImplements) {
      for (CommaSeparatedList<Ide> localSuperTypes = optImplements.getSuperTypes(); localSuperTypes != null; localSuperTypes = localSuperTypes.getTail()) {
        String superClassQName = localSuperTypes.getHead().getDeclaration().getQualifiedNameStr();

        analyzeImplementedMembers(jooc.getCompilationUnit(superClassQName));

        Collection<FunctionDeclaration> superClassMembers = membersByInterfaceQName.get(superClassQName);
        if (isInterface) {
          // add inherited methods to current methods
          membersByInterfaceQName.putAll(qName, superClassMembers);
        } else {
          toBeImplemented.addAll(superClassMembers);
        }
      }
    }

    ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
    if (null != superTypeDeclaration) {
      String superClassQName = superTypeDeclaration.getQualifiedNameStr();
      analyzeImplementedMembers(jooc.getCompilationUnit(superClassQName));

      // add inherited methods to current methods
      membersByClassQName.putAll(qName, membersByClassQName.get(superClassQName));
    }

    ClassDeclaration classModel = (ClassDeclaration) jooc.resolveCompilationUnit(qName).getPrimaryDeclaration();
    Iterable<FunctionDeclaration> methodModels = Iterables.filter(classModel.getMembers(), FunctionDeclaration.class);

    if (isInterface) {
      membersByInterfaceQName.putAll(qName, methodModels);
    } else {

      FunctionDeclaration constructor = classModel.getConstructor();
      Iterable<FunctionDeclaration> publicNonStaticMethods = Iterables.filter(methodModels, Predicates.and(new Predicate<FunctionDeclaration>() {
        @Override
        public boolean apply(@Nullable FunctionDeclaration input) {
          //noinspection ConstantConditions
          return !input.isStatic() && input.isPublic();
        }
      }, Predicates.not(Predicates.equalTo(constructor))));

      membersByClassQName.putAll(qName, publicNonStaticMethods);
    }

    if (!isInterface) {
      Collection<FunctionDeclaration> implemented = membersByClassQName.get(qName);
      toBeImplemented.addAll(membersByInterfaceQName.get(qName));
      toBeImplemented.removeAll(implemented);
      if (!toBeImplemented.isEmpty()) {
        throw JangarooParser.error(optImplements, "Does not implement " + Iterables.transform(toBeImplemented, new Function<TypedIdeDeclaration, String>() {
          @Nullable
          @Override
          public String apply(@Nullable TypedIdeDeclaration input) {
            //noinspection ConstantConditions
            if (input instanceof FunctionDeclaration && ((FunctionDeclaration) input).isGetterOrSetter()) {
              return ((FunctionDeclaration)input).getSymGetOrSet().getText() + " " + input.getName();
            }
            return input == null ? null : input.getName();
          }
        }));
      }
    }
  }

}
