package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class CheckAssignmentAndDeclationVisitor extends AstVisitorBase {

  static final String ASSIGNED_EXPRESSION_ERROR_MESSAGE = "Assigned expression type %s is not assignable to type %s";
  static final String VARIABLE_DECLARATION_ERROR_MESSAGE = "Initializer type %s is not assignable to variable type %s";

  private CompileLog log;
  private Jooc jooc;

  public CheckAssignmentAndDeclationVisitor(Jooc jooc, CompileLog log) {
    this.jooc = jooc;
    this.log = log;
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {

    AS3Type expectedType = getAS3Type(assignmentOpExpr.getArg1());
    ExpressionType type = assignmentOpExpr.getType();
    TypeDeclaration declaration = type == null ? null : type.getDeclaration();

    long opSym = assignmentOpExpr.getOp().sym;
    if (opSym == sym.PLUSEQ) {
      // TODO this evaluates to a number (if all args are numbers) or a string, we could check what it is supposed to be
      return;
    }
    if (opSym == sym.MINUSEQ || opSym == sym.DIVEQ || opSym == sym.MULTEQ) {
      // TODO this evaluates to a number, we could check that, we could check what it is supposed to be
      return;
    }

    // TODO
    // ExpressionType expected = assignmentOpExpr.getType();
    // ExpressionType actual = assignmentOpExpr.getArg2().getType();

    validateTypes(assignmentOpExpr.getArg2().getSymbol(), declaration, expectedType, assignmentOpExpr.getArg2(), false);
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration == null || variableDeclaration.getOptInitializer() == null) {
      return;
    }
    Expr actualExpression = variableDeclaration.getOptInitializer().getValue();
    TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();


    // ExpressionType expected = variableDeclaration.getIde().getScope().getExpressionType(variableDeclaration);
    // ExpressionType actual = actualExpression.getType();



    validateTypes(actualExpression.getSymbol(), getTypeDeclarationFromTypeRelation(typeRelation),
            getAS3TypeFromTypeRelation(typeRelation), actualExpression, true);
  }

  private void validateTypes(@Nonnull JooSymbol symbol,
                             @Nullable TypeDeclaration expectedTypeDeclaration,
                             @Nullable AS3Type expectedType,
                             @Nonnull Expr actualExpression,
                             boolean isDeclaration) {

    ExpressionType actualExpressionType = actualExpression.getType();
    AS3Type actualType = actualExpressionType != null ? actualExpressionType.getAS3Type() : null;

    // no need to check anything that can be anything
    // expectedType == null e.g. in mxml: private var config:AllElement
    if (AS3Type.ANY.equals(actualType) || canBeEverything(expectedTypeDeclaration, expectedType)) {
      return;
    }

    // check if this is a regular expression
    if (AS3Type.REG_EXP.equals(expectedType) && sym.REGEXP_LITERAL == actualExpression.getSymbol().sym) {
      return;
    }

    // check if this is supposed to be a string or number but is not
    validateSimpleTypes(symbol, expectedType, actualExpression, isDeclaration);

    /*  e.g. ArrayLiteral, type = null, LiteralExpression sym=95, 96, 98 (Int, Float,String) */
    if ( actualExpressionType == null) {
      return;
    }

    if (AS3Type.VECTOR.equals(expectedType)) {
      // cannot handle vectors yet
      return;
    }

    // you can only set void to Boolean, Object and Any, which was already checked
    if (AS3Type.VOID.equals(actualExpressionType.getAS3Type())) {
      logException(actualExpression.getSymbol(), expectedType, AS3Type.VOID, isDeclaration);
    }

    TypeDeclaration actualExpressionTypeDeclaration = actualExpressionType.getDeclaration();
    ClassDeclaration actualClassDeclaration = actualExpressionTypeDeclaration instanceof ClassDeclaration ?
            (ClassDeclaration) actualExpressionTypeDeclaration : actualExpressionTypeDeclaration.getClassDeclaration();

    if (actualClassDeclaration == null) {
      return;
    }

    ClassDeclaration expectedClassDeclaration = expectedTypeDeclaration instanceof ClassDeclaration ?
            (ClassDeclaration) expectedTypeDeclaration : expectedTypeDeclaration.getClassDeclaration();

    //  "Object".equals(actualClassDeclaration.getName() would be required for config declaration in mxml files: private var config:AllElements
    if ("Object".equals(expectedClassDeclaration.getName())) {
      // Object can be everything, even a void
      return;
    }

    // actual must be equal to or implement/extend expected
    // !resolveable(expectedClassDeclaration, actualClassDeclaration)
    if (!(AS3Type.isNumber(expectedType) && AS3Type.isNumber(actualExpression.getType().getAS3Type()))
            && !actualClassDeclaration.isAssignableTo(expectedClassDeclaration)) {
      logException(actualExpression.getSymbol(), expectedClassDeclaration.getName(), actualClassDeclaration.getName(), isDeclaration);
    }

  }

  private static boolean canBeEverything(@Nullable TypeDeclaration expectedTypeDeclaration, @Nullable AS3Type expectedType) {
    return (expectedType == null && expectedTypeDeclaration == null) || expectedTypeDeclaration == null
            || isObjectBooleanOrAnything(expectedType);
  }

  private void validateSimpleTypes(JooSymbol symbol, AS3Type expectedType, Expr actualExpression, boolean isDeclaration) {
    if ((actualExpression.getSymbol().sym == sym.INT_LITERAL )
            && !(AS3Type.NUMBER.equals(expectedType) || AS3Type.INT.equals(expectedType) || AS3Type.UINT.equals(expectedType))) {
      // this is supposed to be a number but is not
      logException(symbol, expectedType, AS3Type.INT, isDeclaration);
    } else if ((actualExpression.getSymbol().sym == sym.STRING_LITERAL )
            && !AS3Type.STRING.equals(expectedType))  {
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

    String logMessage = declaration ? VARIABLE_DECLARATION_ERROR_MESSAGE :
            ASSIGNED_EXPRESSION_ERROR_MESSAGE;

    String msg = String.format(logMessage, actualType, expectedType);
    log.error(jooSymbol, msg);
  }

  private static AS3Type getAS3Type(@Nonnull Expr expr) {
    return expr.getType() == null ? null : expr.getType().getAS3Type();
  }

  private static boolean isObjectBooleanOrAnything(AS3Type type) {
    return AS3Type.ANY.equals(type) || AS3Type.OBJECT.equals(type) || AS3Type.BOOLEAN.equals(type);
  }

  @Nullable
  private ClassModel getClassModel(@Nonnull String fullName) {
    CompilationUnitModel compilationUnitModel = jooc.resolveCompilationUnit(fullName);
    return compilationUnitModel.getClassModel();
  }

  private AS3Type getAS3TypeFromTypeRelation(TypeRelation typeRelation) {
    //
    Type type = null;
    if (typeRelation != null) {
      type = typeRelation.getType();
    }
    return  type == null ? null : AS3Type.typeByName(type.getDeclaration().getQualifiedNameStr());
  }

  private TypeDeclaration getTypeDeclarationFromTypeRelation(TypeRelation typeRelation) {
    //
    Type type = null;
    if (typeRelation != null) {
      type = typeRelation.getType();
    }
    return  type == null ? null : type.getDeclaration();
  }
}
