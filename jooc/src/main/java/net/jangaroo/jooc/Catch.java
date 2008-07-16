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
