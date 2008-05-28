/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class PackageDeclaration extends IdeDeclaration  {

  JscSymbol symPackage;

  public PackageDeclaration(JscSymbol symPackage, Ide ide) {
    super(new JscSymbol[0], 0, ide);
    this.symPackage = symPackage;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(symPackage);
    out.write("\"package ");
    ide.generateCode(out);
    out.write("\"");
    out.write(",");
  }

  public void analyze(AnalyzeContext context) {
     context.enterScope(this);
  }

  public JscSymbol getSymbol() {
    return symPackage;
  }

}
