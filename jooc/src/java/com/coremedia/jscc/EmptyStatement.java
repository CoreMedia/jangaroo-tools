/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

public class EmptyStatement extends ExprStatement {
  public EmptyStatement(JscSymbol symSemicolon) {
    super(null, symSemicolon);
  }

  public JscSymbol getSymbol() {
     return symSemicolon;
  }

}
