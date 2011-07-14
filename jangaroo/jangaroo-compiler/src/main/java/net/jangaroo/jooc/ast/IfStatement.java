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

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class IfStatement extends KeywordStatement {

  private Expr cond;
  private Statement ifTrue;
  private JooSymbol symElse;
  private Statement ifFalse;

  public IfStatement(JooSymbol symIf, Expr cond, Statement ifTrue) {
    this(symIf, cond, ifTrue, null, null);
  }

  public IfStatement(JooSymbol symIf, Expr cond, Statement ifTrue, JooSymbol symElse, Statement ifFalse) {
    super(symIf);
    this.cond = cond;
    this.ifTrue = ifTrue;
    this.symElse = symElse;
    this.ifFalse = ifFalse;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIfStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    getCond().scope(scope);
    getIfTrue().scope(scope);
    if (getIfFalse() != null) {
      getIfFalse().scope(scope);
    }
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getCond().analyze(this, context);
    getIfTrue().analyze(this, context);
    if (getIfFalse() != null) {
      getIfFalse().analyze(this, context);
    }
  }

  public Expr getCond() {
    return cond;
  }

  public Statement getIfTrue() {
    return ifTrue;
  }

  public JooSymbol getSymElse() {
    return symElse;
  }

  public Statement getIfFalse() {
    return ifFalse;
  }

}
