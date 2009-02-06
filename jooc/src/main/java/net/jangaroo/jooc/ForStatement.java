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
class ForStatement extends ConditionalLoopStatement {

  JooSymbol lParen;
  ForInitializer forInit;
  JooSymbol symSemicolon1;
  Expr optCond;
  JooSymbol symSemicolon2;
  Expr optStep;
  JooSymbol rParen;

  public ForStatement(JooSymbol symFor, JooSymbol lParen, ForInitializer forInit, JooSymbol symSemicolon1, Expr optCond, JooSymbol symSemicolon2, Expr optStep, JooSymbol rParen, Statement body) {
    super(symFor, optCond, body);
    this.lParen = lParen;
    this.forInit = forInit;
    this.symSemicolon1 = symSemicolon1;
    this.symSemicolon2 = symSemicolon2;
    this.optStep = optStep;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (forInit!=null)
      forInit.generateCode(out);
    out.writeSymbol(symSemicolon1);
    super.generateLoopHeaderCode(out);
    out.writeSymbol(symSemicolon2);
    if (optStep != null)
      optStep.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (forInit!=null)
      forInit.analyze(this, context);
    super.analyzeLoopHeader(context);
  }

}
