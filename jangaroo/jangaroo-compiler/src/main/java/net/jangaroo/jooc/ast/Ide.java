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
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.sym;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Ide extends NodeImplBase {

  private JooSymbol ide;

  private IdeDeclaration declaration;
  private Scope scope;
  private Ide qualified;
  private boolean bound;
  private boolean rewriteThis;

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

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIde(this);
  }

  public Ide(JooSymbol ide) {
    this.setIde(ide);
  }

  public Scope getScope() {
    return scope;
  }

  public JooSymbol getIde() {
    return ide;
  }

  private boolean isThis() {
    return "this".equals(getIde().getText());
  }

  private boolean needsThisAtRuntime() {
    if (isSuper()) return true;
    if (!isQualified() && isDeclared()) {
      IdeDeclaration decl = getDeclaration();
      return decl.isClassMember() && !decl.isStatic();
    }
    return false;
  }

  private boolean isSuper() {
    return "super".equals(getIde().getText());
  }

  @Override
  public void scope(final Scope scope) {
    // scope is "single assignment", allow further assignments to facilitate tree sharing
    if (this.scope == null) {
      this.scope = scope;
    }
  }

  public void writeIde(JsWriter out) throws IOException {
    // take care of reserved words called as functions (Rhino does not like):
    if (SyntacticKeywords.RESERVED_WORDS.contains(getIde().getText())) {
      out.writeToken("$$" + getIde().getText());
    } else {
      out.writeSymbol(getIde(), false);
    }
  }

  public String[] getQualifiedName() {
    return new String[]{getName()};
  }

  public String getQualifiedNameStr() {
    return getName();
  }

  public String getName() {
    return getIde().getText();
  }

  public JooSymbol getSymbol() {
    return getIde();
  }

  public boolean isQualifier() {
    return qualified != null;
  }

  public void setQualified(Ide qualifier) {
    this.qualified = qualifier;
  }

  public boolean isQualified() {
    return getQualifier() != null;
  }

  public Ide getQualified() {
    return qualified;
  }

  public Ide getQualifier() {
    return null;
  }

  public boolean isQualifiedByThis() {
    return getQualifier() != null && getQualifier().isThis();
  }

  public boolean isQualifiedBySuper() {
    return getQualifier() != null && getQualifier().isSuper();
  }

  protected boolean isThisAccess() {
    return getQualifier() == null || getQualifier().isThis();
  }

  public boolean addExternalUsage() {
    IdeDeclaration decl = getDeclaration(false);
    if (decl == null || !decl.isPrimaryDeclaration()) {
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
   * @throws net.jangaroo.jooc.Jooc.CompilerError if undeclared
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
      throw Jooc.error(getIde(), "undeclared identifier '" + getName() + "'");
    }
    return result;
  }

  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return new QualifiedIde(new Ide(symQualifier), symDot, getIde());
  }

  public void analyzeAsExpr(AstNode exprParent, Expr parentExpr, final AnalyzeContext context) {
    if (needsThisAtRuntime()) {
      FunctionExpr funExpr = scope.getFunctionExpr();
      if (funExpr != null) {
        setRewriteThis(funExpr.notifyThisUsed(scope));
      }
    }
    if (isSuper()) {
      FunctionDeclaration currentMethod = getScope().getMethodDeclaration();
      if (currentMethod == null) {
        throw Jooc.error(getIde(), "use of super is only allowed within non-static methods");
      }
      if (currentMethod.isStatic()) {
        throw Jooc.error(getIde(), "use of super inside static method");
      }
      FunctionExpr currentFunction = getScope().getFunctionExpr();
      if (currentFunction.getFunctionDeclaration() != currentMethod) {
        throw Jooc.error(getIde(), "super calls might only be used within instance methods, not in local functions");
      }
      //todo check whether super method exists and is non-static
    }
    checkDefinedAccessChain();
    if (isBoundMethodCandidate(exprParent, parentExpr)) {
      IdeDeclaration memberDeclaration = getMemberDeclaration();
      // check candidates for instance methods, accessed as function:
      if (memberDeclaration != null && memberDeclaration.isMethod() && !((FunctionDeclaration)memberDeclaration).isGetterOrSetter() && !memberDeclaration.isStatic()) {
        // check and handle instance methods declared in same file, accessed as function:
        getScope().getClassDeclaration().addBuiltInUsage("$$bound");
        setBound(true);
      }
    }
    if (scope != null) {
      usageInExpr(exprParent);
    }
  }

  public void usageInExpr(final AstNode exprParent) {
    addExternalUsage();
    //todo handle references to static super members
    // check access to another class or a constant of another class; other class then must be initialized:
    if (!(exprParent instanceof ApplyExpr) && !(exprParent instanceof NewExpr) && !(exprParent instanceof IsExpr) && !(exprParent instanceof AsExpr)) {
      ClassDeclaration classDeclaration = getScope().getClassDeclaration();
      if (classDeclaration != null) {
        if (isQualified()) {
          // access to constant of other class?
          // TODO: If the static member is a method, we should not add a class init.
          //       Unfortunately, declaration information seems not to be available at this point in time.
          classDeclaration.addInitIfClass(getQualifier());
        }
        // access to other class?
        if (!isQualifier()) {
          classDeclaration.addInitIfClass(this);
        }
      }
    }
  }

  public IdeDeclaration getMemberDeclaration() {
    IdeDeclaration declaration = getDeclaration(false);
    if (declaration != null && declaration.isClassMember()) {
      return declaration;
    }
    return declaration;
  }

  private void checkDefinedAccessChain() {
    if (!isQualified() && //this method is called for every node of a qualified ide tree, so we rely on the call on the root ide
      !isDeclared() && !isValidPackageAccessChain()) {
      throw Jooc.error(getIde(), "undeclared identifier '" + getName() + "'");
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
    return exprParent instanceof ParenthesizedExpr ||
        exprParent instanceof CommaSeparatedList ||
        exprParent instanceof Initializer ||
        exprParent instanceof AsExpr ||
        exprParent.getClass().equals(BinaryOpExpr.class) ||
        exprParent instanceof ObjectField ||
        exprParent instanceof ReturnStatement ||
        (exprParent instanceof AssignmentOpExpr && ((AssignmentOpExpr) exprParent).getArg2() == parentExpr);
  }

  public void generateCodeAsExpr(final JsWriter out) throws IOException {
    out.writeSymbolWhitespace(getIde());
    if (isSuper()) {
      writeThis(out);
      return;
    }
    if (!isThis()) {
      IdeDeclaration decl = getDeclaration(false);
      if (decl != null) {
        if (decl.isClassMember()) {
          if (!decl.isPrivateStatic()) {
            if (decl.isStatic()) {
              out.writeToken(decl.getClassDeclaration().getQualifiedNameStr());
            } else {
              if (isBound()) {
                writeBoundMethodAccess(out, null, null, decl);
                return;
              }
              writeThis(out);
            }
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
    writeIde(out);
  }

  private void writeThis(JsWriter out) throws IOException {
    out.writeToken(isRewriteThis() ? "this$" : "this");
  }

  protected void writeBoundMethodAccess(JsWriter out, Ide optIde, JooSymbol optSymDot, IdeDeclaration decl) throws IOException {
    out.writeToken("$$bound(");
    if (optIde != null) {
      optIde.generateCodeAsExpr(out);
    } else {
      writeThis(out);
    }
    if (optSymDot != null) {
      out.writeSymbolWhitespace(optSymDot);
    }
    out.writeToken(",");
    out.beginString();
    if (usePrivateMemberName(decl)) {
      out.writeToken(getName() + "$" + scope.getClassDeclaration().getInheritanceLevel());
    } else {
      out.writeToken(getName());
    }
    out.endString();
    out.writeToken(")");
  }

  public static void writeMemberAccess(IdeDeclaration memberDeclaration, JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace, final JsWriter out) throws IOException {
    if (memberDeclaration != null) {
      if (memberIde.usePrivateMemberName(memberDeclaration)) {
        writePrivateMemberAccess(optSymDot, memberIde, writeMemberWhitespace, memberDeclaration.isStatic(), out);
        return;
      }
    }
    if (optSymDot == null && memberDeclaration != null && !memberDeclaration.isConstructor()) {
      optSymDot = new JooSymbol(".");
    }
    boolean quote = false;
    if (optSymDot != null) {
      if (memberIde.getIde().getText().startsWith("@")) {
        quote = true;
        out.writeSymbolWhitespace(optSymDot);
        out.writeToken("['");
      } else {
        out.writeSymbol(optSymDot);
      }
    }
    out.writeSymbol(memberIde.getIde(), writeMemberWhitespace);
    if (quote) {
      out.writeToken("']");
    }
  }

  private boolean usePrivateMemberName(IdeDeclaration memberDeclaration) {
    return isQualifiedBySuper()
      && scope.getClassDeclaration().getMemberDeclaration(getName()) != null
      || memberDeclaration.isPrivate();
  }

  public static IdeDeclaration resolveMember(final IdeDeclaration type, final Ide memberIde) {
    IdeDeclaration declaration = null;
    if (type != null) {
      declaration = type.resolvePropertyDeclaration(memberIde.getName());
    }
    return declaration;
  }

  static void writePrivateMemberAccess(final JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace, boolean isStatic, final JsWriter out) throws IOException {
    if (writeMemberWhitespace) {
      out.writeSymbolWhitespace(memberIde.getIde());
    }
    if (isStatic) {
      out.writeToken("$$private");
      if (optSymDot != null) {
        out.writeSymbol(optSymDot);
      } else {
        out.writeToken(".");
      }
      out.writeSymbol(memberIde.getIde(), false);
    } else {
      if (optSymDot != null) {
        out.writeSymbol(optSymDot);
      } else {
        out.writeToken(".");
      }
      // awkward, but we have to be careful if we add characters to tokens:
      out.writeToken(memberIde.getName() + "$" + memberIde.scope.getClassDeclaration().getInheritanceLevel());
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

    return !(getIde() != null ? !getIde().getText().equals(ide1.getIde().getText()) : ide1.getIde() != null);

  }

  @Override
  public int hashCode() {
    int result = getIde() != null ? getIde().hashCode() : 0;
    result = 31 * result + (scope != null ? scope.hashCode() : 0);
    return result;
  }

  public void setIde(JooSymbol ide) {
    this.ide = ide;
  }

  public boolean isBound() {
    return bound;
  }

  public void setBound(boolean bound) {
    this.bound = bound;
  }

  public boolean isRewriteThis() {
    return rewriteThis;
  }

  public void setRewriteThis(boolean rewriteThis) {
    this.rewriteThis = rewriteThis;
  }
}
