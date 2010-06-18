/*
 * Copyright 2010 CoreMedia AG
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
class AsExpr extends Expr {

  Expr e;
  JooSymbol symAs;
  Type type;

  public AsExpr(final Expr e, final JooSymbol symAs, final Type type) {
    this.e = e;
    this.symAs = symAs;
    this.type = type;
  }

  @Override
  public void scope(final Scope scope) {
    e.scope(scope);
    type.scope(scope);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    e.generateCode(out);
    out.beginComment();
      out.writeSymbolToken(symAs);
    type.generateCode(out);
      out.endComment();
  }

  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    e.analyze(this, context);
    type.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return e.getSymbol();
  }

  @Override
  boolean isCompileTimeConstant() {
    return e.isCompileTimeConstant();
  }

  @Override
  Ide asQualifiedIde() {
    return e.asQualifiedIde();
  }
}