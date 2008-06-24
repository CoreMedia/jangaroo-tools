/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
abstract class KeywordStatement extends Statement {

  JscSymbol symKeyword;

  protected KeywordStatement(JscSymbol symKeyword) {
    super();
    this.symKeyword = symKeyword;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symKeyword);
  }

  public JscSymbol getSymbol() {
    return symKeyword;
  }

}
