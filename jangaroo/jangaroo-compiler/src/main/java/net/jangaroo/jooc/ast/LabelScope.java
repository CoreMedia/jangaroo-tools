package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ScopeImplBase;

public class LabelScope extends ScopeImplBase {

  private Statement statement;

  public LabelScope(final Statement statement, final Scope parent) {
    super(parent);
    this.statement = statement;
  }

  @Override
  public LabeledStatement lookupLabel(Ide ide) {
    if (statement instanceof LabeledStatement) {
      LabeledStatement ls = (LabeledStatement) statement;
      if (ls.getIde().getName().equals(ide.getName())) {
        return ls;
      }
    }
    return super.lookupLabel(ide);
  }

  @Override
  public LoopStatement getCurrentLoop() {
    Statement s = statement; // NOSONAR no, this is not a JDBC statement that must be closed ...
    if (s instanceof LabeledStatement) {
      s = ((LabeledStatement) s).getStatement();
    }
    if (s instanceof LoopStatement) {
      return (LoopStatement) s;
    }
    return super.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    Statement s = statement; // NOSONAR no, this is not a JDBC statement that must be closed ...
    if (s instanceof LabeledStatement) {
      s = ((LabeledStatement) s).getStatement();
    }
    if (s instanceof LoopStatement) {
      return s;
    }
    if (s instanceof SwitchStatement) {
      return s;
    }
    return super.getCurrentLoopOrSwitch();
  }


}
