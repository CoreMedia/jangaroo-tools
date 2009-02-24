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
class ObjectField extends NodeImplBase {

  Expr nameExpr;
  JooSymbol symColon;
  Expr value;

  public ObjectField(Expr nameExpr, JooSymbol symColon, Expr value) {
    this.nameExpr = nameExpr;
    this.symColon = symColon;
    this.value = value;
  }

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (nameExpr!=null) {
      nameExpr.analyze(this, context);
    }
    value.analyze(this, context);
  }

  public void generateCode(JsWriter out) throws IOException {
    if (nameExpr!=null) {
      nameExpr.generateCode(out);
      out.writeSymbol(symColon);
    }
    value.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return nameExpr.getSymbol();
  }


}
