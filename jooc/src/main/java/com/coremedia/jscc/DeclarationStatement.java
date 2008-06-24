/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class DeclarationStatement extends SemicolonTerminatedStatement {

  Declaration decl;

  public DeclarationStatement(Declaration decl, JscSymbol symSemicolon) {
    super(symSemicolon);
    this.decl = decl;
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    decl.analyze(context);
  }

  protected void generateStatementCode(JsWriter out) throws IOException {
    decl.generateCode(out);
  }

  public JscSymbol getSymbol() {
     return decl.getSymbol();
  }

}
