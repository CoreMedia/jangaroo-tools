/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Parameter extends AbstractVariableDeclaration {

  public Parameter(JooSymbol optSymConst, Ide ide, TypeRelation typeRelation) {
    super(new JooSymbol[0], 0, optSymConst, ide, typeRelation, null, null);
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
