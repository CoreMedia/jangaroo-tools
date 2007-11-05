/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class ForInitializer extends NodeImplBase {

  Declaration decl = null;
  Expr expr = null;

  public ForInitializer(Declaration decl) {
    this.decl = decl;
  }

  public ForInitializer(Expr expr) {
    this.expr = expr;
  }

  public void generateCode(JsWriter out) throws IOException {
    if (decl != null)
      decl.generateCode(out);
    else if (expr != null)
      expr.generateCode(out);
  }

  public JscSymbol getSymbol() {
     return decl != null ? decl.getSymbol() : expr.getSymbol();
  }
  
}
