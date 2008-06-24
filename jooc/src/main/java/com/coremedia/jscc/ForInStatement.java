/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ForInStatement extends LoopStatement {

  JscSymbol lParen;
  Declaration decl;
  JscSymbol symIn;
  Expr expr;
  JscSymbol rParen;

  public ForInStatement(JscSymbol symFor, JscSymbol lParen, Declaration decl, JscSymbol symIn, Expr expr, JscSymbol rParen, Statement body) {
    super(symFor, body);
    this.lParen = lParen;
    this.decl = decl;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    decl.generateCode(out);
    out.writeSymbol(symIn);
    expr.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    decl.analyze(context);
    expr.analyze(context);
  }

}
