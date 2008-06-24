/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 */
class BlockStatement extends Statement {

  JscSymbol rBrace;
  ArrayList statements;
  JscSymbol lBrace;

  public BlockStatement(JscSymbol lBrace, ArrayList statements, JscSymbol rBrace) {
    this.rBrace = rBrace;
    this.statements = statements;
    this.lBrace = lBrace;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    generateCode(statements, out);
    out.writeSymbol(rBrace);
  }

  public void analyze(AnalyzeContext context) {
    analyze(statements, context);
  }

  public JscSymbol getSymbol() {
     return rBrace;
  }

}
