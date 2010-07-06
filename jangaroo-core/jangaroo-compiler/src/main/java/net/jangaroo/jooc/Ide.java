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

/**
 * @author Andreas Gawecki
 */
public class Ide extends NodeImplBase {

  JooSymbol ide;

  private IdeDeclaration declaration;
  private Scope scope;
  private Ide qualified;

  private static final IdeDeclaration NULL_DECL = new VariableDeclaration(null, null, null, null);

  public Ide(final String ide) {
    this(new JooSymbol(ide));
  }

  public static Ide fromQName(JooSymbol qnameSymbol) {
    Ide ide = null;
    int i = 0, j = 0;
    String qname = qnameSymbol.getText();
    String ws = qnameSymbol.getWhitespace();
    do {
      i = qname.indexOf('.', j);
      if (i < 0) {
        i = qname.length();
      }
      assert i > 0;
      String segment = qname.substring(j, i - 1);
      JooSymbol symSegment = new JooSymbol(sym.IDE, qnameSymbol.getFileName(), qnameSymbol.getLine(), qnameSymbol.getColumn() + j, ws, segment);
      ws = "";
      JooSymbol symDot = new JooSymbol(sym.DOT, qnameSymbol.getFileName(), qnameSymbol.getLine(), qnameSymbol.getColumn() + i, "", ".");
      j = i + 1;
      ide = ide == null ? new Ide(symSegment) : new QualifiedIde(ide, symDot, symSegment);
    } while (j < qname.length());
    return ide;
  }


  public Ide(JooSymbol ide) {
    this.ide = ide;
  }

  public Scope getScope() {
    return scope;
  }

  public JooSymbol getIde() {
    return ide;
  }

  private boolean isThis() {
    return "this".equals(ide.getText());
  }

  private boolean needsThisAtRuntime() {
    if (isThis() || isSuper()) return true;
    if (!isQualified() && isDeclared()) {
      IdeDeclaration decl = getDeclaration();
      return decl.isClassMember() && !decl.isStatic();
    }
    return false;
  }

  private boolean isSuper() {
    return "super".equals(ide.getText());
  }

  @Override
  public void scope(final Scope scope) {
    // scope is "single assignment", allow further assignments to facilitate tree sharing
    if (this.scope == null) {
      this.scope = scope;
    }
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(ide);
  }

  public String[] getQualifiedName() {
    return new String[]{getName()};
  }

  public String getQualifiedNameStr() {
    return getName();
  }

  public String getName() {
    return ide.getText();
  }

  public JooSymbol getSymbol() {
    return ide;
  }

  public boolean isQualifier() {
    return qualified != null;
  }

  protected void setQualified(Ide qualifier) {
    this.qualified = qualifier;
  }

  public boolean isQualified() {
    return getQualifier() != null;
  }

  public Ide getQualified() {
    return qualified;
  }

  protected Ide getQualifier() {
    return null;
  }

  protected boolean isQualifiedByThis() {
    return getQualifier() != null && getQualifier().isThis();
  }

  protected boolean isQualifiedBySuper() {
    return getQualifier() != null && getQualifier().isSuper();
  }

  protected boolean isThisAccess() {
    return getQualifier() == null || getQualifier().isThis();
  }

  public boolean addExternalUsage() {
    IdeDeclaration decl = getDeclaration(false);
    if (decl == null || decl instanceof PackageDeclaration) {
      return false;
    }
    CompilationUnit currentUnit = getScope().getCompilationUnit();
    currentUnit.addDependency(decl.getIde().getScope().getCompilationUnit());
    return true;
  }

  /**
   * Resolve the declaration of this ide to the underlying declaration.
   * callable after scoping phase
   *
   * @return null if the declaration cannot be resolved
   */
  public IdeDeclaration resolveDeclaration() {
    IdeDeclaration decl = getDeclaration(false);
    return decl == null ? null : decl.resolveDeclaration();
  }

  /**
   * callable after scoping phase
   *
   * @throws Jooc.CompilerError if undeclared
   */
  public IdeDeclaration getDeclaration() {
    return getDeclaration(true);
  }

