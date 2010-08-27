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
 * @author Frank Wienberg
 */
public class NamespaceDeclaration extends IdeDeclaration {

  private Initializer optInitializer;
  private JooSymbol symNamespace;
  private JooSymbol optSymSemicolon;

  public NamespaceDeclaration(JooSymbol[] modifiers,
                              JooSymbol symNamespace,
                              Ide ide,
                              Initializer optInitializer,
                              JooSymbol optSymSemicolon) {
    super(modifiers,
      MODIFIER_PUBLIC | MODIFIER_INTERNAL,
      ide);
    assert SyntacticKeywords.NAMESPACE.equals(symNamespace.getText());
    this.symNamespace = symNamespace;
    this.optInitializer = optInitializer;
    this.optSymSemicolon = optSymSemicolon;
  }

  @Override
  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(symNamespace);
    ide.generateCode(out);
    out.endString();
    out.writeSymbolWhitespace(optInitializer.symEq);
    out.writeToken(",");
    optInitializer.value.generateCode(out);
    out.writeSymbolWhitespace(optSymSemicolon);
    out.writeToken(",[]");
  }

}
