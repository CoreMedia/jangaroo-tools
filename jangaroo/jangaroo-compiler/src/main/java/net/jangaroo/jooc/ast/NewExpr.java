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
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.ParenthesizedExpr;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class NewExpr extends Expr {

  private JooSymbol symNew;
  private Expr applyConstructor;
  private ParenthesizedExpr<CommaSeparatedList<Expr>> args;

  public NewExpr(JooSymbol symNew, Expr applyConstructor) {
    this.setSymNew(symNew);
    this.setApplyConstructor(applyConstructor);
    if (applyConstructor instanceof ApplyExpr) {
      ((ApplyExpr)applyConstructor).setInsideNewExpr(true);
    }
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitNewExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    getApplyConstructor().scope(scope);
    if (getArgs() != null)
      getArgs().scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getApplyConstructor().analyze(this, context);
    if (getArgs() != null)
      getArgs().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(getSymNew());
    getApplyConstructor().generateCode(out);
    if (getArgs() != null)
      getArgs().generateCode(out);
  }

  public JooSymbol getSymbol() {
      return getSymNew();
  }

  public JooSymbol getSymNew() {
    return symNew;
  }

  public void setSymNew(JooSymbol symNew) {
    this.symNew = symNew;
  }

  public Expr getApplyConstructor() {
    return applyConstructor;
  }

  public void setApplyConstructor(Expr applyConstructor) {
    this.applyConstructor = applyConstructor;
  }

  public ParenthesizedExpr<CommaSeparatedList<Expr>> getArgs() {
    return args;
  }

  public void setArgs(ParenthesizedExpr<CommaSeparatedList<Expr>> args) {
    this.args = args;
  }
}
