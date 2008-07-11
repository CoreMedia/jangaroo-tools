/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class ForInStatement extends LoopStatement {

  JooSymbol lParen;
  Declaration decl;
  JooSymbol symIn;
  Expr expr;
  JooSymbol rParen;

  public ForInStatement(JooSymbol symFor, JooSymbol lParen, Declaration decl, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    super(symFor, body);
    this.lParen = lParen;
    this.decl = decl;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  protected void generateLoopHeaderCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    decl.generateCode(out);
    out.writeSymbol(symIn);
    expr.generateCode(out);
    out.writeSymbol(rParen);
  }

  protected void analyzeLoopHeader(AnalyzeContext context) {
    decl.analyze(context);
    expr.analyze(context);
  }

}
