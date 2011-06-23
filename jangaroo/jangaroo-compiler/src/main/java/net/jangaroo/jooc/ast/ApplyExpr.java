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

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
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
    this.setFun(fun);
    this.setArgs(new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen));
  }

  @Override
  public void visit(AstVisitor visitor) {
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

  public void generateJsCode(JsWriter out) throws IOException {
    generateFunJsCode(out);
    if (getArgs() != null) {
      boolean isAssert = getFun() instanceof IdeExpr && SyntacticKeywords.ASSERT.equals(getFun().getSymbol().getText());
      if (isAssert) {
        JooSymbol symKeyword = getFun().getSymbol();
        out.writeSymbol(getArgs().getlParen());
        getArgs().getExpr().generateCode(out, false);
        out.writeToken(", ");
        out.writeString(symKeyword.getFileName());
        out.writeToken(", ");
        out.writeInt(symKeyword.getLine());
        out.write(", ");
        out.writeInt(symKeyword.getColumn());
        out.writeSymbol(getArgs().getrParen());
      } else {
        getArgs().generateCode(out, false);
      }
    }
  }

  private void generateFunJsCode(JsWriter out) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    if (isTypeCast()) {
      out.beginComment();
      getFun().generateCode(out, false);
      out.endComment();
    } else {
      getFun().generateCode(out, false);
    }
  }

  private boolean isTypeCast() {
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

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getFun().analyze(this, context);
    if (getArgs() != null)
      getArgs().analyze(this, context);
  }

  public JooSymbol getSymbol() {
    return getFun().getSymbol();
  }

  public Expr getFun() {
    return fun;
  }

  public void setFun(Expr fun) {
    this.fun = fun;
  }

  public ParenthesizedExpr<CommaSeparatedList<Expr>> getArgs() {
    return args;
  }

  public void setArgs(ParenthesizedExpr<CommaSeparatedList<Expr>> args) {
    this.args = args;
  }
}
