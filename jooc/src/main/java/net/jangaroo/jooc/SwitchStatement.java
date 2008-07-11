/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 */
class SwitchStatement extends KeywordStatement {

  ParenthesizedExpr cond;
  BlockStatement block;

  public SwitchStatement(JooSymbol symSwitch, ParenthesizedExpr cond, JooSymbol lBrace, ArrayList statements, JooSymbol rBrace) {
    super(symSwitch);
    this.cond = cond;
    this.block = new BlockStatement(lBrace, statements, rBrace);
  }


  public void analyze(AnalyzeContext context) {
    cond.analyze(context);
    context.getScope().enterSwitch(this);
    block.analyze(context);
    context.getScope().exitSwitch(this);
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    cond.generateCode(out);
    block.generateCode(out);
  }

}
