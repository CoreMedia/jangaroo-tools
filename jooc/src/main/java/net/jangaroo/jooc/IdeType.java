/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class IdeType extends Type {

  public Ide getIde() {
    return ide;
  }

  Ide ide;

  public IdeType(Ide ide) {
    this.ide = ide;
  }

  public void generateCode(JsWriter out) throws IOException {
    ide.generateCode(out);
  }

  public JooSymbol getSymbol() {
      return ide.getSymbol();
  }

}
