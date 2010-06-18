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
class ForInitializer extends NodeImplBase {

  Declaration decl = null;
  Expr expr = null;

  public ForInitializer(Declaration decl) {
    this.decl = decl;
  }

  public ForInitializer(Expr expr) {
    this.expr = expr;
  }

  @Override
  public void scope(final Scope scope) {
    if (decl!=null)
      decl.scope(scope);
    if (expr!=null)
      expr.scope(scope);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (decl!=null)
      decl.analyze(this, context);
    if (expr!=null)
      expr = expr.analyze(this, context);
    return this;
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    if (decl != null)
      decl.generateCode(out);
    else if (expr != null)
      expr.generateCode(out);
  }

  public JooSymbol getSymbol() {
     return decl != null ? decl.getSymbol() : expr.getSymbol();
  }
  
}
