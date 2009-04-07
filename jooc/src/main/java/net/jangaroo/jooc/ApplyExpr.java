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

import net.jangaroo.jooc.config.JoocOptions;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
class ApplyExpr extends Expr {

  Expr fun;
  ParenthesizedExpr<CommaSeparatedList<Expr>> args;
  Scope scope;

  public ApplyExpr(Expr fun, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen) {
    this.fun = fun;
    this.args = new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen);
  }

  public void generateCode(JsWriter out) throws IOException {
    // leave out constructor function if called as type cast function!
    if (isTypeCast(out.options)) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      fun.generateCode(out);
    }
    if (args != null)
      args.generateCode(out);
  }

  private boolean isTypeCast(JoocOptions options) {
    if (scope!=null && fun instanceof IdeExpr) {
      // TODO: make it work correctly for fully qualified identifiers!
      String name = ((IdeExpr)fun).ide.getName();
      // special case: it is a type cast to the current class:
      if (scope.getClassDeclaration()!=null && scope.getClassDeclaration().getName().equals(name)) {
        return true;
      }
      // heuristic for types: start with upper case letter.
      // otherwise, it is most likely an imported package-namespaced function.
      if (Character.isUpperCase(name.charAt(0))) {
        Scope declScope = scope.findScopeThatDeclares(name);
        return declScope == null
          ? options.isEnableGuessingTypeCasts()
          : declScope.getDeclaration() == scope.getPackageDeclaration();
      }
    }
    return false;
  }

  public Expr analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    fun = fun.analyze(this, context);
    scope = context.getScope();
    if (args != null)
      args.analyze(this, context);
    return this;
  }

  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }


}
