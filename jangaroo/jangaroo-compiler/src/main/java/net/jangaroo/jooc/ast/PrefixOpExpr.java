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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class PrefixOpExpr extends UnaryOpExpr {

  public PrefixOpExpr(JooSymbol op, Expr arg) {
    super(op, arg);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitPrefixOpExpr(this);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    throw new UnsupportedOperationException();
  }

  public JooSymbol getSymbol() {
    return getOp();
  }

}
