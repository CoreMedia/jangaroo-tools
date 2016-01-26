package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AsExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.BreakStatement;
import net.jangaroo.jooc.ast.CaseStatement;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.ConditionalExpr;
import net.jangaroo.jooc.ast.ContinueStatement;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.DefaultStatement;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DoStatement;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.EmptyDeclaration;
import net.jangaroo.jooc.ast.EmptyStatement;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.ForInStatement;
import net.jangaroo.jooc.ast.ForInitializer;
import net.jangaroo.jooc.ast.ForStatement;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.IfStatement;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.IsExpr;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NamespaceDeclaration;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.PostfixOpExpr;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.PrefixOpExpr;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.SwitchStatement;
import net.jangaroo.jooc.ast.ThrowStatement;
import net.jangaroo.jooc.ast.TryStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.UseNamespaceDirective;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.ast.WhileStatement;
import net.jangaroo.jooc.model.ActionScriptModel;
import net.jangaroo.jooc.model.AnnotatedModel;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.DocumentedModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.model.NamedModel;
import net.jangaroo.jooc.model.NamespaceModel;
import net.jangaroo.jooc.model.NamespacedModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.TypedModel;
import net.jangaroo.jooc.model.ValuedModel;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.TreeSet;

/**
 * Generates a model of the given compilation unit AST.
 * Feeding this model to an {@link ActionScriptCodeGeneratingModelVisitor} generates reduced ActionScript source code.
 * The reduced code is used as reference during compilation
 * and can be run through the asdoc tool.
 */
public class ApiModelGenerator {

  private boolean excludeClassByDefault = false;

  public ApiModelGenerator(boolean excludeClassByDefault) {
    this.excludeClassByDefault = excludeClassByDefault;
  }

  public boolean isExcludeClassByDefault() {
    return excludeClassByDefault;
  }

  public CompilationUnitModel generateModel(CompilationUnit compilationUnit) throws IOException {
    CompilationUnitModel compilationUnitModel = new CompilationUnitModel("");
    generateModel(compilationUnit, compilationUnitModel);
    return compilationUnitModel;
  }

  public void generateModel(CompilationUnit compilationUnit, CompilationUnitModel compilationUnitModel) throws IOException {
    ApiModelGeneratingAstVisitor visitor = new ApiModelGeneratingAstVisitor(compilationUnitModel);
    compilationUnit.visit(visitor);
  }

  
  private class ApiModelGeneratingAstVisitor implements AstVisitor {

  private CompilationUnitModel compilationUnitModel;
  private Deque<ActionScriptModel> modelStack;
  private StringBuilder code;
  private StringBuilder asdoc = new StringBuilder();
  private List<AnnotationModel> annotationModels = new ArrayList<AnnotationModel>();

    public ApiModelGeneratingAstVisitor(CompilationUnitModel compilationUnitModel) {
      this.compilationUnitModel = compilationUnitModel;
    }

    @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    startRecordingCode();
    typeRelation.getType().visit(this);
    ((TypedModel)modelStack.peek()).setType(consumeRecordedCode());
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    AnnotationPropertyModel annotationPropertyModel = new AnnotationPropertyModel();
    modelStack.push(annotationPropertyModel);
    visitIfNotNull(annotationParameter.getOptName());
    startRecordingCode();
    visitIfNotNull(annotationParameter.getValue());
    annotationPropertyModel.setValue(consumeRecordedCode());
    modelStack.pop();
    ((AnnotationModel)modelStack.peek()).addProperty(annotationPropertyModel);
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    getCurrent(ClassModel.class).setSuperclass(anExtends.getSuperClass().getDeclaration().getQualifiedNameStr());
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
    if (initializer.getValue().isCompileTimeConstant()) {
      startRecordingCode();
      initializer.getValue().visit(this);
      ((ValuedModel)modelStack.peek()).setValue(consumeRecordedCode());
    }
  }

  @Override
  public void visitObjectField(ObjectField objectField) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visitForInitializer(ForInitializer forInitializer) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    modelStack = new ArrayDeque<ActionScriptModel>();
    code = null;
    modelStack.push(compilationUnitModel);
    compilationUnit.getPackageDeclaration().visit(this);
    for (String publicApiDependency : new TreeSet<String>(compilationUnit.getPublicApiDependencies())) {
      compilationUnitModel.addImport(publicApiDependency);
    }
    for (CompilationUnit dependencyInModule : compilationUnit.getDependenciesInModule()) {
      compilationUnitModel.addDependencyInModule(dependencyInModule.getPrimaryDeclaration().getQualifiedNameStr());
    }
    visitAll(compilationUnit.getDirectives());
    compilationUnit.getPrimaryDeclaration().visit(this);
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    if (code != null) {
      recordCode(ide.getQualifiedNameStr());
    } else {
      ((NamedModel)modelStack.peek()).setName(ide.getQualifiedNameStr());
    }
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    visitIde(qualifiedIde);
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    recordCode(
      ideWithTypeParam.getOriginalIde().getText()
        + ideWithTypeParam.getSymDotLt().getText());
    ideWithTypeParam.getType().visit(this);
    recordCode(ideWithTypeParam.getSymGt().getText());
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void visitImplements(Implements anImplements) throws IOException {
    CommaSeparatedList<Ide> superTypes = anImplements.getSuperTypes();
    while (superTypes != null) {
      getCurrent(ClassModel.class).addInterface(superTypes.getHead().getQualifiedNameStr());
      superTypes = superTypes.getTail();
    }
  }

