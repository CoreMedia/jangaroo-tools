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
  public static final boolean ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS = Boolean.valueOf("true");
  private Scope scope;
  private DotExpr synthesizedDotExpr;

  public TopLevelIdeExpr(Ide ide) {
    super(ide);
  }

  @Override
  public Expr analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    scope = context.getScope();
    if (scope!=null) {
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      if (declaringScope==null || declaringScope.getDeclaration() instanceof ClassDeclaration) {
        synthesizedDotExpr = new DotExpr(new ThisExpr(new JooSymbol("this")), new JooSymbol("."), new Ide(ide.ide));
        synthesizedDotExpr.analyze(parentNode, context);
      }
    }
    return this;
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
        // check for fully qualified ide:
        DotExpr currentDotExpr = synthesizedDotExpr;
        String ideName = ide.getName();
        while (currentDotExpr.parentNode instanceof DotExpr) {
          currentDotExpr = (DotExpr)currentDotExpr.parentNode;
          ideName += "." + currentDotExpr.getArg2().ide.getName();
          declaringScope = scope.findScopeThatDeclares(ideName);
          if (declaringScope!=null) {
            // it has been defined in the meantime or is an imported qualified identifier:
            return false;
          }
        }
        boolean maybeInScope = Character.isUpperCase(ide.getName().charAt(0));
        String warningMsg = "Undeclared identifier: " + ide.getName();
        if (maybeInScope) {
          warningMsg += ", assuming it is already in scope.";
        } else if (ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS) {
          warningMsg += ", assuming it is an inherited member.";
        }
        Jooc.warning(ide.getSymbol(), warningMsg);
        return !maybeInScope && ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS;
      } else if (declaringScope.getDeclaration() instanceof ClassDeclaration) {
        Node ideDeclaration = declaringScope.getIdeDeclaration(ide);
        if (ideDeclaration instanceof MemberDeclaration) {
          MemberDeclaration memberDeclaration = (MemberDeclaration)ideDeclaration;
          return !memberDeclaration.isStatic() && !memberDeclaration.isConstructor();
        } else {
          // must be an imported namespace.
        }
      }
    }
    return false;
  }

}