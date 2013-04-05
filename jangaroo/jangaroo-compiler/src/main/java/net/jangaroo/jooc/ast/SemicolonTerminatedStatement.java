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

import net.jangaroo.jooc.Debug;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
public class SemicolonTerminatedStatement extends Statement {

  private AstNode optStatement;
  private JooSymbol optSymSemicolon;

  /**
   * Empty statement.
   * @param optSymSemicolon the semicolon symbol
   */
  public SemicolonTerminatedStatement(JooSymbol optSymSemicolon) {
    this(null, optSymSemicolon);
  }

  /**
   * Optional statement with optional semicolon, but at least one must be specified (non-null).
   * @param optStatement the statement
   * @param optSymSemicolon the semicolon symbol
   */
  public SemicolonTerminatedStatement(AstNode optStatement, JooSymbol optSymSemicolon) {
    Debug.assertTrue(optStatement != null || optSymSemicolon != null, "Both statement and semicolon not specified in SemicolonTerminatedStatement.");
    this.setOptStatement(optStatement);
    this.setOptSymSemicolon(optSymSemicolon);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optStatement);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitSemicolonTerminatedStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    if (getOptStatement() != null) {
      getOptStatement().scope(scope);
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (getOptStatement() != null) {
      getOptStatement().analyze(this);
    }
  }

  public JooSymbol getSymbol() {
    return getOptSymSemicolon() == null ? getOptStatement().getSymbol() : getOptSymSemicolon();
  }

  public AstNode getOptStatement() {
    return optStatement;
  }

  public void setOptStatement(AstNode optStatement) {
    this.optStatement = optStatement;
  }

  public JooSymbol getOptSymSemicolon() {
    return optSymSemicolon;
  }

  public void setOptSymSemicolon(JooSymbol optSymSemicolon) {
    this.optSymSemicolon = optSymSemicolon;
  }
}
