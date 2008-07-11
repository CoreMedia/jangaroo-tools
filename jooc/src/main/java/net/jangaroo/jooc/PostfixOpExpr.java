/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class PostfixOpExpr extends UnaryOpExpr {

  PostfixOpExpr(JooSymbol op, Expr arg) {
    super(op, arg);
  }

  public void generateCode(JsWriter out) throws IOException {
    arg.generateCode(out);
    out.writeSymbol(op);
  }

  public JooSymbol getSymbol() {
     return arg.getSymbol();
  }
}
