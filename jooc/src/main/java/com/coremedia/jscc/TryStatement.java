/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;
import java.util.ArrayList;

class TryStatement extends KeywordStatement {

  BlockStatement block;
  ArrayList catches;
  JscSymbol symFinally;
  BlockStatement finallyBlock;

  public TryStatement(JscSymbol symTry, BlockStatement block, ArrayList catches) {
    this(symTry, block, catches, null, null);
  }

  public TryStatement(JscSymbol symTry, BlockStatement block, ArrayList catches, JscSymbol symFinally, BlockStatement finallyBlock) {
    super(symTry);
    this.block = block;
    this.catches = catches;
    this.symFinally = symFinally;
    this.finallyBlock = finallyBlock;
  }

  public void analyze(AnalyzeContext context) {
    block.analyze(context);
    analyze(catches, context);
    if (finallyBlock != null)
      finallyBlock.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    block.generateCode(out);
    generateCode(catches, out);
    if (symFinally != null) {
      out.writeSymbol(symFinally);
      finallyBlock.generateCode(out);
    }
   }
}
