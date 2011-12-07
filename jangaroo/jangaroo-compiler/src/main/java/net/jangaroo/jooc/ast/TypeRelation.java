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

import net.jangaroo.utils.AS3Type;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class TypeRelation extends NodeImplBase {

  private JooSymbol symRelation;

  private Type type;

  public TypeRelation(JooSymbol symAnyType) {
    this(new JooSymbol(sym.COLON, symAnyType.getFileName(), symAnyType.getLine(), symAnyType.getColumn(), symAnyType.getWhitespace(), ":"),
            new Type(new JooSymbol(sym.MUL, symAnyType.getFileName(), symAnyType.getLine(), symAnyType.getColumn() + 1, "", AS3Type.ANY.toString())));
  }

  public TypeRelation(JooSymbol symRelation, Type type) {
    this.symRelation = symRelation;
    this.type = type;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitTypeRelation(this);
  }

  @Override
  public void scope(final Scope scope) {
    getType().scope(scope);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getType().analyze(this);
  }

  public JooSymbol getSymbol() {
    return getSymRelation();
  }

  public JooSymbol getSymRelation() {
    return symRelation;
  }

  public Type getType() {
    return type;
  }
}
