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
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class IdeExpr extends Expr {

  private Ide ide;

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
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIdeExpression(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getIde().analyze(this, context);
    getIde().analyzeAsExpr(parentNode, this, context);
    setType(getIde().resolveDeclaration());
  }

  public JooSymbol getSymbol() {
     return getIde().getSymbol();
  }

  @Override
  public boolean isCompileTimeConstant() {
    IdeDeclaration ideDeclaration = getIde().getDeclaration(false);
    // accept constant fields, not being defined in the current class (because these have to be initialized first):
    return ideDeclaration instanceof VariableDeclaration &&
            ((VariableDeclaration)ideDeclaration).isCompileTimeConstant() &&
            ideDeclaration.getClassDeclaration() != getIde().getScope().getClassDeclaration();
  }

  public Ide getIde() {
    return ide;
  }

}
