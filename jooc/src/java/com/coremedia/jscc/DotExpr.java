/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

class DotExpr extends BinaryOpExpr {

  Expr expr;
  JscSymbol symDot;
  Ide ide;
  private ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JscSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public JscSymbol getSymbol() {
    return expr.getSymbol();
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    classDeclaration = context.getCurrentClass();
  }

  public void generateCode(JsWriter out) throws IOException {
    if (classDeclaration!=null
        && arg1 instanceof ThisExpr && arg2 instanceof IdeExpr
        && classDeclaration.isPrivateMember(((IdeExpr)arg2).ide.getName())) {
      arg1.generateCode(out);
      out.write("[_");
      arg2.generateCode(out);
      out.write("]");
    } else {
      super.generateCode(out);
    }
  }
}
