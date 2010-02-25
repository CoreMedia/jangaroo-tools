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
 * @author Frank Wienberg
 */
class DotExpr extends BinaryOpExpr {

  ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public Ide getArg2() {
    return ((IdeExpr)arg2).ide;
  }

  public Expr analyze(Node parentNode, AnalyzeContext context) {
    this.classDeclaration = context.getCurrentClass();
    // check candidates for instance methods declared in same file, accessed as function:
    if (this.classDeclaration !=null) {
      String property = getArg2().getName();
      // check and handle instance methods declared in same file, accessed as function:
      if (arg1 instanceof ThisExpr
        && !(parentNode instanceof DotExpr)
        && !(parentNode instanceof ApplyExpr)
        && !(parentNode instanceof AssignmentOpExpr && ((AssignmentOpExpr)parentNode).arg1== this)
        && !(parentNode instanceof PrefixOpExpr &&
             (((PrefixOpExpr)parentNode).op.sym==sym.TYPEOF || ((PrefixOpExpr)parentNode).op.sym==sym.DELETE))) {
        this.classDeclaration.addBoundMethodCandidate(property);
      } else {
        Ide packageIde = arg1.asQualifiedIde();
        if (packageIde != null) {
          if (context.getCurrentPackage().isPackage(packageIde.getQualifiedNameStr())) {
            // Replace my AST subtree by qualified identifier subtree:
            return new TopLevelIdeExpr(asQualifiedIde(packageIde)).analyze(parentNode, context);
          }
        }
      }
    }
    super.analyze(parentNode, context);
    // check access to constant from another class; other class then must be initialized:
    if (this.classDeclaration!=null && arg1 instanceof IdeExpr && !(parentNode instanceof ApplyExpr)) {
      classDeclaration.addInitIfClass(((IdeExpr)arg1).ide.getQualifiedNameStr(), context);
    }
    return this;
  }

  @Override
  Ide asQualifiedIde() {
    Ide packageIde = arg1.asQualifiedIde();
    return packageIde == null ? null : asQualifiedIde(packageIde);
  }

  private QualifiedIde asQualifiedIde(Ide packageIde) {
    return new QualifiedIde(packageIde, op, getArg2().getSymbol());
  }

  @Override
  public void generateCode(JsWriter out) throws IOException {
    if (classDeclaration!=null) {
      String property = getArg2().getName();
      // check and handle private instance members and super method access:
      if ( arg1 instanceof ThisExpr && classDeclaration.isPrivateMember(property)
        || arg1 instanceof SuperExpr) {
        out.writeSymbolWhitespace(arg1.getSymbol());
        out.writeToken("this");
        out.write("[");
        // awkward, but we have to be careful if we add characters to tokens:
        out.writeSymbol(getArg2().getSymbol(),  "$", "");
        out.write("]");
        return;
      }
      // check and handle private static member access:
      if (arg1 instanceof IdeExpr && classDeclaration.isPrivateStaticMember(property)) {
        String qualifiedName = ((IdeExpr)arg1).ide.getQualifiedNameStr();
        // Found private static member access candidate qualifiedName+"."+property in class classDeclaration.getQualifiedNameStr()
        if (qualifiedName.equals(classDeclaration.getName())
          || qualifiedName.equals(classDeclaration.getQualifiedNameStr())) {
          // Found private static member access qualifiedName+"."+property
          JooSymbol arg1Symbol = arg1.getSymbol();
          // replace current class by "$jooPrivate":
          arg1 = new IdeExpr(new Ide(new JooSymbol(net.jangaroo.jooc.sym.IDE, arg1Symbol.fileName, arg1Symbol.line,
            arg1Symbol.column, arg1Symbol.whitespace, "$$private")));
        }
      }
    }
    super.generateCode(out);
  }
}