  /**
   * callable after scoping phase
   */
  public IdeDeclaration getDeclaration(boolean errorIfUndeclared) {
    if (declaration == null) {
      declaration = getScope().lookupDeclaration(this);
      if (declaration == null) {
        declaration = NULL_DECL; // prevent multiple lookups when called with !errorIfUndeclared multiple times
      } else if (declaration.getClassDeclaration() != getScope().getClassDeclaration()) {
        if (declaration.isPrivate()) {
          throw new Jooc.CompilerError(this.getSymbol(), "private member access");
        }
        if (declaration.isProtected() && !getScope().getClassDeclaration().isSubclassOf(declaration.getClassDeclaration())) {
          throw new Jooc.CompilerError(this.getSymbol(), "protected member access of non-superclass");
        }
      }
    }
    final IdeDeclaration result = declaration == NULL_DECL ? null : declaration;
    if (result == null && errorIfUndeclared) {
      throw Jooc.error(ide, "undeclared identifier '" + getName() + "'");
    }
    return result;
  }

  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return new QualifiedIde(new Ide(symQualifier), symDot, ide);
  }

  public void analyzeAsExpr(AstNode exprParent, Expr parentExpr, final AnalyzeContext context) {
    if (needsThisAtRuntime()) {
      FunctionExpr funExpr = scope.getFunctionExpr();
      if (funExpr != null) {
        funExpr.notifyThisUsed(scope);
      }
    }
    if (isSuper()) {
      FunctionDeclaration currentMethod = getScope().getMethodDeclaration();
      if (currentMethod == null)
        throw Jooc.error(ide, "use of super is only allowed within non-static methods");
      if (currentMethod.isStatic())
        throw Jooc.error(ide, "use of super inside static method");
      //todo check wheter super method exists and is non-static
    }
    checkDefinedAccessChain();
    ClassDeclaration classDeclaration = getScope().getClassDeclaration();
    // check candidates for instance methods declared in same file, accessed as function:
    if (classDeclaration != null) {
      // check and handle instance methods declared in same file, accessed as function:
      if (isBoundMethodCandidate(exprParent, parentExpr))
        classDeclaration.addBoundMethodCandidate(getName());
    }
    if (scope != null) {
      addExternalUsage();
      //todo handle references to static super members
      // check access to constant of another class; other class then must be initialized:
      if (isQualified() && !isQualifier() && !(exprParent instanceof ApplyExpr)) {
        if (classDeclaration != null) {
          classDeclaration.addInitIfClass(getQualifier());
        }
      }
    }
  }

  private void checkDefinedAccessChain() {
    if (!isQualified() && //this method is called for every node of a qualified ide tree, so we rely on the call on the root ide
      !isDeclared() && !isValidPackageAccessChain()) {
      throw Jooc.error(ide, "undeclared identifier '" + getName() + "'");
    }
  }

  private boolean isValidPackageAccessChain() {
    if (isQualifier()) {
      final Ide qualified = getQualified();
      return qualified.isDeclared() || qualified.isValidPackageAccessChain();
    }
    return false;
  }

  private boolean isDeclared() {
    return getDeclaration(false) != null;
  }

  private boolean isBoundMethodCandidate(final AstNode exprParent, final Expr parentExpr) {
    return isThisAccess() && !isQualifier() &&
      (exprParent instanceof ParenthesizedExpr ||
        exprParent instanceof CommaSeparatedList ||
        exprParent instanceof Initializer ||
        exprParent instanceof AsExpr ||
        exprParent instanceof ObjectField ||
        (exprParent instanceof AssignmentOpExpr && ((AssignmentOpExpr) exprParent).arg2 == parentExpr));
  }

  protected void generateCodeAsExpr(final JsWriter out) throws IOException {
    out.writeSymbolWhitespace(ide);
    if (isSuper()) {
      out.writeToken("this");
      return;
    }
    if (!isThis()) { // shortcut optimization, general case below should generate same code
      IdeDeclaration decl = getDeclaration(false);
      if (decl != null) {
        // todo private static methods are not private yet
        if (decl.isClassMember()) {
          if (decl.isStatic() && !decl.isPrivateStatic()) {
            out.writeToken(decl.getClassDeclaration().getQualifiedNameStr());
          } else if (!decl.isConstructor() && !decl.isPrivateStatic()) {
            out.writeToken("this");
          }
          writeMemberAccess(decl, null, this, false, out);
          return;
        }
        // add package prefix if it is not a local
        if (!decl.isClassMember() && decl.getParentDeclaration() instanceof PackageDeclaration) {
          String qname = ((PackageDeclaration) decl.getParentDeclaration()).getQualifiedNameStr();
          if (!qname.isEmpty()) {
            out.writeToken(qname);
            out.writeToken(".");
          }
        }
      }
    }
    out.writeSymbol(ide, false);
  }

  static void writeMemberAccess(IdeDeclaration memberDeclaration, final JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace, final JsWriter out) throws IOException {
    if (memberDeclaration != null) {
      if (memberIde.isQualifiedBySuper() || memberDeclaration.isPrivate()) {
        writePrivateMemberAccess(optSymDot, memberIde, writeMemberWhitespace, memberDeclaration.isStatic(), out);
        return;
      }
    }
    if (optSymDot != null) {
      out.writeSymbol(optSymDot);
    } else if (!memberDeclaration.isConstructor()) {
      out.writeToken(".");
    }
    out.writeSymbol(memberIde.ide, writeMemberWhitespace);
  }

  protected static IdeDeclaration resolveMember(final IdeDeclaration type, final Ide memberIde) {
    IdeDeclaration declaration = null;
    if (type != null) {
      declaration = type.resolvePropertyDeclaration(memberIde.getName());
    }
    return declaration;
  }

  static void writePrivateMemberAccess(final JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace, boolean isStatic, final JsWriter out) throws IOException {
    if (writeMemberWhitespace) {
      out.writeSymbolWhitespace(memberIde.ide);
    }
    if (isStatic) {
      out.writeToken("$$private");
      if (optSymDot != null) {
        out.writeSymbol(optSymDot);
      } else {
        out.writeToken(".");
      }
      out.writeSymbol(memberIde.ide, false);
    } else {
      out.writeToken("[");
      if (optSymDot != null) {
        out.writeSymbolWhitespace(optSymDot);
      }
      // awkward, but we have to be careful if we add characters to tokens:
      out.writeToken("$" + memberIde.getName());
      out.writeToken("]");
    }
  }

  @Override
  public String toString() {
    return getQualifiedNameStr();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Ide ide1 = (Ide) o;

    return !(ide != null ? !ide.getText().equals(ide1.ide.getText()) : ide1.ide != null);

  }

  @Override
  public int hashCode() {
    int result = ide != null ? ide.hashCode() : 0;
    result = 31 * result + (scope != null ? scope.hashCode() : 0);
    return result;
  }

}
