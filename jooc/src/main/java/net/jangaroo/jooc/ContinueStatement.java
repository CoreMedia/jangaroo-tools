/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

/**
 * @author Andreas Gawecki
 */
class ContinueStatement extends LabelRefStatement {

  public ContinueStatement(JooSymbol symContinue, Ide optLabel, JooSymbol symSemicolon) {
    super(symContinue, optLabel, symSemicolon);
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
  }

}
