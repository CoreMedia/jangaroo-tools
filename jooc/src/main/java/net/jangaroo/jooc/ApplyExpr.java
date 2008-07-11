/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ApplyExpr extends Expr {

  Expr fun;
  JooSymbol lParen;
  Arguments args;
  JooSymbol rParen;

  public ApplyExpr(Expr fun, JooSymbol lParen, Arguments args, JooSymbol rParen) {
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

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }


}
