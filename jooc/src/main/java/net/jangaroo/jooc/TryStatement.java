/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
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
