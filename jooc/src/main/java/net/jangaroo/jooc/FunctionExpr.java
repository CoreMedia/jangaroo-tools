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
class FunctionExpr extends Expr {

  JooSymbol symFun;
  Ide ide;
  JooSymbol lParen;
  Parameters params;
  JooSymbol rParen;
  TypeRelation optTypeRelation;
  BlockStatement body;

  ClassDeclaration classDeclaration;

  public FunctionExpr(JooSymbol symFun, Ide ide, JooSymbol lParen, Parameters params, JooSymbol rParen, TypeRelation optTypeRelation, BlockStatement body) {
    this.symFun = symFun;
    this.ide = ide;
    this.lParen = lParen;
    this.params = params;
    this.rParen = rParen;
    this.optTypeRelation = optTypeRelation;
    this.body = body;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void analyze(AnalyzeContext context) {
    classDeclaration = context.getCurrentClass();
    Debug.assertTrue(classDeclaration != null, "classDeclaration != null");
    super.analyze(context);
    context.enterScope(this);
    if (params != null)
      params.analyze(context);
    if (optTypeRelation != null)
      optTypeRelation.analyze(context);
    body.analyze(context);
    context.leaveScope(this);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symFun);
    if (ide!=null) {
      out.writeToken(ide.getName());
    } else if (out.getKeepSource())
      out.writeToken(out.getFunctionNameAsIde(this));
    out.writeSymbol(lParen);
    if (params != null) params.generateCode(out);
    out.writeSymbol(rParen);
    if (optTypeRelation != null) optTypeRelation.generateCode(out);
    body.generateCode(out);
  }

  public JooSymbol getSymbol() {
     return symFun;
  }

  boolean isCompileTimeConstant() {
    return true;
  }
}
