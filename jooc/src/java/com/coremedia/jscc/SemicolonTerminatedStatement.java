/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

abstract class SemicolonTerminatedStatement extends Statement {

  JscSymbol symSemicolon;

  SemicolonTerminatedStatement(JscSymbol symSemicolon) {
    this.symSemicolon = symSemicolon;
  }

  protected abstract void generateStatementCode(JsWriter out) throws IOException;
  
  public void generateCode(JsWriter out) throws IOException {
    generateStatementCode(out);
    out.writeSymbol(symSemicolon);
  }

}
