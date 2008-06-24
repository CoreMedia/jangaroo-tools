/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ApplyExpr extends Expr {

  Expr fun;
  JscSymbol lParen;
  Arguments args;
  JscSymbol rParen;

  public ApplyExpr(Expr fun, JscSymbol lParen, Arguments args, JscSymbol rParen) {
    this.fun = fun;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    fun.generateCode(out);
  }

  public void generateCode(JsWriter out) throws IOException {
    generateFunCode(out);
    generateArgsCode(out);
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public void analyze(AnalyzeContext context) {
    fun.analyze(context);
    if (args != null)
      args.analyze(context);
  }

  public JscSymbol getSymbol() {
    return fun.getSymbol();
  }


}
