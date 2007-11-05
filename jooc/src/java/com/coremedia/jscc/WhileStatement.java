/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class WhileStatement extends ConditionalLoopStatement {

  public WhileStatement(JscSymbol symWhile, ParenthesizedExpr cond, Statement body) {
    super(symWhile, cond, body);
  }

}
