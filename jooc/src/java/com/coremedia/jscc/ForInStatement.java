/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class ForInStatement extends LoopStatement {

  JscSymbol lParen;
  JscSymbol symVar;
  Ide ide;
  JscSymbol symIn;
  Expr expr;
  JscSymbol rParen;

  public ForInStatement(JscSymbol symFor, JscSymbol lParen, JscSymbol symVar, Ide ide, JscSymbol symIn, Expr expr, JscSymbol rParen, Statement body) {
    super(symFor, body);
    this.lParen = lParen;
    this.symVar = symVar;
    this.ide = ide;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    out.writeSymbol(symVar);
    ide.generateCode(out);
    out.writeSymbol(symIn);
    expr.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    ide.analyze(context);
    expr.analyze(context);
  }

}
