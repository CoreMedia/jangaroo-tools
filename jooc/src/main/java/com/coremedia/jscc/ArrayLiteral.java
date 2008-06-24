/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ArrayLiteral extends Expr {

  JscSymbol lBracket;
  Arguments args;
  JscSymbol rBracket;

  public ArrayLiteral(JscSymbol lBracket, Arguments args, JscSymbol rBracket) {
    this.lBracket = lBracket;
    this.args = args;
    this.rBracket = rBracket;
  }


  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(lBracket);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rBracket);
  }

  public void analyze(AnalyzeContext context) {
    if (args != null)
      args.analyze(context);
  }

  public JscSymbol getSymbol() {
    return lBracket;
  }


}
