/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Catch extends KeywordStatement {

  JooSymbol lParen;
  Parameter param;
  JooSymbol rParen;
  BlockStatement block;

  public Catch(JooSymbol symCatch, JooSymbol lParen, Parameter param, JooSymbol rParen, BlockStatement block) {
    super(symCatch);
    this.lParen = lParen;
    this.param = param;
    this.rParen = rParen;
    this.block = block;
  }

  public void generateCode(JsWriter out) throws IOException {
     super.generateCode(out);
     out.writeSymbol(lParen);
     param.generateCode(out);
     out.writeSymbol(rParen);
     block.generateCode(out);
   }

  public void analyze(AnalyzeContext context) {
    param.analyze(context);
    block.analyze(context);
  }

}
