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
 * @author Frank Wienberg
 */
class ApplyExpr extends Expr {

  Expr fun;
  ParenthesizedExpr<CommaSeparatedList<Expr>> args;

  private boolean insideNewExpr = false;

  public ApplyExpr(Expr fun, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen) {
    this.fun = fun;
    this.args = new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen);
  }

  public boolean isInsideNewExpr() {
    return insideNewExpr;
  }

  public void setInsideNewExpr(final boolean insideNewExpr) {
    this.insideNewExpr = insideNewExpr;
  }

  @Override
  public void scope(final Scope scope) {
    fun.scope(scope);
    args.scope(scope);
  }

  public void generateCode(JsWriter out) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    // let through typecast to String
    if (isTypeCast() && !(fun instanceof IdeExpr && ((IdeExpr)fun).ide.getQualifiedNameStr().equals("String"))) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      fun.generateCode(out);
    }
    if (args != null)
      args.generateCode(out);
  }

  private boolean isTypeCast() {
    return fun instanceof IdeExpr && !isInsideNewExpr() && isType((IdeExpr)fun);
  }

  private boolean isType(IdeExpr fun) {
    final Ide ide = fun.ide;
    AstNode declaration = ide.getDeclaration(false);
    return declaration != null &&
      (declaration instanceof ClassDeclaration || declaration instanceof PredefinedTypeDeclaration ||
        (declaration instanceof FunctionDeclaration && ((FunctionDeclaration)declaration).isConstructor()));
  }

  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    fun = fun.analyze(this, context);
    if (args != null)
      args.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

}
