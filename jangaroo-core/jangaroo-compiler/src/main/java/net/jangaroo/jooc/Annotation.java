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
 * An annotation (square bracket meta data).
 *
 * @author Frank Wienberg
 */
public class Annotation extends NodeImplBase {

  JooSymbol leftBracket;
  Ide ide;
  ObjectLiteral annotationFields;
  JooSymbol rightBracket;

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol rightBracket) {
    this(leftBracket, ide, null, null, null, rightBracket);
  }

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol leftParen, CommaSeparatedList<ObjectField> annotationFields, JooSymbol rightParen, JooSymbol rightBracket) {
    this.leftBracket = leftBracket;
    this.ide = ide;
    if (leftParen!=null && rightParen!=null) {
      this.annotationFields = new ObjectLiteral(leftParen, annotationFields, null, rightParen);
    }
    this.rightBracket = rightBracket;
  }

  @Override
  public void scope(final Scope scope) {
    ide.scope(scope);
    if (annotationFields != null) {
      annotationFields.scope(scope);
    }
  }

  public JooSymbol getSymbol() {
    return ide.getSymbol();
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(leftBracket);
    out.writeToken("{");
    ide.generateCode(out);
    out.writeToken(":");
    if (annotationFields == null) {
      out.writeToken("{}");
    } else {
      annotationFields.generateCode(out);
    }
    out.writeSymbolWhitespace(rightBracket);
    out.writeToken("},");
  }
}
