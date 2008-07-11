/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
abstract class OpExpr extends Expr {
    JooSymbol op;

  protected OpExpr(JooSymbol op) {
    this.op = op;
  }
}
