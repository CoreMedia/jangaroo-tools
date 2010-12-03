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

/**
 * @author Andreas Gawecki
 */
class SemicolonTerminatedStatement extends Statement {

  private ClassDeclaration classDeclaration;
  AstNode optStatement;
  JooSymbol optSymSemicolon;

  /**
   * Empty statement.
   */
  SemicolonTerminatedStatement(JooSymbol optSymSemicolon) {
    this(null, optSymSemicolon);
  }

  /**
   * Optional statement with optional semicolon, but at least one must be specified (non-null).
   */
  SemicolonTerminatedStatement(AstNode optStatement, JooSymbol optSymSemicolon) {
    Debug.assertTrue(optStatement!=null || optSymSemicolon!=null, "Both statement and semicolon not specified in SemicolonTerminatedStatement.");
    this.optStatement = optStatement;
    this.optSymSemicolon = optSymSemicolon;
  }

  @Override
  public void scope(final Scope scope) {
    classDeclaration = scope.getClassDeclaration();
    if (optStatement != null) {
      optStatement.scope(scope);
    }
  }

  protected void generateStatementCode(JsWriter out) throws IOException {
    if (optStatement!=null)
      optStatement.generateCode(out);
  }
  
  protected void generateJsCode(JsWriter out) throws IOException {
    generateStatementCode(out);
    if (optSymSemicolon != null)
      out.writeSymbol(optSymSemicolon);
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    // check for special case "assert statement":
    if (optStatement instanceof ApplyExpr && optSymSemicolon!=null) {
      ApplyExpr applyExpr = (ApplyExpr)optStatement;
      JooSymbol funSymbol = applyExpr.fun.getSymbol();
      String functionName = funSymbol.getText();
      if ("trace".equals(functionName) || SyntacticKeywords.ASSERT.equals(functionName)) {
        classDeclaration.addBuiltInUsage(functionName);
        if (SyntacticKeywords.ASSERT.equals(functionName)) {
          ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.args;
          CommaSeparatedList<Expr> params = args.expr;
          if (params != null && params.tail == null) {
            AssertStatement assertStatement = new AssertStatement(funSymbol, args.lParen, params.head, args.rParen, optSymSemicolon);
            assertStatement.analyze(parentNode, context);
            return assertStatement;
          }
        }
      }
    }
    //: new SemicolonTerminatedStatement(new ApplyExpr(new TopLevelIdeExpr(ide), lp, args, rp), s);
    //? new AssertStatement(ide.ide,lp,(Expr)args.head,rp,s)
    
    super.analyze(parentNode, context);
    if (optStatement!=null) {
      optStatement = optStatement.analyze(this, context);
    }
    return this;
  }

  public JooSymbol getSymbol() {
     return optSymSemicolon==null ? optStatement.getSymbol() : optSymSemicolon;
  }
}
