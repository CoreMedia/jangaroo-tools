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

  protected void generateJsCode(JsWriter out) throws IOException {
    generateFunJsCode(out);
    if (args != null)
      args.generateCode(out);
  }

  private void generateFunJsCode(JsWriter out) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    if (isTypeCast()) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      if (fun instanceof IdeExpr) {
        // take care of reserved words called as functions (Rhino does not like):
        JooSymbol funSymbol = ((IdeExpr)fun).ide.getSymbol();
        if (SyntacticKeywords.RESERVED_WORDS.contains(funSymbol.getText())) {
          out.writeSymbolWhitespace(funSymbol);
          out.writeToken("$$" + funSymbol.getText());
          return;
        }
      }
      fun.generateCode(out);
    }
  }

  private boolean isTypeCast() {
    return fun instanceof IdeExpr && !isInsideNewExpr() && isNonTopLevelType((IdeExpr)fun);
  }

  private boolean isNonTopLevelType(IdeExpr fun) {
    final Ide ide = fun.ide;
    IdeDeclaration declaration = ide.getDeclaration(false);
    return declaration != null &&
      (declaration instanceof ClassDeclaration || declaration instanceof PredefinedTypeDeclaration ||
        (declaration instanceof FunctionDeclaration && declaration.isConstructor()))
      && (declaration.isClassMember() ||
      (declaration.getParentDeclaration() instanceof PackageDeclaration
        && ((PackageDeclaration)declaration.getParentDeclaration()).getQualifiedName().length > 0));
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
