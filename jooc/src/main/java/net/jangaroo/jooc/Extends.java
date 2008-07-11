/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Extends extends NodeImplBase  {
  JooSymbol symExtends;
  Type superClass;

  public Type getSuperClass() {
    return superClass;
  }

  public Extends(JooSymbol extds, Type superClass) {
    this.symExtends = extds;
    this.superClass = superClass;
  }

  public void generateCode(JsWriter out) throws IOException {
     out.writeSymbol(symExtends);
     superClass.generateCode(out);
  }

  public JooSymbol getSymbol() {
     return symExtends;
  }

}
