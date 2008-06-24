/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ForInitializer extends NodeImplBase {

  Declaration decl = null;
  Expr expr = null;

  public ForInitializer(Declaration decl) {
    this.decl = decl;
  }

  public ForInitializer(Expr expr) {
    this.expr = expr;
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    if (decl!=null)
      decl.analyze(context);
    if (expr!=null)
      expr.analyze(context);
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
