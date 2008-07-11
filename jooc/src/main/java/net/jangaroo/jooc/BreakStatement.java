/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;


/**
 * @author Andreas Gawecki
 */
class BreakStatement extends LabelRefStatement {

  public BreakStatement(JooSymbol symBreak, Ide optIde, JooSymbol symSemicolon) {
    super(symBreak, optIde, symSemicolon);
  }

}
