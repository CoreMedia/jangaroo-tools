/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 */
class TryStatement extends KeywordStatement {

  BlockStatement block;
  ArrayList catches;
  JooSymbol symFinally;
  BlockStatement finallyBlock;

  public TryStatement(JooSymbol symTry, BlockStatement block, ArrayList catches) {
    this(symTry, block, catches, null, null);
  }

  public TryStatement(JooSymbol symTry, BlockStatement block, ArrayList catches, JooSymbol symFinally, BlockStatement finallyBlock) {
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
