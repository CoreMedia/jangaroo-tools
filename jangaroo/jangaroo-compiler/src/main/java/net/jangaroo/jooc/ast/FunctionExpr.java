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

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class FunctionExpr extends Expr {

  public static final String ARGUMENTS = "arguments";
  public static final Ide ARGUMENTS_IDE = new Ide(new JooSymbol(ARGUMENTS));

  //todo unify predefined type definitions, they are scattered all over
  public static final Type ANY_TYPE = new Type(new JooSymbol(sym.MUL, "", -1, -1, "", AS3Type.ANY.toString()));

  private JooSymbol symFunction;
  private Ide ide;
  private JooSymbol lParen;
  private TypeRelation optTypeRelation;
  private Parameters params;
  private JooSymbol rParen;
  private BlockStatement optBody;

  private List<Parameter> implicitParams = new LinkedList<Parameter>();
  private FunctionDeclaration functionDeclaration; // null for function expressions
  private boolean thisDefined = false;
  private final Parameter argumentsParameter;
  private boolean argumentsUsedAsArray = false;
  private IdeDeclaration classDeclaration;

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
    // is there an single rest parameter called 'arguments'?
    if (params != null && params.getHead() != null && params.getTail() == null && params.getHead().isRest()
      && ARGUMENTS.equals(params.getHead().getName())) {
      argumentsParameter = params.getHead();
    } else {
      // 'arguments' is implicitly defined inside a function!
      argumentsParameter = new Parameter(null, ARGUMENTS_IDE, null, null);
      implicitParams.add(argumentsParameter);
    }
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide, params, optTypeRelation, optBody);
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
      addImplicitParam(new Parameter(null, new Ide(Ide.THIS), new TypeRelation(null, ANY_TYPE), null));
    }
    withNewDeclarationScope(functionDeclaration == null ? this : functionDeclaration, scope, new Scoped() {
      public void run(final Scope scope) {
        //declare ide inside Function if not already scoped:
        if ((functionDeclaration == null || !functionDeclaration.isMethod()) && ide != null && ide.getScope() == null) {
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

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (params != null) {
      params.analyze(this);
    }
    if (optTypeRelation != null) {
      optTypeRelation.analyze(this);
    }
    setType(new ExpressionType(ExpressionType.MetaType.FUNCTION,
            optTypeRelation == null
                    ? null
                    : optTypeRelation.getType().resolveDeclaration()));
    if (optBody != null) {
      optBody.analyze(this);
    }
  }

  public void addImplicitParam(Parameter parameter) {
    implicitParams.add(parameter);
  }

  public void setThisDefined() {
    thisDefined = true;
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

  public JooSymbol getLParen() {
    return lParen;
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public JooSymbol getRParen() {
    return rParen;
  }

  public JooSymbol getFunSymbol() {
    return symFunction;
  }

  boolean notifyThisUsed(Scope scope) {
    if (functionDeclaration == null || !functionDeclaration.isClassMember()) {
      FunctionDeclaration methodDeclaration = scope.getMethodDeclaration();
      // if "this" is used inside non-static method, remember that:
      if (methodDeclaration != null && !methodDeclaration.isStatic()) {
        methodDeclaration.aliasThis();
        return true;
      }
    }
    return false;
  }

  void notifyArgumentsUsed(IdeDeclaration argumentsIdeDeclaration) {
    if (argumentsIdeDeclaration == argumentsParameter) {
      argumentsUsedAsArray = true;
    }
  }

  public boolean isArgumentsUsedAsArray() {
    return argumentsUsedAsArray;
  }

}
