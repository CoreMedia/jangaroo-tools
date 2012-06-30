package net.jangaroo.jooc.ast;

import java.io.IOException;

/**
 * Visitor for the ActionScript abstract syntax tree.
 */
public interface AstVisitor {
  void visitTypeRelation(TypeRelation typeRelation) throws IOException;

  void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException;

  void visitExtends(Extends anExtends) throws IOException;

  void visitInitializer(Initializer initializer) throws IOException;

  void visitObjectField(ObjectField objectField) throws IOException;

  void visitForInitializer(ForInitializer forInitializer) throws IOException;

  void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException;

  void visitIde(Ide ide) throws IOException;

  void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException;

  void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException;

  void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException;

  void visitImplements(Implements anImplements) throws IOException;

  void visitType(Type type) throws IOException;

  void visitObjectLiteral(ObjectLiteral objectLiteral) throws IOException;

  void visitIdeExpression(IdeExpr ideExpr) throws IOException;

  <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException;

  void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException;

  void visitLiteralExpr(LiteralExpr literalExpr) throws IOException;

  void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException;

  void visitDotExpr(DotExpr dotExpr) throws IOException;

  void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException;

  void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException;

  void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException;

  void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException;

  void visitAsExpr(AsExpr asExpr) throws IOException;

  void visitIsExpr(IsExpr isExpr) throws IOException;

  void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException;

  void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException;

  <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> nodeTypeCommaSeparatedList) throws IOException;

  void visitParameters(Parameters parameters) throws IOException;

  void visitFunctionExpr(FunctionExpr functionExpr) throws IOException;

  void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException;

  void visitApplyExpr(ApplyExpr applyExpr) throws IOException;

  void visitNewExpr(NewExpr newExpr) throws IOException;

  void visitClassBody(ClassBody classBody) throws IOException;

  void visitBlockStatement(BlockStatement blockStatement) throws IOException;

  void visitDefaultStatement(DefaultStatement defaultStatement) throws IOException;

  void visitLabeledStatement(LabeledStatement labeledStatement) throws IOException;

  void visitIfStatement(IfStatement ifStatement) throws IOException;

  void visitCaseStatement(CaseStatement caseStatement) throws IOException;

  void visitTryStatement(TryStatement tryStatement) throws IOException;

  void visitCatch(Catch aCatch) throws IOException;

  void visitForInStatement(ForInStatement forInStatement) throws IOException;

  void visitWhileStatement(WhileStatement whileStatement) throws IOException;

  void visitForStatement(ForStatement forStatement) throws IOException;

  void visitDoStatement(DoStatement doStatement) throws IOException;

  void visitSwitchStatement(SwitchStatement switchStatement) throws IOException;

  void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement) throws IOException;

  void visitContinueStatement(ContinueStatement continueStatement) throws IOException;

  void visitBreakStatement(BreakStatement breakStatement) throws IOException;

  void visitThrowStatement(ThrowStatement throwStatement) throws IOException;

  void visitReturnStatement(ReturnStatement returnStatement) throws IOException;

  void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException;

  void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration) throws IOException;

  void visitParameter(Parameter parameter) throws IOException;

  void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException;

  void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException;

  void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException;

  void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException;

  void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException;

  void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException;

  void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException;

  void visitAnnotation(Annotation annotation) throws IOException;

  void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException;

  void visitImportDirective(ImportDirective importDirective) throws IOException;
}
