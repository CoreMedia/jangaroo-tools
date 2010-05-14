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
 * @author Frank Wienberg
 */
class TopLevelIdeExpr extends IdeExpr {

  private Scope scope;
  private DotExpr synthesizedDotExpr;

  public TopLevelIdeExpr(Ide ide) {
    super(ide);
  }

  @Override
  Ide asQualifiedIde() {
    return ide;
  }

  @Override
  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    scope = context.getScope();
    if (scope!=null) {
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      if (declaringScope != null) {
        scope.addExternalUsage(ide);
      }
      if (declaringScope==null || declaringScope.getDefiningNode() instanceof ClassDeclaration) {
        // move ide's white-space in front of the newly introduced "this.":
        JooSymbol thisSymbol = new JooSymbol(sym.THIS, ide.ide.fileName, ide.ide.line, ide.ide.column, ide.ide.whitespace, "this");
        JooSymbol ideWithoutWhitespace = new JooSymbol(ide.ide.sym, ide.ide.fileName, ide.ide.line, ide.ide.column, "", ide.ide.text);
        synthesizedDotExpr = new DotExpr(new ThisExpr(thisSymbol), new JooSymbol("."), new Ide(ideWithoutWhitespace));
        synthesizedDotExpr.analyze(parentNode, context);
      }
      if (!(parentNode instanceof DotExpr)
        && !(parentNode instanceof ApplyExpr)) {
        ClassDeclaration classDeclaration = scope.getClassDeclaration();
        if (classDeclaration!=null) {
          classDeclaration.addInitIfClass(ide.getQualifiedNameStr(), context);
        }
      }
    }
    return this;
  }

  @Override
  public void generateCode(JsWriter out) throws IOException {
    if (addThis(out.getOptions())) {
      synthesizedDotExpr.generateCode(out);
    } else {
      super.generateCode(out);
    }
  }

  private boolean addThis(JoocOptions options) {
    if (synthesizedDotExpr!=null) {
      // verify that ide is actually undefined or a non-static, non-constructor member:
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      if (declaringScope==null) {
        // check for fully qualified ide:
        DotExpr currentDotExpr = synthesizedDotExpr;
        StringBuilder ideName = new StringBuilder(ide.getName());
        while (currentDotExpr.parentNode instanceof DotExpr) {
          currentDotExpr = (DotExpr)currentDotExpr.parentNode;
          ideName.append('.').append(currentDotExpr.getArg2().getName());
          declaringScope = scope.findScopeThatDeclares(ideName.toString());
          if (declaringScope!=null) {
            // it has been defined in the meantime or is an imported qualified identifier:
            return false;
          }
        }
        boolean maybeInScope = options.isEnableGuessingClasses() && Character.isUpperCase(ide.getName().charAt(0));
        String warningMsg = "Undeclared identifier: " + ide.getName();
        if (maybeInScope) {
          warningMsg += ", assuming it is a top-level or *-imported class.";
        } else if (options.isEnableGuessingMembers()) {
          warningMsg += ", assuming it is an inherited member.";
        }
        Jooc.warning(ide.getSymbol(), warningMsg);
        return !maybeInScope && options.isEnableGuessingMembers();
      } else if (declaringScope.getDefiningNode() instanceof ClassDeclaration) {
        AstNode ideDeclaration = declaringScope.getIdeDeclaration(ide);
        if (ideDeclaration instanceof MemberDeclaration) {
          MemberDeclaration memberDeclaration = (MemberDeclaration)ideDeclaration;
          return !memberDeclaration.isStatic() && !memberDeclaration.isConstructor();
        //} else {
          // must be an imported namespace.
        }
      }
    }
    return false;
  }

}