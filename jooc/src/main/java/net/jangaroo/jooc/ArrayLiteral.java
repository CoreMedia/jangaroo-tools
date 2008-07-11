/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ArrayLiteral extends Expr {

  JooSymbol lBracket;
  Arguments args;
  JooSymbol rBracket;

  public ArrayLiteral(JooSymbol lBracket, Arguments args, JooSymbol rBracket) {
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

  public JooSymbol getSymbol() {
    return lBracket;
  }


}
