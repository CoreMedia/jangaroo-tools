package net.jangaroo.jooc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.TransitiveAstVisitor;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;

import java.io.IOException;

class StaticDependencyVisitor extends TransitiveAstVisitor {
  private final Object classBody;
  private final CompilationUnit compilationUnit;
  final Multimap<FunctionDeclaration, FunctionDeclaration> internalUses = HashMultimap.create();
  final Multimap<FunctionDeclaration, Dependency> nonFunctionUses = HashMultimap.create();
  final FunctionDeclaration[] currentDeclaration = {null};

  public StaticDependencyVisitor(Object classBody, CompilationUnit compilationUnit) {
    super(new AstVisitorBase());
    this.classBody = classBody;
    this.compilationUnit = compilationUnit;
  }

  public Multimap<FunctionDeclaration, FunctionDeclaration> getInternalUses() {
    return internalUses;
  }

  public Multimap<FunctionDeclaration, Dependency> getNonFunctionUses() {
    return nonFunctionUses;
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    // Ignore instance methods.
    if (functionDeclaration.isStatic()) {
      // For static methods, register all nested ides as dependencies of the method.
      boolean isTopLevel = currentDeclaration[0] == null &&
              classBody.equals(functionDeclaration.getParentNode());
      if (isTopLevel) {
        currentDeclaration[0] = functionDeclaration;
      }
      super.visitFunctionDeclaration(functionDeclaration);
      if (isTopLevel) {
        currentDeclaration[0] = null;
      }
    }
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    // Ignore instance fields, but include package locals.
    if (variableDeclaration.isStatic() || compilationUnit.getPrimaryDeclaration() instanceof VariableDeclaration) {
      // For static fields, register all nested ides as init dependencies.
      super.visitVariableDeclaration(variableDeclaration);
    }
  }

  private void declarationReferenced(IdeDeclaration ideDeclaration, boolean isNew) {
    if (ideDeclaration != null) {
      CompilationUnit ideCompilationUnit = ideDeclaration.getCompilationUnit();
      if (ideCompilationUnit != null && ideCompilationUnit.isInSourcePath()) {
        if (isNew) {
          constructorReferenced(ideDeclaration);
        } else if (!ideCompilationUnit.getPrimaryDeclaration().getQualifiedNameStr().equals(compilationUnit.getPrimaryDeclaration().getQualifiedNameStr())) {
          otherCompilationUnitReferenced(ideDeclaration);
        } else if (ideDeclaration instanceof FunctionDeclaration) {
          ownFunctionReferenced((FunctionDeclaration) ideDeclaration);
        }
      }
    }
  }

  private void ownFunctionReferenced(FunctionDeclaration ideDeclaration) {
    FunctionDeclaration functionDeclaration = ideDeclaration;
    if (functionDeclaration.isMethod() && functionDeclaration.isStatic()) {
      // A local constructor call or a local static call.
      internalUses.put(currentDeclaration[0], functionDeclaration);
    }
  }

  private void otherCompilationUnitReferenced(IdeDeclaration ideDeclaration) {
    // The identifier is defined in a different compilation unit in the same module.
    // The dependency must be analysed, because it might have to be
    // strengthened into a required dependency.
    // Ignore ordinary method calls: The called class must have been initialized,
    // because an instance has already been created.
    if (ideDeclaration.isStatic() || ideDeclaration instanceof ClassDeclaration) {
      nonFunctionUses.put(currentDeclaration[0],
              new Dependency(ideDeclaration.getCompilationUnit(), DependencyLevel.STATIC));
    }
  }

  private void constructorReferenced(IdeDeclaration ideDeclaration) {
    nonFunctionUses.put(currentDeclaration[0], new Dependency(ideDeclaration.getCompilationUnit(), DependencyLevel.DYNAMIC));
  }

  @Override
  public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
    Expr normalizedExpr = ideExpr.getNormalizedExpr();
    if (normalizedExpr != ideExpr) {
      normalizedExpr.visit(this);
    } else {
      declarationReferenced(ideExpr.getIde().getDeclaration(false),
              ideExpr.getParentNode() instanceof NewExpr);
    }
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    boolean isNew = dotExpr.getParentNode() instanceof NewExpr;

    Ide ide = dotExpr.getIde();
    declarationReferenced(ide.getDeclaration(false), isNew);

    Expr arg = dotExpr.getArg();
    arg.visit(this);

    // In some cases, the ide is not properly scoped, but the arg
    // is known to refer to a class declaration, which can be used
    // to resolve static members.
    if (arg instanceof IdeExpr) {
      IdeDeclaration argDeclaration = ((IdeExpr) arg).getIde().getDeclaration(false);
      if (argDeclaration instanceof ClassDeclaration) {
        TypedIdeDeclaration staticMemberDeclaration = ((ClassDeclaration) argDeclaration).getStaticMemberDeclaration(ide.getName());
        if (staticMemberDeclaration != null) {
          declarationReferenced(staticMemberDeclaration, isNew);
        }
      }
    }
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    visitIde(qualifiedIde);
  }
}
