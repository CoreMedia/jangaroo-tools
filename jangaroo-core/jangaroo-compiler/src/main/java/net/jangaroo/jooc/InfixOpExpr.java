package net.jangaroo.jooc;

import java.io.IOException;

/**
 * A BinaryOpExpr for AS3 infix operators that are not supported directly in JS.
 * For JS, the operand is generated as a function receiving the two arguments.
 * @author Frank Wienberg
 */
public class InfixOpExpr extends BinaryOpExpr {
  public InfixOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    arg1.generateCode(out);
    out.writeSymbol(op);
    arg2.generateCode(out);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolToken(op);
    out.write('(');
    arg1.generateCode(out);
    out.write(',');
    out.writeSymbolWhitespace(op);
    arg2.generateCode(out);
    out.write(')');
  }
}
