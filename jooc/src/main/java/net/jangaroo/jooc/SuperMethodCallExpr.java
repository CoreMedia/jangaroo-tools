/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class SuperMethodCallExpr extends ApplyExpr {

  protected JooSymbol symSuper;
  protected Ide methodIde;

  protected ClassDeclaration classDeclaration;

  public SuperMethodCallExpr(JooSymbol symSuper, JooSymbol symDot, Ide ide, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    //TODO: define super ide
    super(new DotExpr(new IdeExpr(new Ide(symSuper)), symDot, ide),lParen,args,rParen);
    this.methodIde = ide;
    this.symSuper = symSuper;
  }

  protected void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symSuper);
    //TODO:report error on super calls within closures
    out.writeToken("this[_");
    out.write(methodIde.getName());
    out.writeToken("]");
  }

  public void analyze(AnalyzeContext context) {
    //TODO:check whether called method is overridden
    //TODO:analyze super
    this.classDeclaration = context.getCurrentClass();
    if (args != null)
      args.analyze(context);
  }

}
