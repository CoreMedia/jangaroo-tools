/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class FunctionExpr extends Expr {

  JscSymbol symFun;
  Ide ide;
  JscSymbol lParen;
  Parameters params;
  JscSymbol rParen;
  TypeRelation optTypeRelation;
  BlockStatement body;

  ClassDeclaration classDeclaration;

  public FunctionExpr(JscSymbol symFun, Ide ide, JscSymbol lParen, Parameters params, JscSymbol rParen, TypeRelation optTypeRelation, BlockStatement body) {
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
    if (params != null)
      params.analyze(context);
    if (optTypeRelation != null)
      optTypeRelation.analyze(context);
    body.analyze(context);
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

  public JscSymbol getSymbol() {
     return symFun;
  }

  boolean isCompileTimeConstant() {
    return true;
  }
}
