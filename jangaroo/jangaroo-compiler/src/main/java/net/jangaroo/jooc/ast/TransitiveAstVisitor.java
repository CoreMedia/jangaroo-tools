package net.jangaroo.jooc.ast;

import java.io.IOException;
import java.util.List;

/**
 * Traverse the entire AST.
 */
public class TransitiveAstVisitor implements AstVisitor {
  private final AstVisitor delegate;

  public TransitiveAstVisitor(AstVisitor delegate) {
    this.delegate = delegate;
  }

  private void visitChildren(List<? extends AstNode> children) throws IOException {
    for (AstNode child : children) {
      child.visit(this);
    }
  }

  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    visitChildren(typeRelation.getChildren());
    delegate.visitTypeRelation(typeRelation);
  }

  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitChildren(annotationParameter.getChildren());
    delegate.visitAnnotationParameter(annotationParameter);
  }

  public void visitExtends(Extends anExtends) throws IOException {
    visitChildren(anExtends.getChildren());
    delegate.visitExtends(anExtends);
  }

  public void visitInitializer(Initializer initializer) throws IOException {
    visitChildren(initializer.getChildren());
    delegate.visitInitializer(initializer);
  }

  public void visitObjectField(ObjectField objectField) throws IOException {
    visitChildren(objectField.getChildren());
    delegate.visitObjectField(objectField);
  }

  public void visitForInitializer(ForInitializer forInitializer) throws IOException {
    visitChildren(forInitializer.getChildren());
    delegate.visitForInitializer(forInitializer);
  }

  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    visitChildren(compilationUnit.getChildren());
    delegate.visitCompilationUnit(compilationUnit);
  }

  public void visitIde(Ide ide) throws IOException {
    visitChildren(ide.getChildren());
    delegate.visitIde(ide);
  }

  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    visitChildren(qualifiedIde.getChildren());
    delegate.visitQualifiedIde(qualifiedIde);
  }

  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    visitChildren(ideWithTypeParam.getChildren());
    delegate.visitIdeWithTypeParam(ideWithTypeParam);
  }

  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    visitChildren(namespacedIde.getChildren());
    delegate.visitNamespacedIde(namespacedIde);
  }

  public void visitImplements(Implements anImplements) throws IOException {
    visitChildren(anImplements.getChildren());
    delegate.visitImplements(anImplements);
  }

  public void visitType(Type type) throws IOException {
    visitChildren(type.getChildren());
    delegate.visitType(type);
  }

  public void visitObjectLiteral(ObjectLiteral objectLiteral) throws IOException {
    visitChildren(objectLiteral.getChildren());
    delegate.visitObjectLiteral(objectLiteral);
  }

  public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
    visitChildren(ideExpr.getChildren());
    delegate.visitIdeExpression(ideExpr);
  }

  public <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException {
    visitChildren(parenthesizedExpr.getChildren());
    delegate.visitParenthesizedExpr(parenthesizedExpr);
  }

  public void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException {
    visitChildren(arrayLiteral.getChildren());
    delegate.visitArrayLiteral(arrayLiteral);
  }

  public void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    visitChildren(literalExpr.getChildren());
    delegate.visitLiteralExpr(literalExpr);
  }

  public void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
    visitChildren(postfixOpExpr.getChildren());
    delegate.visitPostfixOpExpr(postfixOpExpr);
  }

  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    visitChildren(dotExpr.getChildren());
    delegate.visitDotExpr(dotExpr);
  }

  public void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
    visitChildren(prefixOpExpr.getChildren());
    delegate.visitPrefixOpExpr(prefixOpExpr);
  }

  public void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
    visitChildren(binaryOpExpr.getChildren());
    delegate.visitBinaryOpExpr(binaryOpExpr);
  }

  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
    visitChildren(assignmentOpExpr.getChildren());
    delegate.visitAssignmentOpExpr(assignmentOpExpr);
  }

  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
    visitChildren(infixOpExpr.getChildren());
    delegate.visitInfixOpExpr(infixOpExpr);
  }

  public void visitAsExpr(AsExpr asExpr) throws IOException {
    visitChildren(asExpr.getChildren());
    delegate.visitAsExpr(asExpr);
  }

  public void visitIsExpr(IsExpr isExpr) throws IOException {
    visitChildren(isExpr.getChildren());
    delegate.visitIsExpr(isExpr);
  }

  public void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
    visitChildren(conditionalExpr.getChildren());
    delegate.visitConditionalExpr(conditionalExpr);
  }

  public void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException {
    visitChildren(arrayIndexExpr.getChildren());
    delegate.visitArrayIndexExpr(arrayIndexExpr);
  }

  public <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> nodeTypeCommaSeparatedList) throws IOException {
    visitChildren(nodeTypeCommaSeparatedList.getChildren());
    delegate.visitCommaSeparatedList(nodeTypeCommaSeparatedList);
  }

  public void visitParameters(Parameters parameters) throws IOException {
    visitChildren(parameters.getChildren());
    delegate.visitParameters(parameters);
  }

  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    visitChildren(functionExpr.getChildren());
    delegate.visitFunctionExpr(functionExpr);
  }

  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    visitChildren(vectorLiteral.getChildren());
    delegate.visitVectorLiteral(vectorLiteral);
  }

  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    visitChildren(applyExpr.getChildren());
    delegate.visitApplyExpr(applyExpr);
  }

  public void visitNewExpr(NewExpr newExpr) throws IOException {
    visitChildren(newExpr.getChildren());
    delegate.visitNewExpr(newExpr);
  }

  public void visitClassBody(ClassBody classBody) throws IOException {
    visitChildren(classBody.getChildren());
    delegate.visitClassBody(classBody);
  }

  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    visitChildren(blockStatement.getChildren());
    delegate.visitBlockStatement(blockStatement);
  }

  public void visitDefaultStatement(DefaultStatement defaultStatement) throws IOException {
    visitChildren(defaultStatement.getChildren());
    delegate.visitDefaultStatement(defaultStatement);
  }

  public void visitLabeledStatement(LabeledStatement labeledStatement) throws IOException {
    visitChildren(labeledStatement.getChildren());
    delegate.visitLabeledStatement(labeledStatement);
  }

  public void visitIfStatement(IfStatement ifStatement) throws IOException {
    visitChildren(ifStatement.getChildren());
    delegate.visitIfStatement(ifStatement);
  }

  public void visitCaseStatement(CaseStatement caseStatement) throws IOException {
    visitChildren(caseStatement.getChildren());
    delegate.visitCaseStatement(caseStatement);
  }

  public void visitTryStatement(TryStatement tryStatement) throws IOException {
    visitChildren(tryStatement.getChildren());
    delegate.visitTryStatement(tryStatement);
  }

  public void visitCatch(Catch aCatch) throws IOException {
    visitChildren(aCatch.getChildren());
    delegate.visitCatch(aCatch);
  }

  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
    visitChildren(forInStatement.getChildren());
    delegate.visitForInStatement(forInStatement);
  }

  public void visitWhileStatement(WhileStatement whileStatement) throws IOException {
    visitChildren(whileStatement.getChildren());
    delegate.visitWhileStatement(whileStatement);
  }

  public void visitForStatement(ForStatement forStatement) throws IOException {
    visitChildren(forStatement.getChildren());
    delegate.visitForStatement(forStatement);
  }

  public void visitDoStatement(DoStatement doStatement) throws IOException {
    visitChildren(doStatement.getChildren());
    delegate.visitDoStatement(doStatement);
  }

  public void visitSwitchStatement(SwitchStatement switchStatement) throws IOException {
    visitChildren(switchStatement.getChildren());
    delegate.visitSwitchStatement(switchStatement);
  }

  public void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement) throws IOException {
    visitChildren(semicolonTerminatedStatement.getChildren());
    delegate.visitSemicolonTerminatedStatement(semicolonTerminatedStatement);
  }

  public void visitContinueStatement(ContinueStatement continueStatement) throws IOException {
    visitChildren(continueStatement.getChildren());
    delegate.visitContinueStatement(continueStatement);
  }

  public void visitBreakStatement(BreakStatement breakStatement) throws IOException {
    visitChildren(breakStatement.getChildren());
    delegate.visitBreakStatement(breakStatement);
  }

  public void visitThrowStatement(ThrowStatement throwStatement) throws IOException {
    visitChildren(throwStatement.getChildren());
    delegate.visitThrowStatement(throwStatement);
  }

  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
    visitChildren(returnStatement.getChildren());
    delegate.visitReturnStatement(returnStatement);
  }

  public void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException {
    visitChildren(emptyStatement.getChildren());
    delegate.visitEmptyStatement(emptyStatement);
  }

  public void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration) throws IOException {
    visitChildren(emptyDeclaration.getChildren());
    delegate.visitEmptyDeclaration(emptyDeclaration);
  }

  public void visitParameter(Parameter parameter) throws IOException {
    visitChildren(parameter.getChildren());
    delegate.visitParameter(parameter);
  }

  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    visitChildren(variableDeclaration.getChildren());
    delegate.visitVariableDeclaration(variableDeclaration);
  }

  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    visitChildren(functionDeclaration.getChildren());
    delegate.visitFunctionDeclaration(functionDeclaration);
  }

  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitChildren(classDeclaration.getChildren());
    delegate.visitClassDeclaration(classDeclaration);
  }

  public void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException {
    visitChildren(predefinedTypeDeclaration.getChildren());
    delegate.visitPredefinedTypeDeclaration(predefinedTypeDeclaration);
  }

  public void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException {
    visitChildren(namespaceDeclaration.getChildren());
    delegate.visitNamespaceDeclaration(namespaceDeclaration);
  }

  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    visitChildren(packageDeclaration.getChildren());
    delegate.visitPackageDeclaration(packageDeclaration);
  }

  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    visitChildren(superConstructorCallStatement.getChildren());
    delegate.visitSuperConstructorCallStatement(superConstructorCallStatement);
  }

  public void visitAnnotation(Annotation annotation) throws IOException {
    visitChildren(annotation.getChildren());
    delegate.visitAnnotation(annotation);
  }

  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    visitChildren(useNamespaceDirective.getChildren());
    delegate.visitUseNamespaceDirective(useNamespaceDirective);
  }

  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    visitChildren(importDirective.getChildren());
    delegate.visitImportDirective(importDirective);
  }
}
