/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
abstract class KeywordStatement extends Statement {

  JooSymbol symKeyword;

  protected KeywordStatement(JooSymbol symKeyword) {
    super();
    this.symKeyword = symKeyword;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symKeyword);
  }

  public JooSymbol getSymbol() {
    return symKeyword;
  }

}
