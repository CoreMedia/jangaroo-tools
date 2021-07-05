package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.sym;

public class Spread extends PrefixOpExpr implements ObjectFieldOrSpread {

  public Spread(JooSymbol op, Expr arg) {
    super(op, arg);
    assert op.sym == sym.REST && "...".equals(op.getText());
  }

}
