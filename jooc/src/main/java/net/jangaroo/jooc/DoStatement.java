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
class DoStatement extends ConditionalLoopStatement {
  JooSymbol symWhile;
  JooSymbol symSemicolon;

  public DoStatement(JooSymbol symDo, Statement statement, JooSymbol symWhile, ParenthesizedExpr cond, JooSymbol symSemicolon) {
    super(symDo, cond, statement);
    this.symWhile = symWhile;
    this.symSemicolon = symSemicolon;
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
  }

  protected void analyzeLoopFooter(AnalyzeContext context) {
    optCond.analyze(context);
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
  }

  protected void generateLoopFooterCode(JsWriter out) throws IOException {
    out.writeSymbol(symWhile);
    optCond.generateCode(out);
    out.writeSymbol(symSemicolon);
  }
}
