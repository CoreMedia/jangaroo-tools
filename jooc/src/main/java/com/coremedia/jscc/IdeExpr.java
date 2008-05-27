/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class IdeExpr extends Expr {

  Ide ide;

  public IdeExpr(Ide ide) {
    this.ide = ide;
  }

   public void generateCode(JsWriter out) throws IOException {
    ide.generateCode(out);
  }

  public JscSymbol getSymbol() {
     return ide.getSymbol();
  }

}
