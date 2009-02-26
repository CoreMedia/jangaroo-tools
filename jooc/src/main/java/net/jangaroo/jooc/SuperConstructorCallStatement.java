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

  JooSymbol symSuper;
  JooSymbol lParen;
  Arguments args;
  JooSymbol rParen;
  Expr fun;

  public SuperConstructorCallStatement(JooSymbol symSuper, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    this.symSuper = symSuper;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
    this.fun = new SuperExpr(symSuper);
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    MethodDeclaration method = context.getCurrentMethod();
    if (method == null || !method.isConstructor()) {
      Jooc.error(symSuper, "must only call super constructor from constructor method");
    }
    if (method.containsSuperConstructorCall()) {
      Jooc.error(symSuper, "must not call super constructor twice");
    }
    method.setContainsSuperConstructorCall(true);

    if (args != null)
      args.analyze(this, context);
    return this;
  }

  public void generateCode(JsWriter out) throws IOException {
    generateFunCode(out);
    generateArgsCode(out);
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSuper);
    out.writeToken("this[$super]");
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

}
