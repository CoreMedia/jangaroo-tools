/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ParenthesizedExpr extends Expr {

  JooSymbol lParen;
  Expr expr;
  JooSymbol rParen;

  public ParenthesizedExpr(JooSymbol lParen, Expr expr, JooSymbol rParen) {
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

  public JooSymbol getSymbol() {
    return lParen;
  }

}
