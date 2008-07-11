/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
abstract class SemicolonTerminatedStatement extends Statement {

  JooSymbol symSemicolon;

  SemicolonTerminatedStatement(JooSymbol symSemicolon) {
    this.symSemicolon = symSemicolon;
  }

  protected abstract void generateStatementCode(JsWriter out) throws IOException;
  
  public void generateCode(JsWriter out) throws IOException {
    generateStatementCode(out);
    out.writeSymbol(symSemicolon);
  }

}
