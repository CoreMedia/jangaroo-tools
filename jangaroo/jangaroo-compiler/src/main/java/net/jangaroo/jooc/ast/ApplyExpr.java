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
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ApplyExpr extends Expr {

  public static final String TYPE_CHECK_OBJECT_LITERAL_FUNCTION_NAME = "__typeCheckObjectLiteral__";

  private Expr fun;
  private ParenthesizedExpr<CommaSeparatedList<Expr>> args;

  private boolean insideNewExpr = false;
  private static final Set<String> COERCE_FUNCTION_NAMES = new HashSet<String>(Arrays.asList("Number", "String", "Boolean", "int", "uint", "Date", "Object", "Array", "RegExp", "XML", "Error"));
  private Scope scope;
  private Map<Expr, ClassDeclaration> argToPropertiesClass = new HashMap<>();

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
    this.scope = scope;
    getFun().scope(scope);
    getArgs().scope(scope);
  }

  public boolean isTypeCast() {
    return getFun() instanceof IdeExpr && !isInsideNewExpr() && isNonCoercingType((IdeExpr) getFun())
            && hasExactlyOneArgument();
  }

  public boolean isTypeCheckObjectLiteralFunctionCall() {
    return getFun() instanceof IdeExpr
            && TYPE_CHECK_OBJECT_LITERAL_FUNCTION_NAME.equals(((IdeExpr) getFun()).getIde().getQualifiedNameStr());
  }

  public boolean isAssert() {
    return getFun() instanceof IdeExpr
            && SyntacticKeywords.ASSERT.equals(((IdeExpr) getFun()).getIde().getQualifiedNameStr());
  }

  private boolean hasExactlyOneArgument() {
    if (getArgs() != null) {
      CommaSeparatedList<Expr> expr = getArgs().getExpr();
      if (expr != null && expr.getHead() != null && expr.getTail() == null) {
        return true;
      }
    }
    return false;
  }

  private boolean isNonCoercingType(IdeExpr fun) {
    // treat any expression of type "Class" as a type cast:
    return fun.getType() != null && fun.getType().getAS3Type() == AS3Type.CLASS
            && !isCoerceFunction(fun.getIde().getQualifiedNameStr());
  }

  public static boolean isCoerceFunction(String qualifiedName) {
    return COERCE_FUNCTION_NAMES.contains(qualifiedName);
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    getFun().analyze(this);
    if (getArgs() != null) {
      mapPropertiesClassReferences();
      getArgs().analyze(this);
    }
    boolean isTypeCast = isTypeCast();
    Boolean isConfigFactory = false;
    ExpressionType type = getFun().getType();
    if (type != null && (type.getAS3Type() == AS3Type.FUNCTION || type.getAS3Type() == AS3Type.CLASS)) {
      ExpressionType classType = type.getTypeParameter();
      if (isTypeCast && classType != null && ((ClassDeclaration) classType.getDeclaration()).hasConfigClass()) {
        if (getArgs().getExpr().getHead() instanceof ObjectLiteral) {
          classType.markAsConfigTypeIfPossible();
          isConfigFactory = classType.isConfigType();
        } else {
          isConfigFactory = null; // may be or may be not...
        }
      }
      setType(classType);
    }
    if (isTypeCast) {
      if (isConfigFactory != Boolean.TRUE) {
        scope.getCompilationUnit().addBuiltInIdentifierUsage("cast");
      }
      if (isConfigFactory != Boolean.FALSE) {
        scope.getCompilationUnit().addBuiltInIdentifierUsage("Config");
      }
    } else if (isAssert()) {
      scope.getCompilationUnit().addBuiltInIdentifierUsage(SyntacticKeywords.ASSERT);
    } else if (isTypeCheckObjectLiteralFunctionCall()) {
      scope.getCompilationUnit().addBuiltInIdentifierUsage("Config");
    }
  }

  public FunctionDeclaration resolveFunction() {
    Expr fun = this.getFun();
    boolean isNew = false;
    if (fun instanceof NewExpr) {
      fun = ((NewExpr) fun).getApplyConstructor();
      isNew = true;
    }
    if (fun instanceof IdeExpr) {
      fun = ((IdeExpr) fun).getNormalizedExpr();
      if (fun instanceof IdeExpr) {
        IdeDeclaration declaration = ((IdeExpr) fun).getIde().getDeclaration(false);
        if (isNew) {
          return declaration instanceof ClassDeclaration ? ((ClassDeclaration) declaration).getConstructor() : null;
        }
        if (declaration instanceof FunctionDeclaration) {
          return (FunctionDeclaration) declaration;
        }
      }
    }
    if (fun instanceof DotExpr) {
      DotExpr dotExpr = (DotExpr) fun;
      ExpressionType type = dotExpr.getArg().getType();
      if (type != null) {
        IdeDeclaration declaration = type.resolvePropertyDeclaration(dotExpr.getIde().getName());
        if (declaration instanceof FunctionDeclaration) {
          return (FunctionDeclaration) declaration;
        }
      }
    }
    return null;
  }

  private void mapPropertiesClassReferences() {
    FunctionDeclaration functionDeclaration = resolveFunction();
    if (functionDeclaration != null) {
      Collection<String> propertyClassReferenceParameterNames =
              functionDeclaration.getAnnotations(Jooc.PARAMETER_ANNOTATION_NAME)
              .stream()
              .map(parameterAnnotation -> parameterAnnotation.getPropertiesByName())
              .filter(parameterProperties -> Jooc.COERCE_TO_VALUE_PROPERTIES_CLASS
                      .equals(parameterProperties.get(Jooc.PARAMETER_ANNOTATION_COERCE_TO_PROPERTY)))
              .map(parameterProperties -> (String) parameterProperties.get(null))
              .collect(Collectors.toList());

      Parameters params = functionDeclaration.getParams();
      CommaSeparatedList<Expr> args = this.args.getExpr();
      while (params != null && args != null) {
        // did we reach the annotated parameter?
        if (propertyClassReferenceParameterNames.contains(params.getHead().getName())) {
          // then we have the argument of that parameter:
          Expr arg = args.getHead();
          if (arg instanceof LiteralExpr) {
            String resourceBundleName = (String) ((LiteralExpr) arg).getValue().getJooValue();
            String propertiesClassName = resourceBundleName + CompilerUtils.PROPERTIES_CLASS_SUFFIX;
            CompilationUnit propertiesClass = scope.getCompiler().getCompilationUnit(propertiesClassName);
            if (propertiesClass == null) {
              scope.getCompiler().getLog().error(arg.getSymbol(), String.format("Properties class '%s' corresponding to resource bundle '%s' not found.", propertiesClassName, resourceBundleName));
            } else {
              scope.getCompilationUnit().addDependency(propertiesClass, true);
              argToPropertiesClass.put(arg, (ClassDeclaration) propertiesClass.getPrimaryDeclaration());
            }
          }
        }
        params = params.getTail();
        args = args.getTail();
      }
    }
  }

  public ClassDeclaration getPropertiesClass(Expr arg) {
    return argToPropertiesClass.get(arg);
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

  public boolean isFlexAddEventListener() {
    Expr fun = getFun();
    if (fun instanceof IdeExpr) {
      fun = ((IdeExpr) fun).getNormalizedExpr();
    }
    if (fun instanceof DotExpr) {
      Ide ide = ((DotExpr) fun).getIde();
      if ("addEventListener".equals(ide.getName())) {
        ExpressionType argType = ((DotExpr) fun).getArg().getType();
        if (argType != null) {
          IdeDeclaration declaration = argType.getDeclaration().resolvePropertyDeclaration(ide.getName());
          if (declaration instanceof FunctionDeclaration
                  && ("ext.mixin.IObservable.addEventListener".equals(declaration.getQualifiedNameStr())
                  || "ext.mixin.Observable.addEventListener".equals(declaration.getQualifiedNameStr()))) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
