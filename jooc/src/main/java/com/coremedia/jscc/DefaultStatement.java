/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class DefaultStatement extends Statement {

  JscSymbol symDefault;
  JscSymbol symColon;

  public DefaultStatement(JscSymbol symDefault, JscSymbol symColon) {
    this.symDefault = symDefault;
    this.symColon = symColon;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symDefault);
    out.writeSymbol(symColon);
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
  }

  public JscSymbol getSymbol() {
     return symDefault;
  }



}
