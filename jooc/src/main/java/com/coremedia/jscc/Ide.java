/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Ide extends NodeImplBase {

  JscSymbol ide;

  public Ide(JscSymbol ide) {
    this.ide = ide;
  }

  public void generateCode(JsWriter out) throws IOException {
     out.writeSymbol(ide);
  }

  public String[] getQualifiedName() {
    return new String[] { ide.getText() };
  }

  public String getName() {
    return ide.getText();
  }

  public JscSymbol getSymbol() {
    return ide;
  }


}
