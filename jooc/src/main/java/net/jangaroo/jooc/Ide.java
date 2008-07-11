/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Ide extends NodeImplBase {

  JooSymbol ide;

  public Ide(JooSymbol ide) {
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

  public JooSymbol getSymbol() {
    return ide;
  }


}
