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

import java.util.List;


/**
 * @author Andreas Gawecki
 */
public abstract class LoopStatement extends KeywordStatement {

  private Statement body;

  public LoopStatement(JooSymbol symLoop, Statement body) {
    super(symLoop);
    this.setBody(body);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), body);
  }

  @Override
  public void scope(final Scope scope) {
    withNewLabelScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        getBody().scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    analyzeLoopHeader();
    getBody().analyze(this);
    analyzeLoopFooter();
  }

  protected abstract void analyzeLoopHeader();

  protected void analyzeLoopFooter() {
  }

  public Statement getBody() {
    return body;
  }

  public void setBody(Statement body) {
    this.body = body;
  }
}
