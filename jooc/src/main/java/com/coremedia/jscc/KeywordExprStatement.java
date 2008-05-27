/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

abstract class KeywordExprStatement extends ExprStatement {

  JscSymbol symKeyword;

  protected KeywordExprStatement(JscSymbol symKeyword, Expr optExpr, JscSymbol symSemicolon) {
    super(optExpr, symSemicolon);
    this.symKeyword = symKeyword;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symKeyword);
    super.generateCode(out);
  }

  public JscSymbol getSymbol() {
    return symKeyword;
  }

}
