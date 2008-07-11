/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class NewExpr extends Expr {

  JooSymbol symNew;
  Type type;
  JooSymbol lParen;
  Arguments args;
  JooSymbol rParen;

  public NewExpr(JooSymbol symNew, Type type, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    this.symNew = symNew;
    this.type = type;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
  }

  public void analyze(AnalyzeContext context) {
     if (args != null)
       args.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symNew);
    type.generateCode(out);
    out.writeSymbol(lParen);
    if (args != null) args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public JooSymbol getSymbol() {
      return symNew;
  }

}
