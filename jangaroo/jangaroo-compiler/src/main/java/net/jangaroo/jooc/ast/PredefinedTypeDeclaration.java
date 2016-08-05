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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class PredefinedTypeDeclaration extends TypeDeclaration {
  private final boolean dynamic;

  //todo define well-known types as final consts here

  public PredefinedTypeDeclaration(final String name, boolean dynamic) {
    super(new Ide(new JooSymbol(name)));
    this.dynamic = dynamic;
  }

  @Override
  public boolean isDynamic() {
    return dynamic;
  }

  @Override
  public TypedIdeDeclaration getMemberDeclaration(String memberName) {
    return null;
  }

  @Override
  public TypedIdeDeclaration getStaticMemberDeclaration(String memberName) {
    return null;
  }

  @Override
  public ClassDeclaration getSuperTypeDeclaration() {
    return null;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitPredefinedTypeDeclaration(this);
  }

  @Override
  protected int getAllowedModifiers() {
    return MODIFIER_PUBLIC;
  }
}
