/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Arguments extends NodeImplBase {
  Expr arg;
  JooSymbol symComma;
  Arguments tail;

  public Arguments(Expr arg) {
    this(arg, null, null);
  }

  public Arguments(Expr arg, JooSymbol comma, Arguments tail) {
    this.arg = arg;
    this.symComma = comma;
    this.tail = tail;
  }

  public void generateCode(JsWriter out) throws IOException {
    arg.generateCode(out);
    if (symComma != null) {
      out.writeSymbol(symComma);
      tail.generateCode(out);
    }
  }


  public void analyze(AnalyzeContext context) {
    arg.analyze(context);
    if (tail != null)
      tail.analyze(context);
  }

  public JooSymbol getSymbol() {
    return arg.getSymbol();
  }

}
