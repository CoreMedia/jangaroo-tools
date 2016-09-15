package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class CheckAssignmentAndDeclationVisitor extends AstVisitorBase {

  static final String ASSIGNED_EXPRESSION_ERROR_MESSAGE = "Assigned expression type %s is not assignable to type %s";
  static final String VARIABLE_DECLARATION_ERROR_MESSAGE = "Initializer type %s is not assignable to variable type %s";

  private CompileLog log;

  public CheckAssignmentAndDeclationVisitor(CompileLog log) {
    this.log = log;
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
    validateTypes(assignmentOpExpr.getArg2().getSymbol(), expected, assignmentOpExpr.getArg2(), false);
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration == null || variableDeclaration.getOptInitializer() == null) {
      return;
    }
    Expr actualExpression = variableDeclaration.getOptInitializer().getValue();

    ExpressionType expected = variableDeclaration.getIde().getScope().getExpressionType(variableDeclaration);

    validateTypes(actualExpression.getSymbol(), expected, actualExpression, true);
  }

  private void validateTypes(@Nonnull JooSymbol symbol,
                             @Nullable ExpressionType expectedType,
                             @Nonnull Expr actualExpression,
                             boolean isDeclaration) {

    if (expectedType == null
            || AS3Type.ANY.equals(expectedType.getAS3Type()) ||  AS3Type.BOOLEAN.equals(expectedType.getAS3Type())) {
      return;
    }

    TypeDeclaration expectedTypeDeclaration = expectedType.getDeclaration();

    /*  e.g. ArrayLiteral, type = null, LiteralExpression sym=95, 96, 98 (Int, Float,String) */
    if ( actualExpression.getType() == null) {
      if ((expectedTypeDeclaration instanceof ClassDeclaration)
              && !((ClassDeclaration)expectedTypeDeclaration).isObject()) {
        // this is a LiteralExpr, check types, but only if we do not expect it is supposed to be an object anyway
        validateSimpleTypes(symbol, expectedType.getAS3Type(), actualExpression, isDeclaration);
      }
      return;
    }

    // actual must be equal to or implement/extend expected
    if (!actualExpression.getType().isAssignableTo(expectedType)) {
      logException(symbol, expectedTypeDeclaration.getName(),
              actualExpression.getType().getDeclaration().getName(), isDeclaration);
    }

  }

  private void validateSimpleTypes(JooSymbol symbol, AS3Type expectedType, Expr actualExpression, boolean isDeclaration) {

    if ((actualExpression.getSymbol().sym == sym.INT_LITERAL) && !AS3Type.isNumber(expectedType)) {
      // this is supposed to be a number but is not
      logException(symbol, expectedType, AS3Type.INT, isDeclaration);
    } else if ((actualExpression.getSymbol().sym == sym.STRING_LITERAL ) && !AS3Type.STRING.equals(expectedType))  {
      // this is supposed to be a string but is not
      logException(symbol, expectedType, AS3Type.STRING, isDeclaration);
    }
  }


  private void logException(JooSymbol jooSymbol, AS3Type expectedType, AS3Type actualType, boolean declaration) {
    String actualTypeString = actualType == null ? null : actualType.name;
    String expectedTypeString = expectedType == null ? null : expectedType.name;

    logException(jooSymbol, expectedTypeString, actualTypeString, declaration);
  }

  private void logException(JooSymbol jooSymbol, String expectedType, String actualType, boolean declaration) {
    String logMessage = declaration ? VARIABLE_DECLARATION_ERROR_MESSAGE : ASSIGNED_EXPRESSION_ERROR_MESSAGE;
    log.error(jooSymbol, String.format(logMessage, actualType, expectedType));
  }
}
