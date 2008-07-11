/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Implements extends NodeImplBase {
  JooSymbol symImplements;
  TypeList superTypes;

  public Implements(JooSymbol symImplements, TypeList superTypes) {
    this.symImplements = symImplements;
    this.superTypes = superTypes;
  }

  public void generateCode(JsWriter out) throws IOException {
     out.writeSymbol(symImplements);
     superTypes.generateCode(out);
  }

  public JooSymbol getSymbol() {
      return symImplements;
  }

}
