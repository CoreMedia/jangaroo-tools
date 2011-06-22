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
class ObjectField extends NodeImplBase {

  AstNode label;
  JooSymbol symColon;
  Expr value;

  public ObjectField(AstNode node, JooSymbol symColon, Expr value) {
    this.label = node;
    this.symColon = symColon;
    this.value = value;
  }

  @Override
  public void scope(final Scope scope) {
    label.scope(scope);
    value.scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    label.analyze(this, context);
    value.analyze(this, context);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    label.generateCode(out);
    out.writeSymbol(symColon);
    value.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return label.getSymbol();
  }


}
