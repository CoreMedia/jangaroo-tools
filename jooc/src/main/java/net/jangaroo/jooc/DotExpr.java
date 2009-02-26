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

  ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public Expr analyze(Node parentNode, AnalyzeContext context) {
    this.classDeclaration = context.getCurrentClass();
    // check candidates for instance methods declared in same file, accessed as function:
    if (this.classDeclaration !=null && arg2 instanceof IdeExpr) {
      String property = ((IdeExpr)arg2).ide.getName();
      // check and handle instance methods declared in same file, accessed as function:
      if (arg1 instanceof ThisExpr
        && !(parentNode instanceof DotExpr)
        && !(parentNode instanceof ApplyExpr)
        && !(parentNode instanceof DeleteStatement)
        && !(parentNode instanceof AssignmentOpExpr && ((AssignmentOpExpr)parentNode).arg1== this)
        && !(parentNode instanceof PrefixOpExpr && ((PrefixOpExpr)parentNode).op.sym==sym.TYPEOF)) {
        this.classDeclaration.addBoundMethodCandidate(property);
      } else {
        QualifiedIde fqie = getFullyQualifiedIde();
        if (fqie!=null) {
          String qualifiedName = fqie.getQualifiedNameStr();
          if (context.getCurrentPackage().isFullyQualifiedIde(context, qualifiedName)) {
            // Replace my AST subtree by qualified identifier subtree:
            return new TopLevelIdeExpr(fqie);
          }
        }
        // check access to constant from another class; other class then must be initialized:
        if (arg1 instanceof IdeExpr && !(parentNode instanceof ApplyExpr)) {
          Ide ide = ((IdeExpr)arg1).ide;
          String qualifiedNameStr = ide.getQualifiedNameStr();
          Scope declaringScope = context.getScope().findScopeThatDeclares(qualifiedNameStr);
          if (declaringScope==null && !(ide instanceof QualifiedIde) && Character.isUpperCase(qualifiedNameStr.charAt(0))
            || declaringScope!=null && declaringScope.getDeclaration().equals(context.getCurrentPackage())) {
            this.classDeclaration.addClassInit(qualifiedNameStr);
          }
        }
      }
    }
    return super.analyze(parentNode, context);
  }

  private QualifiedIde getFullyQualifiedIde() {
    if (arg2 instanceof IdeExpr) {
      Ide prefixIde = 
        arg1 instanceof IdeExpr && !(arg1 instanceof ThisExpr || arg1 instanceof SuperExpr) ? ((IdeExpr)arg1).ide
      : arg1 instanceof DotExpr ? ((DotExpr)arg1).getFullyQualifiedIde()
      : null;
      if (prefixIde!=null) {
        return new QualifiedIde(prefixIde, op, ((IdeExpr)arg2).ide.getSymbol());
      }
    }
    return null;
  }

  public void generateCode(JsWriter out) throws IOException {
    if (classDeclaration!=null
      && arg2 instanceof IdeExpr) {
      String property = ((IdeExpr)arg2).ide.getName();
      // check and handle private instance members and super method access:
      if ( arg1 instanceof ThisExpr && classDeclaration.isPrivateMember(property)
        || arg1 instanceof SuperExpr) {
        out.writeSymbolWhitespace(arg1.getSymbol());
        out.writeToken("this");
        out.write("[");
        // awkward, but we have to be careful if we add characters to tokens:
        out.writeSymbol(arg2.getSymbol(),  "$", "");
        out.write("]");
        return;
      }
      // check and handle private static member access:
      if (arg1 instanceof IdeExpr && classDeclaration.isPrivateStaticMember(property)) {
        String qualifiedName = ((IdeExpr)arg1).ide.getQualifiedNameStr();
        if (qualifiedName.equals(classDeclaration.getName())
          || qualifiedName.equals(classDeclaration.getQualifiedNameStr())) {
          JooSymbol arg1Symbol = arg1.getSymbol();
          // replace current class by "$jooPrivate":
          arg1 = new IdeExpr(new Ide(new JooSymbol(net.jangaroo.jooc.sym.IDE, arg1Symbol.fileName, arg1Symbol.line,
            arg1Symbol.column, arg1Symbol.whitespace, "$jooPrivate")));
        }
      }
    }
    super.generateCode(out);
  }
}
