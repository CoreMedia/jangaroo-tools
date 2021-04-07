/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc.ast;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.DeclarationScope;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ClassDeclaration extends TypeDeclaration {

  private static final String OBJECT_CLASSNAME = "Object";

  private JooSymbol symClass;
  private Extends optExtends;
  private Map<String, TypedIdeDeclaration> members = new LinkedHashMap<>();
  private Map<String, TypedIdeDeclaration> staticMembers = new LinkedHashMap<>();
  private ClassBody body;
  private FunctionDeclaration constructor = null;
  private Type thisType;
  private Type superType;
  private List<VariableDeclaration> fieldsWithInitializer = new ArrayList<>();
  private List<IdeDeclaration> secondaryDeclarations = Collections.emptyList();
  private String qualifiedNameHash;

  private List<ClassDeclaration> assignableClasses;

  private Implements optImplements;
  private Scope scope;

  public ClassDeclaration(AnnotationsAndModifiers am, JooSymbol cls, Ide ide, Extends ext, Implements impl, ClassBody body) {
    super(am, ide);
    this.symClass = cls;
    this.optExtends = ext;
    this.optImplements = impl;
    this.body = body;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optExtends, optImplements, body);
  }

  @Override
  public JooSymbol getDeclarationSymbol() {
    return getSymClass();
  }

  public FunctionDeclaration getConstructor() {
    return constructor;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitClassDeclaration(this);
  }

  @Override
  protected int getAllowedModifiers() {
    return MODIFIER_ABSTRACT | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_DYNAMIC;
  }

  public boolean isInterface() {
    return "interface".equals(getSymClass().getText());
  }

  public boolean isAbstract() {
    return isInterface() || super.isAbstract();
  }

  @Override
  public boolean isStatic() {
    return super.isStatic() || !isPrimaryDeclaration(); // secondary classes are always static!
  }

  @Override
  public boolean isClassMember() {
    return super.isClassMember() || !isPrimaryDeclaration(); // secondary classes are (static) class members!
  }

  @Override
  public boolean isPrivate() {
    return super.isPrivate() || !isPrimaryDeclaration(); // secondary classes are considered private statics!
  }

  public String getName() {
    return getIde().getName();
  }

  public void setConstructor(FunctionDeclaration methodDeclaration) {
    if (constructor != null) {
      throw JangarooParser.error(methodDeclaration, "Only one constructor allowed per class");
    }
    constructor = methodDeclaration;
  }

  public JooSymbol getSymClass() {
    return symClass;
  }

  public Extends getOptExtends() {
    return optExtends;
  }

  public Implements getOptImplements() {
    return optImplements;
  }

  public List<VariableDeclaration> getFieldsWithInitializer() {
    return fieldsWithInitializer;
  }

  public ClassBody getBody() {
    return body;
  }

  public List<IdeDeclaration> getSecondaryDeclarations() {
    return secondaryDeclarations;
  }

  public Map<String, TypedIdeDeclaration> getStaticMembers() {
    return staticMembers;
  }

  @Override
  public void scope(final Scope scope) {
    this.scope = scope;
    // this declares this class's ide:
    super.scope(scope);

    // define these here so they get the right scope:
    thisType = new Type(new Ide(getIde().getSymbol()));
    fixDefaultSuperClass();
    superType = isInterface() || isObject(getQualifiedNameStr()) ? null
            : new Type(getOptExtends() == null ? new Ide(OBJECT_CLASSNAME) : getOptExtends().getSuperClass());

    thisType.scope(scope);
    if (superType != null) {
      superType.scope(scope);
    }
    if (getOptImplements() != null) {
      getOptImplements().scope(scope);
    }

    // one scope for static members...
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope staticScope) {
        // ...and one scope for instance members!
        withNewDeclarationScope(ClassDeclaration.this, staticScope, new Scoped() {
          @Override
          public void run(final Scope instanceScope) {
            VariableDeclaration thisDeclaration
                    = new VariableDeclaration(new JooSymbol("var"), new Ide(Ide.THIS), new TypeRelation(null, getThisType()));
            thisDeclaration.scope(instanceScope);
            //todo ugly, maybe we should define ClassScope implements Scope to lookup inherited members
            if(instanceScope instanceof  DeclarationScope) {
              ((DeclarationScope) instanceScope).setIsInstanceScope(true);
            }
            body.scope(staticScope, instanceScope);
          }
        });
        for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
          secondaryDeclaration.scope(staticScope); //todo is this the correct scope?!
        }
      }
    });
  }

  private void fixDefaultSuperClass() {
    // several plugins don't declare to extend ext.Base, but only implement ext.Plugin. Still, the use the config
    // system defined by ext.Base, so patch "extends ext.Base" in:
    if (optExtends == null && optImplements != null
            && "Plugin".equals(optImplements.getSuperTypes().getHead().getName())) {
      QualifiedIde extDotBase = new QualifiedIde(new Ide("ext"), new JooSymbol("."), new JooSymbol("Base"));
      new ImportDirective(null, extDotBase, null).scope(scope.getParentScope());
      JooSymbol extendsSymbol = new JooSymbol(sym.EXTENDS, "<generated>", -1, -1, " ", "extends");
      optExtends = new Extends(extendsSymbol, extDotBase);
    }
  }

  @Override
  public void handleDuplicateDeclaration(final Scope scope, final AstNode oldNode) {
    // allow same package import of this class
    if (!(oldNode instanceof ImportDirective)) { //todo check for same package import
      super.handleDuplicateDeclaration(scope, oldNode);
    }
  }

  public boolean implementsMoreThanOneInterface() {
    return getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null
            && getOptImplements() != null
            && (!(isInterface() || isMixin()) || getOptImplements().getSuperTypes().getTail() != null);
  }

  public void analyze(AstNode parentNode) {
    analyzeSymModifiers();
    super.analyze(parentNode);
    if (getOptExtends() != null) {
      getOptExtends().analyze(this);
    } else if (superType != null) {
      // establish dependency on and import of "Object" compilation unit:
      new Extends(null, superType.getIde()).analyze(this);
    }
    if (getOptImplements() != null) {
      getOptImplements().analyze(this);
    }
    if (implementsMoreThanOneInterface()) {
      scope.getCompilationUnit().addBuiltInIdentifierUsage("mixin");
    }
    body.analyze(this);
    for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
      secondaryDeclaration.analyze(this);
    }
  }

  /**
   * Check if interfaces have only modifiers public or internal as described
   * <a href="http://help.adobe.com/en_US/ActionScript/3.0_ProgrammingAS3/WS5b3ccc516d4fbf351e63e3d118a9b90204-7f41.html">here</a>.
   */
  private void analyzeSymModifiers() {
    if(isInterface()) {
      //noinspection LoopStatementThatDoesntLoop
      for (JooSymbol symModifier : getSymModifiers()) {
        switch (symModifier.sym) {
          case sym.PUBLIC: ;
          case sym.INTERNAL: break;
          default: throw JangarooParser.error(symModifier, "illegal modifier: " + symModifier.getText());
        }
      }
    }
  }

  public void registerMember(TypedIdeDeclaration memberDeclaration) {
    String name = memberDeclaration.getName();
    if (name.length() != 0) {
      Map<String, TypedIdeDeclaration> targetMembers = memberDeclaration.isStatic() ? staticMembers : members;
      TypedIdeDeclaration previousDeclaration = targetMembers.get(name);
      if (previousDeclaration instanceof FunctionDeclaration) {
        FunctionDeclaration previousFunctionDeclaration = (FunctionDeclaration) previousDeclaration;
        if (previousFunctionDeclaration.isGetterOrSetter()) {
          memberDeclaration = PropertyDeclaration.addDeclaration(previousFunctionDeclaration, memberDeclaration);
          if (memberDeclaration == null) {
            // TODO: handle all kinds of errors: two getters, two setters, other duplicate declarations
            // For now, ignore the new member.
            return;
          }
        }
      }

      targetMembers.put(name, memberDeclaration);
    }
  }

  @Override
  public TypedIdeDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  public Collection<TypedIdeDeclaration> getMembers() {
    return members.values();
  }

  public Collection<FunctionDeclaration> getMethods() {
    return FluentIterable.from(members.values()).transformAndConcat(new Function<TypedIdeDeclaration, Iterable<FunctionDeclaration>>() {
      @Nullable
      @Override
      public Iterable<FunctionDeclaration> apply(@Nullable TypedIdeDeclaration typedIdeDeclaration) {
        return typedIdeDeclaration instanceof FunctionDeclaration ? Collections.singleton((FunctionDeclaration) typedIdeDeclaration)
                : typedIdeDeclaration instanceof PropertyDeclaration ? ((PropertyDeclaration) typedIdeDeclaration).getMethods()
                : Collections.emptyList();
      }
    }).toList();
  }

  @Override
  public TypedIdeDeclaration getStaticMemberDeclaration(String memberName) {
    return staticMembers.get(memberName);
  }

  public boolean isMixin() {
    if (isInterface()) {
      return getAnnotation(Jooc.MIXIN_ANNOTATION_NAME) != null;
    }
    return getMyMixinInterface() != null;
  }

  public ClassDeclaration getMyMixinInterface() {
    if (!isInterface() && getOptImplements() != null) {
      CommaSeparatedList<Ide> interfaces = getOptImplements().getSuperTypes();
      String myQualifiedName = getQualifiedNameStr();
      while (interfaces != null) {
        Ide oneInterface = interfaces.getHead();
        ClassDeclaration interfaceDeclaration = (ClassDeclaration) oneInterface.getScope().lookupDeclaration(oneInterface);
        Annotation mixinAnnotation = interfaceDeclaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME);
        if (mixinAnnotation != null && myQualifiedName.equals(mixinAnnotation.getPropertiesByName().get(null))) {
          return interfaceDeclaration;
        }
        interfaces = interfaces.getTail();
      }
    }
    return null;
  }

  public ClassDeclaration getConfigClassDeclaration() {
    // special cases: ext.Base and all Mixin implementations use Ext Config system, although
    // they don't have the corresponding constructor
    if ("ext.Base".equals(getQualifiedNameStr()) || isMixin()) {
      return this;
    }
    FunctionDeclaration constructor = getConstructor();
    if (constructor != null) {
      Parameters params = constructor.getParams();
      while (params != null) {
        Parameter param = params.getHead();
        if ("config".equals(param.getName()) && param.getOptTypeRelation() != null) {
          TypeDeclaration declaration = param.getOptTypeRelation().getType().getDeclaration();
          if (equals(declaration)
                  // some MXML base classes do not use their own config type, but the one of their MXML subclass :(
                  || equals(declaration.getSuperTypeDeclaration())) {
            return (ClassDeclaration) declaration;
          }
        }
        params = params.getTail();
      }
    }
    return null;
  }

  public boolean hasConfigClass() {
    return getConfigClassDeclaration() != null;
  }

  public boolean isSubclassOf(final ClassDeclaration classDeclaration) {
    ClassDeclaration superTypeDeclaration = getSuperTypeDeclaration();
    return superTypeDeclaration != null &&
            (superTypeDeclaration == classDeclaration || superTypeDeclaration.isSubclassOf(classDeclaration)); // NOSONAR no equals here
  }

  public boolean isJavaScriptObject() {
    for (ClassDeclaration classDeclaration = this;
         classDeclaration != null;
         classDeclaration = classDeclaration.getSuperTypeDeclaration()) {
      if ("joo.JavaScriptObject".equals(classDeclaration.getQualifiedNameStr())) {
        return true;
      }
    }
    return false;
  }

  public Type getThisType() {
    return thisType;
  }

  public Type getSuperType() {
    return superType;
  }

  public void setSecondaryDeclarations(List<IdeDeclaration> secondaryDeclarations) {
    this.secondaryDeclarations = secondaryDeclarations;
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return null;
  }

  public IdeDeclaration resolvePropertyDeclaration(String ide, boolean isStatic) {
    IdeDeclaration declaration = null;
    FunctionDeclaration getterOrSetter = null;
    ensureAssignableClasses();
    for (ClassDeclaration classDecl: assignableClasses) {
      declaration = isStatic ? classDecl.getStaticMemberDeclaration(ide) : classDecl.getMemberDeclaration(ide);
      if (getterOrSetter == null) {
        // we are still looking for getter or setter:
        if (declaration instanceof FunctionDeclaration && ((FunctionDeclaration)declaration).isGetterOrSetter()) {
          // found a getter or setter; remember it:
          getterOrSetter = (FunctionDeclaration) declaration;
          // keep on searching for the complementing setter or getter:
          declaration = null;
        }
      } else {
        // we already found a getter or setter...
        if (declaration != null) {
          // ...and now found another declaration, so try to merge both:
          declaration = PropertyDeclaration.addDeclaration(getterOrSetter, declaration);
        }
      }
      if (declaration != null) {
        return declaration;
      }
    }
    return getterOrSetter;
  }


   private void resolveAssignablesDeclaration1(ClassDeclaration classDecl, List<ClassDeclaration> result, Deque<ClassDeclaration> chain) {
    if (result.contains(classDecl)) {
      return;
    }
    result.add(classDecl);
    final int chainSize = chain.size();
    chain.add(classDecl);

    IdeDeclaration superTypeDeclaration = null;
    if (!classDecl.isInterface()) {
      if (classDecl.getSuperType() != null) {
        // used to be based on Type, now it can lead to ClassDeclarations not yet available to be loaded twice
        superTypeDeclaration = classDecl.getSuperType().getIde().getDeclaration(false);
      }
    } else if (classDecl.getOptImplements() == null) {
      // it is a top-level interface: simulate inheritance from Object!
      superTypeDeclaration = classDecl.getIde().getScope().getExpressionType(AS3Type.OBJECT).getDeclaration();
    }
    if (superTypeDeclaration != null) {
      resolveAssignablesInSuper(result, chain, superTypeDeclaration);
    }
    if (classDecl.getOptImplements() != null) {
      CommaSeparatedList<Ide> implemented = classDecl.getOptImplements().getSuperTypes();
      while (implemented != null) {
        resolveAssignablesInSuper(result, chain, implemented.getHead().getDeclaration(false));
        implemented = implemented.getTail();
      }
    }
    chain.removeLast();
    assert chainSize == chain.size();
  }

  private void resolveAssignablesInSuper(final List<ClassDeclaration> visited,
                                                final Deque<ClassDeclaration> chain,
                                                final IdeDeclaration superClassDecl) {
    if (superClassDecl != null) {
      if (!(superClassDecl instanceof ClassDeclaration)) {
        throw new CompilerError(superClassDecl.getSymbol(), "expected class identifier");
      }
      resolveAssignablesDeclaration1((ClassDeclaration) superClassDecl, visited, chain);
    }
  }

  public boolean isAssignableTo(ClassDeclaration classToCheck) {
    ensureAssignableClasses();

    // TODO should use "|| assignableClasses.contains(classToCheck)" and not iterate over the list
    boolean result = getQualifiedNameStr().equals(classToCheck.getQualifiedNameStr());

    Iterator<ClassDeclaration> iterator = assignableClasses.iterator();
    while(!result && iterator.hasNext()) {
      result = iterator.next().getQualifiedNameStr().equals(classToCheck.getQualifiedNameStr());
    }
    return result;
  }

  private void ensureAssignableClasses() {
    if (assignableClasses == null) {
      assignableClasses = new ArrayList<>();
      resolveAssignablesDeclaration1(this, assignableClasses, new LinkedList<ClassDeclaration>());
    }
  }

  public boolean notExtendsObject() {
    return superType != null && !isObject(superType.getIde().getQualifiedNameStr());
  }

  public String getQualifiedNameHash() {
    if (qualifiedNameHash == null) {
      qualifiedNameHash = computeQualifiedNameHash();
    }
    return qualifiedNameHash;
  }

  private static final String BASE_64_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_$";

  private static String base64Char(int n) {
    int i = n & 63;
    return BASE_64_CHARS.substring(i, i + 1);
  }

  private String computeQualifiedNameHash() {
    int hashCode = getTargetQualifiedNameStr().hashCode();
    return  base64Char(hashCode >>> 18) +
            base64Char(hashCode >>> 12) +
            base64Char(hashCode >>> 6) +
            base64Char(hashCode);
  }

  @Override
  public ClassDeclaration getSuperTypeDeclaration() {
    return superType == null ? null : (ClassDeclaration) superType.getDeclaration();
  }

  public List<ClassDeclaration> getSuperTypeDeclarations() {
    List<ClassDeclaration> superTypeDeclarations = new ArrayList<>();
    if (getSuperTypeDeclaration() != null) {
      superTypeDeclarations.add(getSuperTypeDeclaration());
    }
    Implements optImplements = getOptImplements();
    if (optImplements != null) {
      CommaSeparatedList<Ide> superTypes = optImplements.getSuperTypes();
      while (superTypes != null) {
        superTypeDeclarations.add((ClassDeclaration) getIde().getScope().lookupDeclaration(superTypes.getHead()));
        superTypes = superTypes.getTail();
      }
    }
    return superTypeDeclarations;
  }

  public void addFieldWithInitializer(VariableDeclaration fieldDeclaration) {
    fieldsWithInitializer.add(fieldDeclaration);
  }

  private static boolean isObject(String fullyQualifiedName) {
    return OBJECT_CLASSNAME.equals(fullyQualifiedName);
  }

  public boolean isObject() {
    return isObject(getQualifiedNameStr());
  }
}
