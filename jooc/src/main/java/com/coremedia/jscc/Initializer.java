/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Initializer extends NodeImplBase {

  JscSymbol symEq;
  Expr value;

  public Initializer(JscSymbol symEq, Expr value) {
    this.symEq = symEq;
    this.value = value;
  }

  public void analyze(AnalyzeContext context) {
    value.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symEq);
    value.generateCode(out);
  }

  public JscSymbol getSymbol() {
      return symEq;
  }

}
