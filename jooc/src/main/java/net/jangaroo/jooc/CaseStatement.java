/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class CaseStatement extends KeywordStatement {

  Expr expr;
  JooSymbol symColon;

  public CaseStatement(JooSymbol symCase, Expr expr, JooSymbol symColon) {
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
