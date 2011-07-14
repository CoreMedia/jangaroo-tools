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

/**
 * @author Andreas Gawecki
 */
public class ForInitializer extends NodeImplBase {

  private Declaration decl = null;
  private Expr expr = null;

  public ForInitializer(Declaration decl) {
    this.decl = decl;
  }

  public ForInitializer(Expr expr) {
    this.expr = expr;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitForInitializer(this);
  }

  @Override
  public void scope(final Scope scope) {
    if (getDecl() != null) {
      getDecl().scope(scope);
    }
    if (getExpr() != null) {
      getExpr().scope(scope);
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (getDecl() != null) {
      getDecl().analyze(this);
    }
    if (getExpr() != null) {
      getExpr().analyze(this);
    }
  }

  public JooSymbol getSymbol() {
    return getDecl() != null ? getDecl().getSymbol() : getExpr().getSymbol();
  }

  public Declaration getDecl() {
    return decl;
  }

  public Expr getExpr() {
    return expr;
  }

}
