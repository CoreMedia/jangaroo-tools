package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * A BinaryOpExpr for AS3 infix operators that are not supported directly in JS.
 * For JS, the operand is generated as a function receiving the two arguments.
 *
 * @author Frank Wienberg
 */
public class InfixOpExpr extends BinaryOpExpr {
  protected InfixOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitInfixOpExpr(this);
  }

  @Override
  public void scope(Scope scope) {
    super.scope(scope);
    scope.getCompilationUnit().addBuiltInUsage(getOp().getText());
  }
}
