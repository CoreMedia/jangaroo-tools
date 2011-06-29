package net.jangaroo.jooc.ast;

import java.io.IOException;

public class AstVisitorBase implements AstVisitor {
  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
  }

  @Override
  public void visitObjectField(ObjectField objectField) throws IOException {
  }

  @Override
  public void visitForInitializer(ForInitializer forInitializer) throws IOException {
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
  }

  @Override
  public void visitImplements(Implements anImplements) throws IOException {
  }

  @Override
  public void visitType(Type type) throws IOException {
  }

  @Override
  public void visitObjectLiteral(ObjectLiteral objectLiteral) throws IOException {
  }

  @Override
  public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
  }

  @Override
  public <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException {
  }

  @Override
  public void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException {
  }

  @Override
  public void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
  }

  @Override
  public void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
  }

  @Override
  public void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
  }

  @Override
  public void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
  }

  @Override
  public void visitAsExpr(AsExpr asExpr) throws IOException {
  }

  @Override
  public void visitIsExpr(IsExpr isExpr) throws IOException {
  }

  @Override
  public void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
  }

  @Override
  public void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException {
  }

  @Override
  public <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> nodeTypeCommaSeparatedList) throws IOException {
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
  }

  @Override
  public void visitNewExpr(NewExpr newExpr) throws IOException {
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
  }

  @Override
  public void visitDefaultStatement(DefaultStatement defaultStatement) throws IOException {
  }

  @Override
  public void visitLabeledStatement(LabeledStatement labeledStatement) throws IOException {
  }

  @Override
  public void visitIfStatement(IfStatement ifStatement) throws IOException {
  }

  @Override
  public void visitCaseStatement(CaseStatement caseStatement) throws IOException {
  }

  @Override
  public void visitTryStatement(TryStatement tryStatement) throws IOException {
  }

  @Override
  public void visitCatch(Catch aCatch) throws IOException {
  }

  @Override
  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
  }

  @Override
  public void visitWhileStatement(WhileStatement whileStatement) throws IOException {
  }

  @Override
  public void visitForStatement(ForStatement forStatement) throws IOException {
  }

  @Override
  public void visitDoStatement(DoStatement doStatement) throws IOException {
  }

  @Override
  public void visitSwitchStatement(SwitchStatement switchStatement) throws IOException {
  }

  @Override
  public void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement) throws IOException {
  }

  @Override
  public void visitContinueStatement(ContinueStatement continueStatement) throws IOException {
  }

  @Override
  public void visitBreakStatement(BreakStatement breakStatement) throws IOException {
  }

  @Override
  public void visitThrowStatement(ThrowStatement throwStatement) throws IOException {
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
  }

  @Override
  public void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException {
  }

  @Override
  public void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration) throws IOException {
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
  }

  @Override
  public void visitGetterSetterPair(GetterSetterPair getterSetterPair) throws IOException {
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
  }

  @Override
  public void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException {
  }

  @Override
  public void visitNamespacedDeclaration(NamespacedDeclaration namespacedDeclaration) throws IOException {
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
  }
}
