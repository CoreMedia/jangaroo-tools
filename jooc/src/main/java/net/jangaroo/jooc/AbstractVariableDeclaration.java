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
abstract class AbstractVariableDeclaration extends MemberDeclaration {

  JooSymbol optSymConstOrVar;
  Initializer optInitializer;
  JooSymbol optSymSemicolon;

  protected AbstractVariableDeclaration(JooSymbol[] modifiers, int allowedModifiers, JooSymbol optSymConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, JooSymbol optSymSemicolon) {
    super(modifiers, allowedModifiers, ide, optTypeRelation);
    this.optSymConstOrVar = optSymConstOrVar;
    this.optInitializer = optInitializer;
    this.optSymSemicolon = optSymSemicolon;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    generateIdeCode(out);
    if (optTypeRelation != null)
      optTypeRelation.generateCode(out);
    generateInitializerCode(out);
    if (optSymSemicolon != null)
      out.writeSymbol(optSymSemicolon);
  }

  protected void generateInitializerCode(JsWriter out) throws IOException {
    if (optInitializer != null)
      optInitializer.generateCode(out);
  }

  public abstract void generateIdeCode(JsWriter out) throws IOException;

  public boolean isConst() {
    return optSymConstOrVar != null && optSymConstOrVar.sym == sym.CONST;
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (optInitializer == null && isConst())
      Jooc.error(optSymConstOrVar, "constant must be initialized");
    if (optInitializer != null)
      optInitializer.analyze(this, context);
    return this;
  }

}
