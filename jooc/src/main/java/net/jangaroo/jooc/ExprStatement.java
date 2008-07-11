/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ExprStatement extends Statement {

  Expr optExpr;
  JooSymbol symSemicolon;

  public ExprStatement(Expr optExpr, JooSymbol symSemicolon) {
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

  public JooSymbol getSymbol() {
    return optExpr == null ? symSemicolon : optExpr.getSymbol(); 
  }

}
