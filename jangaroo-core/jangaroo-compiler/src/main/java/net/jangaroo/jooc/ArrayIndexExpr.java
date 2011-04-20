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
class ArrayIndexExpr extends Expr {

  private Expr array;
  private ParenthesizedExpr indexExpr;

  public ArrayIndexExpr(Expr array, JooSymbol lBrac, Expr index, JooSymbol rBrac) {
    this.array = array;
    this.indexExpr = new ParenthesizedExpr(lBrac, index, rBrac);
  }

  @Override
  public void scope(final Scope scope) {
    array.scope(scope);
    indexExpr.scope(scope);
  }

  public JooSymbol getSymbol() {
    return array.getSymbol();
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    array.analyze(this, context);
    indexExpr.analyze(this, context);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    array.generateCode(out);
    indexExpr.generateCode(out);
  }
}
