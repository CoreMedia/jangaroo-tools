/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class VariableDeclaration extends AbstractVariableDeclaration {

  public VariableDeclaration(JooSymbol symConstOrVar, Ide ide,
      TypeRelation optTypeRelation, Initializer optInitializer) {
    super(new JooSymbol[]{}, 0, symConstOrVar, ide, optTypeRelation, optInitializer, null);
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
