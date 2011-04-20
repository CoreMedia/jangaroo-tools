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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
class ApplyExpr extends Expr {

  Expr fun;
  ParenthesizedExpr<CommaSeparatedList<Expr>> args;

  private boolean insideNewExpr = false;
  private static final Set<String> COERCE_FUNCTION_NAMES = new HashSet<String>(Arrays.asList("Number", "String", "Boolean", "int", "uint", "Date", "Array", "RegExp"));

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
    if (args != null) {
      boolean isAssert = fun instanceof IdeExpr && SyntacticKeywords.ASSERT.equals(fun.getSymbol().getText());
      if (isAssert) {
        JooSymbol symKeyword = fun.getSymbol();
        out.writeSymbol(args.lParen);
        args.expr.generateCode(out);
        out.writeToken(", ");
        out.writeString(symKeyword.getFileName());
        out.writeToken(", ");
        out.writeInt(symKeyword.getLine());
        out.write(", ");
        out.writeInt(symKeyword.getColumn());
        out.writeSymbol(args.rParen);
      } else {
        args.generateCode(out);
      }
    }
  }

  private void generateFunJsCode(JsWriter out) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    if (isTypeCast()) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      fun.generateCode(out);
    }
  }

  private boolean isTypeCast() {
    return fun instanceof IdeExpr && !isInsideNewExpr() && isNonCoercingType((IdeExpr)fun);
  }

  private boolean isNonCoercingType(IdeExpr fun) {
    final Ide ide = fun.ide;
    IdeDeclaration declaration = ide.getDeclaration(false);
    return declaration != null &&
      (declaration instanceof ClassDeclaration || declaration instanceof PredefinedTypeDeclaration ||
        (declaration instanceof FunctionDeclaration && declaration.isConstructor()))
      && (declaration.isClassMember() || !COERCE_FUNCTION_NAMES.contains(declaration.getQualifiedNameStr())
      );
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    fun.analyze(this, context);
    if (args != null)
      args.analyze(this, context);
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

}
