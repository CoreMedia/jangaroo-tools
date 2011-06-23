package net.jangaroo.jooc;

/**
 * Visitor for the ActionScript abstract syntax tree.
 */
public interface AstVisitor {
  void visitTypeRelation(TypeRelation typeRelation);

  void visitAnnotationParameter(AnnotationParameter annotationParameter);

  void visitExtends(Extends anExtends);

  void visitInitializer(Initializer visitor);

  void visitObjectField(ObjectField objectField);

  void visitForInitializer(ForInitializer forInitializer);

  void visitCompilationUnit(CompilationUnit compilationUnit);

  void visitIde(Ide ide);

  void visitQualifiedIde(QualifiedIde qualifiedIde);

  void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam);

  void visitNamespacedIde(NamespacedIde namespacedIde);

  void visitImplements(Implements anImplements);

  void visitIdeType(IdeType ideType);

  void visitObjectLiteral(ObjectLiteral objectLiteral);

  void visitIdeExpression(IdeExpr ideExpr);

  <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr);

  void visitArrayLiteral(ArrayLiteral arrayLiteral);

  void visitLiteralExpr(LiteralExpr literalExpr);

  void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr);

  void visitDotExpr(DotExpr dotExpr);

  void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr);

  void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr);

  void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr);

  void visitInfixOpExpr(InfixOpExpr infixOpExpr);

  void visitAsExpr(AsExpr asExpr);

  void visitIsExpr(IsExpr isExpr);

  void visitConditionalExpr(ConditionalExpr conditionalExpr);

  void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr);

  <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> nodeTypeCommaSeparatedList);

  void visitParameters(Parameters parameters);

  void visitFunctionExpr(FunctionExpr functionExpr);

  void visitVectorLiteral(VectorLiteral vectorLiteral);

  void visitApplyExpr(ApplyExpr applyExpr);

  void visitNewExpr(NewExpr newExpr);

  void visitClassBody(ClassBody classBody);

  void visitBlockStatement(BlockStatement blockStatement);

  void visitDefaultStatement(DefaultStatement defaultStatement);

  void visitLabeledStatement(LabeledStatement labeledStatement);

  void visitIfStatement(IfStatement ifStatement);

  void visitCaseStatement(CaseStatement caseStatement);

  void visitTryStatement(TryStatement tryStatement);

  void visitCatch(Catch aCatch);

  void visitForInStatement(ForInStatement forInStatement);

  void visitWhileStatement(WhileStatement whileStatement);

  void visitForStatement(ForStatement forStatement);

  void visitDoStatement(DoStatement doStatement);

  void visitSwitchStatement(SwitchStatement switchStatement);

  void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement);

  void visitContinueStatement(ContinueStatement continueStatement);

  void visitBreakStatement(BreakStatement breakStatement);

  void visitThrowStatement(ThrowStatement throwStatement);

  void visitReturnStatement(ReturnStatement returnStatement);

  void visitEmptyStatement(EmptyStatement emptyStatement);

  void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration);

  void visitParameter(Parameter parameter);

  void visitGetterSetterPair(GetterSetterPair getterSetterPair);

  void visitVariableDeclaration(VariableDeclaration variableDeclaration);

  void visitFunctionDeclaration(FunctionDeclaration functionDeclaration);

  void visitClassDeclaration(ClassDeclaration classDeclaration);

  void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration);

  void visitNamespacedDeclaration(NamespacedDeclaration namespacedDeclaration);

  void visitPackageDeclaration(PackageDeclaration packageDeclaration);

  void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement);

  void visitAnnotation(Annotation annotation);

  void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective);

  void visitImportDirective(ImportDirective importDirective);
}
