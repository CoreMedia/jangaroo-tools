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
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;

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
  private String packagePrefix = "";

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

  public String getPackagePrefix() {
    return packagePrefix;
  }

  public boolean isThis() {
    return "this".equals(getIde().getText());
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
    AstNode parentNode = getParentNode();
    if (parentNode instanceof DotExpr) {
      DotExpr dotExpr = (DotExpr) parentNode;
      if (dotExpr.getIde() == this) {
        return dotExpr.getArg() instanceof IdeExpr && ((IdeExpr) dotExpr.getArg()).getIde().isSuper();
      }
    }
    return false;
  }

  public boolean addExternalUsage() {
    Scope scope = getScope();
    AstNode definingNode = scope.getDefiningNode();
    return addExternalUsage(definingNode instanceof ClassDeclaration ||
            definingNode instanceof PackageDeclaration);
  }

  public boolean addExternalUsage(boolean required) {
    IdeDeclaration decl = getDeclaration(false);
    if (decl != null && (decl.isPrimaryDeclaration() || decl.isClassMember() && decl.isStatic())) {
      CompilationUnit currentUnit = getScope().getCompilationUnit();
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
      declaration = getScope().lookupDeclaration(this);
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
      throw Jooc.error(getIde(), "undeclared identifier '" + getName() + "'");
    }
    return result;
  }

  public Ide qualify(final JooSymbol symQualifier, final JooSymbol symDot) {
    return new QualifiedIde(new Ide(symQualifier), symDot, getIde());
  }

  public void analyzeAsExpr(AstNode exprParent, Expr parentExpr) {
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
      if (memberDeclaration != null && memberDeclaration.isMethod() && !((FunctionDeclaration) memberDeclaration).isGetterOrSetter() && !memberDeclaration.isStatic()) {
        // check and handle instance methods declared in same file, accessed as function:
        getScope().getCompilationUnit().addBuiltInUsage("$$bound");
        setBound(true);
      }
    }
    if (scope != null) {
      usageInExpr(exprParent);
      if (!isThis() && !isQualified()) {
        IdeDeclaration decl = getDeclaration(false);
        if (decl != null) {
          if ((!isQualifier() || exprParent instanceof ApplyExpr)
            && !(exprParent instanceof ArrayIndexExpr) && (decl instanceof Parameter)) {
            FunctionExpr currentFunction = scope.getFunctionExpr();
            if (currentFunction != null) {
              currentFunction.notifyArgumentsUsed(decl);
            }
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
          Jooc.warning(getSymbol(), "'this' may be unbound and is untyped in functions, even inside methods. Consider removing 'this.' (members are in scope!) or refactoring inner function to method.");
        }
      }
    }
    addExternalUsage();
    //todo handle references to static super members
    // check access to another class or a constant of another class; other class then must be initialized:
    if (!(exprParent instanceof NewExpr) && !(exprParent instanceof IsExpr) && !(exprParent instanceof AsExpr)) {
      ClassDeclaration classDeclaration = getScope().getClassDeclaration();
      if (classDeclaration != null) {
        if (isQualified()) {
          // access to constant of other class?
          // TODO: If the static member is a method, we should not add a class init.
          //       Unfortunately, declaration information seems not to be available at this point in time.
          if (exprParent instanceof ApplyExpr) {
            // It seems to be a method. Static methods take care of initializing their class,
            // but methods of plackage-scope variables do not initialize the compilation unit.
            // Thus, we only add initialization if the compilation unit is a package-scope variable.
            classDeclaration.addInitIfGlobalVar(getQualifier());
          } else {
            classDeclaration.addInitIfClassOrGlobalVar(getQualifier());
          }
        }
        // access to other class?
        if (!isQualifier()) {
          classDeclaration.addInitIfClassOrGlobalVar(this);
        }
      }
    }
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

  public IdeDeclaration getMemberDeclaration() {
    IdeDeclaration ideDeclaration = getDeclaration(false);
    if (ideDeclaration != null && ideDeclaration.isClassMember()) {
      return ideDeclaration;
    }
    return ideDeclaration;
  }

  private void checkDefinedAccessChain() {
    if (!isQualified() && //this method is called for every node of a qualified ide tree, so we rely on the call on the root ide
            !isDeclared() && !isValidPackageAccessChain()) {
      throw Jooc.error(getIde(), "undeclared identifier '" + getName() + "'");
    }
  }

  private boolean isValidPackageAccessChain() {
    if (isQualifier()) {
      final Ide qualifiedIde = getQualified();
      return qualifiedIde.isDeclared() || qualifiedIde.isValidPackageAccessChain();
    }
    return false;
  }

  private boolean isDeclared() {
    return getDeclaration(false) != null;
  }

  private boolean isBoundMethodCandidate(final AstNode exprParent, final Expr parentExpr) {
    return exprParent instanceof ParenthesizedExpr &&
            !(exprParent.getParentNode() instanceof KeywordStatement) ||
            exprParent instanceof CommaSeparatedList ||
            exprParent instanceof Initializer ||
            exprParent instanceof AsExpr ||
            exprParent.getClass().equals(BinaryOpExpr.class) ||
            exprParent instanceof ObjectField ||
            exprParent instanceof ReturnStatement ||
            (exprParent instanceof AssignmentOpExpr && ((AssignmentOpExpr) exprParent).getArg2() == parentExpr);
  }

  public boolean usePrivateMemberName(IdeDeclaration memberDeclaration) {
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
  }

  public boolean isRewriteThis() {
    return rewriteThis;
  }

  public void setRewriteThis(boolean rewriteThis) {
    this.rewriteThis = rewriteThis;
  }
}
