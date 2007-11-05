/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class Extends extends NodeImplBase  {
  JscSymbol symExtends;
  Type superClass;

  public Type getSuperClass() {
    return superClass;
  }

  public Extends(JscSymbol extds, Type superClass) {
    this.symExtends = extds;
    this.superClass = superClass;
  }

  public void generateCode(JsWriter out) throws IOException {
     out.writeSymbol(symExtends);
     superClass.generateCode(out);
  }

  public JscSymbol getSymbol() {
     return symExtends;
  }

}
