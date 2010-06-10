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
import java.util.List;

/**
 * @author Andreas Gawecki
 */
class SwitchStatement extends KeywordStatement {

  ParenthesizedExpr cond;
  BlockStatement block;

  public SwitchStatement(JooSymbol symSwitch, ParenthesizedExpr cond, JooSymbol lBrace, List<AstNode> statements, JooSymbol rBrace) {
    super(symSwitch);
    this.cond = cond;
    this.block = new BlockStatement(lBrace, statements, rBrace);
  }

  @Override
  public void scope(final Scope scope) {
    withNewLabelScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        cond.scope(scope);
        block.scope(scope);
      }
    });
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    cond.analyze(this, context);
    block.analyze(this, context);
    return this;
  }

  public void generateCode(JsWriter out) throws IOException {
    super.generateCode(out);
    cond.generateCode(out);
    block.generateCode(out);
  }

}
