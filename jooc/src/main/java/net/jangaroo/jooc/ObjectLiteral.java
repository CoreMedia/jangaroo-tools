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

  public Expr analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (fields != null)
      fields.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return lBrace;
  }


}
