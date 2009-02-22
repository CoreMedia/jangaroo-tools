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
class ApplyExpr extends Expr {

  Expr fun;
  boolean isType;
  JooSymbol lParen;
  Arguments args;
  JooSymbol rParen;

  public ApplyExpr(Expr fun, JooSymbol lParen, Arguments args, JooSymbol rParen) {
    this.fun = fun;
    this.lParen = lParen;
    this.args = args;
    this.rParen = rParen;
  }

  public void generateCode(JsWriter out) throws IOException {
    if (isType) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      fun.generateCode(out);
    }
    generateArgsCode(out);
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rParen);
  }

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    // leave out constructor function if called as type cast function!
    if (fun instanceof IdeExpr) {
      // TODO: make it work for fully qualified identifiers!
      Ide funIde = ((IdeExpr)fun).ide;
      // heuristic for types: start with upper case letter.
      // otherwise, it is most likely an imported package-namespaced function.
      if (Character.isUpperCase(funIde.getName().charAt(0))) {
        Scope scope = context.getScope().findScopeThatDeclares(funIde);
        if (scope!=null) {
          isType = scope.getDeclaration()==context.getScope().getPackageDeclaration();
        }
      }
    }
    fun.analyze(this, context);
    if (args != null)
      args.analyze(this, context);
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }


}
