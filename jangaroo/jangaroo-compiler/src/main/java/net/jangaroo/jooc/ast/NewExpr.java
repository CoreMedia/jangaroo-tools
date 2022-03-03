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
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class NewExpr extends Expr {

  private JooSymbol symNew;
  private Expr applyConstructor;

  public NewExpr(JooSymbol symNew, Expr applyConstructor) {
    this.symNew = symNew;
    this.applyConstructor = applyConstructor;
    if (applyConstructor instanceof ApplyExpr) {
      ((ApplyExpr) applyConstructor).setInsideNewExpr(true);
    }
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), applyConstructor);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitNewExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    getApplyConstructor().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    Expr applyConstructor = getApplyConstructor();
    applyConstructor.analyze(this);
    // since the constructor-apply-expression looks like a type cast, we can simply hand through its type:
    ExpressionType type = applyConstructor.getType();
    if (!(parentNode instanceof ApplyExpr)) {
      // it is a 'new' without parenthesis:
      type = type.getAS3Type() == AS3Type.CLASS ? type.getTypeParameter() : null;
    }
    setType(type);
  }

  public JooSymbol getSymbol() {
    return getSymNew();
  }

  public JooSymbol getSymNew() {
    return symNew;
  }

  public Expr getApplyConstructor() {
    return applyConstructor;
  }

}
