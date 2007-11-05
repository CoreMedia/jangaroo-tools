/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class BinaryOpExpr extends OpExpr {

  Expr arg1;
  Expr arg2;

  public BinaryOpExpr(Expr arg1, JscSymbol op, Expr arg2) {
    super(op);
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  public void generateCode(JsWriter out) throws IOException {
    arg1.generateCode(out);
    out.writeSymbol(op);
    arg2.generateCode(out);
  }

  public void analyze(AnalyzeContext context) {
    arg1.analyze(context);
    arg2.analyze(context);
  }

  public JscSymbol getSymbol() {
    return arg1.getSymbol();
  }
  
}
