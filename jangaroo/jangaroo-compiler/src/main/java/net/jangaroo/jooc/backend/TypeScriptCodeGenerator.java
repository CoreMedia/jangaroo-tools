package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.ForInStatement;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;
import org.apache.tools.ant.util.LinkedHashtable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private CompilationUnit compilationUnit;
  private Map<String, VariableDeclaration> configs;

  TypeScriptCodeGenerator(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
  }

  @Override
  protected void writeModifiers(JsWriter out, Declaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration instanceof IdeDeclaration && ((IdeDeclaration) declaration).isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbolWhitespace(modifier);
      if (!(isPrimaryDeclaration
              || modifier.sym == sym.INTERNAL
              || SyntacticKeywords.OVERRIDE.equals(modifier.getText())
              || SyntacticKeywords.NATIVE.equals(modifier.getText()))) {
        out.writeSymbol(modifier, false);
      }
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    this.compilationUnit = compilationUnit;
    this.configs = new LinkedHashtable();
    out.beginComment();
    compilationUnit.getPackageDeclaration().visit(this);
    out.endComment();
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      if (isCompilationUnitAmbient() && dependentCompilationUnitModel.isInSourcePath() && isAmbient(dependentCompilationUnitModel)) {
        continue;
      }
      IdeDeclaration primaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      List<Annotation> nativeAnnotations = primaryDeclaration.getAnnotations(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotations.isEmpty()) {
        visitImportDirective(new ImportDirective(dependentCompilationUnitModel.getPackageDeclaration().getIde(),
                dependentCompilationUnitModel.getPrimaryDeclaration().getName()));
      } else {
        String nativeCode = compilationUnitAccessCode(dependentCompilationUnitModel.getPrimaryDeclaration());
        if (nativeCode.contains(".")) {
          out.write("  import ");
          out.write(dependentCompilationUnitModel.getPrimaryDeclaration().getName());
          out.write(" from '");
          out.write(nativeCode.replace('.', '/'));
          out.write("';\n");
        }
      }
    }

    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    primaryDeclaration.visit(this);
    String primaryDeclarationName = primaryDeclaration.getName();
    if (primaryDeclaration instanceof ClassDeclaration) {
      ClassDeclaration superTypeDeclaration = ((ClassDeclaration) primaryDeclaration).getSuperTypeDeclaration();
      if (superTypeDeclaration != null && !"Object".equals(superTypeDeclaration.getName())) {
        out.write("namespace ");
        out.write(primaryDeclarationName);
        out.write(" {\n");
        out.write("  export abstract class Config extends "  + superTypeDeclaration.getName() + ".Config {\n");
        for (VariableDeclaration configDeclaration : configs.values()) {
          out.write("    " + configDeclaration.getName() + "?");
          visitIfNotNull(configDeclaration.getOptTypeRelation());
          out.write(";\n");
        }
        out.write("  }\n");
        out.write("}\n\n");

        out.write("export function " + primaryDeclarationName + "_(config?: " + primaryDeclarationName + ".Config): " + primaryDeclarationName + ".Config { return config; }\n");
      }
    }
    out.write("\nexport default " + primaryDeclarationName + ";\n");
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    classDeclaration.getBody().visit(this);
    visitAll(classDeclaration.getSecondaryDeclarations());
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    out.writeToken(anExtends.getSuperClass().getName());
  }

  @Override
  public void visitType(Type type) throws IOException {
    String tsType = getTypeScriptTypeForActionScriptType(type);
    writeSymbolReplacement(type.getSymbol(), tsType);
  }

  private static String getTypeScriptTypeForActionScriptType(Type type) {
    IdeDeclaration declaration = type.getDeclaration();
    String as3TypeName = declaration.getIde().getName();
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
      case VECTOR:
      case ARRAY:
        String arrayElementType = "any";
        if (type.getParentNode() instanceof TypeRelation) {
          ExpressionType expressionType = type.getIde().getScope().getExpressionType((TypeRelation) type.getParentNode());
          ExpressionType typeParameter = expressionType.getTypeParameter();
          if (typeParameter != null && typeParameter.getType() != null) {
            arrayElementType = getTypeScriptTypeForActionScriptType(typeParameter.getType());
          }
        }
        return "Array<" + arrayElementType + ">";
      case BOOLEAN:
      case NUMBER:
      case STRING:
      // TODO: maybe also case OBJECT:
        return as3Type.name.toLowerCase();
    }
    return as3Type.name;
  }

  private static String compilationUnitAccessCode(IdeDeclaration declaration) {
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null) {
      String nativeAnnotationValue = getNativeAnnotationValue(nativeAnnotation);
      return nativeAnnotationValue == null ? declaration.getQualifiedNameStr() : nativeAnnotationValue;
    }
    return declaration.getIde().getName();
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymRest());
    parameter.getIde().visit(this);
    boolean hasInitializer = parameter.getOptInitializer() != null;
    if (hasInitializer && isCompilationUnitAmbient()) {
      out.write("?");
    }
    visitIfNotNull(parameter.getOptTypeRelation());
    if (hasInitializer && !isCompilationUnitAmbient()) {
      parameter.getOptInitializer().visit(this);
    }
  }

  private boolean isCompilationUnitAmbient() {
    return isAmbient(compilationUnit);
  }

  private static boolean isAmbientOrInterface(CompilationUnit compilationUnit) {
    return isAmbient(compilationUnit)
            || isInterface(compilationUnit.getPrimaryDeclaration());
  }

  private static boolean isAmbient(CompilationUnit compilationUnit) {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    return primaryDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) != null
            || primaryDeclaration.isNative();
  }

  private static boolean isInterface(IdeDeclaration primaryDeclaration) {
    return primaryDeclaration instanceof ClassDeclaration && ((ClassDeclaration) primaryDeclaration).isInterface();
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
    if (!isAmbientOrInterface(compilationUnit)) {
      super.visitInitializer(initializer);
    }
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
    if (infixOpExpr.getOp().sym == sym.IS) {
      infixOpExpr.getArg1().visit(this);
      writeSymbolReplacement(infixOpExpr.getOp(), "instanceof");
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
      out.writeToken(CompilerUtils.className(qualifiedName));
      out.write(" from '");
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
    if (Jooc.NATIVE_ANNOTATION_NAME.equals(annotation.getMetaName()) ||
            Jooc.ARRAY_ELEMENT_TYPE_ANNOTATION_NAME.equals(annotation.getMetaName())) {
      return;
    }
    out.beginComment();
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
    out.endComment();
    out.writeSymbolWhitespace(annotation.getRightBracket());
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitIfNotNull(annotationParameter.getOptName(), "\"_\"");
    if (annotationParameter.getOptSymEq() != null) {
      writeSymbolReplacement(annotationParameter.getOptSymEq(), ":");
    }
    visitIfNotNull(annotationParameter.getValue(), "true");
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.isClassMember()) {
      if (variableDeclaration.getAnnotation(Jooc.BINDABLE_ANNOTATION_NAME) != null) {
        configs.put(variableDeclaration.getName(), variableDeclaration);
      }
      for (VariableDeclaration currentVariableDeclaration = variableDeclaration;
           currentVariableDeclaration != null;
           currentVariableDeclaration = currentVariableDeclaration.getOptNextVariableDeclaration()) {
        visitDeclarationAnnotationsAndModifiers(variableDeclaration);
        // for class members, leave out "var", replace "const" by "readonly":
        if (variableDeclaration.isConst()) {
          out.writeToken("readonly");
        }
        visitVariableDeclarationBase(currentVariableDeclaration);
        writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");
      }
    } else {
      super.visitVariableDeclaration(variableDeclaration);
    }
  }

  @Override
  void visitVariableDeclarationBase(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.getOptTypeRelation() != null
            && variableDeclaration.getOptInitializer() != null
            && variableDeclaration.getOptInitializer().getValue() instanceof ApplyExpr
            && ((ApplyExpr) variableDeclaration.getOptInitializer().getValue()).isTypeCast()
            && isConfigFactory((ApplyExpr) variableDeclaration.getOptInitializer().getValue())) {
      variableDeclaration.getIde().visit(this);
      variableDeclaration.getOptTypeRelation().visit(this);
      // now, the difference: use Config type instead of Class type:
      out.write(".Config");
      // this will render as a Config factory invocation:
      visitIfNotNull(variableDeclaration.getOptInitializer());
    } else {
      super.visitVariableDeclarationBase(variableDeclaration);
    }
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    if (!isCompilationUnitAmbient()) {
      super.visitBlockStatement(blockStatement);
    }
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    if (functionDeclaration.isClassMember()) {
      boolean isNativeGetter = false;
      if (functionDeclaration.isNative() || isAmbientOrInterface(compilationUnit)) {
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
      if (isConfigFactory(applyExpr)) {
        out.writeSymbol(((IdeExpr)applyExpr.getFun()).getIde().getIde());
        out.write("_"); // use config factory function instead of the class itself!
        applyExpr.getArgs().visit(this);
      } else {
        out.writeSymbol(applyExpr.getArgs().getLParen());
        applyExpr.getArgs().getExpr().visit(this);
        out.writeToken(" as");
        applyExpr.getFun().visit(this);
        out.writeSymbol(applyExpr.getArgs().getRParen());
      }
    } else {
      super.visitApplyExpr(applyExpr);
    }
  }

  static boolean isConfigFactory(ApplyExpr applyExpr) {
    Expr arg1 = applyExpr.getArgs().getExpr().getHead();
    return arg1 instanceof ObjectLiteral && applyExpr.getFun() instanceof IdeExpr;
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

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    Ide ide = dotExpr.getIde();
    if (ide.isBound()) {
      // found access to a method without applying it immediately: bind!
      out.writeToken("AS3.bind(");
      dotExpr.getArg().visit(this);
      writeSymbolReplacement(dotExpr.getOp(), ",");
      writeSymbolReplacement(ide.getIde(), CompilerUtils.quote(ide.getName()));
      out.writeToken(")");
    } else {
      super.visitDotExpr(dotExpr);
    }
  }
}
