/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

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

  public JscSymbol getSymbol() {
      return ide.getSymbol();
  }

}
