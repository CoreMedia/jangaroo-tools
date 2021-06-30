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
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Andreas Gawecki
 */
public class Ide extends NodeImplBase {

  private static final Pattern IDE_PATTERN = Pattern.compile("[a-zA-Z$_@]([a-zA-Z0-9$_@])*");

  public static final String THIS = "this";
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

  public boolean isThis() {
    return THIS.equals(getIde().getText());
  }

  private boolean needsThisAtRuntime() {
    if (isSuper()) {
      return true;
    }
    if (!isQualified() && isDeclared()) {
      IdeDeclaration decl = getDeclaration();
      return decl.isClassMember() && !decl.isStatic();
    }
    return false;
  }

  public boolean isSuper() {
    return "super".equals(getIde().getText());
  }

  @Override
  public void scope(final Scope scope) {
    // scope is "single assignment", allow further assignments to facilitate tree sharing
    if (this.scope == null) {
      this.scope = scope;
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

  private boolean isQualifier() {
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
    AstNode parentNode = getParentNode();
    if (parentNode instanceof DotExpr) {
      DotExpr dotExpr = (DotExpr) parentNode;
      if (dotExpr.getIde() == this) {
        return dotExpr.getArg() instanceof IdeExpr && ((IdeExpr) dotExpr.getArg()).getIde().isSuper();
      }
    }
    return false;
  }

  public boolean addExternalUsage(Boolean required) {
    IdeDeclaration decl = getDeclaration(false);
    if (decl != null && (decl.isPrimaryDeclaration() || decl.isClassMember() && decl.isStatic())) {
      Scope scope = getScope();
      CompilationUnit currentUnit = scope.getCompilationUnit();
      CompilationUnit compilationUnit = decl.getIde().getScope().getCompilationUnit();
      currentUnit.addDependency(compilationUnit, required);
      return true;
    }
    return false;
  }

  public void addPublicApiDependency() {
    IdeDeclaration decl = getDeclaration(false);
    if (decl != null && decl.isPrimaryDeclaration()) {
      CompilationUnit currentUnit = getScope().getCompilationUnit();
      CompilationUnit compilationUnit = decl.getIde().getScope().getCompilationUnit();
      currentUnit.addPublicApiDependency(compilationUnit);
    } else if (isQualified()) {
      getQualifier().addPublicApiDependency();
    }
  }

  void setDeclaration(IdeDeclaration declaration) {
    this.declaration = declaration;
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
   * @throws net.jangaroo.jooc.CompilerError
   *          if undeclared
   */
  public IdeDeclaration getDeclaration() {
    return getDeclaration(true);
  }

  /**
   * callable after scoping phase
   */
  public IdeDeclaration getDeclaration(boolean errorIfUndeclared) {
    if (declaration == null) {
      declaration = lookupDeclaration(errorIfUndeclared);
      if (declaration != null) {
        if (declaration.getClassDeclaration() != getScope().getClassDeclaration()) {
          if (declaration.isPrivate()) {
            // private member access
            declaration = null;
          } else if (declaration.isProtected() && !getScope().getClassDeclaration().isSubclassOf(declaration.getClassDeclaration())) {
            // protected member access of non-superclass
            declaration = null;
          }
        }
      }
      if (declaration == null) {
        declaration = NULL_DECL; // prevent multiple lookups when called with !errorIfUndeclared multiple times
      } else if (declaration.getClassDeclaration() != getScope().getClassDeclaration()) {
        if (declaration.isPrivate()) {
          throw new CompilerError(this.getSymbol(), "private member access");
        }
        if (declaration.isProtected() && !getScope().getClassDeclaration().isSubclassOf(declaration.getClassDeclaration())) {
          throw new CompilerError(this.getSymbol(), "protected member access of non-superclass");
        }
      }
    }
    final IdeDeclaration result = declaration == NULL_DECL ? null : declaration; // NOSONAR no equals here
    if (result == null && errorIfUndeclared) {
      throw JangarooParser.error(getIde(), "undeclared identifier '" + getName() + "'");
    }
    return result;
  }

  private IdeDeclaration lookupDeclaration(boolean errorIfUndeclared) {
    IdeDeclaration ideDeclaration = null;
    try {
      ideDeclaration = getScope().lookupDeclaration(this);
    } catch (CompilerError e) {
      if(errorIfUndeclared) {
        throw e;
      }
      JangarooParser.warning(getSymbol(), e.getMessage());
    }
    return ideDeclaration;
  }


  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return new QualifiedIde(new Ide(symQualifier), symDot, getIde());
  }

  public void analyzeAsExpr(AstNode exprParent, Expr parentExpr) {
    FunctionExpr funExpr = scope.getFunctionExpr();
    if (funExpr != null) {
      boolean isInConstructor = funExpr.getFunctionDeclaration() != null && funExpr.getFunctionDeclaration().isConstructor();
      if (isThis() && !isRewriteThis()) {
        funExpr.notifyExplicitThisUsed();
        if (isInConstructor) {
          notifyInstanceThisUsed();
        }
      }
      if (needsThisAtRuntime()) {
        setRewriteThis(funExpr.notifyThisUsed(scope));
        if (!isSuper() && isInConstructor) {
          notifyInstanceThisUsed();
        }
      }
    }
    if (isSuper()) {
      FunctionDeclaration currentMethod = getScope().getMethodDeclaration();
      if (currentMethod == null) {
        throw JangarooParser.error(getIde(), "use of super is only allowed within non-static methods");
      }
      if (currentMethod.isStatic()) {
        throw JangarooParser.error(getIde(), "use of super inside static method");
      }
      FunctionExpr currentFunction = getScope().getFunctionExpr();
      if (currentFunction.getFunctionDeclaration() != currentMethod) {
        throw JangarooParser.error(getIde(), "super calls might only be used within instance methods, not in local functions");
      }
      //todo check whether super method exists and is non-static
    }
    if (isBoundMethodCandidate(exprParent, parentExpr)) {
      IdeDeclaration memberDeclaration = getMemberDeclaration();
      // check candidates for instance methods, accessed as function:
      if (memberDeclaration != null && memberDeclaration.isMethod() && !((FunctionDeclaration) memberDeclaration).isGetterOrSetter() && !memberDeclaration.isStatic()) {
        // check and handle instance methods declared in same file, accessed as function:
        setBound(true);
      }
    }
    if (scope != null) {
      usageInExpr(exprParent);
      if (!isThis() && !isQualified()) {
        IdeDeclaration decl = getDeclaration(false);
        if (decl instanceof Parameter) {
          FunctionExpr currentFunction = scope.getFunctionExpr();
          if (currentFunction != null && currentFunction.isMyArguments((Parameter) decl)) {
            currentFunction.notifyArgumentsUsed((!isQualifier() || exprParent instanceof ApplyExpr)
                    && !(exprParent instanceof ArrayIndexExpr));
          }
        }
      }
    }
  }

  private void usageInExpr(final AstNode exprParent) {
    if (isThis() && !isRewriteThis()) {
      FunctionExpr funExpr = getScope().getFunctionExpr();
      if (funExpr != null && funExpr.getFunctionDeclaration() == null) {
        FunctionDeclaration methodDeclaration = getScope().getMethodDeclaration();
        if (methodDeclaration != null && !methodDeclaration.isStatic() && !isArgumentOfTypeCast(exprParent)) {
          JangarooParser.warning(getSymbol(), "'this' may be unbound and is untyped in functions, even inside methods. Consider removing 'this.' (members are in scope!) or refactoring inner function to method.");
        }
      }
    }
    addExternalUsage(false);
  }

  private boolean isArgumentOfTypeCast(AstNode parentNode) {
    if (parentNode instanceof CommaSeparatedList) {
      AstNode argumentsCandidate = parentNode.getParentNode();
      if (argumentsCandidate instanceof ParenthesizedExpr) {
        AstNode typeCastCandidate = argumentsCandidate.getParentNode();
        return typeCastCandidate instanceof ApplyExpr && ((ApplyExpr)typeCastCandidate).isTypeCast();
      }
    } else if (parentNode instanceof AsExpr) {
      return ((AsExpr)parentNode).getArg1() == getParentNode();
    }
    return false;
  }

  public boolean isAssignmentLHS() {
    AstNode parentNode = getParentNode();
    if (parentNode instanceof IdeExpr || (parentNode instanceof DotExpr && ((DotExpr) parentNode).getIde() == this)) {
      AstNode containingExpr = parentNode.getParentNode();
      if (containingExpr instanceof AssignmentOpExpr) {
        Expr arg1 = ((AssignmentOpExpr) containingExpr).getArg1();
        if (arg1 instanceof IdeExpr) {
          arg1 = ((IdeExpr) arg1).getNormalizedExpr();
        }
        return arg1 == parentNode;
      }
    }
    return false;
  }

  public IdeDeclaration getMemberDeclaration() {
    IdeDeclaration ideDeclaration = getDeclaration(false);
    if (ideDeclaration != null && ideDeclaration.isClassMember()) {
      return ideDeclaration;
    }
    return ideDeclaration;
  }

  boolean isDeclared() {
    return getDeclaration(false) != null;
  }

  private boolean isBoundMethodCandidate(final AstNode exprParent, final Expr parentExpr) {
    return exprParent instanceof ParenthesizedExpr &&
            !(exprParent.getParentNode() instanceof KeywordStatement) ||
            exprParent instanceof CommaSeparatedList ||
            exprParent instanceof Initializer ||
            exprParent instanceof AsExpr ||
            exprParent instanceof BinaryOpExpr &&
            exprParent.getClass().equals(BinaryOpExpr.class) ||
            exprParent instanceof ObjectField ||
            exprParent instanceof ReturnStatement ||
            (exprParent instanceof AssignmentOpExpr && ((AssignmentOpExpr) exprParent).getArg2() == parentExpr);
  }

  @Override
  public String toString() {
    return getQualifiedNameStr();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

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
    if (bound) {
      scope.getCompilationUnit().addBuiltInIdentifierUsage("bind");
    }
  }

  public boolean isRewriteThis() {
    return rewriteThis;
  }

  void setRewriteThis(boolean rewriteThis) {
    this.rewriteThis = rewriteThis;
  }

  public static boolean isValidIdentifier(String identifier) {
    return IDE_PATTERN.matcher(identifier).matches();
  }

  public static void verifyIdentifier(String identifier, JooSymbol source) {
    if(!isValidIdentifier(identifier)) {
      throw JangarooParser.error(source, "invalid action script identifier");
    }
  }
}
