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
import net.jangaroo.jooc.Jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class EmptyStatement extends SemicolonTerminatedStatement {

  public EmptyStatement(JooSymbol symSemicolon) {
    super(symSemicolon);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitEmptyStatement(this);
  }

  @Override
  public void analyze(final AstNode parentNode, final AnalyzeContext context) {
    // this is an empty statement.  Check conformance to ECMA-262 7.9.1:
    //   'a semicolon is never inserted automatically if the semicolon would then be parsed as an empty statement'
    if (getOptSymSemicolon().isVirtual()) {
      throw Jooc.error(getOptSymSemicolon(), "missing ';' (automatic semicolon insertion would create an empty statement)");
    }
    super.analyze(parentNode, context);
  }
}