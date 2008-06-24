/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
class DeleteStatement extends KeywordExprStatement {

  public DeleteStatement(JscSymbol symDelete, Expr expr, JscSymbol symSemicolon) {
    super(symDelete, expr, symSemicolon);
  }
}
