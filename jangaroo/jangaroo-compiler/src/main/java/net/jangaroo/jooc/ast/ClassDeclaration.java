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

import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.DeclarationScope;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ClassDeclaration extends IdeDeclaration {

  private JooSymbol symClass;
  private Extends optExtends;
  private Map<String, TypedIdeDeclaration> members = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Map<String, TypedIdeDeclaration> staticMembers = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Set<String> classInit = new LinkedHashSet<String>();
  private ClassBody body;
  private FunctionDeclaration constructor = null;
  private Type thisType;
  private Type superType;
  private List<VariableDeclaration> fieldsWithInitializer = new ArrayList<VariableDeclaration>();
  private List<IdeDeclaration> secondaryDeclarations = Collections.emptyList();
  private int inheritanceLevel = -1;

  private Implements optImplements;
  private Scope scope;

  public ClassDeclaration(JooSymbol[] modifiers, JooSymbol cls, Ide ide, Extends ext, Implements impl, ClassBody body) {
    super(modifiers, ide);
    this.symClass = cls;
    this.optExtends = ext;
    this.optImplements = impl;
    this.body = body;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optExtends, optImplements, body);
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
//     if (methodDeclaration != body.declararations.get(0))
//       JangarooParser.error(methodDeclaration, "Constructor declaration must be the first declaration in a class");
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

  public Set<String> getClassInit() {
    return classInit;
  }

  @Override
  public void scope(final Scope scope) {
    this.scope = scope;
    // this declares this class's ide:
    super.scope(scope);

    // define these here so they get the right scope:
    thisType = new Type(new Ide(getIde().getSymbol()));
    superType = "Object".equals(getQualifiedNameStr()) ? null
            : new Type(getOptExtends() == null ? new Ide("Object") : getOptExtends().getSuperClass());

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

  @Override
  public void handleDuplicateDeclaration(final Scope scope, final AstNode oldNode) {
    // allow same package import of this class
    if (!(oldNode instanceof ImportDirective)) { //todo check for same package import
      super.handleDuplicateDeclaration(scope, oldNode);
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (getOptExtends() != null) {
      getOptExtends().analyze(this);
      String packageName = getOptExtends().getSuperClass().getDeclaration().getPackageDeclaration().getQualifiedNameStr();
      if (packageName.length() > 0 && parentNode instanceof CompilationUnit) {
        ((CompilationUnit)parentNode).getAuxVarForPackage(scope, packageName);
      }
    }
    if (getOptImplements() != null) {
      getOptImplements().analyze(this);
    }
    body.analyze(this);
    for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
      secondaryDeclaration.analyze(this);
    }
  }

  public void registerMember(TypedIdeDeclaration memberDeclaration) {
    String name = memberDeclaration.getName();
    if (name.length() != 0) {
      (memberDeclaration.isStatic() ? staticMembers : members).put(name, memberDeclaration);
    }
  }

  public TypedIdeDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  public TypedIdeDeclaration getStaticMemberDeclaration(String memberName) {
    return staticMembers.get(memberName);
  }

  public void addInitIfGlobalVar(Ide ide) {
    addInitIf(ide, false);
  }

  public void addInitIfClassOrGlobalVar(Ide ide) {
    addInitIf(ide, true);
  }

  private void addInitIf(Ide ide, boolean allowClass) {
    final IdeDeclaration decl = ide.getDeclaration(false);
    if (decl != this      // Classes should not try to init themselves. It does not help and it produces strange warnings.
      && (allowClass && decl instanceof ClassDeclaration || decl instanceof VariableDeclaration) // no init necessary for package-scope functions!
      && decl.isPrimaryDeclaration()) {
      CompilationUnit compilationUnit = decl.getIde().getScope().getCompilationUnit();
      if (compilationUnit.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null) {
        classInit.add(decl.getQualifiedNameStr());
      }
    }
  }

  public boolean isSubclassOf(final ClassDeclaration classDeclaration) {
    ClassDeclaration superTypeDeclaration = getSuperTypeDeclaration();
    return superTypeDeclaration != null &&
            (superTypeDeclaration == classDeclaration || superTypeDeclaration.isSubclassOf(classDeclaration)); // NOSONAR no equals here
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

  /**
   * Lookup a non-static member of the given name
   *
   * @param ide the member name
   * @return a non-static member if found, null otherwise
   */
  public IdeDeclaration resolvePropertyDeclaration(String ide) {
    IdeDeclaration ideDeclaration = resolvePropertyDeclaration1(ide, this, new HashSet<ClassDeclaration>(), new LinkedList<ClassDeclaration>(), false);
    if (ideDeclaration == null) {
      ideDeclaration = resolvePropertyDeclaration1(ide, this, new HashSet<ClassDeclaration>(), new LinkedList<ClassDeclaration>(), true);
    }
    return ideDeclaration;
  }

  private IdeDeclaration resolvePropertyDeclaration1(String ide, ClassDeclaration classDecl, Set<ClassDeclaration> visited, Deque<ClassDeclaration> chain, boolean inStatic) {
    if (visited.contains(classDecl)) {
      if (chain.contains(classDecl)) {
        throw new CompilerError(classDecl.getSymbol(), "cyclic superclass chain");
      }
      return null;
    }
    visited.add(classDecl);
    final int chainSize = chain.size();
    chain.add(classDecl);
    IdeDeclaration declaration = inStatic ? classDecl.getStaticMemberDeclaration(ide) : classDecl.getMemberDeclaration(ide);
    if (declaration == null && classDecl.getSuperType() != null) {
      declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, classDecl.getSuperType().getIde(), inStatic);
    }
    if (declaration == null && classDecl.getOptImplements() != null) {
      CommaSeparatedList<Ide> implemented = classDecl.getOptImplements().getSuperTypes();
      while (implemented != null && declaration == null) {
        declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, implemented.getHead(), inStatic);
        implemented = implemented.getTail();
      }
    }
    chain.removeLast();
    assert chainSize == chain.size();
    return declaration;
  }

  private IdeDeclaration resolvePropertyInSuper(final String ide,
                                                final ClassDeclaration classDecl,
                                                final Set<ClassDeclaration> visited,
                                                final Deque<ClassDeclaration> chain,
                                                final Ide superIde, boolean inStatic) {
    IdeDeclaration superClassDecl = superIde.getDeclaration(false);
    if (superClassDecl != null) {
      if (!(superClassDecl instanceof ClassDeclaration)) {
        throw new CompilerError(classDecl.getOptExtends().getSuperClass().getSymbol(), "expected class identifier");
      }
      return resolvePropertyDeclaration1(ide, (ClassDeclaration) superClassDecl, visited, chain, inStatic);
    }
    return null;
  }

  public int getInheritanceLevel() {
    if (inheritanceLevel < 0) {
      inheritanceLevel = computeInheritanceLevel();
    }
    return inheritanceLevel;
  }

  private int computeInheritanceLevel() {
    if (superType == null) {
      return 0;
    }
    if ("Object".equals(superType.getIde().getQualifiedNameStr())) {
      return 1;
    }
    IdeDeclaration superClassDecl = superType.getIde().getDeclaration();
    if (!(superClassDecl instanceof ClassDeclaration)) {
      throw new CompilerError(getOptExtends().getSuperClass().getSymbol(), "expected class identifier");
    }
    return 1 + ((ClassDeclaration) superClassDecl).getInheritanceLevel();
  }


  public ClassDeclaration getSuperTypeDeclaration() {
    return superType == null ? null : (ClassDeclaration) superType.getIde().getDeclaration();
  }

  public void addFieldWithInitializer(VariableDeclaration fieldDeclaration) {
    fieldsWithInitializer.add(fieldDeclaration);
  }
}
