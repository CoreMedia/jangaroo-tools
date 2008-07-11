/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class QualifiedIde extends Ide {

  protected Ide prefix;
  private JooSymbol symDot;


  public QualifiedIde(Ide prefix, JooSymbol symDot, JooSymbol symIde) {
    super(symIde);
    this.prefix = prefix;
    this.symDot = symDot;
  }

  public void generateCode(JsWriter out) throws IOException {
    prefix.generateCode(out);
    out.writeSymbol(symDot);
    out.writeSymbol(ide);
  }

  public String[] getQualifiedName() {
    String[] prefixName = prefix.getQualifiedName();
    String[] result = new String[prefixName.length+1];
    System.arraycopy(prefixName, 0, result, 0, prefixName.length);
    result[prefixName.length] = ide.getText();
    return result;
  }

  public JooSymbol getSymbol() {
    return prefix.getSymbol();
  }

}
