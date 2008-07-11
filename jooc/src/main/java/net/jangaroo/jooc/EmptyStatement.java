/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
public class EmptyStatement extends ExprStatement {
  public EmptyStatement(JooSymbol symSemicolon) {
    super(null, symSemicolon);
  }

  public JooSymbol getSymbol() {
     return symSemicolon;
  }

}
