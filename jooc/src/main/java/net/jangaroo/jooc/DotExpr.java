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
class DotExpr extends BinaryOpExpr {

  Expr expr;
  JooSymbol symDot;
  Ide ide;
  private ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public JooSymbol getSymbol() {
    return expr.getSymbol();
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    classDeclaration = context.getCurrentClass();
  }

  public void generateCode(JsWriter out) throws IOException {
    if (classDeclaration!=null
        && arg1 instanceof ThisExpr && arg2 instanceof IdeExpr
        && classDeclaration.isPrivateMember(((IdeExpr)arg2).ide.getName())) {
      arg1.generateCode(out);
      out.write("[");
      // awkward, but we have to be careful if we add characters to tokens:
      final JooSymbol symbol = ((IdeExpr) arg2).getSymbol();
      out.writeSymbol(symbol,  "_", "");
      out.write("]");
    } else {
      super.generateCode(out);
    }
  }
}
