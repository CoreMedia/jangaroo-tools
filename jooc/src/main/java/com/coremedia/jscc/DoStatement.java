/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class DoStatement extends ConditionalLoopStatement {
  JscSymbol symWhile;
  JscSymbol symSemicolon;

  public DoStatement(JscSymbol symDo, Statement statement, JscSymbol symWhile, ParenthesizedExpr cond, JscSymbol symSemicolon) {
    super(symDo, cond, statement);
    this.symWhile = symWhile;
    this.symSemicolon = symSemicolon;
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
  }

  protected void analyzeLoopFooter(AnalyzeContext context) {
    optCond.analyze(context);
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
  }

  protected void generateLoopFooterCode(JsWriter out) throws IOException {
    out.writeSymbol(symWhile);
    optCond.generateCode(out);
    out.writeSymbol(symSemicolon);
  }
}
