package net.jangaroo.jooc;

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
     if (ls.ide.getName().equals(ide.getName())) {
        return ls;
      }
    }
    return super.lookupLabel(ide);
  }

  @Override
  public LoopStatement getCurrentLoop() {
    Statement s = statement;
    if (s instanceof LabeledStatement)
      s = ((LabeledStatement) s).statement;
    if (s instanceof LoopStatement)
      return (LoopStatement) s;
    return super.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    Statement s = statement;
    if (s instanceof LabeledStatement)
      s = ((LabeledStatement) s).statement;
    if (s instanceof LoopStatement)
      return (LoopStatement) s;
    if (s instanceof SwitchStatement)
      return (SwitchStatement) s;
    return super.getCurrentLoopOrSwitch();
  }


}
