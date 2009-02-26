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

  @Override
  public String getQualifiedNameStr() {
    return constructQualifiedNameStr(getQualifiedName());
  }

  public JooSymbol getSymbol() {
    return prefix.getSymbol();
  }

  static String constructQualifiedNameStr(String[] qualifiedName) {
    StringBuilder sb = new StringBuilder(qualifiedName[0]);
    for (int i = 1; i < qualifiedName.length; i++) {
      sb.append(".").append(qualifiedName[i]);
    }
    return sb.toString();
  }
}
