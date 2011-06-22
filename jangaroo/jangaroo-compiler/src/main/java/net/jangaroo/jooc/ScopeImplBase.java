/*
 * Copyright 2010 CoreMedia AG
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

public abstract class ScopeImplBase implements Scope {

  private Scope parent;

  public ScopeImplBase(Scope parent) {
    this.parent = parent;
  }

  @Override
  public Scope getParentScope() {
    return parent;
  }

  @Override
  public void addImport(final ImportDirective importDirective) {
    mustBeInsideValueScope();
    parent.addImport(importDirective);
  }

  @Override
  public AstNode getDefiningNode() {
    if (parent == null) return null;
    return parent.getDefiningNode();
  }

  @Override
  public IdeDeclaration declareIde(final IdeDeclaration decl) {
    mustBeInsideValueScope();
    return parent.declareIde(decl);
  }

  private void mustBeInsideValueScope() {
    if (parent == null)
      throw new UnsupportedOperationException("this scope must be wrapped by a ValueScope");
  }

  @Override
  public LabeledStatement lookupLabel(final Ide ide) {
    if (parent == null) {
        throw Jooc.error(ide, "undeclared label '" + ide.getName() + "'");
    }
    return parent.lookupLabel(ide);
  }

  @Override
  public IdeDeclaration lookupDeclaration(final Ide ide) {
    return parent == null ? null : parent.lookupDeclaration(ide);
  }

  @Override
  public boolean isDeclared(final Ide ide) {
    return parent != null && getParentScope().isDeclared(ide);
  }

  @Override
  public Ide createAuxVar() {
    return parent.createAuxVar();
  }

  @Override
  public LoopStatement getCurrentLoop() {
    if (parent == null) return null;
    return parent.getCurrentLoop();
  }

  @Override
  public Statement getCurrentLoopOrSwitch() {
    if (parent == null) return null;
    return parent.getCurrentLoopOrSwitch();
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return parent == null ? null : parent.getCompilationUnit();
  }

  @Override
  public PackageDeclaration getPackageDeclaration() {
    return parent == null ? null : parent.getPackageDeclaration();
  }

  @Override
  public ClassDeclaration getClassDeclaration() {
    return parent == null ? null : parent.getClassDeclaration();
  }

  @Override
  public FunctionDeclaration getMethodDeclaration() {
    return parent == null ? null : parent.getMethodDeclaration();
  }

  @Override
  public FunctionExpr getFunctionExpr() {
    return parent == null ? null : parent.getFunctionExpr();
  }

  public boolean isPackage(final String fullyQualifiedName) {
    return parent != null && parent.isPackage(fullyQualifiedName);
  }
}
