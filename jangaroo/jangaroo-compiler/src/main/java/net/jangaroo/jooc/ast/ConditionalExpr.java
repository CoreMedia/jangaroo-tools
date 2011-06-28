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
public class ConditionalExpr extends Expr {

  private Expr cond;
  private JooSymbol symQuestion;
  private Expr ifTrue;
  private JooSymbol symColon;
  private Expr ifFalse;

  public ConditionalExpr(Expr cond, JooSymbol symQuestion, Expr ifTrue, JooSymbol symColon, Expr ifFalse) {
    this.cond = cond;
    this.symQuestion = symQuestion;
    this.ifTrue = ifTrue;
    this.symColon = symColon;
    this.ifFalse = ifFalse;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitConditionalExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    getCond().scope(scope);
    getIfTrue().scope(scope);
    getIfFalse().scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getCond().analyze(this, context);
    getIfTrue().analyze(this, context);
    getIfFalse().analyze(this, context);
  }

  public JooSymbol getSymbol() {
     return getCond().getSymbol();
  }

  public boolean isCompileTimeConstant() {
    return getCond().isCompileTimeConstant() && getIfTrue().isCompileTimeConstant() && getIfFalse().isCompileTimeConstant();
  }

  public Expr getCond() {
    return cond;
  }

  public JooSymbol getSymQuestion() {
    return symQuestion;
  }

  public Expr getIfTrue() {
    return ifTrue;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

  public Expr getIfFalse() {
    return ifFalse;
  }

}
