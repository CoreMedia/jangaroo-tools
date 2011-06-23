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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public abstract class ConditionalLoopStatement extends LoopStatement {

  private Expr optCond;

  public ConditionalLoopStatement(JooSymbol symLoop, Expr optCond, Statement body) {
    super(symLoop, body);
    this.setOptCond(optCond);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getOptCond() != null) {
      getOptCond().scope(scope);
    }
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (getOptCond() != null)
      getOptCond().analyze(this, context);
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    if (getOptCond() != null)
      getOptCond().generateJsCode(out);
  }


  public Expr getOptCond() {
    return optCond;
  }

  public void setOptCond(Expr optCond) {
    this.optCond = optCond;
  }
}
