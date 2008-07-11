/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class DeleteStatement extends KeywordExprStatement {

  public DeleteStatement(JooSymbol symDelete, Expr expr, JooSymbol symSemicolon) {
    super(symDelete, expr, symSemicolon);
  }
}
