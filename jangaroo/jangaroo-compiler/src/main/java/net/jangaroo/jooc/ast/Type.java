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

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class Type extends NodeImplBase {

  private Ide ide;

  public Type(Ide ide) {
    this.ide = ide;
  }

  public Type(JooSymbol symIde) {
    this(new Ide(symIde));
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide);
  }

  public Ide getIde() {
    return ide;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitType(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    Ide ide = getIde();
    ide.analyze(this);
    if (ide instanceof IdeWithTypeParam) {
      ((IdeWithTypeParam) ide).getType().analyze(this);
    } else {
      IdeDeclaration declaration = ide.getDeclaration();
      JangarooParser compiler = ide.getScope().getCompiler();
      if (!(declaration instanceof TypeDeclaration)) {
        compiler.getLog().error(ide.getSymbol(), "Type was not found or was not a compile-time constant: " + ide.getSymbol().getText());
      }
      ide.addExternalUsage(null);
    }
  }

  public JooSymbol getSymbol() {
    return getIde().getSymbol();
  }

  public TypeDeclaration getDeclaration() {
    return getDeclaration(true);
  }

  public TypeDeclaration resolveDeclaration() {
    return getDeclaration(false);
  }

  public TypeDeclaration getDeclaration(boolean errorIfUndeclared) {
    IdeDeclaration declaration = getIde().getDeclaration(errorIfUndeclared);
    return declaration instanceof TypeDeclaration ? (TypeDeclaration) declaration : null;
  }

}
