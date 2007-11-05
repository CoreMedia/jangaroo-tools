/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

class DeleteStatement extends KeywordExprStatement {

  public DeleteStatement(JscSymbol symDelete, Expr expr, JscSymbol symSemicolon) {
    super(symDelete, expr, symSemicolon);
  }
}
