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

  protected Map<String,Node> ides = new HashMap<String,Node>();
  protected List<LabeledStatement> labels = new ArrayList<LabeledStatement>();
  protected List<LoopStatement> loopStatementStack = new ArrayList<LoopStatement>();
  protected List<KeywordStatement> loopOrSwitchStatementStack = new ArrayList<KeywordStatement>();

  public Node declareIde(String name, Node decl) {
    return ides.put(name, decl);
  }

  public Node declareIde(String name, Node node, boolean allowDuplicates, JooSymbol ideSymbol) {
    Node oldNode = declareIde(name, node);
    if (oldNode!=null) {
      String msg = "Duplicate declaration of identifier '" + name + "'";
      if (allowDuplicates) {
        Jooc.warning(ideSymbol, msg);
      } else {
        Jooc.error(ideSymbol, msg);
      }
    }
    return oldNode;
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

  public Node getIdeDeclaration(Ide ide) {
    return getIdeDeclaration(ide.getName());
  }

  public Node getIdeDeclaration(String name) {
    return ides.get(name);
  }

  public Scope findScopeThatDeclares(Ide ide) {
    return findScopeThatDeclares(ide.getName());
  }

  public Scope findScopeThatDeclares(String name) {
    return getIdeDeclaration(name)!=null ? this
      : getParentScope()==null ? null
      : getParentScope().findScopeThatDeclares(name);
  }

  public Node lookupIde(Ide ide) {
    Scope scope = findScopeThatDeclares(ide);
    if (scope == null)
      Jooc.error(ide.ide, "undeclared identifier: '" + ide.getName() + "'");
    return scope.getIdeDeclaration(ide);
  }

  public Ide createAuxVar() {
    int i=1;
    while (true) {
      String auxVarName = "$" + i;
      Ide auxVar = new Ide(new JooSymbol(auxVarName));
      if (findScopeThatDeclares(auxVar)==null) {
        declareIde(auxVarName, auxVar);
        return auxVar;
      }
      ++i;
    }
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
    return parent==null ? null : parent.getClassDeclaration();
  }

  public MethodDeclaration getMethodDeclaration() {
    if (ideDeclaration instanceof MethodDeclaration)
      return (MethodDeclaration) ideDeclaration;
    return parent == null ? null : parent.getMethodDeclaration();
  }

}
