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
public abstract class ConditionalLoopStatement extends LoopStatement {

  private Expr optCond;

  public ConditionalLoopStatement(JooSymbol symLoop, Expr optCond, Statement body) {
    super(symLoop, body);
    this.optCond = optCond;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optCond);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getOptCond() != null) {
      getOptCond().scope(scope);
    }
  }

  protected void analyzeLoopHeader() {
    if (getOptCond() != null) {
      getOptCond().analyze(this);
    }
  }

  public Expr getOptCond() {
    return optCond;
  }

}
