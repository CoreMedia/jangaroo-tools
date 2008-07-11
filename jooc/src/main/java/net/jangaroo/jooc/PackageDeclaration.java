/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class PackageDeclaration extends IdeDeclaration  {

  JooSymbol symPackage;

  public PackageDeclaration(JooSymbol symPackage, Ide ide) {
    super(new JooSymbol[0], 0, ide);
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

  public JooSymbol getSymbol() {
    return symPackage;
  }

}
