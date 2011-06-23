package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.Expr;

import java.io.IOException;

/**
 * A BinaryOpExpr for AS3 infix operators that are not supported directly in JS.
 * For JS, the operand is generated as a function receiving the two arguments.
 * @author Frank Wienberg
 */
public class InfixOpExpr extends BinaryOpExpr {
  protected InfixOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitInfixOpExpr(this);
  }

  @Override
  public void scope(Scope scope) {
    super.scope(scope);
    ClassDeclaration classDeclaration = scope.getClassDeclaration();
    if (classDeclaration != null) {
      classDeclaration.addBuiltInUsage(getOp().getText());
    }
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    getArg1().generateCode(out);
    out.writeSymbol(getOp());
    getArg2().generateCode(out);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolToken(getOp());
    out.write('(');
    getArg1().generateCode(out);
    out.write(',');
    out.writeSymbolWhitespace(getOp());
    getArg2().generateCode(out);
    out.write(')');
  }
}
