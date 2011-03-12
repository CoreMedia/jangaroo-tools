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

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 */
class BlockStatement extends Statement {

  JooSymbol lBrace;
  List<AstNode> statements;
  JooSymbol rBrace;
  List<CodeGenerator> blockStartCodeGenerators = new ArrayList<CodeGenerator>(3);

  public BlockStatement(JooSymbol lBrace, List<AstNode> statements, JooSymbol rBrace) {
    this.lBrace = lBrace;
    this.statements = statements;
    this.rBrace = rBrace;
  }

  @Override
  public void scope(final Scope scope) {
    scope(statements, scope);
  }

  public void addBlockStartCodeGenerator(CodeGenerator blockStartCodeGenerator) {
    blockStartCodeGenerators.add(blockStartCodeGenerator);
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    //todo we have to be more restrictive here...
    super.generateAsApiCode(out);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    for (CodeGenerator codeGenerator : blockStartCodeGenerators) {
      codeGenerator.generateCode(out);
    }
    generateCode(statements, out);
    out.writeSymbol(rBrace);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    statements = analyze(this, statements, context);
    return this;
  }

  // TODO: Check when analyzing the super call
  public void checkSuperConstructorCall() {
    for (AstNode o : statements) {
      if (o instanceof SuperConstructorCallStatement) return;
    }
    throw Jooc.error(lBrace, "super constructor must be called directly in method block");
  }

  public JooSymbol getSymbol() {
     return rBrace;
  }
}
