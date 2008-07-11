/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class WhileStatement extends ConditionalLoopStatement {

  public WhileStatement(JooSymbol symWhile, ParenthesizedExpr cond, Statement body) {
    super(symWhile, cond, body);
  }

}
