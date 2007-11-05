/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class ParenthesizedExpr extends Expr {

  JscSymbol lParen;
  Expr expr;
  JscSymbol rParen;

  public ParenthesizedExpr(JscSymbol lParen, Expr expr, JscSymbol rParen) {
    this.lParen = lParen;
    this.expr = expr;
    this.rParen = rParen;
  }

  public void analyze(AnalyzeContext context) {
    expr.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    expr.generateCode(out);
    out.writeSymbol(rParen);
  }

  public JscSymbol getSymbol() {
    return lParen;
  }

}
