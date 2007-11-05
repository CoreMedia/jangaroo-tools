/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

class DotExpr extends BinaryOpExpr {

  Expr expr;
  JscSymbol symDot;
  Ide ide;

  public DotExpr(Expr expr, JscSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public JscSymbol getSymbol() {
    return expr.getSymbol();
  }

}
