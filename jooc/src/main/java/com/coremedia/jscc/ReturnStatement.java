/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
class ReturnStatement extends KeywordExprStatement {

  public ReturnStatement(JscSymbol symReturn, Expr optExpr, JscSymbol symSemicolon) {
    super(symReturn, optExpr, symSemicolon);
  }
}
