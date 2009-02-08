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
 * @author Frank Wienberg
 */
class ForInStatement extends LoopStatement {

  JooSymbol lParen;
  Declaration decl;
  Ide ide; // only as alternative to decl
  JooSymbol symIn;
  Expr expr;
  JooSymbol rParen;

  public ForInStatement(JooSymbol symFor, JooSymbol lParen, Declaration decl, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, lParen, decl, null, symIn, expr, rParen, body);
  }

  public ForInStatement(JooSymbol symFor, JooSymbol lParen, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, lParen, null, ide, symIn, expr, rParen, body);
  }

  private ForInStatement(JooSymbol symFor, JooSymbol lParen, Declaration decl, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    super(symFor, body);
    this.lParen = lParen;
    this.decl = decl;
    this.ide = ide;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (decl!=null) {
      decl.generateCode(out);
    } else {
      ide.generateCode(out);
    }
    out.writeSymbol(symIn);
    expr.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    if (decl!=null) {
      decl.analyze(this, context);
    } else {
      ide.analyze(this, context);
    }
    expr.analyze(this, context);
  }

}
