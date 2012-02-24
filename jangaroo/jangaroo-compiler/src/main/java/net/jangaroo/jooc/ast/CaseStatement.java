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
public class CaseStatement extends KeywordStatement {

  private Expr expr;
  private JooSymbol symColon;

  public CaseStatement(JooSymbol symCase, Expr expr, JooSymbol symColon) {
    super(symCase);
    this.expr = expr;
    this.symColon = symColon;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), expr);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitCaseStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    getExpr().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getExpr().analyze(this);
  }

  public Expr getExpr() {
    return expr;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

}
