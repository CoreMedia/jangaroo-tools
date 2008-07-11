/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Andreas Gawecki
 */
class BlockStatement extends Statement {

  JooSymbol lBrace;
  ArrayList statements;
  JooSymbol rBrace;

  public BlockStatement(JooSymbol lBrace, ArrayList statements, JooSymbol rBrace) {
    this.lBrace = lBrace;
    this.statements = statements;
    this.rBrace = rBrace;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    generateCode(statements, out);
    out.writeSymbol(rBrace);
  }

  public void generateCodeWithSuperCall(JsWriter out) throws IOException {
    out.writeSymbol(lBrace);
    out.write("this[_super]();");
    generateCode(statements, out);
    out.writeSymbol(rBrace);
  }

  public void analyze(AnalyzeContext context) {
    analyze(statements, context);
  }

  // TODO: Check when analyzing the super call
  public void checkSuperConstructorCall() {
    for (Iterator i = statements.iterator(); i.hasNext();) {
      Object o =  i.next();
      if (o instanceof SuperConstructorCallStatement) return;
    }
    Jooc.error(lBrace, "super constructor must be called directly in method block");
  }

  public JooSymbol getSymbol() {
     return rBrace;
  }
}
