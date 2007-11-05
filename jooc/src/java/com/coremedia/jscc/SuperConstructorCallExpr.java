/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;


class SuperConstructorCallExpr extends ApplyExpr {

  protected JscSymbol symSuper;
  protected ClassDeclaration classDeclaration;

  public SuperConstructorCallExpr(JscSymbol symSuper, JscSymbol lParen, Arguments args, JscSymbol rParen) {
    super(new IdeExpr(new Ide(symSuper)),lParen,args,rParen);
    this.symSuper = symSuper;
  }
  //TODO: definde self and super ides

  public void analyze(AnalyzeContext context) {
    //TODO:analyze super
    this.classDeclaration = context.getCurrentClass();
    if (args != null)
      args.analyze(context);
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSuper);
    out.writeToken("this[_super]");
  }

}
