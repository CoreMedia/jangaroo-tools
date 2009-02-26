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
class Arguments extends NodeImplBase {
  Expr arg;
  JooSymbol symComma;
  Arguments tail;

  public Arguments(Expr arg) {
    this(arg, null, null);
  }

  public Arguments(Expr arg, JooSymbol comma, Arguments tail) {
    this.arg = arg;
    this.symComma = comma;
    this.tail = tail;
  }

  public void generateCode(JsWriter out) throws IOException {
    arg.generateCode(out);
    if (symComma != null) {
      out.writeSymbol(symComma);
      tail.generateCode(out);
    }
  }


  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    arg.analyze(this, context);
    if (tail != null)
      tail.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return arg.getSymbol();
  }

}
