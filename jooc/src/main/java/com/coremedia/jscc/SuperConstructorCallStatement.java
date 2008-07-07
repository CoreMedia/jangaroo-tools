/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;


/**
 * @author Andreas Gawecki
 */
class SuperConstructorCallStatement extends Statement {

  JscSymbol symSuper;
  JscSymbol lParen;
  Arguments args;
  JscSymbol rParen;
  Expr fun;

  public SuperConstructorCallStatement(JscSymbol symSuper, JscSymbol lParen, Arguments args, JscSymbol rParen) {
    this.symSuper = symSuper;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
    this.fun = new IdeExpr(new Ide(symSuper));
  }

  public void analyze(AnalyzeContext context) {
    MethodDeclaration method = context.getCurrentMethod();
    if (method == null || !method.isConstructor()) {
      Jscc.error(symSuper, "must only call super constructor from constructor method");
    }
    if (method.containsSuperConstructorCall()) {
      Jscc.error(symSuper, "must not call super constructor twice");
    }
    method.setContainsSuperConstructorCall(true);

    if (args != null)
      args.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    generateFunCode(out);
    generateArgsCode(out);
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSuper);
    out.writeToken("this[_super]");
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public JscSymbol getSymbol() {
    return fun.getSymbol();
  }

}
