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

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class SwitchStatement extends KeywordStatement {

  private ParenthesizedExpr cond;
  private BlockStatement block;

  public SwitchStatement(JooSymbol symSwitch, ParenthesizedExpr cond, JooSymbol lBrace, List<Directive> directives, JooSymbol rBrace) {
    super(symSwitch);
    this.cond = cond;
    this.block = new BlockStatement(lBrace, directives, rBrace);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitSwitchStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    withNewLabelScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        getCond().scope(scope);
        getBlock().scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getCond().analyze(this);
    getBlock().analyze(this);
  }

  public ParenthesizedExpr getCond() {
    return cond;
  }

  public BlockStatement getBlock() {
    return block;
  }

}
