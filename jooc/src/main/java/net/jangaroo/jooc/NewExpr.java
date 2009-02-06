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
class NewExpr extends Expr {

  JooSymbol symNew;
  Type type;
  JooSymbol lParen;
  Arguments args;
  JooSymbol rParen;

  public NewExpr(JooSymbol symNew, Type type, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    this.symNew = symNew;
    this.type = type;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
  }

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (args != null)
      args.analyze(this, context);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.writeSymbol(symNew);
    type.generateCode(out);
    out.writeSymbol(lParen);
    if (args != null) args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public JooSymbol getSymbol() {
      return symNew;
  }

}
