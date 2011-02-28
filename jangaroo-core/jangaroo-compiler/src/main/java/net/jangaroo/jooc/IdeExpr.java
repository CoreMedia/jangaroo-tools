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
class IdeExpr extends Expr {

  Ide ide;

  public IdeExpr(JooSymbol symIde) {
    this(new Ide(symIde));
  }

  public IdeExpr(Ide ide) {
    this.ide = ide;
  }

  public static IdeExpr fromPrefix(JooSymbol symPrefix, JooSymbol symDot, Ide ide) {
    return new IdeExpr(ide.qualify(symPrefix, symDot));
  }

  @Override
  public void scope(final Scope scope) {
    ide.scope(scope);
  }

  @Override
  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    ide.analyze(this, context);
    ide.analyzeAsExpr(parentNode, this, context);
    setType(ide.resolveDeclaration());
    return this;
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    ide.generateCodeAsExpr(out);
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    ide.generateAsApiCode(out);
  }

  public JooSymbol getSymbol() {
     return ide.getSymbol();
  }

  @Override
  boolean isCompileTimeConstant() {
    IdeDeclaration ideDeclaration = ide.getDeclaration(false);
    // accept constant fields, not being defined in the current class (because these have to be initialized first):
    return ideDeclaration instanceof FieldDeclaration && ((FieldDeclaration)ideDeclaration).isConst() && ideDeclaration.getClassDeclaration() != ide.getScope().getClassDeclaration();
  }

}
