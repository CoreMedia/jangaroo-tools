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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public abstract class UnaryOpExpr extends OpExpr {

  private Expr arg;

  public UnaryOpExpr(JooSymbol op, Expr arg) {
    super(op);
    this.arg = arg;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), arg);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    getArg().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getArg().analyze(this);
    int operator = getOp().sym;
    if (numericOperators.contains(operator)) {
      setType(scope.getExpressionType(AS3Type.NUMBER)); // even ++uint becomes type Number!
    } else if (operator == sym.NOT) {
      setType(scope.getExpressionType(AS3Type.BOOLEAN));
    }
  }

  private static final Collection<Integer> numericOperators =
          Arrays.asList(sym.PREFIX_PLUSPLUS, sym.PREFIX_PLUS, sym.PREFIX_MINUS, sym.PREFIX_MINUSMINUS,
                  sym.PLUSPLUS, sym.MINUSMINUS, sym.PLUS, sym.MINUS);

  public boolean isRuntimeConstant() {
    return getArg().isRuntimeConstant();
  }

  public boolean isCompileTimeConstant() {
    return getArg().isCompileTimeConstant();
  }

  public boolean isStandAloneConstant() {
    return getArg().isStandAloneConstant();
  }

  public Expr getArg() {
    return arg;
  }

}
