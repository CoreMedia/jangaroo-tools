/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class PrefixOpExpr extends UnaryOpExpr {

  public PrefixOpExpr(JooSymbol op, Expr arg) {
    super(op, arg);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(op);
    arg.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return op;
  }

}
