/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;


class VariableDeclaration extends AbstractVariableDeclaration {

  public VariableDeclaration(JscSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer) {
    super(new JscSymbol[]{}, 0, symConstOrVar, ide, optTypeRelation, optInitializer, null);
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    if (optSymConstOrVar != null) {
      if (isConst()) {
        out.beginCommentWriteSymbol(optSymConstOrVar);
        out.endComment();
        out.write("var");
      } else
        out.writeSymbol(optSymConstOrVar);
    }
    ide.generateCode(out);
  }

}
