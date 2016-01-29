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
public class ForStatement extends ConditionalLoopStatement {

  private JooSymbol lParen;
  private ForInitializer forInit;
  private JooSymbol symSemicolon1;
  private JooSymbol symSemicolon2;
  private Expr optStep;
  private JooSymbol rParen;

  public ForStatement(JooSymbol symFor, JooSymbol lParen, ForInitializer forInit, JooSymbol symSemicolon1, Expr optCond, JooSymbol symSemicolon2, Expr optStep, JooSymbol rParen, Statement body) {
    super(symFor, optCond, body);
    this.lParen = lParen;
    this.forInit = forInit;
    this.symSemicolon1 = symSemicolon1;
    this.symSemicolon2 = symSemicolon2;
    this.optStep = optStep;
    this.rParen = rParen;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), forInit, optStep);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitForStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    if (getForInit() != null) {
      getForInit().scope(scope);
    }
    super.scope(scope);
    if (getOptStep() != null) {
      getOptStep().scope(scope);
    }
  }

  protected void analyzeLoopHeader() {
    // check conformance to ECMA-262 7.9.1: a semicolon is never inserted automatically if
    // that semicolon would become one of the two semicolons in the header of a for statement
    checkNonVirtualSemicolon(getSymSemicolon1());
    checkNonVirtualSemicolon(getSymSemicolon2());
    if (getForInit() != null) {
      getForInit().analyze(this);
    }
    super.analyzeLoopHeader();
    if (getOptStep() != null) {
      getOptStep().analyze(this);
    }
  }

  private void checkNonVirtualSemicolon(JooSymbol semi) {
    if (semi.isVirtual()) {
      throw JangarooParser.error(semi, "missing ';'" +
              "(automatic semicolon insertion would become one of the two semicolons in the header of a for statement)");
    }
  }

  public JooSymbol getLParen() {
    return lParen;
  }

  public ForInitializer getForInit() {
    return forInit;
  }

  public JooSymbol getSymSemicolon1() {
    return symSemicolon1;
  }

  public JooSymbol getSymSemicolon2() {
    return symSemicolon2;
  }

  public Expr getOptStep() {
    return optStep;
  }

  public JooSymbol getRParen() {
    return rParen;
  }

}
