/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class Parameters extends NodeImplBase {

  Parameter param;
  JooSymbol symComma;

  public Parameter getHead() {
    return param;
  }

  public Parameters getTail() {
    return tail;
  }

  Parameters tail;

  public Parameters(Parameter param, JooSymbol symComma, Parameters tail) {
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

  public JooSymbol getSymbol() {
    return param.getSymbol();
  }


}
