/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class TypeRelation extends NodeImplBase {

  JooSymbol symRelation;

  public Type getType() {
    return type;
  }

  Type type;

  public TypeRelation(JooSymbol symRelation, Type type) {
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

  public JooSymbol getSymbol() {
    return symRelation;
  }

}
