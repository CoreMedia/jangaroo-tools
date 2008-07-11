/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class DoStatement extends ConditionalLoopStatement {
  JooSymbol symWhile;
  JooSymbol symSemicolon;

  public DoStatement(JooSymbol symDo, Statement statement, JooSymbol symWhile, ParenthesizedExpr cond, JooSymbol symSemicolon) {
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
