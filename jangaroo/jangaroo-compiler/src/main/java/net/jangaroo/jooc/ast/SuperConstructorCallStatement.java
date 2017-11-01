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
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;


/**
 * @author Andreas Gawecki
 */
public class SuperConstructorCallStatement extends Statement {

  private Expr fun;
  private ParenthesizedExpr<CommaSeparatedList<Expr>> args;
  private JooSymbol symSemicolon;
  private ClassDeclaration classDeclaration;

  public SuperConstructorCallStatement(JooSymbol symSuper, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen, JooSymbol symSemicolon) {
    this.fun = new IdeExpr(symSuper);
    this.args = new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen);
    this.symSemicolon = symSemicolon;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), fun, args);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitSuperConstructorCallStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    getFun().scope(scope);
    if (getArgs() != null) {
      getArgs().scope(scope);
    }
    setClassDeclaration(scope.getClassDeclaration());
    FunctionDeclaration method = scope.getMethodDeclaration();
    if (method == null || !method.isConstructor()) {
      throw JangarooParser.error(getSymbol(), "must only call super constructor from constructor method");
    }
    if (method.containsSuperConstructorCall()) {
      throw JangarooParser.error(getSymbol(), "must not call super constructor twice");
    }
    method.setContainsSuperConstructorCall(true);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getFun().analyze(this);
    if (getArgs() != null) {
      getArgs().analyze(this);
    }
  }

  public JooSymbol getSymbol() {
    return getFun().getSymbol();
  }

  public Expr getFun() {
    return fun;
  }

  public ParenthesizedExpr<CommaSeparatedList<Expr>> getArgs() {
    return args;
  }

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void setClassDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }
}
