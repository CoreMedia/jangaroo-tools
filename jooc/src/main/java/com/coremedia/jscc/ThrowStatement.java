/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
class ThrowStatement extends KeywordExprStatement {

  public ThrowStatement(JscSymbol symThrow, Expr expr, JscSymbol symSemicolon) {
    super(symThrow, expr, symSemicolon);
  }
}
