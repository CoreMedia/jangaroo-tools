package net.jangaroo.jooc;

import java.io.IOException;

/**
 * A BinaryOpExpr where the operand is generated as a function receiving the two arguments.
 * @author Frank Wienberg
 */
public class BinaryPrefixOpExpr extends BinaryOpExpr {
  public BinaryPrefixOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(op);
    out.write('(');
    arg1.generateCode(out);
    out.write(',');
    arg2.generateCode(out);
    out.write(')');
  }
}
