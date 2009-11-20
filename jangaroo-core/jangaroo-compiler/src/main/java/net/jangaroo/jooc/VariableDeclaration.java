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
 * @author Frank Wienberg
 */
class VariableDeclaration extends AbstractVariableDeclaration {

  public VariableDeclaration(JooSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer) {
    this(symConstOrVar, ide, optTypeRelation, optInitializer, null);
  }

  public VariableDeclaration(JooSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer, VariableDeclaration optNextVariableDeclaration) {
    super(new JooSymbol[]{}, 0, symConstOrVar, ide, optTypeRelation, optInitializer, optNextVariableDeclaration, null);
  }

  @Override
  boolean allowDuplicates(AnalyzeContext context) {
    // It is "worst practice" to redeclare local variables in AS3, so we made this configurable:
    return context.getConfig().isAllowDuplicateLocalVariables();
  }

  protected void generateStartCode(JsWriter out) throws IOException {
    out.beginComment();
    writeModifiers(out);
    out.endComment();
    if (optSymConstOrVar != null) {
      if (isConst()) {
        out.beginCommentWriteSymbol(optSymConstOrVar);
        out.endComment();
        out.writeToken("var");
      } else {
        out.writeSymbol(optSymConstOrVar);
      }
    }
  }

}
