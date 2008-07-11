/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package net.jangaroo.jooc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Gawecki
 */
class Scope {

  protected IdeDeclaration ideDeclaration;
  protected Scope parent;

  public Scope(IdeDeclaration ideDeclaration, Scope parent) {
    this.ideDeclaration = ideDeclaration;
    this.parent = parent;
  }

  public IdeDeclaration getDeclaration() {
    return ideDeclaration;
  }

  public Scope getParentScope() {
    return parent;
  }

  protected Map/*String->IdeDeclaration*/ ides = new HashMap();
  protected ArrayList/*LabeledStatement*/ labels = new ArrayList();
  protected ArrayList/*LabeledStatement*/ loopStatementStack = new ArrayList();
  protected ArrayList/*LabeledStatement*/ loopOrSwitchStatementStack = new ArrayList();

  public void declareIde(IdeDeclaration decl) {
    String name = decl.ide.getName();
    IdeDeclaration alreadyDeclared = (IdeDeclaration) ides.get(name);
    if (alreadyDeclared != null)
      Jooc.error(decl.ide.ide, "duplicate declaration of identifier '" + name + "'");
    ides.put(name, decl);
  }

  public void defineLabel(LabeledStatement labeledStatement) {
    LabeledStatement s = lookupLabel(labeledStatement.ide);
    if (s != null)
      Jooc.error(labeledStatement.ide.ide, "label already defined in scope: '" + labeledStatement.ide.getName() + "'");
    labels.add(labeledStatement);
  }

  public void undefineLabel() {
    labels.remove(labels.size()-1);
  }

  public LabeledStatement lookupLabel(Ide ide) {
    String name = ide.getName();
    for (int i = 0; i < labels.size(); i++) {
      LabeledStatement labeledStatement = (LabeledStatement) labels.get(i);
      if (labeledStatement.ide.getName().equals(name))
        return labeledStatement;
    }
    Jooc.error(ide.ide, "undeclared label '" + name + "'");
    return null; // not reached
  }

  public IdeDeclaration lookupIde(Ide ide) {
    IdeDeclaration decl = (IdeDeclaration) ides.get(ide.getName());
    if (decl != null)
      return decl;
    Scope parent = getParentScope();
    if (parent == null)
      Jooc.error(ide.ide, "undeclared identifier: '" + ide.getName() + "'");
    return parent.lookupIde(ide);
  }

  public void enterLoop(LoopStatement loopStatement) {
    loopStatementStack.add(loopStatement);
    loopOrSwitchStatementStack.add(loopStatement);
  }

  public void enterSwitch(SwitchStatement switchStatement) {
    loopOrSwitchStatementStack.add(switchStatement);
  }

  public void exitLoop(LoopStatement loopStatement) {
    Debug.assertTrue(loopStatement == getCurrentLoop(), "loopStatement == getCurrentLoop()");
    loopStatementStack.remove(loopStatementStack.size()-1);
    Debug.assertTrue(loopStatement == getCurrentLoopOrSwitch(), "loopStatement == getCurrentLoopOrSwitch()");
    loopOrSwitchStatementStack.remove(loopOrSwitchStatementStack.size()-1);
  }

  public void exitSwitch(SwitchStatement switchStatement) {
    Debug.assertTrue(switchStatement == getCurrentLoopOrSwitch(), "switchStatement == getCurrentLoopOrSwitch()");
    loopOrSwitchStatementStack.remove(loopOrSwitchStatementStack.size()-1);
  }

  public LoopStatement getCurrentLoop() {
    if (loopStatementStack.isEmpty())
      return null;
    return (LoopStatement) loopStatementStack.get(loopStatementStack.size()-1);
  }

  public Statement getCurrentLoopOrSwitch() {
    if (loopOrSwitchStatementStack.isEmpty())
      return null;
    return (Statement) loopOrSwitchStatementStack.get(loopOrSwitchStatementStack.size()-1);
  }

  public PackageDeclaration getPackageDeclaration() {
    if (ideDeclaration instanceof PackageDeclaration)
      return (PackageDeclaration) ideDeclaration;
    return parent.getPackageDeclaration();
  }

  public ClassDeclaration getClassDeclaration() {
    if (ideDeclaration instanceof ClassDeclaration)
      return (ClassDeclaration) ideDeclaration;
    return parent.getClassDeclaration();
  }

  public MethodDeclaration getMethodDeclaration() {
    if (ideDeclaration instanceof MethodDeclaration)
      return (MethodDeclaration) ideDeclaration;
    if (parent == null)
      return null;
    return parent.getMethodDeclaration();
  }

}
