/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class ReturnStatement extends KeywordExprStatement {

  public ReturnStatement(JooSymbol symReturn, Expr optExpr, JooSymbol symSemicolon) {
    super(symReturn, optExpr, symSemicolon);
  }
}
