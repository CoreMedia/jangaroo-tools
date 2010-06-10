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
    Scope scope = ide.getScope();
    if (scope != null) {
      ide.addExternalUsage();
      //todo handle references to static super members
      if (!(parentNode instanceof DotExpr)
        && !(parentNode instanceof ApplyExpr)) {
        ClassDeclaration classDeclaration = scope.getClassDeclaration();
        if (classDeclaration != null) {
          classDeclaration.addInitIfClass(ide);
        }
      }
    }
    return this;
  }

 /*
  @Override
  public void generateCode(JsWriter out) throws IOException {
    if (ide instanceof QualifiedIde) { //todo collapse QualifiedIde, NamespacedIde and Ide
      ide.generateCode(out);
    } else {
      out.writeSymbolWhitespace(ide.getSymbol());
      IdeDeclaration decl = ide.getDeclaration();
      String[] prefix = null;
      if (decl.isMember()) {
        if (decl.isPrivate()) {
          DotExpr.writePrivateMemberAccess(null, null, ide, decl.isStatic(), out);
          return;
        }
        if (decl.isStatic()) {
          if (decl.getClassDeclaration() != ide.getScope().getClassDeclaration()) {
            // not within same class, same class is within 'with' scope for now
            prefix = new String[]{ decl.getClassDeclaration().getQualifiedNameStr(), "."};
          }
        } else if (!decl.isConstructor()) {
          prefix = new String[] {"this" , "."};
        }
      } else {
        // add package prefix if it is not a local
        if (decl.getParentDeclaration() instanceof PackageDeclaration) {
          String qname = ((PackageDeclaration)decl.getParentDeclaration()).getQualifiedNameStr();
          if (!qname.isEmpty())
            prefix = new String[]{qname, "."};
        }
      }
      if (prefix != null) {
        for (String token : prefix) {
          out.write(token);
        }
      }
      out.writeSymbol(ide.getSymbol(), false);
    }
  }
*/
}