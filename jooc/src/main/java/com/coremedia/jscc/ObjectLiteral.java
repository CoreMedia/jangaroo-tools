/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ObjectLiteral extends Expr {

  JscSymbol lBrace;
  ObjectFields fields;
  JscSymbol rParen;

  public ObjectLiteral(JscSymbol lBrace, ObjectFields fields, JscSymbol rParen) {
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

  public JscSymbol getSymbol() {
    return lBrace;
  }


}
