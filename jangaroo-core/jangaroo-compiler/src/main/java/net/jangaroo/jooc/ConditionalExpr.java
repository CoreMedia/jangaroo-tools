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
class ConditionalExpr extends Expr {

  Expr cond;
  JooSymbol symQuestion;
  Expr ifTrue;
  JooSymbol symColon;
  Expr ifFalse;

  public ConditionalExpr(Expr cond, JooSymbol symQuestion, Expr ifTrue, JooSymbol symColon, Expr ifFalse) {
    this.cond = cond;
    this.symQuestion = symQuestion;
    this.ifTrue = ifTrue;
    this.symColon = symColon;
    this.ifFalse = ifFalse;
  }

  @Override
  public void scope(final Scope scope) {
    cond.scope(scope);
    ifTrue.scope(scope);
    ifFalse.scope(scope);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    cond.generateCode(out);
    out.writeSymbol(symQuestion);
    ifTrue.generateCode(out);
    out.writeSymbol(symColon);
    ifFalse.generateCode(out);
  }

  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    cond = cond.analyze(this, context);
    ifTrue = ifTrue.analyze(this, context);
    ifFalse = ifFalse.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
     return cond.getSymbol();
  }

  boolean isCompileTimeConstant() {
    return cond.isCompileTimeConstant() && ifTrue.isCompileTimeConstant() && ifFalse.isCompileTimeConstant();
  }
}
