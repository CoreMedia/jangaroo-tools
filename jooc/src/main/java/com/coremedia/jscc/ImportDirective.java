/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class ImportDirective extends NodeImplBase {

  JscSymbol importKeyword;
  Type type;

  public ImportDirective(JscSymbol importKeyword, Type type) {
    this.importKeyword = importKeyword;
    this.type = type;
  }

  public void generateCode(JsWriter out) throws IOException {
    // just as comment yet:
    out.writeSymbolWhitespace(importKeyword);
    out.write("\"");
    out.writeSymbolToken(importKeyword);
    type.generateCode(out);
    out.write("\",");
  }

  public JscSymbol getSymbol() {
      return importKeyword;
  }

}