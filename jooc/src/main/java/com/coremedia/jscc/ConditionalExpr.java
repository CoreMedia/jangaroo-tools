/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ConditionalExpr extends Expr {

  Expr cond;
  JscSymbol symQuestion;
  Expr ifTrue;
  JscSymbol symColon;
  Expr ifFalse;

  public ConditionalExpr(Expr cond, JscSymbol symQuestion, Expr ifTrue, JscSymbol symColon, Expr ifFalse) {
    this.cond = cond;
    this.symQuestion = symQuestion;
    this.ifTrue = ifTrue;
    this.symColon = symColon;
    this.ifFalse = ifFalse;
  }

  public void generateCode(JsWriter out) throws IOException {
    cond.generateCode(out);
    out.writeSymbol(symQuestion);
    ifTrue.generateCode(out);
    out.writeSymbol(symColon);
    ifFalse.generateCode(out);
  }

  public void analyze(AnalyzeContext context) {
    cond.analyze(context);
    ifTrue.analyze(context);
    ifFalse.analyze(context);
  }

  public JscSymbol getSymbol() {
     return cond.getSymbol();
  }

  boolean isCompileTimeConstant() {
    return cond.isCompileTimeConstant() && ifTrue.isCompileTimeConstant() && ifFalse.isCompileTimeConstant();
  }
}
