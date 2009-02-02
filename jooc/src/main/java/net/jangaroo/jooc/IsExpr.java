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
 * @author Frank Wienberg
 */
class IsExpr extends BinaryOpExpr {

  public IsExpr(Expr e1, JooSymbol symIs, Expr e2) {
    super(e1, symIs, e2);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(op);
    out.write('(');
    arg1.generateCode(out);
    out.write(',');
    arg2.generateCode(out);
    out.write(')');
  }
}