/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class LabeledStatement extends Statement {

  Ide ide;
  JscSymbol symColon;
  Statement statement;

  public LabeledStatement(Ide ide, JscSymbol symColon, Statement statement) {
    this.ide = ide;
    this.symColon = symColon;
    this.statement = statement;
  }

  public void analyze(AnalyzeContext context) {
    statement.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    ide.generateCode(out);
    out.writeSymbol(symColon);
    statement.generateCode(out);
  }

  public JscSymbol getSymbol() {
    return ide.getSymbol();
  }


}
