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
 */
class SuperConstructorCallStatement extends Statement {

  Expr fun;
  ParenthesizedExpr<CommaSeparatedList<Expr>> args;
  private ClassDeclaration classDeclaration;

  public SuperConstructorCallStatement(JooSymbol symSuper, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen) {
    this.fun = new IdeExpr(symSuper);
    this.args = new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen);
  }

  @Override
  public void scope(final Scope scope) {
    FunctionDeclaration method = scope.getMethodDeclaration();
    if (method == null || !method.isConstructor()) {
      throw Jooc.error(getSymbol(), "must only call super constructor from constructor method");
    }
    if (method.containsSuperConstructorCall()) {
      throw Jooc.error(getSymbol(), "must not call super constructor twice");
    }
    method.setContainsSuperConstructorCall(true);
    fun.scope(scope);
    if (args != null)
      args.scope(scope);
    classDeclaration = scope.getClassDeclaration();
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    fun.analyze(this, context);
    if (args != null)
      args.analyze(this, context);
    return this;
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    if (classDeclaration.getInheritanceLevel() > 1) {
      generateFunCode(out);
      generateArgsCode(out);
      classDeclaration.generateFieldInitCode(out, true, false);
    } else { // suppress for classes extending Object
      // Object super call does nothing anyway:
      out.beginComment();
      out.writeSymbol(getSymbol());
      generateArgsCode(out);
      out.endComment();
      classDeclaration.generateFieldInitCode(out, false, false);
    }
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(getSymbol());
    out.writeToken("this.super$" + classDeclaration.getInheritanceLevel());
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    if (args != null)
      args.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

}
