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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
@SuppressWarnings({"FieldMayBeFinal", "Convert2Lambda"})
public class FunctionExpr extends Expr {

  public static final String ARGUMENTS = "arguments";
  public static final Ide ARGUMENTS_IDE = new Ide(new JooSymbol(ARGUMENTS).virtual());

  //todo unify predefined type definitions, they are scattered all over
  public static final Type ANY_TYPE = new Type(new JooSymbol(sym.MUL, "", -1, -1, "", AS3Type.ANY.toString()));

  private JooSymbol symFunction;
  private Ide ide;
  private JooSymbol lParen;
  private TypeRelation optTypeRelation;
  private Parameters params;
  private JooSymbol rParen;
  private BlockStatement optBody;

  private List<Parameter> implicitParams = new LinkedList<>();
  private FunctionDeclaration functionDeclaration; // null for function expressions
  private VariableDeclaration variableDeclaration; // if function expression is named (has ide), it creates this declaration
  private boolean thisDefined = false;
  private boolean explicitThisUsed = false;
  private final Parameter argumentsParameter;
  private boolean argumentsUsed = false;
  private boolean argumentsUsedAsArray = false;
  private IdeDeclaration classDeclaration;
  private Scope bodyScope;
  private boolean thisAliased;
  private Collection<FunctionExpr> functionExprs = new HashSet<>();

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

  public String getReturnTypeFromAnnotation() {
    FunctionDeclaration functionDeclaration = getFunctionDeclaration();
    if (functionDeclaration != null) {
      Annotation returnAnnotation = functionDeclaration.getAnnotation(Jooc.RETURN_ANNOTATION_NAME);
      if (returnAnnotation == null && functionDeclaration.isClassMember() && !functionDeclaration.isStatic()) {
        ClassDeclaration superTypeDeclaration = functionDeclaration.getClassDeclaration().getSuperTypeDeclaration();
        if (superTypeDeclaration != null) {
          IdeDeclaration methodDeclaration = superTypeDeclaration.resolvePropertyDeclaration(functionDeclaration.getIde().getName(), false);
          if (methodDeclaration instanceof FunctionDeclaration) {
            return ((FunctionDeclaration) methodDeclaration).getFun().getReturnTypeFromAnnotation();
          }
        }
      }
      if (returnAnnotation != null) {
        Object defaultPropertyValue = returnAnnotation.getPropertiesByName().get(null);
        if (defaultPropertyValue instanceof String) {
          return  (String) defaultPropertyValue;
        }
      }
    }
    return null;
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
          variableDeclaration = new VariableDeclaration(null, ide, null, null);
          variableDeclaration.scope(scope);
        }
        // declare implicitParams
        scope(implicitParams, scope);
        withNewDeclarationScope(FunctionExpr.this, scope, new Scoped() {
          public void run(final Scope scope) {
            bodyScope = scope;
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
    setType(scope.getFunctionSignature(functionDeclaration != null ? functionDeclaration.getMethodType() : null,
            params, scope.getExpressionType(optTypeRelation)));
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    if (params != null) {
      params.analyze(this);
    }
    if (optTypeRelation != null) {
      optTypeRelation.analyze(this);
    }
    if (optBody != null) {
      TypeRelation typeRelation = getOptTypeRelation();
      List<Directive> directives = optBody.getDirectives();
      if (typeRelation != null && AS3Type.VOID.name.equals(typeRelation.getType().getIde().getName()) &&
              Ide.THIS.equals(getReturnTypeFromAnnotation()) &&
              !(!directives.isEmpty() && directives.get(directives.size() - 1) instanceof ReturnStatement)) {
        // complement a final "return this":
        ReturnStatement returnThis = new ReturnStatement(new JooSymbol(sym.RETURN, "return"),null,
                new JooSymbol(sym.SEMICOLON, ";"));
        // "this" expression will be added on render!
        returnThis.scope(bodyScope);
        optBody.getDirectives().add(returnThis);
      }
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
    if (!thisAliased) {
      if (getFunctionDeclaration() != null && getFunctionDeclaration().isClassMember()) {
        return false;
      }
      thisAliased = true;
      Scope parentScope = scope.getParentScope().getParentScope();
      FunctionExpr parentFunctionExpr = parentScope.getFunctionExpr();
      if (parentFunctionExpr != null) {
        // if we are nested inside another function, register there:
        parentFunctionExpr.addFunctionExpr(this);
        // ...and propagate this usage to parent function:
        parentFunctionExpr.notifyThisUsed(parentScope);
      }
    }
    return true;
  }

  private void addFunctionExpr(FunctionExpr functionExpr) {
    functionExprs.add(functionExpr);
  }

  boolean isThisAliased(boolean allowArrowFunctions) {
    return thisAliased ? !(allowArrowFunctions && rewriteToArrowFunction()) :
            functionExprs.stream().anyMatch(functionExpr -> functionExpr.isThisAliased(allowArrowFunctions));
  }

  void notifyExplicitThisUsed() {
    explicitThisUsed = true;
  }

  public boolean isExplicitThisUsed() {
    return explicitThisUsed;
  }

  public boolean rewriteToArrowFunction() {
    return !isExplicitThisUsed() && !argumentsUsed && getBody() != null
            && (variableDeclaration == null || variableDeclaration.getUsages().isEmpty())
            && getFunctionDeclaration() == null;
  }

  boolean isMyArguments(Parameter maybeArgumentsParameter) {
    return argumentsParameter == maybeArgumentsParameter;
  }

  void notifyArgumentsUsed(boolean asArray) {
    argumentsUsed = true;
    if (asArray) {
      argumentsUsedAsArray = true;
    }
  }

  public boolean isArgumentsUsedAsArray() {
    return argumentsUsedAsArray;
  }

}
