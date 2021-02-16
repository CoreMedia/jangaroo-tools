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
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class LiteralExpr extends Expr {

  private JooSymbol value;
  private Scope scope;

  public LiteralExpr(JooSymbol value) {
    this.value = value;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitLiteralExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    this.scope = scope;
  }

  private AS3Type getLiteralType() {
    switch (getValue().sym) {
      case sym.INT_LITERAL:
        return AS3Type.INT;
      case sym.FLOAT_LITERAL:
        return AS3Type.NUMBER;
      case sym.STRING_LITERAL:
        return AS3Type.STRING;
      case sym.BOOL_LITERAL:
        return AS3Type.BOOLEAN;
      case sym.REGEXP_LITERAL:
        return AS3Type.REG_EXP;
      case sym.NULL_LITERAL:
        return AS3Type.ANY; // TODO: should be a special Null type
    }
    throw new IllegalStateException("Encountered LiteralExpr with sym " + getValue());
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    setType(scope.getExpressionType(getLiteralType()));
  }

  public JooSymbol getSymbol() {
    return getValue();
  }

  public boolean isRuntimeConstant() {
    return true;
  }

  public boolean isCompileTimeConstant() {
    return true;
  }

  public JooSymbol getValue() {
    return value;
  }

  public LiteralExpr withStringValue(String newValue) {
    // keep the same quotes if possible:
    String quote = value.getJooValue() instanceof String ? value.getText().substring(0, 1) : "\"";
    LiteralExpr literalExpr = new LiteralExpr(new JooSymbol(value.sym, value.getFileName(),
            value.getLine(), value.getColumn(), value.getWhitespace(),
            quote + newValue + quote,
            newValue));
    literalExpr.scope(scope);
    return literalExpr;
  }
}
