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

import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LoopStatement;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.Statement;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;

public interface Scope {

  AstNode getDefiningNode();

  Scope getParentScope();

  void addImport(ImportDirective importDirective);

  IdeDeclaration declareIde(IdeDeclaration decl);

  LabeledStatement lookupLabel(Ide ide);

  IdeDeclaration lookupDeclaration(Ide ide);

  IdeDeclaration lookupDeclaration(Ide ide, boolean failOnAmbigousImport);

  boolean isDeclared(Ide ide);

  /**
   * Preferred name must be a valid AS identifier!
   * @see Ide#IDE_PATTERN
   */
  Ide createAuxVar(Scope lookupScope, String preferredName);

  LoopStatement getCurrentLoop();

  Statement getCurrentLoopOrSwitch();

  CompilationUnit getCompilationUnit();

  PackageDeclaration getPackageDeclaration();

  ClassDeclaration getClassDeclaration();

  FunctionDeclaration getMethodDeclaration();

  FunctionExpr getFunctionExpr();

  boolean isPackage(String fullyQualifiedName);

  Ide findFreeAuxVar(String preferredName);

  DeclarationScope getPackageDeclarationScope();

  JangarooParser getCompiler();

  ClassDeclaration getClassDeclaration(String qname);

  ExpressionType getExpressionType(AS3Type as3Type);

  FunctionSignature getFunctionSignature(Parameters params, ExpressionType returnType);

  ExpressionType getExpressionType(TypeRelation typeRelation);

  ExpressionType getExpressionType(IdeDeclaration declaration);
}
