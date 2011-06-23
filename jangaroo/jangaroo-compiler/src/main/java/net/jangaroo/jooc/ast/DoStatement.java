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

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class DoStatement extends ConditionalLoopStatement {
  private JooSymbol symWhile;
  private JooSymbol symSemicolon;

  public DoStatement(JooSymbol symDo, Statement statement, JooSymbol symWhile, ParenthesizedExpr cond, JooSymbol symSemicolon) {
    super(symDo, cond, statement);
    this.setSymWhile(symWhile);
    this.setSymSemicolon(symSemicolon);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitDoStatement(this);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
  }

  protected void analyzeLoopFooter(AnalyzeContext context) {
    getOptCond().analyze(this, context);
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
  }

  protected void generateLoopFooterCode(JsWriter out) throws IOException {
    out.writeSymbol(getSymWhile());
    getOptCond().generateCode(out);
    out.writeSymbol(getSymSemicolon());
  }

  public JooSymbol getSymWhile() {
    return symWhile;
  }

  public void setSymWhile(JooSymbol symWhile) {
    this.symWhile = symWhile;
  }

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

  public void setSymSemicolon(JooSymbol symSemicolon) {
    this.symSemicolon = symSemicolon;
  }
}
