/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ObjectLiteral extends Expr {

  JooSymbol lBrace;
  ObjectFields fields;
  JooSymbol rParen;

  public ObjectLiteral(JooSymbol lBrace, ObjectFields fields, JooSymbol rParen) {
    this.lBrace = lBrace;
    this.fields = fields;
    this.rParen = rParen;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    if (fields != null)
      fields.generateCode(out);
    out.writeSymbol(rParen);
  }

  public void analyze(AnalyzeContext context) {
    if (fields != null)
      fields.analyze(context);
  }

  public JooSymbol getSymbol() {
    return lBrace;
  }


}
