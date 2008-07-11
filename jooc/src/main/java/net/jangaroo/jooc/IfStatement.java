/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class IfStatement extends KeywordStatement {

  Expr cond;
  Statement ifTrue;
  JooSymbol symElse;
  Statement ifFalse;

  public IfStatement(JooSymbol symIf, Expr cond, Statement ifTrue) {
    this(symIf, cond, ifTrue, null, null);
  }

  public IfStatement(JooSymbol symIf, Expr cond, Statement ifTrue, JooSymbol symElse, Statement ifFalse) {
    super(symIf);
    this.cond = cond;
    this.ifTrue = ifTrue;
    this.symElse = symElse;
    this.ifFalse = ifFalse;
  }

  public void analyze(AnalyzeContext context) {
    cond.analyze(context);
    ifTrue.analyze(context);
    if (ifFalse != null)
      ifFalse.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    cond.generateCode(out);
    ifTrue.generateCode(out);
    if (symElse != null) {
      out.writeSymbol(symElse);
      ifFalse.generateCode(out);
    }
  }

}
