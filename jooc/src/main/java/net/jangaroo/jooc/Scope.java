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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @author Andreas Gawecki
 */
class Scope {

  protected Node ideDeclaration;
  protected Scope parent;

  public Scope(Node ideDeclaration, Scope parent) {
    this.ideDeclaration = ideDeclaration;
    this.parent = parent;
  }

  public Node getDeclaration() {
    return ideDeclaration;
  }

  public Scope getParentScope() {
    return parent;
  }

  protected Map<String,IdeDeclaration> ides = new HashMap<String,IdeDeclaration>();
  protected List<LabeledStatement> labels = new ArrayList<LabeledStatement>();
  protected List<LoopStatement> loopStatementStack = new ArrayList<LoopStatement>();
  protected List<KeywordStatement> loopOrSwitchStatementStack = new ArrayList<KeywordStatement>();

  public void declareIde(IdeDeclaration decl) {
    String name = decl.ide.getName();
    IdeDeclaration alreadyDeclared = ides.get(name);
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
    for (LabeledStatement label : labels) {
      if (label.ide.getName().equals(name))
        return label;
    }
    Jooc.error(ide.ide, "undeclared label '" + name + "'");
    return null; // not reached
  }

  public IdeDeclaration getIdeDeclaration(Ide ide) {
    return ides.get(ide.getName());
  }

  public Scope findScopeThatDeclares(Ide ide) {
    return getIdeDeclaration(ide)!=null ? this
      : getParentScope()==null ? null
      : getParentScope().findScopeThatDeclares(ide);
  }

  public IdeDeclaration lookupIde(Ide ide) {
    Scope scope = findScopeThatDeclares(ide);
    if (scope == null)
      Jooc.error(ide.ide, "undeclared identifier: '" + ide.getName() + "'");
    return scope.getIdeDeclaration(ide);
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
    return loopStatementStack.get(loopStatementStack.size()-1);
  }

  public Statement getCurrentLoopOrSwitch() {
    if (loopOrSwitchStatementStack.isEmpty())
      return null;
    return loopOrSwitchStatementStack.get(loopOrSwitchStatementStack.size()-1);
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
