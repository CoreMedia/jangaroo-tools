/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class PackageDeclaration extends IdeDeclaration  {

  JscSymbol symPackage;
  JscSymbol symSemicolon;

  public PackageDeclaration(JscSymbol symPackage, Ide ide, JscSymbol symSemicolon) {
    super(new JscSymbol[0], 0, ide);
    this.symPackage = symPackage;
    this.symSemicolon = symSemicolon;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symPackage);
    out.writeSymbolWhitespace(symSemicolon);
    out.write("\"package ");
    ide.generateCode(out);
    out.write("\",");
  }

  public void analyze(AnalyzeContext context) {
     context.enterScope(this);
  }

  public JscSymbol getSymbol() {
    return symPackage;
  }

}
