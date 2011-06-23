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

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
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
      new IdeType(new JooSymbol(sym.MUL, symAnyType.getFileName(), symAnyType.getLine(), symAnyType.getColumn() + 1, "", "*")));
  }

  public TypeRelation(JooSymbol symRelation, Type type) {
    this.setSymRelation(symRelation);
    this.setType(type);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitTypeRelation(this);
  }

  @Override
  public void scope(final Scope scope) {
    getType().scope(scope);
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getType().analyze(this, context);
  }

  @Override
  public void generateAsApiCode(final JsWriter out) throws IOException {
    out.writeSymbol(getSymRelation());
    getType().generateJsCode(out);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    out.beginCommentWriteSymbol(getSymRelation());
    getType().generateAsApiCode(out);
    out.endComment();
   }

  public JooSymbol getSymbol() {
    return getSymRelation();
  }

  public JooSymbol getSymRelation() {
    return symRelation;
  }

  public void setSymRelation(JooSymbol symRelation) {
    this.symRelation = symRelation;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }
}
