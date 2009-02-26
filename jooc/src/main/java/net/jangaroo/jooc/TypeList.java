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

  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    type.analyze(this, context);
    if (tail != null)
      tail.analyze(this, context);
    return this;
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
