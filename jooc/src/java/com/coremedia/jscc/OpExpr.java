/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

abstract class OpExpr extends Expr {
    JscSymbol op;

  protected OpExpr(JscSymbol op) {
    this.op = op;
  }
}
