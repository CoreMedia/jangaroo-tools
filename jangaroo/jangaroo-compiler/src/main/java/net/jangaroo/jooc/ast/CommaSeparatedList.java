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
public class CommaSeparatedList<T extends AstNode> extends Expr {
  private T head;
  private JooSymbol symComma;
  private CommaSeparatedList<T> tail;

  public CommaSeparatedList(T head) {
    this(head, null, null);
  }

  public CommaSeparatedList(T head, JooSymbol comma, CommaSeparatedList<T> tail) {
    this.head = head;
    this.symComma = comma;
    this.tail = tail;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), head, tail);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitCommaSeparatedList(this);
  }

  @Override
  public void scope(final Scope scope) {
    if (getHead() != null) {
      getHead().scope(scope);
    }
    if (getTail() != null) {
      getTail().scope(scope);
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (getHead() != null) {
      getHead().analyze(this);
    }
    if (getTail() != null) {
      getTail().analyze(this);
    }
  }

  public JooSymbol getSymbol() {
    return getHead() != null ? getHead().getSymbol() : getSymComma();
  }

  public T getHead() {
    return head;
  }

  public JooSymbol getSymComma() {
    return symComma;
  }

  public CommaSeparatedList<T> getTail() {
    return tail;
  }

  public boolean isStandAloneConstant() {
    return (head instanceof Expr && ((Expr)head).isStandAloneConstant() || head instanceof  ObjectField && ((ObjectField)head).isStandAloneConstant()) &&
            (tail == null || tail.isStandAloneConstant());
  }

}
