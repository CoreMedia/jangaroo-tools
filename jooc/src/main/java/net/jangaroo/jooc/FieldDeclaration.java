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
public class FieldDeclaration extends AbstractVariableDeclaration {

  public FieldDeclaration(JooSymbol[] modifiers, JooSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, JooSymbol symSemicolon) {
    super(modifiers,
            MODIFIERS_SCOPE|MODIFIER_STATIC,
            symConstOrVar, ide, optTypeRelation, optInitializer, symSemicolon);
  }

  @Override
  public boolean isField() {
    return true;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginString();
    writeModifiers(out);
    out.writeSymbol(optSymConstOrVar);
    out.endString();
    out.write(",{");
    generateIdeCode(out);
    if (optTypeRelation != null)
      optTypeRelation.generateCode(out);
    if (optInitializer != null) {
      out.writeSymbolWhitespace(optInitializer.symEq);
      out.write(':');
      boolean isCompileTimeConstant = optInitializer.value.isCompileTimeConstant();
      if (!isCompileTimeConstant) {
        out.write("function(){return ");
      }
      optInitializer.value.generateCode(out);
      if (!isCompileTimeConstant) {
        out.write(";}");
      }
    } else {
      out.write(": undefined");
    }
    out.write('}');
    Debug.assertTrue(optSymSemicolon != null, "optSymSemicolon != null");
    out.write(",");
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(ide.ide);
    out.writeSymbolToken(ide.ide);
  }



}
