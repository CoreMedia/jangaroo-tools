/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class PostfixOpExpr extends UnaryOpExpr {

  PostfixOpExpr(JscSymbol op, Expr arg) {
    super(op, arg);
  }

  public void generateCode(JsWriter out) throws IOException {
    arg.generateCode(out);
    out.writeSymbol(op);
  }

  public JscSymbol getSymbol() {
     return arg.getSymbol();
  }
}
