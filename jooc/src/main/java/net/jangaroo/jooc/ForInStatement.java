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
import java.util.Arrays;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
class ForInStatement extends LoopStatement {

  JooSymbol symEach;
  JooSymbol lParen;
  VariableDeclaration decl;
  Ide ide; // only as alternative to decl
  JooSymbol symIn;
  Expr expr;
  JooSymbol rParen;
  Ide auxIde; // generated for each loop auxilliary variable
  private static final JooSymbol SYM_VAR = new JooSymbol(sym.VAR, "var");
  private static final JooSymbol SYM_EQ = new JooSymbol(sym.EQ, "=");
  private static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");

  public ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, VariableDeclaration decl, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, symEach, lParen, decl, null, symIn, expr, rParen, body);
  }

  public ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, symEach, lParen, null, ide, symIn, expr, rParen, body);
  }

  private ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, VariableDeclaration decl, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    super(symFor, body);
    this.symEach = symEach;
    this.lParen = lParen;
    this.decl = decl;
    this.ide = ide;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    if (symEach!=null) {
      out.beginComment();
      out.writeSymbol(symEach);
      out.endComment();
    }
    out.writeSymbol(lParen);
    if (symEach!=null) {
      new VariableDeclaration(SYM_VAR, auxIde, null, null).generateCode(out);
    } else {
      if (decl!=null) {
        decl.generateCode(out);
      } else {
        ide.generateCode(out);
      }
    }
    out.writeSymbol(symIn);
    expr.generateCode(out);
    out.writeSymbol(rParen);
    if (symEach!=null) {
      // synthesize assigning the correct index to the variable given in the original for each statement:
      ArrayIndexExpr indexExpr = new ArrayIndexExpr(expr, new JooSymbol(sym.LBRACK, "["),
        new CommaSeparatedList(new IdeExpr(auxIde)),
        new JooSymbol(sym.RBRACK, "]"));
      Node assignment = decl!=null
        ? new DeclarationStatement(new VariableDeclaration(SYM_VAR, decl.ide, decl.optTypeRelation, new Initializer(SYM_EQ, indexExpr)), SYM_SEMICOLON)
        : new ExprStatement(new AssignmentOpExpr(new IdeExpr(ide), SYM_EQ, indexExpr), SYM_SEMICOLON);
      // inject synthesized statement into loop body:
      if (body instanceof BlockStatement) {
        ((BlockStatement)body).statements.add(0, assignment);
      } else {
        body = new BlockStatement(new JooSymbol(sym.LBRACE, "{"), Arrays.asList(assignment, body), new JooSymbol(sym.RBRACE, "}"));
      }
    }
  }

  @Override
  protected void generateLoopFooterCode(JsWriter out) throws IOException {
    super.generateLoopFooterCode(out);    //To change body of overridden methods use File | Settings | File Templates.
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (symEach!=null) {
      auxIde = context.getScope().createAuxVar();
    }
    if (decl!=null) {
      decl.analyze(this, context);
    } else {
      ide.analyze(this, context);
    }
    expr = expr.analyze(this, context);
  }

}
