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

import net.jangaroo.jooc.config.JoocOptions;

/**
 * @author Andreas Gawecki
 */
public class AnalyzeContext {

  protected PackageDeclaration packageDeclaration = null;
  protected Scope scope = null;
  private JoocOptions config;

  public AnalyzeContext(JoocOptions config) {
    this.config = config;
  }

  public void enterScope(AstNode declaration) {
    scope = new ValueScope(declaration, scope);
  }

  public void leaveScope(AstNode declaration) {
    if (declaration != scope.getDefiningNode())
      throw Jooc.error("internal error: wrong scope to leave");
    scope = scope.getParentScope();
  }

  public Scope getScope() {
    return scope;
  }

  void setScope(Scope scope) {
    this.scope = scope;
  }

  public PackageDeclaration getCurrentPackage() {
    if (packageDeclaration == null) {
      packageDeclaration = scope.getPackageDeclaration();
    }
    return packageDeclaration;
  }

  public ClassDeclaration getCurrentClass() {
    return scope.getClassDeclaration();
  }

  public MethodDeclaration getCurrentMethod() {
    return scope.getMethodDeclaration();
  }

  public JoocOptions getConfig() {
    return config;
  }

  public void defineLabel(final LabeledStatement labeledStatement) {
    scope = new LabelScope(labeledStatement, scope);
  }

  public void undefineLabel() {
    assert scope instanceof LabelScope;
    scope = scope.getParentScope();
  }

  public void enterLoop(final LoopStatement loopStatement) {
    scope = new LabelScope(loopStatement, scope);
  }

  public void exitLoop(final LoopStatement statement) {
    assert scope instanceof LabelScope;
    assert ((LabelScope) scope).getStatement() == statement;
    scope = scope.getParentScope();
  }

  public void enterSwitch(final SwitchStatement statement) {
    scope = new LabelScope(statement, scope);
  }

  public void exitSwitch(final SwitchStatement statement) {
    assert scope instanceof LabelScope;
    assert ((LabelScope) scope).getStatement() == statement;
    scope = scope.getParentScope();
  }
}
