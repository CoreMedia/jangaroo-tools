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
public class LabeledStatement extends Statement {

  private Ide ide;
  private JooSymbol symColon;
  private Statement statement;

  public LabeledStatement(Ide ide, JooSymbol symColon, Statement statement) {
    this.setIde(ide);
    this.setSymColon(symColon);
    this.setStatement(statement);
  }

  @Override
  public void visit(AstVisitor visitor) {
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

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getStatement().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    getIde().generateCode(out, false);
    out.writeSymbol(getSymColon());
    getStatement().generateCode(out, false);
  }

  public JooSymbol getSymbol() {
    return getIde().getSymbol();
  }


  public Ide getIde() {
    return ide;
  }

  public void setIde(Ide ide) {
    this.ide = ide;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

  public void setSymColon(JooSymbol symColon) {
    this.symColon = symColon;
  }

  public Statement getStatement() {
    return statement;
  }

  public void setStatement(Statement statement) {
    this.statement = statement;
  }
}
