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
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class ObjectLiteral extends Expr {

  private JooSymbol lBrace;
  private CommaSeparatedList<ObjectField> fields;
  private JooSymbol optComma;
  private JooSymbol rBrace;

  /**
   * @param lBrace the left brace
   * @param fields the object fields
   * @param optComma null for the time being, Flex compc does not accept a trailing comma, contrary to array literals...
   * @param rBrace the right brace
   */
  public ObjectLiteral(JooSymbol lBrace, CommaSeparatedList<ObjectField> fields, JooSymbol optComma, JooSymbol rBrace) {
    this.lBrace = lBrace;
    this.fields = fields;
    this.optComma = optComma;
    this.rBrace = rBrace;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitObjectLiteral(this);
  }

  @Override
  public void scope(final Scope scope) {
    if (getFields() != null) {
      getFields().scope(scope);
    }
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (getFields() != null) {
      getFields().analyze(this, context);
    }
  }

  public JooSymbol getSymbol() {
    return getLBrace();
  }

  public JooSymbol getLBrace() {
    return lBrace;
  }

  public CommaSeparatedList<ObjectField> getFields() {
    return fields;
  }

  public JooSymbol getOptComma() {
    return optComma;
  }

  public JooSymbol getRBrace() {
    return rBrace;
  }
}
