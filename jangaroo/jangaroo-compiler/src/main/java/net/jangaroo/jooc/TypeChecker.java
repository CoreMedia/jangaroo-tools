package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeChecker extends AstVisitorBase {

  static final String ASSIGNED_EXPRESSION_ERROR_MESSAGE = "Assigned expression type %s is not assignable to type %s";
  static final String VARIABLE_DECLARATION_ERROR_MESSAGE = "Initializer type %s is not assignable to variable type %s";
  static final String ARGUMENT_EXPRESSION_ERROR_MESSAGE = "Argument type %s is not assignable to parameter type %s";
  static final String RETURN_EXPRESSION_ERROR_MESSAGE = "Return value type %s is not assignable to return type %s";

  private CompileLog log;

  TypeChecker(CompileLog log) {
    this.log = log;
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
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
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
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
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    ExpressionType type = applyExpr.getFun().getType();
    if (type != null && applyExpr.getFun() instanceof NewExpr) {
      ExpressionType classToConstruct = type.getTypeParameter();
      if (classToConstruct != null && classToConstruct.getDeclaration() instanceof ClassDeclaration) {
        FunctionDeclaration constructor = ((ClassDeclaration) classToConstruct.getDeclaration()).getConstructor();
        Scope scope = type.getDeclaration().getIde().getScope();
        type = scope.getFunctionSignature(null, constructor == null ? null : constructor.getParams(), type);
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
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {

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
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
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

    if (expectedType == null
            || AS3Type.ANY.equals(expectedType.getAS3Type()) ||  AS3Type.BOOLEAN.equals(expectedType.getAS3Type())) {
      return;
    }

    TypeDeclaration expectedTypeDeclaration = expectedType.getDeclaration();

    /*  e.g. ArrayLiteral, type = null, LiteralExpression sym=95, 96, 98 (Int, Float,String) */
    if ( actualExpression.getType() == null) {
      if (actualExpression instanceof LiteralExpr
              && (expectedType.getAS3Type().equals(AS3Type.VOID)
              || expectedTypeDeclaration instanceof ClassDeclaration
              && !((ClassDeclaration)expectedTypeDeclaration).isObject())) {
        // this is a LiteralExpr, check types, but only if we do not expect it is supposed to be an object anyway
        validateSimpleTypes(symbol, expectedType, actualExpression, logMessage);
      }
      return;
    }

    // actual must be equal to or implement/extend expected
    if (!actualExpression.getType().isAssignableTo(expectedType)) {
      logException(symbol, expectedTypeDeclaration.getQualifiedNameStr(),
              actualExpression.getType().getDeclaration().getQualifiedNameStr(), logMessage);
    }

  }

  private void validateSimpleTypes(JooSymbol symbol, ExpressionType expectedType, Expr actualExpression, String logMessage) {

    if ((actualExpression.getSymbol().sym == sym.INT_LITERAL) && !ExpressionType.isNumber(expectedType.getAS3Type())) {
      // this is a number but is not supposed to be one
      logException(symbol, expectedType, AS3Type.INT, logMessage);
    } else if ((actualExpression.getSymbol().sym == sym.STRING_LITERAL ) && !AS3Type.STRING.equals(expectedType.getAS3Type()))  {
      // this is a string but is not supposed to be one 
      logException(symbol, expectedType, AS3Type.STRING, logMessage);
    }
  }


  private void logException(JooSymbol jooSymbol, ExpressionType expectedType, AS3Type actualType, String logMessage) {
    String actualTypeString = actualType == null ? null : actualType.name;
    String expectedTypeString = expectedType == null ? null : expectedType.getDeclaration().getQualifiedNameStr();

    logException(jooSymbol, expectedTypeString, actualTypeString, logMessage);
  }

  private void logException(JooSymbol jooSymbol, String expectedType, String actualType, String logMessage) {
    log.error(jooSymbol, String.format(logMessage, actualType, expectedType));
  }
}
