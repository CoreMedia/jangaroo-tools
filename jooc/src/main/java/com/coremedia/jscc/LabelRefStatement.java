/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * A LabelRefStatement is either a break or continue statement
 *
 * @author Andreas Gawecki
 */
class LabelRefStatement extends KeywordExprStatement {

  LabelRefStatement(JscSymbol symStatement, Ide optLabel, JscSymbol symSemicolon) {
    super(symStatement, null, symSemicolon);
    this.optLabel = optLabel;
  }

  protected Ide optLabel;

  protected LabeledStatement labelDeclaration = null;

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    Scope scope = context.getScope();
    if (optLabel == null) {
      Statement loopOrSwitchStatement = scope.getCurrentLoopOrSwitch();
      if (loopOrSwitchStatement == null)
        Jscc.error(this, "not inside loop or switch");
    } else {
      labelDeclaration = scope.lookupLabel(optLabel);
      if (!(labelDeclaration.statement instanceof LoopStatement))
        Jscc.error(this, "label does not reference loop statement");
    }
  }

}
