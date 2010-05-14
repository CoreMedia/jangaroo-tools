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

package net.jangaroo.jooc;

/**
 * A LabelRefStatement is either a break or continue statement
 *
 * @author Andreas Gawecki
 */
class LabelRefStatement extends KeywordExprStatement {

  LabelRefStatement(JooSymbol symStatement, Ide optLabel, JooSymbol symSemicolon) {
    super(symStatement, null, symSemicolon);
    this.optLabel = optLabel;
  }

  protected Ide optLabel;

  protected LabeledStatement labelDeclaration = null;

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    Scope scope = context.getScope();
    if (optLabel == null) {
      Statement loopOrSwitchStatement = scope.getCurrentLoopOrSwitch();
      if (loopOrSwitchStatement == null)
        throw Jooc.error(this, "not inside loop or switch");
    } else {
      labelDeclaration = scope.lookupLabel(optLabel);
      if (!(labelDeclaration.statement instanceof LoopStatement))
        throw Jooc.error(this, "label does not reference loop statement");
    }
    return this;
  }

}
