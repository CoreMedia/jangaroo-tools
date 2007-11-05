/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class LiteralExpr extends Expr {

  JscSymbol value;

  public LiteralExpr(JscSymbol value) {
    this.value = value;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(value);
  }

  public JscSymbol getSymbol() {
    return value;
  }

  boolean isCompileTimeConstant() {
    return true;
  }
}
