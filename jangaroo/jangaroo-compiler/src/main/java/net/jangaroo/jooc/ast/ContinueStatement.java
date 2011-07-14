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
import net.jangaroo.jooc.Jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class ContinueStatement extends LabelRefStatement {

  public ContinueStatement(JooSymbol symContinue, Ide optLabel, JooSymbol symSemicolon) {
    super(symContinue, optLabel, symSemicolon);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitContinueStatement(this);
  }

  @Override
  protected void checkValidLabeledStatement(final LabeledStatement labelDeclaration) {
    if (!(labelDeclaration.getStatement() instanceof LoopStatement)) {
      throw Jooc.error(this, "label '" + getOptLabel().getName() + "' does not reference a loop statement");
    }
  }
}
