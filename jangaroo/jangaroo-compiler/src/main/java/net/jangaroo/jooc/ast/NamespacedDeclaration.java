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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class NamespacedDeclaration extends IdeDeclaration {

  private Initializer optInitializer;
  private JooSymbol symNamespace;
  private JooSymbol optSymSemicolon;

  public NamespacedDeclaration(JooSymbol[] modifiers,
                               JooSymbol symNamespace,
                               Ide ide,
                               Initializer optInitializer,
                               JooSymbol optSymSemicolon) {
    super(modifiers,ide);
    assert SyntacticKeywords.NAMESPACE.equals(symNamespace.getText());
    this.symNamespace = symNamespace;
    this.optInitializer = optInitializer;
    this.optSymSemicolon = optSymSemicolon;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitNamespacedDeclaration(this);
  }

  @Override
  protected int getAllowedModifiers() {
    return MODIFIER_PUBLIC | MODIFIER_INTERNAL;
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    writeModifiers(out);
    out.writeSymbol(symNamespace);
    getIde().generateAsApiCode(out);
    if (optInitializer != null) {
      out.writeSymbol(optInitializer.getSymEq());
      optInitializer.getValue().generateAsApiCode(out);
    }
    if (optSymSemicolon != null) {
      out.writeSymbol(optSymSemicolon);
    } else {
      out.writeToken(";");
    }
  }

  @Override
  public void generateJsCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(symNamespace);
    getIde().generateJsCode(out);
    out.endString();
    out.writeSymbolWhitespace(optInitializer.getSymEq());
    out.writeToken(",");
    optInitializer.getValue().generateJsCode(out);
    if (optSymSemicolon != null) {
      out.writeSymbolWhitespace(optSymSemicolon);
    }
    out.writeToken(",[]");
  }

}
