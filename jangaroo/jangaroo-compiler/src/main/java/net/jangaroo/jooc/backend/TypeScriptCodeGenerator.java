package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.ForInStatement;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.List;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private CompilationUnit compilationUnit;

  TypeScriptCodeGenerator(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
  }

  @Override
  protected void writeModifiers(JsWriter out, Declaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration instanceof IdeDeclaration && ((IdeDeclaration) declaration).isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      if (isPrimaryDeclaration
              || modifier.sym == sym.INTERNAL
              || SyntacticKeywords.OVERRIDE.equals(modifier.getText())
              || SyntacticKeywords.NATIVE.equals(modifier.getText())) {
        out.writeSymbolWhitespace(modifier);
      } else {
        out.writeSymbol(modifier);
      }
    }
    if (isPrimaryDeclaration) {
      out.writeToken("export");
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    this.compilationUnit = compilationUnit;
    out.beginComment();
    compilationUnit.getPackageDeclaration().visit(this);
    out.endComment();
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      IdeDeclaration primaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      List<Annotation> nativeAnnotations = primaryDeclaration.getAnnotations(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotations.isEmpty()) {
        visitImportDirective(new ImportDirective(dependentCompilationUnitModel.getPackageDeclaration().getIde(),
                dependentCompilationUnitModel.getPrimaryDeclaration().getName()));
      }
    }

    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    primaryDeclaration.visit(this);
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    super.visitClassDeclaration(classDeclaration);
    visitAll(classDeclaration.getSecondaryDeclarations());
  }

  @Override
  public void visitType(Type type) throws IOException {
    String tsType = getTypeScriptTypeForActionScriptType(type);
    writeSymbolReplacement(type.getSymbol(), tsType);
  }

  private static String getTypeScriptTypeForActionScriptType(Type type) {
    JooSymbol symbol = type.getSymbol();
    String as3TypeName = symbol.getText();
    AS3Type as3Type = AS3Type.typeByName(as3TypeName);
    if (as3Type == null) {
      return as3TypeName;
    }
    switch (as3Type) {
      case ANY:
        return "any";
      case UINT:
      case INT:
        return "number";
      case ARRAY:
        return "Array<any>";
      case BOOLEAN:
      case NUMBER:
      case STRING:
      // TODO: maybe also case OBJECT:
        return as3Type.name.toLowerCase();
      case VECTOR:
        return "Array<" + getTypeScriptTypeForActionScriptType(((IdeWithTypeParam)type.getIde()).getType()) + ">";
    }
    return as3Type.name;
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymRest());
    parameter.getIde().visit(this);
    boolean hasInitializer = parameter.getOptInitializer() != null;
    if (hasInitializer && isInterface()) {
      out.write("?");
    }
    visitIfNotNull(parameter.getOptTypeRelation());
    if (hasInitializer && !isInterface()) {
      parameter.getOptInitializer().visit(this);
    }
  }

  boolean isInterface() {
    return compilationUnit.getPrimaryDeclaration() instanceof ClassDeclaration
            && ((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).isInterface();
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
    if (infixOpExpr.getOp().sym == sym.IS) {
      infixOpExpr.getArg1().visit(this);
      out.writeSymbol(new JooSymbol(infixOpExpr.getOp().sym, "instanceof"));
      infixOpExpr.getArg2().visit(this);
    } else {
      super.visitInfixOpExpr(infixOpExpr);
    }
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    vectorLiteral.getArrayLiteral().visit(this);
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    String qualifiedName = importDirective.getQualifiedName();
    if (!qualifiedName.endsWith("*")) {
      out.writeSymbol(importDirective.getImportKeyword());
      out.writeSymbolWhitespace(importDirective.getIde().getSymbol());
      out.write("{");
      out.write(CompilerUtils.className(qualifiedName));
      out.write("} from '");
      out.write(qualifiedName.replace('.', '/'));
      out.write("'");
      if (importDirective.isExplicit()) {
        writeOptSymbol(importDirective.getSymSemicolon());
      } else {
        out.write(";");
        writeOptSymbolWhitespace(importDirective.getSymSemicolon());
      }
    }
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    writeSymbolReplacement(annotation.getLeftBracket(), "@");
    annotation.getIde().visit(this);
    writeOptSymbol(annotation.getOptLeftParen());
    CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
    if (annotationParameters != null) {
      out.write("{");
      annotationParameters.visit(this);
      out.write("}");
    }
    writeOptSymbol(annotation.getOptRightParen());
    out.writeSymbolWhitespace(annotation.getRightBracket());
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitIfNotNull(annotationParameter.getOptName(), "\"_\"");
    writeSymbolReplacement(annotationParameter.getOptSymEq(), ":");
    visitIfNotNull(annotationParameter.getValue(), "true");
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.isClassMember()) {
      for (VariableDeclaration currentVariableDeclaration = variableDeclaration;
           currentVariableDeclaration != null;
           currentVariableDeclaration = currentVariableDeclaration.getOptNextVariableDeclaration()) {
        visitDeclarationAnnotationsAndModifiers(variableDeclaration);
        // leave out "var" or "const" symbol for class members!
        visitVariableDeclarationBase(currentVariableDeclaration);
        writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");
      }
    } else {
      super.visitVariableDeclaration(variableDeclaration);
    }
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    if (functionDeclaration.isClassMember()) {
      boolean isNativeGetter = false;
      if (functionDeclaration.isNative() || isInterface()) {
        if (functionDeclaration.isSetter()) {
          return; // completely suppress native setter class members!
        }
        isNativeGetter = functionDeclaration.isGetter();
      }
      visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      // leave out "function" symbol for class members!
      writeOptSymbolWhitespace(functionDeclaration.getSymbol());
      if (!isNativeGetter) {
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
      }
      if (functionDeclaration.isConstructor()) {
        writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), "constructor");
      } else {
        functionDeclaration.getIde().visit(this);
      }
      FunctionExpr functionExpr = functionDeclaration.getFun();
      if (!isNativeGetter) {
        out.writeSymbol(functionExpr.getLParen());
        visitIfNotNull(functionExpr.getParams());
        out.writeSymbol(functionExpr.getRParen());
      }
      if (!functionDeclaration.isSetter()) { // in TypeScript, setters may not declare a return type, not even "void"!
        visitIfNotNull(functionExpr.getOptTypeRelation());
      }
      visitIfNotNull(functionExpr.getBody());
      writeOptSymbol(functionDeclaration.getOptSymSemicolon());
    } else {
      super.visitFunctionDeclaration(functionDeclaration);
    }
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    if (applyExpr.isTypeCast()) {
      out.writeSymbol(applyExpr.getArgs().getLParen());
      applyExpr.getArgs().getExpr().visit(this);
      out.writeToken(" as");
      applyExpr.getFun().visit(this);
      out.writeSymbol(applyExpr.getArgs().getRParen());
    } else {
      super.visitApplyExpr(applyExpr);
    }
  }

  @Override
  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
    VariableDeclaration decl = forInStatement.getDecl();
    if (decl != null && decl.getOptTypeRelation() != null) {
      out.writeSymbol(forInStatement.getSymKeyword());
      out.writeSymbol(forInStatement.getLParen());
      writeOptSymbol(decl.getOptSymConstOrVar());
      decl.getIde().visit(this);
      if (forInStatement.getSymEach() != null) {
        // In ECMAScript 6, "for each (... in ...)" is replaced by "for (... of ...)":
        writeSymbolReplacement(forInStatement.getSymIn(), "of");
      } else {
        out.writeSymbol(forInStatement.getSymIn());
      }
      forInStatement.getExpr().visit(this);
      out.writeSymbol(forInStatement.getRParen());
      forInStatement.getBody().visit(this);
    } else {
      super.visitForInStatement(forInStatement);
    }
  }

  @Override
  public void visitCatch(Catch aCatch) throws IOException {
    out.writeSymbol(aCatch.getSymKeyword());
    out.writeSymbol(aCatch.getLParen());
    aCatch.getParam().getIde().visit(this); // suppress type annotation in catch clause
    out.writeSymbol(aCatch.getRParen());
    aCatch.getBlock().visit(this);
  }

}
