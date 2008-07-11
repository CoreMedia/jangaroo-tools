/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ObjectFields extends NodeImplBase {

  ObjectField field;
  JooSymbol symComma;
  ObjectFields tail;

  public ObjectFields(ObjectField field) {
    this(field, null, null);
  }

  public ObjectFields(ObjectField field, JooSymbol symComma, ObjectFields tail) {
    this.field = field;
    this.symComma = symComma;
    this.tail = tail;
  }

  public void analyze(AnalyzeContext context) {
    field.analyze(context);
    if (tail != null)
      tail.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    field.generateCode(out);
    if (symComma != null) {
      out.writeSymbol(symComma);
      tail.generateCode(out);
    }
  }

  public JooSymbol getSymbol() {
    return field.getSymbol();
  }

}
