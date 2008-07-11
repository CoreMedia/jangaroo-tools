/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class ThrowStatement extends KeywordExprStatement {

  public ThrowStatement(JooSymbol symThrow, Expr expr, JooSymbol symSemicolon) {
    super(symThrow, expr, symSemicolon);
  }
}
