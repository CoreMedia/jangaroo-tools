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
class AssertStatement extends KeywordExprStatement {

  JooSymbol lParen;
  JooSymbol rParen;

  public AssertStatement(JooSymbol symAssert, JooSymbol lParen, Expr expr, JooSymbol rParen, JooSymbol symSemicolon) {
    super(symAssert, expr, symSemicolon);
    this.lParen = lParen;
    this.rParen = rParen;
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    if (out.getEnableAssertions()) {
      out.writeSymbolWhitespace(symKeyword);
      out.writeToken("assert");
      out.writeSymbol(lParen);
      out.write("(");
      optStatement.generateCode(out);
      out.write(")");
      out.write(", ");
      out.writeString(symKeyword.getFileName());
      out.write(", ");
      out.writeInt(symKeyword.getLine());
      out.write(", ");
      out.writeInt(symKeyword.getColumn());
      out.writeSymbol(rParen);
      out.writeSymbol(optSymSemicolon);
    } else {
      out.beginComment();
      super.generateJsCode(out);
      out.endComment();
    }
  }

}
