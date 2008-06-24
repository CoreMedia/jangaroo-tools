/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class PrefixOpExpr extends UnaryOpExpr {

  public PrefixOpExpr(JscSymbol op, Expr arg) {
    super(op, arg);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(op);
    arg.generateCode(out);
  }

  public JscSymbol getSymbol() {
    return op;
  }

}
