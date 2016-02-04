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

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Catch extends KeywordStatement {

  private JooSymbol lParen;
  private Parameter param;
  private JooSymbol rParen;
  private BlockStatement block;
  private TryStatement parentNode;

  public Catch(JooSymbol symCatch, JooSymbol lParen, Parameter param, JooSymbol rParen, BlockStatement block) {
    super(symCatch);
    this.lParen = lParen;
    this.param = param;
    this.rParen = rParen;
    this.block = block;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), param, block);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitCatch(this);
  }

  public TryStatement getParentTryStatement() {
    return parentNode;
  }

  public JooSymbol getLParen() {
    return lParen;
  }

  public Parameter getParam() {
    return param;
  }

  public JooSymbol getRParen() {
    return rParen;
  }

  public BlockStatement getBlock() {
    return block;
  }

  public boolean hasCondition() {
    TypeRelation typeRelation = param.getOptTypeRelation();
    return typeRelation != null && typeRelation.getType().getSymbol().sym != sym.MUL;
  }

  @Override
  public void scope(final Scope scope) {
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        param.scope(scope);
        block.scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (!(parentNode instanceof TryStatement)) {
      throw new IllegalArgumentException("the parent node of a catch block must be a try statement, but is " + parentNode);
    }
    this.parentNode = (TryStatement) parentNode;
    param.analyze(this);
    TypeRelation typeRelation = param.getOptTypeRelation();
    if (typeRelation != null) {
      Type type = typeRelation.getType();
      type.getIde().addExternalUsage(false);
    }
    block.analyze(this);
  }
}
