package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ScopeImplBase;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LoopStatement;
import net.jangaroo.jooc.ast.Statement;
import net.jangaroo.jooc.ast.SwitchStatement;

public class LabelScope extends ScopeImplBase {

  private Statement statement;

  public LabelScope(final Statement statement, final Scope parent) {
    super(parent);
    this.statement = statement;
  }

  public Statement getStatement() {
    return statement;
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
    Statement s = statement;
    if (s instanceof LabeledStatement)
      s = ((LabeledStatement) s).getStatement();
    if (s instanceof LoopStatement)
      return (LoopStatement) s;
    return super.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    Statement s = statement;
    if (s instanceof LabeledStatement)
      s = ((LabeledStatement) s).getStatement();
    if (s instanceof LoopStatement)
      return (LoopStatement) s;
    if (s instanceof SwitchStatement)
      return (SwitchStatement) s;
    return super.getCurrentLoopOrSwitch();
  }


}
