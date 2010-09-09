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

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.*;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ClassDeclaration extends IdeDeclaration {

  private List<AstNode> directives;
  protected JooSymbol symClass;
  protected Extends optExtends;
  private Map<String, TypedIdeDeclaration> members = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Map<String, TypedIdeDeclaration> staticMembers = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Set<String> classInit = new HashSet<String>();
  private ClassBody body;
  private FunctionDeclaration constructor = null;
  private IdeType thisType;
  private IdeType superType;
  private List<FieldDeclaration> fieldsWithInitializer = new ArrayList<FieldDeclaration>();
  private List<IdeDeclaration> secondaryDeclarations = Collections.emptyList();

  public Extends getOptExtends() {
    return optExtends;
  }

  protected Implements optImplements;

  public ClassBody getBody() {
    return body;
  }

  public FunctionDeclaration getConstructor() {
    return constructor;
  }

  public FunctionDeclaration getConstructorDeclaration() {
    return constructor;
  }

  public ClassDeclaration(List<AstNode> directives, JooSymbol[] modifiers, JooSymbol cls, Ide ide, Extends ext, Implements impl, ClassBody body) {
    super(modifiers,
      MODIFIER_ABSTRACT | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_DYNAMIC,
      ide);
    this.directives = directives;
    this.symClass = cls;
    this.optExtends = ext;
    this.optImplements = impl;
    this.body = body;
  }

  public boolean isInterface() {
    return "interface".equals(symClass.getText());
  }

  public boolean isAbstract() {
    return isInterface() || super.isAbstract();
  }

  @Override
  public boolean isPrivate() {
    return super.isPrivate() || !isPrimaryDeclaration(); // secondary classes are always private!
  }

  @Override
  public boolean isStatic() {
    return super.isStatic() || !isPrimaryDeclaration(); // secondary classes are always static!
  }

  @Override
  public boolean isClassMember() {
    return super.isClassMember() || !isPrimaryDeclaration(); // secondary classes are private static class members!
  }

  @Override
  public String[] getQualifiedName() {
    return  isPrimaryDeclaration()
      ? super.getQualifiedName()
      : new String[]{"$$private", getName()}; // secondary class is in static private scope!
  }

  public String getName() {
    return ide.getName();
  }

  public void setConstructor(FunctionDeclaration methodDeclaration) {
    if (constructor != null) {
      throw Jooc.error(methodDeclaration, "Only one constructor allowed per class");
    }
//     if (methodDeclaration != body.declararations.get(0))
//       Jooc.error(methodDeclaration, "Constructor declaration must be the first declaration in a class");
    constructor = methodDeclaration;
  }

  protected void generateAsApiCode(JsWriter out) throws IOException {
    generateCode(directives, out);

    writeModifiers(out);
    out.writeSymbol(symClass);
    ide.generateCode(out);
    if (optExtends != null) {
      optExtends.generateCode(out);
    }
    if (optImplements != null) {
      optImplements.generateCode(out);
    }
    body.generateCode(out);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginComment();
    generateCode(directives, out);
    out.endComment();
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(symClass);
    ide.generateCode(out);
    if (optExtends != null) {
      optExtends.generateCode(out);
    }
    if (optImplements != null) {
      optImplements.generateCode(out);
    }
    out.endString();
    out.write(",");
    out.write("function($$l,$$private){var is=joo.is,assert=joo.assert,trace=joo.trace,$$bound=joo.boundMethod,$super=$$l+'super'");
    for (TypedIdeDeclaration member : members.values()) {
      if (member.isPrivate() || member.isOverride()) {
        out.write(",$" + member.getName() + "=$$l+'" + member.getName() + "'");
      }
    }
    out.write(";return[");
    generateClassInits(out);
    body.generateCode(out);
    if (constructor == null && !fieldsWithInitializer.isEmpty()) {
      // generate default constructor that calls field initializers:
      out.write("\"public function " + getName() + "\",function $" + getName() + "(){this[$super]();");
      generateFieldInitCode(out);
      out.write("}");
    }

    for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
      secondaryDeclaration.generateJsCode(out);
      out.writeToken(",");
    }

    out.write("];},");
    generateStaticMethodList(out);
  }

  private void generateClassInits(JsWriter out) throws IOException {
    boolean first = true;
    for (String qualifiedNameStr : classInit) {
      if (first) {
        first = false;
        out.write("function(){" + Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".init(");
      } else {
        out.write(",");
      }
      out.write(qualifiedNameStr);
    }
    if (!first) {
      out.write(");},");
    }
  }

  private void generateStaticMethodList(JsWriter out) throws IOException {
    out.write("[");
    boolean isFirst = true;
    for (TypedIdeDeclaration memberDeclaration : staticMembers.values()) {
      if (memberDeclaration.isMethod() && !memberDeclaration.isPrivate() && !memberDeclaration.isProtected() && memberDeclaration.isStatic() && !memberDeclaration.isNative()) {
        if (isFirst) {
          isFirst = false;
        } else {
          out.write(",");
        }
        out.write('"');
        out.write(memberDeclaration.getName());
        out.write('"');
      }
    }
    out.write("]");
  }

  void scopeDirectives(Scope scope, Ide packageIde) {
    if (packageIde != null)
      addStarImport(packageIde);
    // add implicit toplevel package import
    addStarImport(null);
    scope(directives, scope);
  }

  @Override
  public void scope(final Scope scope) {
    // this declares this class's ide:
    super.scope(scope);

    // define these here so they get the right scope:
    thisType = new IdeType(new Ide(getIde().getSymbol()));
    superType = "Object".equals(getQualifiedNameStr()) ? null
      : new IdeType(optExtends == null ? new Ide("Object") : optExtends.superClass);

    thisType.scope(scope);
    if (superType != null) {
      superType.scope(scope);
    }
    if (optImplements != null) {
      optImplements.scope(scope);
    }

    // one scope for static members...
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        // ...and one scope for instance members!
        withNewDeclarationScope(ClassDeclaration.this, scope, new Scoped() {
          @Override
          public void run(final Scope scope) {
            for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
              secondaryDeclaration.scope(scope);
            }
            body.scope(scope);
          }
        });
      }
    });
  }

  @Override
  void handleDuplicateDeclaration(final Scope scope, final AstNode oldNode) {
    // allow same package import of this class
    if (!(oldNode instanceof ImportDirective)) { //todo check for same package import
      super.handleDuplicateDeclaration(scope, oldNode);
    }
  }

  void addStarImport(final Ide packageIde) {
    ImportDirective importDirective = new ImportDirective(packageIde, "*");
    directives.add(0, importDirective);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    this.directives = analyze(this, this.directives, context);
    super.analyze(parentNode, context);
    if (optExtends != null) {
      optExtends.analyze(this, context);
    }
    if (optImplements != null) {
      optImplements.analyze(this, context);
    }
    body.analyze(this, context);
    return this;
  }

  public void registerMember(TypedIdeDeclaration memberDeclaration) {
    (memberDeclaration.isStatic() ? staticMembers : members).put(memberDeclaration.getName(), memberDeclaration);
  }

  public TypedIdeDeclaration getMemberDeclaration(String memberName) {
    return members.get(memberName);
  }

  public TypedIdeDeclaration getStaticMemberDeclaration(String memberName) {
    return staticMembers.get(memberName);
  }

  public void addInitIfClass(Ide ide) {
    final IdeDeclaration decl = ide.getDeclaration(false);
    if (decl != null && decl != this && decl instanceof ClassDeclaration) {
      classInit.add(decl.getQualifiedNameStr());
    }
  }

  public boolean isSubclassOf(final ClassDeclaration classDeclaration) {
    ClassDeclaration superTypeDeclaration = getSuperTypeDeclaration();
    return superTypeDeclaration != null &&
      (superTypeDeclaration == classDeclaration || superTypeDeclaration.isSubclassOf(classDeclaration));
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
    return this;
  }

  /**
   * Lookup a non-static member of the given name
   * @param ide the member name
   * @return a non-static member if found, null otherwise
   */
  public IdeDeclaration resolvePropertyDeclaration(String ide) {
    return resolvePropertyDeclaration1(ide, this, new HashSet<ClassDeclaration>(), new LinkedList<ClassDeclaration>());
  }

  private IdeDeclaration resolvePropertyDeclaration1(String ide, ClassDeclaration classDecl, Set<ClassDeclaration> visited, LinkedList<ClassDeclaration> chain) {
    if (visited.contains(classDecl)) {
      if (chain.contains(classDecl)) {
        throw new Jooc.CompilerError(classDecl.getSymbol(), "cyclic syperclass chain");
      }
      return null;
    }
    visited.add(classDecl);
    final int chainSize = chain.size();
    chain.add(classDecl);
    IdeDeclaration declaration = classDecl.getMemberDeclaration(ide);
    if (declaration == null) {
      declaration = classDecl.getStaticMemberDeclaration(ide);
    }
    if (declaration == null && classDecl.optExtends != null) {
      declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, classDecl.optExtends.superClass);
    }
    if (declaration == null && classDecl.optImplements != null) {
      CommaSeparatedList<Ide> implemented = classDecl.optImplements.superTypes;
      while (implemented != null && declaration == null) {
        declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, implemented.head);
        implemented = implemented.tail;
      }
    }
    chain.removeLast();
    assert chainSize == chain.size();
    return declaration;
  }

  private IdeDeclaration resolvePropertyInSuper(final String ide,
                                              final ClassDeclaration classDecl,
                                              final Set<ClassDeclaration> visited,
                                              final LinkedList<ClassDeclaration> chain,
                                              final Ide superIde) {
    IdeDeclaration superClassDecl = superIde.getDeclaration();
    if (superClassDecl != null)
      if (!(superClassDecl instanceof ClassDeclaration)) {
        throw new Jooc.CompilerError(classDecl.optExtends.superClass.getSymbol(), "expected class identifier");
      }
    return resolvePropertyDeclaration1(ide, (ClassDeclaration) superClassDecl, visited, chain);
  }


  public ClassDeclaration getSuperTypeDeclaration() {
    return superType == null ? null : (ClassDeclaration)superType.ide.getDeclaration();
  }

  public void addFieldWithInitializer(FieldDeclaration fieldDeclaration) {
    fieldsWithInitializer.add(fieldDeclaration);
  }

  public void generateFieldInitCode(JsWriter out) throws IOException {
    for (FieldDeclaration field : fieldsWithInitializer) {
      field.generateInitCode(out);
    }
  }
}
