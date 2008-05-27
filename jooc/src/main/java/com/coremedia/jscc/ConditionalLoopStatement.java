/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

abstract class ConditionalLoopStatement extends LoopStatement {

  Expr optCond;

  public ConditionalLoopStatement(JscSymbol symLoop, Expr optCond, Statement body) {
    super(symLoop, body);
    this.optCond = optCond;
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (optCond != null)
      optCond.analyze(context);
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    if (optCond != null)
      optCond.generateCode(out);
  }


}
