/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;



public abstract class IdeDeclaration extends Declaration {

  Ide ide;

  protected IdeDeclaration(JscSymbol[] modifiers, int allowedModifiers, Ide ide) {
    super(modifiers, allowedModifiers);
    this.ide = ide;
  }

  public Ide getIde() {
    return ide;
  }

  public JscSymbol getSymbol() {
    return ide.getSymbol();
  }

  public String getName() {
    return ide.getName();
  }

  public String[] getQualifiedName() {
    IdeDeclaration parentDeclaration = getParentDeclaration();
    if (parentDeclaration == null)
      return getIde().getQualifiedName();
    else {
      String[] prefixName = parentDeclaration.getQualifiedName();
      String[] result = new String[prefixName.length+1];
      System.arraycopy(prefixName, 0, result, 0, prefixName.length);
      result[prefixName.length] = ide.getName();
      return result;
    }
  }

  protected static String toPath(String[] qn) {
    StringBuffer result = new StringBuffer(20);
    for (int i = 0; i < qn.length; i++) {
      if (i > 0)
        result.append('.');
      result.append(qn[i]);
    }
    return result.toString();
  }

  public String getPath() {
    return toPath(getQualifiedName());
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
  }

}
