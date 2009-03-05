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

  // TODO: add a compiler option for this:
  public static final boolean ASSUME_UNDECLARED_UPPER_CASE_FUNCTIONS_CALLS_ARE_TYPE_CASTS = Boolean.valueOf("true");

  Expr fun;
  Scope scope;
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
    // leave out constructor function if called as type cast function!
    if (isTypeCast()) {
      out.beginComment();
      fun.generateCode(out);
      out.endComment();
    } else {
      fun.generateCode(out);
    }
    generateArgsCode(out);
  }

  private boolean isTypeCast() {
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
          ? ASSUME_UNDECLARED_UPPER_CASE_FUNCTIONS_CALLS_ARE_TYPE_CASTS
          : declScope.getDeclaration() == scope.getPackageDeclaration();
      }
    }
    return false;
  }

  protected void generateArgsCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (args != null)
      args.generateCode(out);
    out.writeSymbol(rParen);
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
