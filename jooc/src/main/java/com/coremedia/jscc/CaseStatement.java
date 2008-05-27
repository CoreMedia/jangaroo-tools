/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class CaseStatement extends KeywordStatement {

  Expr expr;
  JscSymbol symColon;

  public CaseStatement(JscSymbol symCase, Expr expr, JscSymbol symColon) {
    super(symCase);
    this.expr = expr;
    this.symColon = symColon;
  }

  public void analyze(AnalyzeContext context) {
    expr.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    expr.generateCode(out);
    out.writeSymbol(symColon);
  }

}
