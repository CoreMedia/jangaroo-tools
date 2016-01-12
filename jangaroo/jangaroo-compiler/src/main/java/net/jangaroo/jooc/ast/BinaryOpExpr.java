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

import java.io.IOException;
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
    getArg1().scope(scope);
    getArg2().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getArg1().analyze(this);
    getArg2().analyze(this);
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
