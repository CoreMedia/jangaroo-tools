/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ObjectField extends NodeImplBase {

  Expr nameExpr;
  JooSymbol symColon;
  Expr value;

  public ObjectField(Expr nameExpr, JooSymbol symColon, Expr value) {
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

  public JooSymbol getSymbol() {
    return nameExpr.getSymbol();
  }


}
