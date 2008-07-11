/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class AssertStatement extends KeywordExprStatement {


  public AssertStatement(JooSymbol symAssert, Expr expr, JooSymbol symSemicolon) {
    super(symAssert, expr, symSemicolon);
  }

  public void generateCode(JsWriter out) throws IOException {
    if (out.getEnableAssertions()) {
      out.writeSymbolWhitespace(symKeyword);
      out.writeToken("_joo_assert(");
      optExpr.generateCode(out);
      out.write(", ");
      out.writeString(symKeyword.getFileName()+"("+symKeyword.getLine()+","+symKeyword.getColumn()+")");
      out.write(");");
    } else {
      out.beginComment();
      super.generateCode(out);
      out.endComment();
    }
  }

}
