/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
abstract class KeywordExprStatement extends ExprStatement {

  JooSymbol symKeyword;

  protected KeywordExprStatement(JooSymbol symKeyword, Expr optExpr, JooSymbol symSemicolon) {
    super(optExpr, symSemicolon);
    this.symKeyword = symKeyword;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symKeyword);
    super.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return symKeyword;
  }

}
