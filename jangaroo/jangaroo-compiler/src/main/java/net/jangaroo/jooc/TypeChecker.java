package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectFieldOrSpread;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TypeChecker extends AstVisitorBase {

  static final String ASSIGNED_EXPRESSION_ERROR_MESSAGE = "Assigned expression type %s is not assignable to type %s";
  static final String VARIABLE_DECLARATION_ERROR_MESSAGE = "Initializer type %s is not assignable to variable type %s";
  static final String ARGUMENT_EXPRESSION_ERROR_MESSAGE = "Argument type %s is not assignable to parameter type %s";
  static final String RETURN_EXPRESSION_ERROR_MESSAGE = "Return value type %s is not assignable to return type %s";
  static final String OBJECT_FIELD_EXPRESSION_ERROR_MESSAGE = "Object literal field value of type %s is not assignable to property type %s";
  static final String ARRAY_ELEMENT_EXPRESSION_ERROR_MESSAGE = "Array literal element type %s is not assignable to array element type %s";

  private CompileLog log;

  TypeChecker(CompileLog log) {
    this.log = log;
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) {
    Expr returnExpr = returnStatement.getOptExpr();
    if (returnExpr != null) {
      AstNode parentNode = returnExpr.getParentNode();
      while (parentNode != null && !(parentNode instanceof FunctionExpr)) {
        parentNode = parentNode.getParentNode();
      }
      if (parentNode == null) {
        // hmmm, return statement without containing function?!
        return;
      }
      FunctionExpr functionExpr = (FunctionExpr) parentNode;
      ExpressionType type = functionExpr.getType();
      if (type != null) {
        validateTypes(returnExpr.getSymbol(), type.getTypeParameter(), returnExpr, RETURN_EXPRESSION_ERROR_MESSAGE);
      }
    }
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) {
    FunctionDeclaration superConstructor = superConstructorCallStatement.getClassDeclaration().getSuperTypeDeclaration().getConstructor();
    if (superConstructor != null) {
      if (superConstructor.getFun().getType() instanceof FunctionSignature) {
        checkParameterTypes(
                (FunctionSignature) superConstructor.getFun().getType(),
                superConstructorCallStatement.getSymbol(),
                superConstructorCallStatement.getArgs());
      }
    }
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) {
    ExpressionType type = applyExpr.getFun().getType();
    if (applyExpr.getFun() instanceof IdeExpr) {
      Scope scope = ((IdeExpr) applyExpr.getFun()).getIde().getScope();
      if (applyExpr.isTypeCast() && applyExpr.getArgs().getExpr() != null
              && applyExpr.getArgs().getExpr().getHead() instanceof ObjectLiteral) {
        // special dynamic signature: type cast C(o: C) => C
        ExpressionType typeToCast = applyExpr.getFun().getType().getTypeParameter();
        type = new FunctionSignature(scope.getClassDeclaration(AS3Type.FUNCTION.name),
                null, 1, false,
                Collections.singletonList(typeToCast), typeToCast);
      } else if (applyExpr.isTypeCheckObjectLiteralFunctionCall()) {
        // poor man's generics: compute type assertion function signature on the fly
        ExpressionType typeToAssert = applyExpr.getArgs().getExpr().getHead().getType().getTypeParameter();
        type = new FunctionSignature(scope.getClassDeclaration(AS3Type.FUNCTION.name),
                null, 2, false,
                Arrays.asList(scope.getExpressionType(AS3Type.CLASS), typeToAssert), typeToAssert);
      }
    }
    if (type != null && applyExpr.getFun() instanceof NewExpr) {
      ExpressionType classToConstruct = type.getTypeParameter();
      if (classToConstruct != null && classToConstruct.getDeclaration() instanceof ClassDeclaration) {
        FunctionDeclaration constructor = ((ClassDeclaration) classToConstruct.getDeclaration()).getConstructor();
        Scope scope = type.getDeclaration().getIde().getScope();
        type = scope.getFunctionSignature(null, constructor == null ? null : constructor.getParams(), classToConstruct);
      }
    }
    if (type instanceof FunctionSignature) {
      checkParameterTypes((FunctionSignature) type, applyExpr.getSymbol(), applyExpr.getArgs());
    }
  }

  private void checkParameterTypes(FunctionSignature functionSignature, FilePosition parameterCountErrorSymbol, ParenthesizedExpr<CommaSeparatedList<Expr>> parameters) {
    List<Expr> args = new ArrayList<>();
    if (parameters != null) {
      CommaSeparatedList<Expr> argsCSL = parameters.getExpr();
      while (argsCSL != null) {
        args.add(argsCSL.getHead());
        argsCSL = argsCSL.getTail();
      }
    }
    // check number of arguments against number of parameters:
    int maxParameterCount = functionSignature.getParameterTypes().size();
    if (args.size() < functionSignature.getMinArgumentCount() || !functionSignature.hasRest() && args.size() > maxParameterCount) {
      log.error(parameterCountErrorSymbol, "Wrong number of arguments, must be " + functionSignature.getMinArgumentCount() + (functionSignature.hasRest() || maxParameterCount == functionSignature.getMinArgumentCount() ? "" : " to " + maxParameterCount) + ".");
    } else {
      List<ExpressionType> parameterTypes = functionSignature.getParameterTypes();
      for (int i = 0; i < Math.min(parameterTypes.size(), args.size()); i++) {
        ExpressionType parameterType = parameterTypes.get(i);
        Expr arg = args.get(i);
        validateTypes(arg.getSymbol(), parameterType, arg, ARGUMENT_EXPRESSION_ERROR_MESSAGE);
      }
    }
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) {

    long opSym = assignmentOpExpr.getOp().sym;
    if (opSym == sym.PLUSEQ) {
      // TODO this evaluates to a number (if all args are numbers) or a string, we could check what it is supposed to be
      return;
    }
    if (opSym == sym.MINUSEQ || opSym == sym.DIVEQ || opSym == sym.MULTEQ) {
      // TODO this evaluates to a number, we could check that, we could check what it is supposed to be
      return;
    }

    ExpressionType expected = assignmentOpExpr.getArg1().getType();
    validateTypes(assignmentOpExpr.getArg2().getSymbol(), expected, assignmentOpExpr.getArg2(), ASSIGNED_EXPRESSION_ERROR_MESSAGE);
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) {
    if (variableDeclaration == null || variableDeclaration.getOptInitializer() == null) {
      return;
    }
    Expr actualExpression = variableDeclaration.getOptInitializer().getValue();

    ExpressionType expected = variableDeclaration.getIde().getScope().getExpressionType(variableDeclaration);

    validateTypes(actualExpression.getSymbol(), expected, actualExpression, VARIABLE_DECLARATION_ERROR_MESSAGE);
  }

  private void validateTypes(@Nonnull JooSymbol symbol,
                             @Nullable ExpressionType expectedType,
                             @Nonnull Expr actualExpression,
                             String logMessage) {

    if (expectedType == null || actualExpression.getType() == null
            || AS3Type.ANY.equals(expectedType.getAS3Type()) || AS3Type.BOOLEAN.equals(expectedType.getAS3Type())) {
      return;
    }

    TypeDeclaration expectedTypeDeclaration = expectedType.getDeclaration();

    if (actualExpression instanceof ObjectLiteral && expectedType.getDeclaration() instanceof ClassDeclaration) {
      validateObjectLiteral((ClassDeclaration) expectedType.getDeclaration(), (ObjectLiteral) actualExpression);
      return;
    }

    // actual must be equal to or implement/extend expected
    if (!actualExpression.getType().isAssignableTo(expectedType)) {
      logException(symbol, expectedTypeDeclaration.getQualifiedNameStr(),
              actualExpression.getType().getDeclaration().getQualifiedNameStr(), logMessage);
    } else if (actualExpression instanceof ArrayLiteral && expectedType.isArrayLike()) {
      validateArrayLiteral(expectedType.getTypeParameter(), (ArrayLiteral) actualExpression);
    }
  }

  private void validateObjectLiteral(ClassDeclaration classDeclaration, ObjectLiteral objectLiteral) {
    if (classDeclaration.isObject()) {
      // nothing to check here, any property is allowed.
      return;
    }
    for (CommaSeparatedList<ObjectFieldOrSpread> fields = objectLiteral.getFields();
         fields != null;
         fields = fields.getTail()) {
      ObjectFieldOrSpread fieldOrSpread = fields.getHead();
      if (fieldOrSpread instanceof ObjectField) {
        ObjectField field = (ObjectField) fieldOrSpread;
        AstNode fieldLabel = field.getLabel();
        // only check object fields with a non-quoted identifier label:
        if (fieldLabel instanceof Ide) {
          String propertyName = ((Ide) fieldLabel).getName();
          IdeDeclaration propertyDeclaration = classDeclaration.resolvePropertyDeclaration(propertyName);
          if (propertyDeclaration != null) {
            if (propertyDeclaration instanceof PropertyDeclaration) {
              // always prefer the setter's (parameter) type, which might be more general than the getter's return type:
              propertyDeclaration = ((PropertyDeclaration) propertyDeclaration).getSetter();
            }
            ExpressionType type = propertyDeclaration.getType();
            if (type != null) {
              type = type.getEvalType();
            }
            if (type == null) {
              type = propertyDeclaration.getIde().getScope().getExpressionType(propertyDeclaration);
            }
            validateTypes(field.getValue().getSymbol(), type, field.getValue(), OBJECT_FIELD_EXPRESSION_ERROR_MESSAGE);
          } else if (!classDeclaration.isDynamic()) {
            log.warning(getErrorSymbol(field),
                    String.format("Property '%s' not found in type %s.", propertyName, classDeclaration.getQualifiedNameStr()));
          }
        }
      } // ignore Spreads, they are either untyped or contain their own __typeCheckObjectLiteral__ call
    }
  }

  private void validateArrayLiteral(ExpressionType itemType, ArrayLiteral arrayLiteral) {
    if (itemType != null && !AS3Type.ANY.equals(itemType.getAS3Type()) && !itemType.isObject()) {
      for (CommaSeparatedList<Expr> arrayItems = arrayLiteral.getExpr();
           arrayItems != null;
           arrayItems = arrayItems.getTail()) {
        Expr arrayItem = arrayItems.getHead();
        validateTypes(arrayItem.getSymbol(), itemType, arrayItem, ARRAY_ELEMENT_EXPRESSION_ERROR_MESSAGE);
      }
    }
  }

  private static JooSymbol getErrorSymbol(ObjectField field) {
    JooSymbol errorSymbol = field.getSymbol();
    // MXML field label symbols are "virtual", resulting in no error location, so then use the value's symbol:
    if (errorSymbol.getFileName().isEmpty()) {
      errorSymbol = field.getValue().getSymbol();
    }
    return errorSymbol;
  }

  private void logException(JooSymbol jooSymbol, String expectedType, String actualType, String logMessage) {
    log.error(jooSymbol, String.format(logMessage, actualType, expectedType));
  }
}