  @Override
  public void visitType(Type type) throws IOException {
    type.getIde().visit(this);
  }

  @Override
  public void visitObjectLiteral(ObjectLiteral objectLiteral) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  private IllegalStateException shouldOnlyBeCalledForCompileTimeConstants() {
    return new IllegalStateException("should only be called for compile time constants");
  }

  @Override
  public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
    ideExpr.getIde().visit(this);
  }

  @Override
  public <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
    visitBinaryOpExpr(infixOpExpr);
  }

  private void consumeRecordedAnnotations() {
    getCurrent(AnnotatedModel.class).setAnnotations(annotationModels);
    annotationModels = new ArrayList<AnnotationModel>();
  }

  private void recordAsdoc(JooSymbol symbol) {
    if (symbol != null) {
      String whitespace = symbol.getWhitespace();
      int startPos = whitespace.indexOf("/**");
      if (startPos != -1) {
        int endPos = whitespace.indexOf("*/", startPos);
        if (asdoc.length() > 0) {
          asdoc.append('\n'); // avoid missing white-space between two ASDoc sections
        }
        asdoc.append(whitespace.substring(startPos + 2, endPos - 1));
      }
    }
  }

  private void recordAsdoc(Declaration declaration) {
    for (JooSymbol symbol : declaration.getSymModifiers()) {
      recordAsdoc(symbol);
    }
  }

  private void consumeRecordedAsdoc() {
    ((DocumentedModel)modelStack.peek()).setAsdoc(trimAsdoc(asdoc.toString()));
    asdoc.setLength(0);
  }

  private void startRecordingCode() {
    code = new StringBuilder();
  }

  private void recordCode(JooSymbol symbol) {
    recordCode(symbol.getText()); // TODO: what about white-space?
  }

  private void recordCode(String text) {
    code.append(text);
  }

  private String consumeRecordedCode() {
    String recordedCode = code.toString();
    code = null;
    return recordedCode;
  }

  @Override
  public void visitAsExpr(AsExpr asExpr) throws IOException {
    visitInfixOpExpr(asExpr);
  }

  @Override
  public void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitNewExpr(NewExpr newExpr) throws IOException {
    throw shouldOnlyBeCalledForCompileTimeConstants();
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    for (Directive directive : classBody.getDirectives()) {
      directive.visit(this);
    }
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitDefaultStatement(DefaultStatement defaultStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitLabeledStatement(LabeledStatement labeledStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitIfStatement(IfStatement ifStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitCaseStatement(CaseStatement caseStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitTryStatement(TryStatement tryStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitCatch(Catch aCatch) throws IOException {
    throw new IllegalStateException("should not occur, because we are omitting try statements");
  }

  @Override
  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitWhileStatement(WhileStatement whileStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitForStatement(ForStatement forStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitDoStatement(DoStatement doStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitSwitchStatement(SwitchStatement switchStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitContinueStatement(ContinueStatement continueStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitBreakStatement(BreakStatement breakStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitThrowStatement(ThrowStatement throwStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  protected void visitIfNotNull(AstNode args) throws IOException {
    if (args != null) {
      args.visit(this);
    }
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    ParamModel paramModel = new ParamModel();
    paramModel.setRest(parameter.isRest());
    modelStack.push(paramModel);
    parameter.getIde().visit(this);
    visitIfNotNull(parameter.getOptTypeRelation());
    visitIfNotNull(parameter.getOptInitializer());
    modelStack.pop();
    ((MethodModel)modelStack.peek()).addParam(paramModel);
  }

  private void generateVisibility(Declaration declaration) {
    NamespacedModel namespacedModel = (NamespacedModel)modelStack.peek();
    if (namespacedModel instanceof MemberModel) {
      ((MemberModel)namespacedModel).setStatic(declaration.isStatic());
    }
    // Public API only, thus either "protected", "public", or custom namespace:
    if (declaration instanceof TypedIdeDeclaration) {
      Ide namespace = ((TypedIdeDeclaration)declaration).getNamespace();
      if (namespace != null) {
        namespacedModel.setNamespace(namespace.getQualifiedNameStr());
        return;
      }
    }
    namespacedModel.setNamespace(declaration.isProtected() ? NamespacedModel.PROTECTED : NamespacedModel.PUBLIC);
  }

  private void generateStaticFlag(Declaration declaration) {
    MemberModel memberModel = (MemberModel)modelStack.peek();
    memberModel.setStatic(declaration.isStatic());
  }

  private void generateMemberModifiers(Declaration declaration) {
    generateVisibility(declaration);
    generateStaticFlag(declaration);
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    boolean isTopLevelDeclaration = modelStack.peek() instanceof CompilationUnitModel;
    if (variableDeclaration.isPublicApi() || isTopLevelDeclaration) {
      FieldModel fieldModel = new FieldModel();
      modelStack.push(fieldModel);
      consumeRecordedAnnotations();
      if (isTopLevelDeclaration) {
        handleExcludeClassByDefault(fieldModel);
      }
      recordAsdoc(variableDeclaration);
      recordAsdoc(variableDeclaration.getOptSymConstOrVar());
      consumeRecordedAsdoc();
      generateMemberModifiers(variableDeclaration);
      fieldModel.setConst(variableDeclaration.isConst());
      variableDeclaration.getIde().visit(this);
      TypeRelation optTypeRelation = variableDeclaration.getOptTypeRelation();
      if (optTypeRelation != null) {
        optTypeRelation.visit(this);
      }
      if (variableDeclaration.isConst()) {
        visitIfNotNull(variableDeclaration.getOptInitializer());
      }
      popMember();
      visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
    }
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    boolean isTopLevelDeclaration = modelStack.peek() instanceof CompilationUnitModel;
    if (functionDeclaration.isPublicApi() || isTopLevelDeclaration) {
      MethodModel methodModel = new MethodModel();
      modelStack.push(methodModel);
      consumeRecordedAnnotations();
      if (isTopLevelDeclaration) {
        handleExcludeClassByDefault(methodModel);
      }
      recordAsdoc(functionDeclaration);
      recordAsdoc(functionDeclaration.getSymbol());
      consumeRecordedAsdoc();
      generateMemberModifiers(functionDeclaration);
      methodModel.setOverride(functionDeclaration.isOverride());
      methodModel.setFinal(functionDeclaration.isFinal());
      methodModel.setMethodType(functionDeclaration.isGetter() ? MethodType.GET
        : functionDeclaration.isSetter() ? MethodType.SET
        : null);
      functionDeclaration.getIde().visit(this);
      generateSignatureAsApiCode(functionDeclaration.getFun());

      if (functionDeclaration.isConstructor() && !functionDeclaration.isNative()) {
        // ASDoc does not allow a native constructor if the super class constructor needs parameters!
        StringBuilder superCallCode = new StringBuilder();
        superCallCode.append("super(");
        if (functionDeclaration.getClassDeclaration() != null) {
          ClassDeclaration superType = functionDeclaration.getClassDeclaration().getSuperTypeDeclaration();
          if (superType != null) {
            FunctionDeclaration superConstructor = superType.getConstructor();
            if (superConstructor != null) {
              Parameters superParameters = superConstructor.getParams();
              boolean first = true;
              while (superParameters != null && superParameters.getHead().getOptInitializer() == null) {
                if (first) {
                  first = false;
                } else {
                  superCallCode.append(", ");
                }
                superCallCode.append(VariableDeclaration.getDefaultValue(superParameters.getHead().getOptTypeRelation()));
                superParameters = superParameters.getTail();
              }
            }
          }
        }
        superCallCode.append(");");
        ((MethodModel)modelStack.peek()).setBody(superCallCode.toString());
      }
      popMember();
    }
  }

  private void popMember() {
    MemberModel member = (MemberModel)modelStack.pop();
    ActionScriptModel current = modelStack.peek();
    if (current instanceof ClassModel) {
      ((ClassModel)current).addMember(member);
    } else if (current instanceof CompilationUnitModel) {
      ((CompilationUnitModel)current).setPrimaryDeclaration(member);
    } else {
      throw new IllegalArgumentException("Members outside class or package not allowed.");
    }
  }

  public void generateSignatureAsApiCode(FunctionExpr fun) throws IOException {
    visitIfNotNull(fun.getParams());
    visitIfNotNull(fun.getOptTypeRelation());
  }

  protected void visitAll(Iterable<? extends AstNode> nodes) throws IOException {
    for (AstNode node : nodes) {
      node.visit(this);
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    ClassModel classModel = new ClassModel();
    getCurrent(CompilationUnitModel.class).setPrimaryDeclaration(classModel);
    modelStack.push(classModel);
    recordAsdoc(classDeclaration);
    recordAsdoc(classDeclaration.getSymClass());
    consumeRecordedAsdoc();
    classModel.setFinal(classDeclaration.isFinal());
    classModel.setDynamic(classDeclaration.isDynamic());
    generateVisibility(classDeclaration);
    consumeRecordedAnnotations();
    handleExcludeClassByDefault(classModel);
    classDeclaration.getIde().visit(this);
    classModel.setInterface(classDeclaration.isInterface());
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    classDeclaration.getBody().visit(this);
    modelStack.pop();
  }

  private void handleExcludeClassByDefault(AnnotatedModel annotatedModel) {
    if (isExcludeClassByDefault()) {
      // Add an [ExcludeClass] annotation, unless
      boolean needsExcludeClassAnnotation = true;
      for (AnnotationModel annotationModel : annotatedModel.getAnnotations()) {
        String metaName = annotationModel.getName();
        // ... an [PublicApi] or [ExcludeClass] annotation is already present.
        needsExcludeClassAnnotation = needsExcludeClassAnnotation &&
                !Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME.equals(metaName) &&
                !Jooc.PUBLIC_API_EXCLUSION_ANNOTATION_NAME.equals(metaName);
      }
      if (needsExcludeClassAnnotation) {
        annotatedModel.addAnnotation(new AnnotationModel(Jooc.PUBLIC_API_EXCLUSION_ANNOTATION_NAME));
      }
    }
  }

  @Override
  public void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException {
    NamespaceModel namespaceModel = new NamespaceModel();
    modelStack.push(namespaceModel);
    consumeRecordedAnnotations();
    handleExcludeClassByDefault(namespaceModel);
    recordAsdoc(namespaceDeclaration);
    recordAsdoc(namespaceDeclaration.getSymNamespace());
    consumeRecordedAsdoc();
    generateVisibility(namespaceDeclaration);
    namespaceDeclaration.getIde().visit(this);
    visitIfNotNull(namespaceDeclaration.getOptInitializer());
    modelStack.pop();
    getCurrent(CompilationUnitModel.class).setPrimaryDeclaration(namespaceModel);
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    Ide packageIde = packageDeclaration.getIde();
    if (packageIde != null) {
      getCurrent(CompilationUnitModel.class).setPackage(packageIde.getQualifiedNameStr());
    }
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    AnnotationModel annotationModel = new AnnotationModel();
    modelStack.push(annotationModel);
    recordAsdoc(annotation.getLeftBracket());
    consumeRecordedAsdoc();
    annotation.getIde().visit(this);
    visitIfNotNull(annotation.getOptAnnotationParameters());
    modelStack.pop();
    annotationModels.add(annotationModel);
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    // no api code generated
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    // no api code generated
  }

  @Override
  public void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    recordCode(literalExpr.getValue());
  }

  @Override
  public void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
    postfixOpExpr.getArg().visit(this);
    recordCode(postfixOpExpr.getOp());
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    dotExpr.getArg().visit(this);
    recordCode(dotExpr.getOp());
    recordCode(dotExpr.getIde().getName());
  }

  @Override
  public void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
    recordCode(prefixOpExpr.getOp());
    prefixOpExpr.getArg().visit(this);
  }

  @Override
  public void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
    binaryOpExpr.getArg1().visit(this);
    recordCode(binaryOpExpr.getOp());
    binaryOpExpr.getArg2().visit(this);
  }

  @Override
  public void visitIsExpr(IsExpr isExpr) throws IOException {
    visitBinaryOpExpr(isExpr);
  }

  @Override
  public void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
    conditionalExpr.getCond().visit(this);
    recordCode(conditionalExpr.getSymQuestion());
    conditionalExpr.getIfTrue().visit(this);
    recordCode(conditionalExpr.getSymColon());
    conditionalExpr.getIfFalse().visit(this);
  }

  @Override
  public <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> commaSeparatedList) throws IOException {
    visitIfNotNull(commaSeparatedList.getHead());
    if (commaSeparatedList.getSymComma() != null) {
      visitIfNotNull(commaSeparatedList.getTail());
    }
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
    visitCommaSeparatedList(parameters);
  }

  @Override
  public void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) throws IOException {
    throw new IllegalStateException("there should be no code generation for predefined types");
  }

  private <T extends ActionScriptModel> T getCurrent(Class<T> targetClass) {
    return targetClass.cast(modelStack.peek());
  }
  
}

  private static String trimAsdoc(String asdoc) {
    int oldLength;
    do {
      oldLength = asdoc.length();
      asdoc = asdoc.trim();
      if (asdoc.startsWith("*")) {
        asdoc = asdoc.substring(1);
      }
      if (asdoc.endsWith("*")) {
        asdoc = asdoc.substring(0, asdoc.length() - 1);
      }
    } while (asdoc.length() < oldLength);
    return asdoc;
  }

}
