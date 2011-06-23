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
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * A LabelRefStatement is either a break or continue statement
 *
 * @author Andreas Gawecki
 */
public abstract class LabelRefStatement extends KeywordExprStatement {

  LabelRefStatement(JooSymbol symStatement, Ide optLabel, JooSymbol symSemicolon) {
    super(symStatement, null, symSemicolon);
    this.setOptLabel(optLabel);
  }

  private Ide optLabel;

  private LabeledStatement labelDeclaration = null;

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getOptLabel() == null) {
      Statement loopOrSwitchStatement = scope.getCurrentLoopOrSwitch();
      if (loopOrSwitchStatement == null)
        throw Jooc.error(this, "not inside loop or switch");
    } else {
      setLabelDeclaration(scope.lookupLabel(getOptLabel()));
      checkValidLabeledStatement(getLabelDeclaration());
    }
  }

  protected abstract void checkValidLabeledStatement(final LabeledStatement labelDeclaration);

  @Override
  protected void generateStatementCode(final JsWriter out) throws IOException {
    super.generateStatementCode(out);
    if (getOptLabel() != null)
      getOptLabel().generateJsCode(out);
  }

  public Ide getOptLabel() {
    return optLabel;
  }

  public void setOptLabel(Ide optLabel) {
    this.optLabel = optLabel;
  }

  public LabeledStatement getLabelDeclaration() {
    return labelDeclaration;
  }

  public void setLabelDeclaration(LabeledStatement labelDeclaration) {
    this.labelDeclaration = labelDeclaration;
  }
}
