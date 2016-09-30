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
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
  private int inheritanceLevel = -1;

  private List<ClassDeclaration> assignableClasses;

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

  @Override
  public void handleDuplicateDeclaration(final Scope scope, final AstNode oldNode) {
    // allow same package import of this class
    if (!(oldNode instanceof ImportDirective)) { //todo check for same package import
      super.handleDuplicateDeclaration(scope, oldNode);
    }
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
      (memberDeclaration.isStatic() ? staticMembers : members).put(name, memberDeclaration);
    }
  }

  @Override
  public TypedIdeDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  @Override
  public TypedIdeDeclaration getStaticMemberDeclaration(String memberName) {
    return staticMembers.get(memberName);
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

  public IdeDeclaration resolvePropertyDeclaration(String ide, boolean isStatic) {
    IdeDeclaration declaration = null;
    ensureAssignableClasses();
    for (ClassDeclaration classDecl: assignableClasses) {
      declaration = isStatic ? classDecl.getStaticMemberDeclaration(ide) : classDecl.getMemberDeclaration(ide);
      if (declaration != null) {
        break;
      }
    }
    return declaration;
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
    if (isObject(superType.getIde().getQualifiedNameStr())) {
      return 1;
    }
    TypeDeclaration superClassDecl = superType.getDeclaration();
    if (!(superClassDecl instanceof ClassDeclaration)) {
      throw new CompilerError(getOptExtends().getSuperClass().getSymbol(), "expected class identifier");
    }
    return 1 + ((ClassDeclaration) superClassDecl).getInheritanceLevel();
  }


  @Override
  public ClassDeclaration getSuperTypeDeclaration() {
    return superType == null ? null : (ClassDeclaration) superType.getDeclaration();
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
