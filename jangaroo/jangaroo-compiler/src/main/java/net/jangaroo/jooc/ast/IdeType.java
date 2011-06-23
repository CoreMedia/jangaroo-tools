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

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class IdeType extends Type {

  private Ide ide;

  public IdeType(Ide ide) {
    this.setIde(ide);
  }

  public IdeType(JooSymbol symIde) {
    this(new Ide(symIde));
  }

  public Ide getIde() {
    return ide;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitIdeType(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getIde().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    getIde().generateJsCode(out);
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    getIde().generateAsApiCode(out);
  }

  public JooSymbol getSymbol() {
      return getIde().getSymbol();
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    final IdeDeclaration ideDeclaration = getIde().getDeclaration(false);
    return ideDeclaration == null ? null : ideDeclaration.resolveDeclaration();
  }

  public void setIde(Ide ide) {
    this.ide = ide;
  }
}
