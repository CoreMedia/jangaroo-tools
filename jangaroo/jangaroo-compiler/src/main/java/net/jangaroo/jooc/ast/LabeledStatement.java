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
public class LabeledStatement extends Statement {

  private Ide ide;
  private JooSymbol symColon;
  private Statement statement;

  public LabeledStatement(Ide ide, JooSymbol symColon, Statement statement) {
    this.ide = ide;
    this.symColon = symColon;
    this.statement = statement;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide, statement);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitLabeledStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    getIde().scope(scope);
    withNewLabelScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        getStatement().scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getStatement().analyze(this);
  }

  public JooSymbol getSymbol() {
    return getIde().getSymbol();
  }


  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

  public Statement getStatement() {
    return statement;
  }

}
