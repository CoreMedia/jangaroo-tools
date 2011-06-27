/*
 * Copyright 2011 CoreMedia AG
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
import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class FunctionExpr extends Expr {

  public static final Ide ARGUMENTS_IDE = new Ide(new JooSymbol("arguments"));

  //todo unify predefined type definitions, they are scattered all over
  public static final Type ANY_TYPE = new Type(new JooSymbol(sym.MUL, "", -1, -1, "", "*"));

  private JooSymbol symFunction;
  private Ide ide;
  private JooSymbol lParen;
  private TypeRelation optTypeRelation;
  private Parameters params;
  private JooSymbol rParen;
  private BlockStatement optBody;

  private List<Parameter> implicitParams = new LinkedList<Parameter>();
  private FunctionDeclaration functionDeclaration; // nul for function expressions
  private boolean thisDefined = false;
  private IdeDeclaration classDeclaration;
  private boolean thisUsed;

  public FunctionExpr(FunctionDeclaration functionDeclaration, JooSymbol symFunction, Ide ide, JooSymbol lParen,
                  Parameters params, JooSymbol rParen, TypeRelation optTypeRelation, BlockStatement optBody) {
    this.functionDeclaration = functionDeclaration;
    this.ide = ide;
    this.optTypeRelation = optTypeRelation;
    this.symFunction = symFunction;
    this.lParen = lParen;
    this.params = params;
    this.rParen = rParen;
    this.optBody = optBody;
    // 'arguments' is always defined inside a function!
    implicitParams.add(new Parameter(null, ARGUMENTS_IDE, null, null));
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitFunctionExpr(this);
  }

  public FunctionDeclaration getFunctionDeclaration() {
    return functionDeclaration;
  }

  public Parameters getParams() {
    return params;
  }

  public BlockStatement getBody() {
    return optBody;
  }

  @Override
  public JooSymbol getSymbol() {
    return symFunction;
  }

  public IdeDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  @Override
  public void scope(Scope scope) {
    classDeclaration = scope.getClassDeclaration();
    /*
    if (parentDeclaration == null) {
      AstNode declaration = scope.getDefiningNode();
      if (declaration instanceof IdeDeclaration) {
        parentDeclaration = (IdeDeclaration) declaration;
      }
    }
    */
    if (!thisDefined) {
      addImplicitParam(new Parameter(null, new Ide("this"), new TypeRelation(null, ANY_TYPE), null));
    }
    withNewDeclarationScope(functionDeclaration == null ? this : functionDeclaration, scope, new Scoped() {
      public void run(final Scope scope) {
        //declare ide inside Function if not already scoped:
        if (ide != null && ide.getScope() == null) {
          IdeDeclaration decl = new VariableDeclaration(null, ide, null, null);
          decl.scope(scope);
        }
        // declare implicitParams
        scope(implicitParams, scope);
        withNewDeclarationScope(FunctionExpr.this, scope, new Scoped() {
          public void run(final Scope scope) {
            if (params != null) {
              params.scope(scope);
            }
            if (optTypeRelation != null) {
              optTypeRelation.scope(scope);
            }
            if (optBody != null) {
              optBody.scope(scope);
            }
          }
        });
      }
    });
  }

  public String getFunctionNameAsIde(JsWriter out) {
    IdeDeclaration classDeclaration = getClassDeclaration();
    String classNameAsIde = "";
    if (classDeclaration != null) {
      classNameAsIde = out.getQualifiedNameAsIde(classDeclaration);
    }
    JooSymbol sym = getSymbol();
    return classNameAsIde + "$" + sym.getLine() + "_" + sym.getColumn();
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (params != null) {
      params.analyze(this, context);
    }
    if (optTypeRelation != null) {
      optTypeRelation.analyze(this, context);
    }
    if (optBody != null) {
      optBody.analyze(this, context);
    }
  }

  public void addImplicitParam(Parameter parameter) {
    implicitParams.add(parameter);
    thisDefined = thisDefined || parameter.getIde().getName().equals("this");
  }
  public void generateSignatureAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(lParen);
    if (params != null) {
      params.generateAsApiCode(out);
    }
    out.writeSymbol(rParen);
    if (optTypeRelation != null) {
      optTypeRelation.generateAsApiCode(out);
    }
  }

  public boolean hasBody() {
    return getBody() != null;
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymFunction() {
    return symFunction;
  }

  public JooSymbol getlParen() {
    return lParen;
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public JooSymbol getrParen() {
    return rParen;
  }

  public BlockStatement getOptBody() {
    return optBody;
  }

  public List<Parameter> getImplicitParams() {
    return implicitParams;
  }

  public JooSymbol getFunSymbol() {
    return symFunction;
  }

  public boolean notifyThisUsed(Scope scope) {
    if (!thisUsed && (functionDeclaration == null || !functionDeclaration.isClassMember())) {
      FunctionDeclaration methodDeclaration = scope.getMethodDeclaration();
      // if "this" is used inside non-static method, remember that:
      if (methodDeclaration != null && !methodDeclaration.isStatic()) {
        thisUsed = true;
        methodDeclaration.getBody().addBlockStartCodeGenerator(new CodeGenerator() {
          @Override
          public void generate(final JsWriter out) throws IOException {
            out.write("var this$=this;");
          }

          public void generateAsApiCode(JsWriter out) throws IOException {
            throw new UnsupportedOperationException();
          }
        });
        return true;
      }
    }
    return thisUsed;
  }

  public boolean isCompileTimeConstant() {
    return false;
  }

}
