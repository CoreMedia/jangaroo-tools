/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class DefaultStatement extends Statement {

  JooSymbol symDefault;
  JooSymbol symColon;

  public DefaultStatement(JooSymbol symDefault, JooSymbol symColon) {
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

  public JooSymbol getSymbol() {
     return symDefault;
  }



}
