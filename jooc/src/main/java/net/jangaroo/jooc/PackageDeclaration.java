/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
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
    if (ide!=null) {
      ide.generateCode(out);
    }
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
