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

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class TypeRelation extends NodeImplBase {

  JooSymbol symRelation;

  public Type getType() {
    return type;
  }

  Type type;

  public TypeRelation(JooSymbol symAnyType) {
    this(new JooSymbol(sym.COLON, symAnyType.getFileName(), symAnyType.getLine(), symAnyType.getColumn(), symAnyType.getWhitespace(), ":"),
      new IdeType(new JooSymbol(sym.MUL, symAnyType.getFileName(), symAnyType.getLine(), symAnyType.getColumn() + 1, "", "*")));
  }

  public TypeRelation(JooSymbol symRelation, Type type) {
    this.symRelation = symRelation;
    this.type = type;
  }

  @Override
  public void scope(final Scope scope) {
    type.scope(scope);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    type.analyze(this, context);
    return this;
  }

  @Override
  protected void generateAsApiCode(final JsWriter out) throws IOException {
    out.writeSymbol(symRelation);
    type.generateCode(out);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginCommentWriteSymbol(symRelation);
    type.generateAsApiCode(out);
    out.endComment();
   }

  public JooSymbol getSymbol() {
    return symRelation;
  }

}
