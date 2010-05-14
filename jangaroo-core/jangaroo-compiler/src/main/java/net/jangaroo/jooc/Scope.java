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
import java.util.List;
import java.util.Map;

/**
 * @author Andreas Gawecki
 */
class Scope {

  protected AstNode ideDeclaration;
  protected Scope parent;

  public Scope(AstNode ideDeclaration, Scope parent) {
    this.ideDeclaration = ideDeclaration;
    this.parent = parent;
  }

  public AstNode getDeclaration() {
    return ideDeclaration;
  }

  public Scope getParentScope() {
    return parent;
  }

  protected Map<String, AstNode> ides = new HashMap<String, AstNode>();
  protected List<LabeledStatement> labels = new ArrayList<LabeledStatement>();
  protected List<LoopStatement> loopStatementStack = new ArrayList<LoopStatement>();
  protected List<KeywordStatement> loopOrSwitchStatementStack = new ArrayList<KeywordStatement>();

  public AstNode declareIde(String name, AstNode decl) {
    return ides.put(name, decl);
  }

  public AstNode declareIde(String name, AstNode node, boolean allowDuplicates, JooSymbol ideSymbol) {
    AstNode oldNode = declareIde(name, node);
    if (oldNode != null) {
      String msg = "Duplicate declaration of identifier '" + name + "'";
      if (allowDuplicates) {
        Jooc.warning(ideSymbol, msg);
      } else {
        throw Jooc.error(ideSymbol, msg);
      }
    }
    return oldNode;
  }

  public LabeledStatement lookupLabel(Ide ide) {
    String name = ide.getName();
    for (LabeledStatement label : labels) {
      if (label.ide.getName().equals(name)) {
        return label;
      }
    }
    throw Jooc.error(ide.ide, "undeclared label '" + name + "'");
  }

  public AstNode getIdeDeclaration(Ide ide) {
    return getIdeDeclaration(ide.getName());
  }

  public AstNode getIdeDeclaration(String name) {
    return ides.get(name);
  }

  public Scope findScopeThatDeclares(Ide ide) {
    return findScopeThatDeclares(ide.getName());
  }

  public Scope findScopeThatDeclares(String name) {
    return getIdeDeclaration(name) != null ? this
        : getParentScope() == null ? null
        : getParentScope().findScopeThatDeclares(name);
  }

  public AstNode lookupIde(Ide ide) {
    Scope scope = findScopeThatDeclares(ide);
    if (scope == null) {
      throw Jooc.error(ide.ide, "undeclared identifier: '" + ide.getName() + "'");
    }
    return scope.getIdeDeclaration(ide);
  }

  public Ide createAuxVar() {
    int i = 1;
    while (true) {
      String auxVarName = "$" + i;
      Ide auxVar = new Ide(new JooSymbol(auxVarName));
      if (findScopeThatDeclares(auxVar) == null) {
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
    loopStatementStack.remove(loopStatementStack.size() - 1);
    Debug.assertTrue(loopStatement == getCurrentLoopOrSwitch(), "loopStatement == getCurrentLoopOrSwitch()");
    loopOrSwitchStatementStack.remove(loopOrSwitchStatementStack.size() - 1);
  }

  public void exitSwitch(SwitchStatement switchStatement) {
    Debug.assertTrue(switchStatement == getCurrentLoopOrSwitch(), "switchStatement == getCurrentLoopOrSwitch()");
    loopOrSwitchStatementStack.remove(loopOrSwitchStatementStack.size() - 1);
  }

  public LoopStatement getCurrentLoop() {
    if (loopStatementStack.isEmpty()) {
      return null;
    }
    return loopStatementStack.get(loopStatementStack.size() - 1);
  }

  public Statement getCurrentLoopOrSwitch() {
    if (loopOrSwitchStatementStack.isEmpty()) {
      return null;
    }
    return loopOrSwitchStatementStack.get(loopOrSwitchStatementStack.size() - 1);
  }

  public void addExternalUsage(Ide ide) {
    String fqn = ide.getQualifiedNameStr();
    Scope packageScope = findScopeThatDeclares(fqn);
    if (packageScope != null) {
      AstNode classImport = packageScope.getIdeDeclaration(fqn);
      if (classImport instanceof ImportDirective) {
        ((ImportDirective)classImport).wasUsed();
      }
    }
  }

  public CompilationUnit getCompilationUnit() {
    return (CompilationUnit)getPackageDeclaration().parentNode;
  }

  public PackageDeclaration getPackageDeclaration() {
    if (ideDeclaration instanceof PackageDeclaration) {
      return (PackageDeclaration) ideDeclaration;
    }
    return parent.getPackageDeclaration();
  }

  public ClassDeclaration getClassDeclaration() {
    if (ideDeclaration instanceof ClassDeclaration) {
      return (ClassDeclaration) ideDeclaration;
    }
    return parent == null ? null : parent.getClassDeclaration();
  }

  public MethodDeclaration getMethodDeclaration() {
    if (ideDeclaration instanceof MethodDeclaration) {
      return (MethodDeclaration) ideDeclaration;
    }
    return parent == null ? null : parent.getMethodDeclaration();
  }

}
