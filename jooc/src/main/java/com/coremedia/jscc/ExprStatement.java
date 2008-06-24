/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ExprStatement extends Statement {

  Expr optExpr;
  JscSymbol symSemicolon;

  public ExprStatement(Expr optExpr, JscSymbol symSemicolon) {
    this.optExpr = optExpr;
    this.symSemicolon = symSemicolon;
  }

  public void generateCode(JsWriter out) throws IOException {
    if (optExpr != null)
      optExpr.generateCode(out);
    out.writeSymbol(symSemicolon);
  }

  public void analyze(AnalyzeContext context) {
     if (optExpr != null)
       optExpr.analyze(context);
  }

  public JscSymbol getSymbol() {
    return optExpr == null ? symSemicolon : optExpr.getSymbol(); 
  }

}
