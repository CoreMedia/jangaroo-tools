/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class Parameter extends AbstractVariableDeclaration {

  public Parameter(JscSymbol optSymConst, Ide ide, TypeRelation typeRelation) {
    super(new JscSymbol[0], 0, optSymConst, ide, typeRelation, null, null);
  }

  public void generateIdeCode(JsWriter out) throws IOException {
    Debug.assertTrue(getModifiers() == 0, "Parameters must not have any modifiers");
    if (optSymConstOrVar != null) {
      out.beginCommentWriteSymbol(optSymConstOrVar);
      out.endComment();
    }
    ide.generateCode(out);
  }

}
