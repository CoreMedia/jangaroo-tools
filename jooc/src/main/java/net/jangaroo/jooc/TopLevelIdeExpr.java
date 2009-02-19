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
 * @author Frank Wienberg
 */
class TopLevelIdeExpr extends IdeExpr {

  // TODO: add a compiler option for this:
  public static final boolean ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS = true;
  private Scope scope;
  private DotExpr synthesizedDotExpr;

  public TopLevelIdeExpr(Ide ide) {
    super(ide);
  }

  @Override
  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    scope = context.getScope();
    if (scope!=null) {
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      if (declaringScope==null && ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS && !Character.isUpperCase(ide.getName().charAt(0))
       || declaringScope!=null && declaringScope.getDeclaration() instanceof ClassDeclaration) {
        synthesizedDotExpr = new DotExpr(new ThisExpr(new JooSymbol("this")), new JooSymbol("."), ide);
        synthesizedDotExpr.analyze(parentNode, context);
      }
    }
  }

  @Override
  public void generateCode(JsWriter out) throws IOException {
    if (addThis()) {
      synthesizedDotExpr.generateCode(out);
    } else {
      super.generateCode(out);
    }
  }

  private boolean addThis() {
    if (synthesizedDotExpr!=null) {
      // verify that ide is actually undefined or a non-static, non-constructor member:
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      if (declaringScope==null) {
        Jooc.warning(ide.getSymbol(), "Undeclared identifier: "+ide.getName()
          +(ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS ? ", assuming it is an inherited member." : ""));
        return ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS;
      } else if (declaringScope.getDeclaration() instanceof ClassDeclaration) {
        MemberDeclaration memberDeclaration = (MemberDeclaration)declaringScope.getIdeDeclaration(ide);
        return !memberDeclaration.isStatic() && !memberDeclaration.isConstructor();
      }
    }
    return false;
  }
}