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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;


/**
 * @author Andreas Gawecki
 */
public class SuperConstructorCallStatement extends Statement {

  private Expr fun;
  private ParenthesizedExpr<CommaSeparatedList<Expr>> args;
  private JooSymbol symSemicolon;
  private ClassDeclaration classDeclaration;

  public SuperConstructorCallStatement(JooSymbol symSuper, JooSymbol lParen, CommaSeparatedList<Expr> args, JooSymbol rParen, JooSymbol symSemicolon) {
    this.setFun(new IdeExpr(symSuper));
    this.setArgs(new ParenthesizedExpr<CommaSeparatedList<Expr>>(lParen, args, rParen));
    this.setSymSemicolon(symSemicolon);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitSuperConstructorCallStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    FunctionDeclaration method = scope.getMethodDeclaration();
    if (method == null || !method.isConstructor()) {
      throw Jooc.error(getSymbol(), "must only call super constructor from constructor method");
    }
    if (method.containsSuperConstructorCall()) {
      throw Jooc.error(getSymbol(), "must not call super constructor twice");
    }
    method.setContainsSuperConstructorCall(true);
    getFun().scope(scope);
    if (getArgs() != null)
      getArgs().scope(scope);
    classDeclaration = scope.getClassDeclaration();
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    getFun().analyze(this, context);
    if (getArgs() != null)
      getArgs().analyze(this, context);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    if (classDeclaration.getInheritanceLevel() > 1) {
      generateFunCode(out);
      generateArgsCode(out);
      classDeclaration.generateFieldInitCode(out, true, false);
    } else { // suppress for classes extending Object
      // Object super call does nothing anyway:
      out.beginComment();
      out.writeSymbol(getSymbol());
      generateArgsCode(out);
      out.endComment();
      classDeclaration.generateFieldInitCode(out, false, false);
    }
    out.writeSymbol(getSymSemicolon());
  }

  private void generateFunCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(getSymbol());
    out.writeToken("this.super$" + classDeclaration.getInheritanceLevel());
  }

  private void generateArgsCode(JsWriter out) throws IOException {
    if (getArgs() != null)
      getArgs().generateCode(out, false);
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

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

  public void setSymSemicolon(JooSymbol symSemicolon) {
    this.symSemicolon = symSemicolon;
  }
}
