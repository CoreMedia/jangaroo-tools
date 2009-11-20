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
class BinaryOpExpr extends OpExpr {

  Expr arg1;
  Expr arg2;

  public BinaryOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(op);
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  public void generateCode(JsWriter out) throws IOException {
    arg1.generateCode(out);
    out.writeSymbol(op);
    arg2.generateCode(out);
  }

  public Expr analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    arg1 = arg1.analyze(this, context);
    arg2 = arg2.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return arg1.getSymbol();
  }

  boolean isCompileTimeConstant() {
    return arg1.isCompileTimeConstant() && arg2.isCompileTimeConstant();
  }
}
