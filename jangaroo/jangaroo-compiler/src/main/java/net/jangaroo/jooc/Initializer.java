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
class Initializer extends NodeImplBase {

  JooSymbol symEq;
  Expr value;

  public Initializer(JooSymbol symEq, Expr value) {
    this.symEq = symEq;
    this.value = value;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitInitializer(this);
  }

  @Override
  public void scope(final Scope scope) {
    value.scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    value.analyze(this, context);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(symEq);
    value.generateCode(out);
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    //todo clarify whether initializers are part of public API
    if (value instanceof LiteralExpr) {
      super.generateAsApiCode(out);
    }
  }

  public JooSymbol getSymbol() {
      return symEq;
  }

}
