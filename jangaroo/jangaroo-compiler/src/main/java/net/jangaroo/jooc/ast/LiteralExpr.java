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
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class LiteralExpr extends Expr {

  private JooSymbol value;

  public LiteralExpr(JooSymbol value) {
    this.setValue(value);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitLiteralExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(getValue());
  }

  public JooSymbol getSymbol() {
    return getValue();
  }

  public boolean isCompileTimeConstant() {
    return true;
  }

  public JooSymbol getValue() {
    return value;
  }

  public void setValue(JooSymbol value) {
    this.value = value;
  }
}
