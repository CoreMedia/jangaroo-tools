/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
abstract class OpExpr extends Expr {
    JscSymbol op;

  protected OpExpr(JscSymbol op) {
    this.op = op;
  }
}
