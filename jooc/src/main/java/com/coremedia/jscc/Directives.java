/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class Directives extends NodeImplBase {

  ImportDirective directive; // other directive types may follow later
  Directives tail;

  public Directives(ImportDirective directive, Directives tail) {
    this.directive = directive;
    this.tail = tail;
  }

  public void analyze(AnalyzeContext context) {
    directive.analyze(context);
    if (tail != null)
      tail.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    directive.generateCode(out);
    if (tail != null) {
      tail.generateCode(out);
    }
  }

  public JscSymbol getSymbol() {
    return directive.getSymbol();
  }

}