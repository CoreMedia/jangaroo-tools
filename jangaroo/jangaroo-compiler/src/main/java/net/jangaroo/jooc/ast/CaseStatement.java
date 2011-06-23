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
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class CaseStatement extends KeywordStatement {

  private Expr expr;
  private JooSymbol symColon;

  public CaseStatement(JooSymbol symCase, Expr expr, JooSymbol symColon) {
    super(symCase);
    this.setExpr(expr);
    this.setSymColon(symColon);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitCaseStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    getExpr().scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getExpr().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    super.generateJsCode(out);
    getExpr().generateCode(out);
    out.writeSymbol(getSymColon());
  }

  public Expr getExpr() {
    return expr;
  }

  public void setExpr(Expr expr) {
    this.expr = expr;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

  public void setSymColon(JooSymbol symColon) {
    this.symColon = symColon;
  }
}
