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
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class BinaryOpExpr extends OpExpr {

  private Expr arg1;
  private Expr arg2;

  public BinaryOpExpr(Expr arg1, JooSymbol op, Expr arg2) {
    super(op);
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), arg1, arg2);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitBinaryOpExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    getArg1().scope(scope);
    getArg2().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getArg1().analyze(this);
    getArg2().analyze(this);
    setType(computeResultingType());
  }

  private ExpressionType computeResultingType() {
    AS3Type as3Type = getResultingAs3Type();
    if (as3Type != null) {
      return scope.getExpressionType(as3Type);
    } else if (logicalOperators.contains(getOp().sym)) {
      ExpressionType type1 = getArg1().getType();
      ExpressionType type2 = getArg2().getType();
      if (type1 != null && type2 != null) {
        if (type1.isAssignableTo(type2)) {
          return type2;
        } else if (type2.isAssignableTo(type1)) {
          return type1;
        }
      }
    }
    return null;
  }

  private AS3Type getResultingAs3Type() {
    int operator = getOp().sym;
    // check overloaded '+' first:
    if (operator == sym.PLUS && !(isNumeric(getArg1().getAS3Type()) && isNumeric(getArg2().getAS3Type()))) {
      return AS3Type.STRING;
    } else if (numericOperators.contains(operator)) {
      return AS3Type.NUMBER;
    } else if (comparisonOperators.contains(operator)) {
      return AS3Type.BOOLEAN;
    }
    return null;
  }

  private static final Collection<Integer> numericOperators =
          Arrays.asList(sym.PLUS, sym.MINUS, sym.MUL, sym.DIV, sym.MOD,
                  sym.AND, sym.OR, sym.LSHIFT, sym.RSHIFT, sym.URSHIFT);

  private static final Collection<Integer> comparisonOperators =
          Arrays.asList(sym.EQEQ, sym.EQEQEQ, sym.NOTEQ, sym.NOTEQEQ, sym.LT, sym.LTEQ, sym.GT, sym.GTEQ);

  private static final Collection<Integer> logicalOperators =
          Arrays.asList(sym.OROR, sym.ANDAND);

  private static boolean isNumeric(AS3Type as3Type) {
    return as3Type == AS3Type.NUMBER || as3Type == AS3Type.INT || as3Type == AS3Type.UINT
            || as3Type == AS3Type.BOOLEAN // booleans true/false are converted to 1 / 0
            || as3Type == AS3Type.VOID; // undefined is converted to NaN
  }

  public JooSymbol getSymbol() {
    return getArg1().getSymbol();
  }

  public boolean isRuntimeConstant() {
    return getArg1().isRuntimeConstant() && getArg2().isRuntimeConstant();
  }

  public boolean isCompileTimeConstant() {
    return getArg1().isCompileTimeConstant() && getArg2().isCompileTimeConstant();
  }

  public boolean isStandAloneConstant() {
    return getArg1().isStandAloneConstant() && getArg2().isStandAloneConstant();
  }

  public Expr getArg1() {
    return arg1;
  }

  public Expr getArg2() {
    return arg2;
  }

}
