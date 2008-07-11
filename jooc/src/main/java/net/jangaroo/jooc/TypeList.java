/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class TypeList extends NodeImplBase {

  Type type;
  JooSymbol optComma;
  TypeList tail;

  public TypeList(Type type) {
    this(type,null,null);
  }

  public TypeList(Type type, JooSymbol optComma, TypeList tail) {
    this.type = type;
    this.optComma = optComma;
    this.tail = tail;
  }

  public void analyze(AnalyzeContext context) {
    type.analyze(context);
    if (tail != null)
      tail.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    type.generateCode(out);
    if (optComma != null) {
      out.writeSymbol(optComma);
      tail.generateCode(out);
    }
  }

  public JooSymbol getSymbol() {
    return type.getSymbol();
  }

}
