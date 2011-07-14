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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ForInStatement extends LoopStatement {

  private JooSymbol symEach;
  private JooSymbol lParen;
  private VariableDeclaration decl;
  private Ide ide; // only as alternative to decl
  private JooSymbol symIn;
  private Expr expr;
  private JooSymbol rParen;
  private Ide auxIde; // generated for each loop auxiliary variable

  public ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, VariableDeclaration decl, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, symEach, lParen, decl, null, symIn, expr, rParen, body);
  }

  public ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    this(symFor, symEach, lParen, null, ide, symIn, expr, rParen, body);
  }

  private ForInStatement(JooSymbol symFor, JooSymbol symEach, JooSymbol lParen, VariableDeclaration decl, Ide ide, JooSymbol symIn, Expr expr, JooSymbol rParen, Statement body) {
    super(symFor, body);
    if (!(symEach == null || SyntacticKeywords.EACH.equals(symEach.getText()))) {
      throw Jooc.error(symEach, "'for' must be followed by '(' or 'each'.");
    }
    this.symEach = symEach;
    this.lParen = lParen;
    this.decl = decl;
    this.ide = ide;
    this.symIn = symIn;
    this.expr = expr;
    this.rParen = rParen;
  }

  public JooSymbol getSymEach() {
    return symEach;
  }

  public JooSymbol getLParen() {
    return lParen;
  }

  public VariableDeclaration getDecl() {
    return decl;
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymIn() {
    return symIn;
  }

  public Expr getExpr() {
    return expr;
  }

  public JooSymbol getRParen() {
    return rParen;
  }

  public Ide getAuxIde() {
    return auxIde;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitForInStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (symEach != null) {
      auxIde = scope.createAuxVar();
      auxIde.scope(scope);
    }
    if (decl != null) {
      decl.scope(scope);
    } else {
      ide.scope(scope);
    }
    expr.scope(scope);
  }

  protected void analyzeLoopHeader() {
    if (decl != null) {
      decl.analyze(this);
    } else {
      ide.analyze(this);
    }
    expr.analyze(this);
  }

}
