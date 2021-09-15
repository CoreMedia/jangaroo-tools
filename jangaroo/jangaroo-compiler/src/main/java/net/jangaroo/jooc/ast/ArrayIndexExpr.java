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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.types.ExpressionType;

import java.io.IOException;
import java.util.List;


/**
 * @author Andreas Gawecki
 */
public class ArrayIndexExpr extends Expr {

  private Expr array;
  private ParenthesizedExpr<Expr> indexExpr;
  private Scope scope;

  public ArrayIndexExpr(Expr array, JooSymbol lBrac, Expr index, JooSymbol rBrac) {
    this.array = array;
    this.indexExpr = new ParenthesizedExpr<Expr>(lBrac, index, rBrac);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), array, indexExpr);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitArrayIndexExpr(this);
  }

  @Override
  public void scope(final Scope scope) {
    this.scope = scope;
    array.scope(scope);
    indexExpr.scope(scope);
  }

  public JooSymbol getSymbol() {
    return array.getSymbol();
  }

  public Expr getArray() {
    return array;
  }

  public ParenthesizedExpr<Expr> getIndexExpr() {
    return indexExpr;
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    array.analyze(this);
    indexExpr.analyze(this);
    ExpressionType arrayType = array.getType();
    if (arrayType != null && arrayType.isArrayLike()) {
      setType(arrayType.getTypeParameter());
    }
    CompilationUnit compilationUnitFromJooGetOrCreatePackage = getCompilationUnitFromJooGetOrCreatePackage();
    if (compilationUnitFromJooGetOrCreatePackage != null) {
      scope.addDependencyFromJooGetOrCreatePackage(compilationUnitFromJooGetOrCreatePackage, indexExpr.getExpr().getSymbol());
    }
  }

  public CompilationUnit getCompilationUnitFromJooGetOrCreatePackage() {
    if (array instanceof ApplyExpr && indexExpr.getExpr() instanceof LiteralExpr) {
      Object value = ((LiteralExpr) indexExpr.getExpr()).getValue().getJooValue();
      if (value instanceof String) {
        return scope.getCompiler().getCompilationUnitFromJooGetOrCreatePackage(((ApplyExpr) array).getPackageNameFromJooGetOrCreatePackage(), (String) value);
      }
    }
    return null;
  }
}
