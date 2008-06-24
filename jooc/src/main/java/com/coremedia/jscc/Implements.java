/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Implements extends NodeImplBase {
  JscSymbol symImplements;
  TypeList superTypes;

  public Implements(JscSymbol symImplements, TypeList superTypes) {
    this.symImplements = symImplements;
    this.superTypes = superTypes;
  }

  public void generateCode(JsWriter out) throws IOException {
     out.writeSymbol(symImplements);
     superTypes.generateCode(out);
  }

  public JscSymbol getSymbol() {
      return symImplements;
  }

}
