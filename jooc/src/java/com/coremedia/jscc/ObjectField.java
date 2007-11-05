/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class ObjectField extends NodeImplBase {

  Ide name;
  JscSymbol symColon;
  Expr value;

  public ObjectField(Ide name, JscSymbol symColon, Expr value) {
    this.name = name;
    this.symColon = symColon;
    this.value = value;
  }

  public void analyze(AnalyzeContext context) {
    name.analyze(context);
    value.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    name.generateCode(out);
    out.writeSymbol(symColon);
    value.generateCode(out);
  }

  public JscSymbol getSymbol() {
    return name.getSymbol();
  }


}
