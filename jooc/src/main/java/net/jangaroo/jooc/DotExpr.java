/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class DotExpr extends BinaryOpExpr {

  Expr expr;
  JooSymbol symDot;
  Ide ide;
  private ClassDeclaration classDeclaration;

  public DotExpr(Expr expr, JooSymbol symDot, Ide ide) {
    super(expr, symDot, new IdeExpr(ide));
  }

  public JooSymbol getSymbol() {
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
      out.write("[");
      // awkward, but we have to be careful if we add characters to tokens:
      final JooSymbol symbol = ((IdeExpr) arg2).getSymbol();
      out.writeSymbol(symbol,  "_", "");
      out.write("]");
    } else {
      super.generateCode(out);
    }
  }
}
