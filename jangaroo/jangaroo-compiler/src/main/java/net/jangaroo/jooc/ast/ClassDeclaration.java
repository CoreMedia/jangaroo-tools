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

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.DeclarationScope;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.*;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ClassDeclaration extends IdeDeclaration {

  private List<AstNode> directives;
  private JooSymbol symClass;
  private Extends optExtends;
  private Map<String, TypedIdeDeclaration> members = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Map<String, TypedIdeDeclaration> staticMembers = new LinkedHashMap<String, TypedIdeDeclaration>();
  private Set<String> classInit = new HashSet<String>();
  private ClassBody body;
  private FunctionDeclaration constructor = null;
  private IdeType thisType;
  private IdeType superType;
  private List<VariableDeclaration> fieldsWithInitializer = new ArrayList<VariableDeclaration>();
  private List<IdeDeclaration> secondaryDeclarations = Collections.emptyList();
  private Set<String> usedBuiltIns = new LinkedHashSet<String>();
  private int inheritanceLevel = -1;

  private Implements optImplements;

  public ClassDeclaration(List<AstNode> directives, JooSymbol[] modifiers, JooSymbol cls, Ide ide, Extends ext, Implements impl, ClassBody body) {
    super(modifiers, ide);
    this.directives = directives;
    this.setSymClass(cls);
    this.setOptExtends(ext);
    this.setOptImplements(impl);
    this.body = body;
  }

  public FunctionDeclaration getConstructor() {
    return constructor;
  }

  @Override
  public void visit(AstVisitor visitor) {
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

  public String getName() {
    return getIde().getName();
  }

  public void setConstructor(FunctionDeclaration methodDeclaration) {
    if (constructor != null) {
      throw Jooc.error(methodDeclaration, "Only one constructor allowed per class");
    }
//     if (methodDeclaration != body.declararations.get(0))
//       Jooc.error(methodDeclaration, "Constructor declaration must be the first declaration in a class");
    constructor = methodDeclaration;
  }

  public void generateAsApiCode(JsWriter out) throws IOException {
    generateCode(directives, out);

    writeModifiers(out);
    out.writeSymbol(getSymClass());
    getIde().generateCode(out);
    if (getOptExtends() != null) {
      getOptExtends().generateCode(out);
    }
    if (getOptImplements() != null) {
      getOptImplements().generateCode(out);
    }
    body.generateCode(out);
  }

  public void addBuiltInUsage(String builtIn) {
    usedBuiltIns.add(builtIn);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    generateCode(directives, out);
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(getSymClass());
    getIde().generateCode(out);
    if (getOptExtends() != null) {
      getOptExtends().generateCode(out);
    }
    if (getOptImplements() != null) {
      getOptImplements().generateCode(out);
    }
    out.endString();
    out.write(",");
    out.write(getInheritanceLevel() + ",");
    out.write("function($$private){");
    writeBuiltInAliases(out);
    out.write("return[");
    generateClassInits(out);
    body.generateCode(out);
    if (constructor == null && !fieldsWithInitializer.isEmpty()) {
      // generate default constructor that calls field initializers:
      out.write("\"public function " + getName() + "\",function " + getName() + "$(){");
      new SuperCallCodeGenerator().generateCode(out);
      out.write("}");
    }

    for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
      secondaryDeclaration.generateJsCode(out);
      out.writeToken(",");
    }

    out.write("];},");
    generateStaticMethodList(out);
  }

  public void addSuperCallCodeGenerator(BlockStatement body) {
    body.addBlockStartCodeGenerator(new SuperCallCodeGenerator());
  }

  public JooSymbol getSymClass() {
    return symClass;
  }

  public void setSymClass(JooSymbol symClass) {
    this.symClass = symClass;
  }

  public Extends getOptExtends() {
    return optExtends;
  }

  public void setOptExtends(Extends optExtends) {
    this.optExtends = optExtends;
  }

  public Implements getOptImplements() {
    return optImplements;
  }

  public void setOptImplements(Implements optImplements) {
    this.optImplements = optImplements;
  }

  private class SuperCallCodeGenerator implements CodeGenerator {

    public void generateCode(JsWriter out) throws IOException {
      int inheritanceLevel = getInheritanceLevel();
      if (inheritanceLevel > 1) { // suppress for classes extending Object
        out.writeToken("this.super$" + inheritanceLevel + "();");
      }
      generateFieldInitCode(out, false, true);
    }
  }

  private void writeBuiltInAliases(JsWriter out) throws IOException {
    boolean first = true;
    for (String builtIn : usedBuiltIns) {
      String sourceName = "joo." + ("$$bound".equals(builtIn) ? "boundMethod" : builtIn);
      if (first) {
        out.writeToken("var");
        first = false;
      } else {
        out.writeToken(",");
      }
      out.writeToken(builtIn);
      out.writeToken("=");
      out.writeToken(sourceName);
    }
    out.writeToken(";");
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
      : new IdeType(getOptExtends() == null ? new Ide("Object") : getOptExtends().getSuperClass());

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
            //todo ugly, maybe we should define ClassScope implements Scope to lookup inherited members
            ((DeclarationScope)instanceScope).setIsInstanceScope(true);
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

  void addStarImport(final Ide packageIde) {
    ImportDirective importDirective = new ImportDirective(packageIde, "*");
    directives.add(0, importDirective);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    analyze(this, directives, context);
    super.analyze(parentNode, context);
    if (getOptExtends() != null) {
      getOptExtends().analyze(this, context);
    }
    if (getOptImplements() != null) {
      getOptImplements().analyze(this, context);
    }
    body.analyze(this, context);
    for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
      secondaryDeclaration.analyze(this, context);
    }
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
        throw new Jooc.CompilerError(classDecl.getSymbol(), "cyclic superclass chain");
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
    if (declaration == null && classDecl.getOptExtends() != null) {
      declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, classDecl.getOptExtends().getSuperClass());
    }
    if (declaration == null && classDecl.getOptImplements() != null) {
      CommaSeparatedList<Ide> implemented = classDecl.getOptImplements().getSuperTypes();
      while (implemented != null && declaration == null) {
        declaration = resolvePropertyInSuper(ide, classDecl, visited, chain, implemented.getHead());
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
                                              final LinkedList<ClassDeclaration> chain,
                                              final Ide superIde) {
    IdeDeclaration superClassDecl = superIde.getDeclaration();
    if (superClassDecl != null)
      if (!(superClassDecl instanceof ClassDeclaration)) {
        throw new Jooc.CompilerError(classDecl.getOptExtends().getSuperClass().getSymbol(), "expected class identifier");
      }
    return resolvePropertyDeclaration1(ide, (ClassDeclaration) superClassDecl, visited, chain);
  }

  public int getInheritanceLevel() {
    if (inheritanceLevel < 0) {
      inheritanceLevel = computeInheritanceLevel();
    }
    return inheritanceLevel;
  }

  private int computeInheritanceLevel() {
    if (superType == null)  {
      return 0;
    }
    if ("Object".equals(superType.getIde().getQualifiedNameStr())) {
      return 1;
    }
    IdeDeclaration superClassDecl = superType.getIde().getDeclaration();
    if (!(superClassDecl instanceof ClassDeclaration)) {
      throw new Jooc.CompilerError(getOptExtends().getSuperClass().getSymbol(), "expected class identifier");
    }
    return 1 + ((ClassDeclaration)superClassDecl).getInheritanceLevel();
  }


  public ClassDeclaration getSuperTypeDeclaration() {
    return superType == null ? null : (ClassDeclaration) superType.getIde().getDeclaration();
  }

  public void addFieldWithInitializer(VariableDeclaration fieldDeclaration) {
    fieldsWithInitializer.add(fieldDeclaration);
  }

  public void generateFieldInitCode(JsWriter out, boolean startWithSemicolon, boolean endWithSemicolon) throws IOException {
    Iterator<VariableDeclaration> iterator = fieldsWithInitializer.iterator();
    if (iterator.hasNext()) {
      if (startWithSemicolon) {
        out.write(";");
      }
      do {
        VariableDeclaration field = iterator.next();
        field.generateInitCode(out, endWithSemicolon || iterator.hasNext());
      } while (iterator.hasNext());
    }
  }
}
