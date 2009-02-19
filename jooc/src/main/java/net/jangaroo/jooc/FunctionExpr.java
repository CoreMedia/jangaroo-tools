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
  private boolean thisUsed;

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

  public void analyze(Node parentNode, AnalyzeContext context) {
    classDeclaration = context.getCurrentClass();
    Debug.assertTrue(classDeclaration != null, "classDeclaration != null");
    super.analyze(parentNode, context);
    if (ide!=null) {
      context.getScope().declareIde(ide.getName(), this);
    }
    context.enterScope(this);
    if (params != null)
      params.analyze(this, context);
    if (context.getScope().getIdeDeclaration("arguments")==null) {
      context.getScope().declareIde("arguments", this); // is always defined inside a function!
    }
    if (optTypeRelation != null)
      optTypeRelation.analyze(this, context);
    body.analyze(this, context);
    context.leaveScope(this);
  }

  public void notifyThisUsed(AnalyzeContext context) {
    MethodDeclaration methodDeclaration = context.getScope().getMethodDeclaration();
    // if "this" is used inside non-static method, remember that:
    if (methodDeclaration!=null && !methodDeclaration.isStatic()) {
      thisUsed = true;
    }
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
    if (thisUsed) {
      out.write(".bind(this)");
    }
  }

  public JooSymbol getSymbol() {
     return symFun;
  }

  boolean isCompileTimeConstant() {
    return true;
  }

}
