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
class StaticInitializer extends Declaration {

  BlockStatement block;

  public StaticInitializer(BlockStatement block) {
    super(new JooSymbol[]{ }, MODIFIER_STATIC);
    this.block = block;
  }

  @Override
  protected void generateAsApiCode(final JsWriter out) throws IOException {
    //skip it
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    out.write("function()");
    block.generateCode(out);
    out.write(",");
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    // pop non-static member scope
    withNewDeclarationScope(this, scope.getParentScope(), new Scoped() {
      @Override
      public void run(final Scope scope) {
        block.scope(scope);
      }
    });
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    block.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return symModifiers[0];
  }
}
