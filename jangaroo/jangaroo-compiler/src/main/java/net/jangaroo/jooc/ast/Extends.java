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

import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class Extends extends NodeImplBase {

  private JooSymbol symExtends;
  private Ide superClass;

  public Extends(JooSymbol extds, Ide superClass) {
    this.symExtends = extds;
    this.superClass = superClass;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), superClass);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitExtends(this);
  }

  @Override
  public void scope(final Scope scope) {
    getSuperClass().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (!(getSuperClass().getDeclaration() instanceof ClassDeclaration)) {
      throw new CompilerError(getSuperClass().getSymbol(), "identifier in extends clause must denote a class");
    }
    getSuperClass().analyze(this);
    getSuperClass().analyzeAsExpr(this, null);
    getSuperClass().addExternalUsage();
    getSuperClass().addPublicApiDependency();
  }

  public JooSymbol getSymbol() {
    return getSymExtends();
  }

  public JooSymbol getSymExtends() {
    return symExtends;
  }

  public Ide getSuperClass() {
    return superClass;
  }

}
