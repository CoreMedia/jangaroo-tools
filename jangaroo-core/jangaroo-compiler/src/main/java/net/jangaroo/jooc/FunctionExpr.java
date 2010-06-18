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

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
class FunctionExpr extends Expr {

  public final static Ide ARGUMENTS_IDE = new Ide(new JooSymbol("arguments"));

  private JooSymbol symFun;
  private Ide ide;
  private JooSymbol lParen;
  private Parameters params;
  private JooSymbol rParen;
  private TypeRelation optTypeRelation;
  private BlockStatement body;

  private IdeDeclaration parentDeclaration;
  private boolean thisUsed;

  public FunctionExpr(JooSymbol symFun, Ide ide, JooSymbol lParen, Parameters params, JooSymbol rParen, TypeRelation optTypeRelation, BlockStatement body) {
    this.symFun = symFun;
    this.ide = ide;
    this.lParen = lParen;
    this.params = params;
    this.rParen = rParen;
    this.optTypeRelation = optTypeRelation;
    this.body = body;
  }

  public IdeDeclaration getParentDeclaration() {
    return parentDeclaration;
  }

  public void scope(final Scope scope) {
    parentDeclaration = scope.getClassDeclaration();
    if (parentDeclaration == null) {
      AstNode declaration = scope.getDefiningNode();
      if (declaration instanceof IdeDeclaration) {
        parentDeclaration = (IdeDeclaration) declaration;
      }
    }
    if (ide != null) {
      IdeDeclaration decl = new VariableDeclaration(null, ide, null, null);
      decl.scope(scope);
    }
    withNewDeclarationScope(this, scope, new Scoped() {
      public void run(final Scope scope) {
        new Parameter(null, ARGUMENTS_IDE, null, null).scope(scope); // is always defined inside a function!
        withNewDeclarationScope(FunctionExpr.this, scope, new Scoped() {
          public void run(final Scope scope) {
            if (params != null) {
              params.scope(scope);
            }
            if (optTypeRelation != null) {
              optTypeRelation.scope(scope);
            }
            body.scope(scope);
          }
        });
      }
    });
  }

  public Expr analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (params != null) {
      params.analyze(this, context);
    }
    if (optTypeRelation != null) {
      optTypeRelation.analyze(this, context);
    }
    body.analyze(this, context);
    return this;
  }

  public void notifyThisUsed(Scope scope) {
    FunctionDeclaration methodDeclaration = scope.getMethodDeclaration();
    // if "this" is used inside non-static method, remember that:
    if (methodDeclaration != null && !methodDeclaration.isStatic()) {
      thisUsed = true;
    }
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbol(symFun);
    if (ide != null) {
      out.writeToken(ide.getName());
    } else if (out.getKeepSource()) {
      out.writeToken(out.getFunctionNameAsIde(this));
    }
    out.writeSymbol(lParen);
    if (params != null) {
      params.generateCode(out);
    }
    out.writeSymbol(rParen);
    if (optTypeRelation != null) {
      optTypeRelation.generateCode(out);
    }
    body.generateCode(out);
    if (thisUsed) {
      out.write(".bind(this)");
    }
  }

  public JooSymbol getSymbol() {
    return symFun;
  }

  boolean isCompileTimeConstant() {
    return true;
  }

}
