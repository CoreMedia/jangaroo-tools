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

  public TopLevelIdeExpr(Ide ide) {
    super(ide);
  }

  @Override
  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    scope = context.getScope();
  }

  @Override
  public void generateCode(JsWriter out) throws IOException {
    if (scope!=null) {
      Scope declaringScope = scope.findScopeThatDeclares(ide);
      boolean addMissingThis = false;
      if (declaringScope == null) {
        addMissingThis = ASSUME_UNDECLARED_IDENTIFIERS_ARE_MEMBERS && Character.isLowerCase(ide.getName().charAt(0));
        Jooc.warning(ide.getSymbol(), "Undeclared identifier: "+ide.getName()
          +(addMissingThis ? ", assuming it is an inherited member." : ""));
      } else {
        Node declaration = declaringScope.getDeclaration();
        if (declaration instanceof ClassDeclaration) {
          MemberDeclaration memberDeclaration = (MemberDeclaration)declaringScope.getIdeDeclaration(ide);
          if (!memberDeclaration.isStatic() && !memberDeclaration.isConstructor()) {
            addMissingThis = true;
          }
        }
      }
      if (addMissingThis) {
        DotExpr synthesizedDotExpr = new DotExpr(new ThisExpr(new JooSymbol("this")), new JooSymbol("."), this.ide);
        synthesizedDotExpr.parentNode = parentNode;
        synthesizedDotExpr.classDeclaration = scope.getClassDeclaration();
        synthesizedDotExpr.generateCode(out);
        return;
      }
    }
    super.generateCode(out);
  }
}