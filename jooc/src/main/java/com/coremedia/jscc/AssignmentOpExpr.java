/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;


/**
 * @author Andreas Gawecki
 */
class AssignmentOpExpr extends BinaryOpExpr {
  public AssignmentOpExpr(Expr arg1, JscSymbol op, Expr arg2) {
    super(arg1, op, arg2);
  }

  
}
