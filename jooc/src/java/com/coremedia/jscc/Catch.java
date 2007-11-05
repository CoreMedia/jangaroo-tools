/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class Catch extends KeywordStatement {

  JscSymbol lParen;
  Parameter param;
  JscSymbol rParen;
  BlockStatement block;

  public Catch(JscSymbol symCatch, JscSymbol lParen, Parameter param, JscSymbol rParen, BlockStatement block) {
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
