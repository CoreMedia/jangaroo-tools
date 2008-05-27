/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class ForStatement extends ConditionalLoopStatement {

  JscSymbol lParen;
  ForInitializer forInit;
  JscSymbol symSemicolon1;
  Expr optCond;
  JscSymbol symSemicolon2;
  Expr optStep;
  JscSymbol rParen;

  public ForStatement(JscSymbol symFor, JscSymbol lParen, ForInitializer forInit, JscSymbol symSemicolon1, Expr optCond, JscSymbol symSemicolon2, Expr optStep, JscSymbol rParen, Statement body) {
    super(symFor, optCond, body);
    this.lParen = lParen;
    this.forInit = forInit;
    this.symSemicolon1 = symSemicolon1;
    this.symSemicolon2 = symSemicolon2;
    this.optStep = optStep;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (forInit!=null)
      forInit.generateCode(out);
    out.writeSymbol(symSemicolon1);
    super.generateLoopHeaderCode(out);
    out.writeSymbol(symSemicolon2);
    if (optStep != null)
      optStep.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (forInit!=null)
      forInit.analyze(context);
    super.analyzeLoopHeader(context);
  }

}
