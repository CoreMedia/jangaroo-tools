/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class ImportDirective extends NodeImplBase {

  JooSymbol importKeyword;
  Type type;

  public ImportDirective(JooSymbol importKeyword, Type type) {
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

  public JooSymbol getSymbol() {
      return importKeyword;
  }

}