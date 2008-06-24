/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ObjectField extends NodeImplBase {

  Expr nameExpr;
  JscSymbol symColon;
  Expr value;

  public ObjectField(Expr nameExpr, JscSymbol symColon, Expr value) {
    this.nameExpr = nameExpr;
    this.symColon = symColon;
    this.value = value;
  }

  public void analyze(AnalyzeContext context) {
    nameExpr.analyze(context);
    value.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    nameExpr.generateCode(out);
    out.writeSymbol(symColon);
    value.generateCode(out);
  }

  public JscSymbol getSymbol() {
    return nameExpr.getSymbol();
  }


}
