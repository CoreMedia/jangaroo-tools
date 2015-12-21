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
public class Implements extends NodeImplBase {
  private JooSymbol symImplements;
  private CommaSeparatedList<Ide> superTypes;

  public Implements(JooSymbol symImplements, CommaSeparatedList<Ide> superTypes) {
    this.symImplements = symImplements;
    this.superTypes = superTypes;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), superTypes);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitImplements(this);
  }

  @Override
  public void scope(final Scope scope) {
    getSuperTypes().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getSuperTypes().analyze(this);
    CommaSeparatedList<Ide> localSuperTypes = this.getSuperTypes();
    while (localSuperTypes != null) {
      Ide superType = localSuperTypes.getHead();
      superType.addExternalUsage(true);
      superType.addPublicApiDependency();
      localSuperTypes = localSuperTypes.getTail();
    }
  }

  public JooSymbol getSymbol() {
    return getSymImplements();
  }

  public JooSymbol getSymImplements() {
    return symImplements;
  }

  public CommaSeparatedList<Ide> getSuperTypes() {
    return superTypes;
  }

}
