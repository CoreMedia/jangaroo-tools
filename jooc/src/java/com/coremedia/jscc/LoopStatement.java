/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

abstract class LoopStatement extends KeywordStatement {

  Statement body;

  public LoopStatement(JscSymbol symLoop, Statement body) {
    super(symLoop);
    this.body = body;
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    generateLoopHeaderCode(out);
    body.generateCode(out);
    generateLoopFooterCode(out);
  }

  protected abstract void generateLoopHeaderCode(JsWriter out) throws IOException;

  protected void generateLoopFooterCode(JsWriter out) throws IOException {
  }

  public void analyze(AnalyzeContext context) {
    context.getScope().enterLoop(this);
    analyzeLoopHeader(context);
    body.analyze(context);
    analyzeLoopFooter(context);
    context.getScope().exitLoop(this);
  }

  protected abstract void analyzeLoopHeader(AnalyzeContext context);

  protected void analyzeLoopFooter(AnalyzeContext context) {
  }

}
