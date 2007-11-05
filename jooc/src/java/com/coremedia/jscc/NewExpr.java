/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class NewExpr extends Expr {

  JscSymbol symNew;
  Type type;
  JscSymbol lParen;
  Arguments args;
  JscSymbol rParen;

  public NewExpr(JscSymbol symNew, Type type, JscSymbol lParen, Arguments args, JscSymbol rParen) {
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

  public JscSymbol getSymbol() {
      return symNew;
  }

}
