/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Parameters extends NodeImplBase {

  Parameter param;
  JscSymbol symComma;

  public Parameter getHead() {
    return param;
  }

  public Parameters getTail() {
    return tail;
  }

  Parameters tail;

  public Parameters(Parameter param, JscSymbol symComma, Parameters tail) {
    this.param = param;
    this.symComma = symComma;
    this.tail = tail;
  }

  public Parameters(Parameter param) {
    this(param, null, null);
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    param.analyze(context);
    if (tail != null)
      tail.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    param.generateCode(out);
    if (symComma != null) {
      out.writeSymbol(symComma);
      tail.generateCode(out);
    }
  }

  public JscSymbol getSymbol() {
    return param.getSymbol();
  }


}
