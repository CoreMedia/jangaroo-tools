package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AsExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.BreakStatement;
import net.jangaroo.jooc.ast.CaseStatement;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.ContinueStatement;
import net.jangaroo.jooc.ast.DefaultStatement;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DoStatement;
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
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.NamespacedDeclaration;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.SwitchStatement;
import net.jangaroo.jooc.ast.ThrowStatement;
import net.jangaroo.jooc.ast.TryStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.UseNamespaceDirective;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.ast.WhileStatement;

import java.io.IOException;

/**
 * A visitor of the AST that generates executable reduced ActionScript code on
 * a {@link net.jangaroo.jooc.JsWriter}. The reduced code can then be run through
 * the asdoc tool.
 */
public class ApiCodeGenerator extends CodeGeneratorBase {

  public ApiCodeGenerator(JsWriter out) {
    super(out);
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    out.writeSymbol(typeRelation.getSymRelation());
    typeRelation.getType().getIde().visit(this);
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitIfNotNull(annotationParameter.getOptName());
    writeOptSymbol(annotationParameter.getOptSymEq());
    visitIfNotNull(annotationParameter.getValue());
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    anExtends.getSuperClass().generateCodeAsExpr(out);
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
    if (initializer.getValue().isCompileTimeConstant()) {
      out.writeSymbol(initializer.getSymEq());
      initializer.getValue().visit(this);
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
    compilationUnit.getPackageDeclaration().visit(this);
    out.writeSymbol(compilationUnit.getLBrace());
    compilationUnit.getPrimaryDeclaration().visit(this);
    out.writeSymbol(compilationUnit.getRBrace());
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    out.writeSymbol(ide.getIde());
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    qualifiedIde.getQualifier().visit(this);
    out.writeSymbol(qualifiedIde.getSymDot());
    out.writeSymbol(qualifiedIde.getIde());
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    out.writeSymbol(ideWithTypeParam.getOriginalIde());
    out.writeSymbol(ideWithTypeParam.getSymDotLt());
    ideWithTypeParam.getType().visit(this);
    out.writeSymbol(ideWithTypeParam.getSymGt());
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    out.writeSymbol(namespacedIde.getNamespace());
    out.writeSymbol(namespacedIde.getSymNamespaceSep());
    out.writeSymbol(namespacedIde.getIde());
  }

  @Override
  public void visitImplements(Implements anImplements) throws IOException {
    out.writeSymbol(anImplements.getSymImplements());
    generateImplements(anImplements.getSuperTypes());
  }

  private void generateImplements(CommaSeparatedList<Ide> superTypes) throws IOException {
    superTypes.getHead().generateCodeAsExpr(out);
    if (superTypes.getSymComma() != null) {
      out.writeSymbol(superTypes.getSymComma());
      generateImplements(superTypes.getTail());
    }
  }

  @Override
  public void visitType(Type type) throws IOException {
    out.writeSymbol(type.getIde().getIde());
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
    visitIde(ideExpr.getIde());
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
    infixOpExpr.getArg1().visit(this);
    out.writeSymbol(infixOpExpr.getOp());
    infixOpExpr.getArg2().visit(this);
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
  public void visitParameters(Parameters parameters) throws IOException {
    visitIfNotNull(parameters.getHead());
    if (parameters.getSymComma() != null) {
      out.writeSymbol(parameters.getSymComma());
      parameters.getTail().visit(this);
    }
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
    out.writeSymbol(classBody.getLBrace());
    for (Directive directive : classBody.getDirectives()) {
      directive.visit(this);
    }
    out.writeSymbol(classBody.getRBrace());
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

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymConstOrRest());
    parameter.getIde().visit(this);
    visitIfNotNull(parameter.getOptTypeRelation());
    visitIfNotNull(parameter.getOptInitializer());
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (!variableDeclaration.isPrivate()) {
      writeModifiers(out, variableDeclaration);
      out.writeSymbol(variableDeclaration.getOptSymConstOrVar());
      variableDeclaration.getIde().visit(this);
      visitIfNotNull(variableDeclaration.getOptTypeRelation());
      visitIfNotNull(variableDeclaration.getOptInitializer());
      visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
      writeOptSymbol(variableDeclaration.getOptSymSemicolon());
    }
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    if (!functionDeclaration.isPrivate()) {
      writeModifiers(out, functionDeclaration);
      if (!functionDeclaration.isNative() && !functionDeclaration.isAbstract() && !functionDeclaration.isConstructor()) {
        out.writeSymbolWhitespace(functionDeclaration.getFun().getFunSymbol());
        out.writeToken(SyntacticKeywords.NATIVE);
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol(), false);
      } else {
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
      }
      writeOptSymbol(functionDeclaration.getSymGetOrSet());
      functionDeclaration.getIde().visit(this);
      generateSignatureAsApiCode(out, functionDeclaration.getFun());
      if (functionDeclaration.isConstructor() && !functionDeclaration.isNative()) {
        // ASDoc does not allow a native constructor if the super class constructor needs parameters!
        out.writeToken("{super(");
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
                  out.writeToken(",");
                }
                out.write(VariableDeclaration.getDefaultValue(superParameters.getHead().getOptTypeRelation()));
                superParameters = superParameters.getTail();
              }
            }
          }
        }
        out.writeToken(");}");
      } else {
        out.writeToken(";");
      }
    }
  }

  public void generateSignatureAsApiCode(JsWriter out, FunctionExpr fun) throws IOException {
    out.writeSymbol(fun.getLParen());
    visitIfNotNull(fun.getParams());
    out.writeSymbol(fun.getRParen());
    visitIfNotNull(fun.getOptTypeRelation());
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitAll(classDeclaration.getDirectives());
    writeModifiers(out, classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    classDeclaration.getBody().visit(this);
  }

  @Override
  public void visitNamespacedDeclaration(NamespacedDeclaration namespacedDeclaration) throws IOException {
    writeModifiers(out, namespacedDeclaration);
    out.writeSymbol(namespacedDeclaration.getSymNamespace());
    namespacedDeclaration.getIde().visit(this);
    if (namespacedDeclaration.getOptInitializer() != null) {
      out.writeSymbol(namespacedDeclaration.getOptInitializer().getSymEq());
      namespacedDeclaration.getOptInitializer().getValue().visit(this);
    }
    writeOptSymbol(namespacedDeclaration.getOptSymSemicolon(), ";");
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    out.writeSymbol(packageDeclaration.getSymPackage());
    visitIfNotNull(packageDeclaration.getIde());
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    // This may appear as a class initializer, in which case we can simply skip it.
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    out.writeSymbol(annotation.getLeftBracket());
    annotation.getIde().visit(this);
    writeOptSymbol(annotation.getOptLeftParen());
    visitIfNotNull(annotation.getOptAnnotationParameters());
    writeOptSymbol(annotation.getOptRightParen());
    out.writeSymbol(annotation.getRightBracket());
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    // no api code generated
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    if (importDirective.isExplicit()) {
      out.writeSymbol(importDirective.getImportKeyword());
      importDirective.getIde().visit(this);
      out.writeSymbol(importDirective.getSymSemicolon());
    }
    // else skip it
  }
}
