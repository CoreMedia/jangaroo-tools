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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ApplyExpr extends Expr {

  private Expr fun;
  private ParenthesizedExpr<CommaSeparatedList<Expr>> args;

  private boolean insideNewExpr = false;
  private static final Set<String> COERCE_FUNCTION_NAMES = new HashSet<String>(Arrays.asList("Number", "String", "Boolean", "int", "uint", "Date", "Array", "RegExp"));

  public ApplyExpr(Expr fun, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen) {
    this.fun = fun;
    this.args = new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen);
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), fun, args);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitApplyExpr(this);
  }

  public boolean isInsideNewExpr() {
    return insideNewExpr;
  }

  public void setInsideNewExpr(final boolean insideNewExpr) {
    this.insideNewExpr = insideNewExpr;
  }

  @Override
  public void scope(final Scope scope) {
    getFun().scope(scope);
    getArgs().scope(scope);
  }

  public boolean isTypeCast() {
    return getFun() instanceof IdeExpr && !isInsideNewExpr() && isNonCoercingType((IdeExpr) getFun());
  }

  private boolean isNonCoercingType(IdeExpr fun) {
    final Ide ide = fun.getIde();
    IdeDeclaration declaration = ide.getDeclaration(false);
    return declaration != null &&
            (declaration instanceof ClassDeclaration || declaration instanceof PredefinedTypeDeclaration ||
                    (declaration instanceof FunctionDeclaration && declaration.isConstructor()))
            && (declaration.isClassMember() || !COERCE_FUNCTION_NAMES.contains(declaration.getQualifiedNameStr())
    );
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getFun().analyze(this);
    if (getArgs() != null) {
      getArgs().analyze(this);
    }
  }

  public JooSymbol getSymbol() {
    return getFun().getSymbol();
  }

  public Expr getFun() {
    return fun;
  }

  public ParenthesizedExpr<CommaSeparatedList<Expr>> getArgs() {
    return args;
  }

}
