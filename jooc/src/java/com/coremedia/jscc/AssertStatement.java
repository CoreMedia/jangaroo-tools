/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class AssertStatement extends KeywordExprStatement {


  public AssertStatement(JscSymbol symAssert, Expr expr, JscSymbol symSemicolon) {
    super(symAssert, expr, symSemicolon);
  }

  public void generateCode(JsWriter out) throws IOException {
    if (out.getEnableAssertions()) {
      out.writeSymbolWhitespace(symKeyword);
      out.writeToken("_jsc_assert(");
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
