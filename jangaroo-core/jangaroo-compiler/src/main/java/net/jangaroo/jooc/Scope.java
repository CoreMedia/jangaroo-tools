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

public interface Scope {

  AstNode getDefiningNode();

  Scope getParentScope();

  void addImport(ImportDirective importDirective);

  IdeDeclaration declareIde(IdeDeclaration decl);

  LabeledStatement lookupLabel(Ide ide);

  IdeDeclaration lookupDeclaration(Ide ide);

  boolean isDeclared(Ide ide);

  Ide createAuxVar();

  LoopStatement getCurrentLoop();

  Statement getCurrentLoopOrSwitch();

  CompilationUnit getCompilationUnit();

  PackageDeclaration getPackageDeclaration();

  ClassDeclaration getClassDeclaration();

  FunctionDeclaration getMethodDeclaration();

  FunctionExpr getFunctionExpr();

  boolean isPackage(String fullyQualifiedName);

}
