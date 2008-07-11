/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class LiteralExpr extends Expr {

  JooSymbol value;

  public LiteralExpr(JooSymbol value) {
    this.value = value;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(value);
  }

  public JooSymbol getSymbol() {
    return value;
  }

  boolean isCompileTimeConstant() {
    return true;
  }
}
