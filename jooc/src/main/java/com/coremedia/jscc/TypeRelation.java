/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

public class TypeRelation extends NodeImplBase {

  JscSymbol symRelation;

  public Type getType() {
    return type;
  }

  Type type;

  public TypeRelation(JscSymbol symRelation, Type type) {
    this.symRelation = symRelation;
    this.type = type;
  }

  public void analyze(AnalyzeContext context) {
    type.analyze(context);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginCommentWriteSymbol(symRelation);
    type.generateCode(out);
    out.endComment();
   }

  public JscSymbol getSymbol() {
    return symRelation;
  }

}
