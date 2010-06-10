/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */
package net.jangaroo.jooc;

import java.io.IOException;

/**
 * An annotation (sqare bracket meta data).
 */
public class Annotation extends NodeImplBase {

  JooSymbol leftBracket;
  Ide ide;
  ParenthesizedExpr<CommaSeparatedList<ObjectField>> annotationFields;
  JooSymbol rightBracket;

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol rightBracket) {
    this(leftBracket, ide, null, null, null, rightBracket);
  }

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol leftBrace, CommaSeparatedList<ObjectField> annotationFields, JooSymbol rightBrace, JooSymbol rightBracket) {
    this.leftBracket = leftBracket;
    this.ide = ide;
    if (leftBrace!=null && rightBrace!=null) {
      this.annotationFields = new ParenthesizedExpr<CommaSeparatedList<ObjectField>>(leftBrace, annotationFields, rightBrace);
    }
    this.rightBracket = rightBracket;
  }

  @Override
  public void scope(final Scope scope) {
    ide.scope(scope);
    annotationFields.scope(scope);
  }

  public JooSymbol getSymbol() {
    return ide.getSymbol();
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginComment();
    out.writeSymbol(leftBracket);
    ide.generateCode(out);
    if (annotationFields!=null) {
      annotationFields.generateCode(out);
    }
    out.writeSymbol(rightBracket);
    out.endComment();
  }
}
