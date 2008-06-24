/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

/**
 * @author Andreas Gawecki
 */
class ContinueStatement extends LabelRefStatement {

  public ContinueStatement(JscSymbol symContinue, Ide optLabel, JscSymbol symSemicolon) {
    super(symContinue, optLabel, symSemicolon);
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
  }

}
