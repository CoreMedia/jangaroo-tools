/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;



abstract class UnaryOpExpr extends OpExpr {

  Expr arg;

  public UnaryOpExpr(JscSymbol op, Expr arg) {
    super(op);
    this.arg = arg;
  }

  public void analyze(AnalyzeContext context) {
    arg.analyze(context);
  }
  
}
