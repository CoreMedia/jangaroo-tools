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
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.KeywordStatement;
import net.jangaroo.jooc.ast.Statement;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public abstract class LoopStatement extends KeywordStatement {

  private Statement body;

  public LoopStatement(JooSymbol symLoop, Statement body) {
    super(symLoop);
    this.setBody(body);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    super.generateJsCode(out);
    generateLoopHeaderCode(out);
    getBody().generateCode(out);
    generateLoopFooterCode(out);
  }

  protected abstract void generateLoopHeaderCode(JsWriter out) throws IOException;

  protected void generateLoopFooterCode(JsWriter out) throws IOException {
  }

  @Override
  public void scope(final Scope scope) {
    withNewLabelScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        getBody().scope(scope);
      }
    });
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    analyzeLoopHeader(context);
    getBody().analyze(this, context);
    analyzeLoopFooter(context);
  }

  protected abstract void analyzeLoopHeader(AnalyzeContext context);

  protected void analyzeLoopFooter(AnalyzeContext context) {
  }

  public Statement getBody() {
    return body;
  }

  public void setBody(Statement body) {
    this.body = body;
  }
}
