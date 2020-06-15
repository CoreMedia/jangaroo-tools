package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.EmptyStatement;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.ForInStatement;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private CompilationUnit compilationUnit;
  private Map<String, TypedIdeDeclaration> configs;
  private Map<String, String> imports;

  TypeScriptCodeGenerator(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
  }

  @Override
  protected void writeModifiers(JsWriter out, IdeDeclaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration.isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbolWhitespace(modifier);
      if (!isPrimaryDeclaration && (modifier.sym == sym.PUBLIC
              || modifier.sym == sym.PROTECTED
              || modifier.sym == sym.IDE && SyntacticKeywords.STATIC.equals(modifier.getText())
      )) {
        out.writeSymbol(modifier, false);
      }
    }
    if (isPrimaryDeclaration && declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) != null) {
      out.write("declare");
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    this.compilationUnit = compilationUnit;
    this.configs = new LinkedHashMap<>();
    this.imports = new HashMap<>();
    out.beginComment();
    compilationUnit.getPackageDeclaration().visit(this);
    out.endComment();
    out.write("import * as AS3 from 'AS3';");

    Set<String> localNames = new HashSet<>();

    // initialize with name of current compilation unit:
    String primaryLocalName = compilationUnit.getPrimaryDeclaration().getName();
    localNames.add(primaryLocalName);
    imports.put(compilationUnit.getPrimaryDeclaration().getQualifiedNameStr(), primaryLocalName);

    // generate imports
    // first pass: detect import local name clashes:
    Set<String> localNameClashes = new HashSet<>();
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      String localName = getDefaultImportName(dependentCompilationUnitModel.getPrimaryDeclaration());
      localName = localName.split("\\.")[0]; // may be a native fully qualified name which "occupies" its first namespace!
      if (!localNames.add(localName)) {
        localNameClashes.add(localName);
      }
    }
    // second pass: generate imports, using fully-qualified names for local name clashes:
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      IdeDeclaration primaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = getRequireModuleName(primaryDeclaration);
      String localName = getDefaultImportName(primaryDeclaration);
      if (requireModuleName != null && localNameClashes.contains(localName)) {
        localName = toLocalName(primaryDeclaration.getQualifiedName());
      }
      imports.put(primaryDeclaration.getQualifiedNameStr(), localName);
      if (requireModuleName != null) {
        out.write(String.format("import %s from '%s';\n", localName, requireModuleName));
      }
    }

    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();

    primaryDeclaration.visit(this);

    out.write("\nexport default " + primaryDeclaration.getName() + ";\n");
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    for (TypedIdeDeclaration member : classDeclaration.getStaticMembers().values()) {
      if (member.isPrivate()) {
        out.write(MessageFormat.format("const ${0} = Symbol(\"{0}\");\n", member.getName()));
      }
    }

    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    if (classDeclaration.isInterface()) {
      out.write("{}\n");
      out.write("abstract class ");
      classDeclaration.getIde().visit(this);
      if (classDeclaration.getOptImplements() != null) {
        out.write(" implements ");
        classDeclaration.getOptImplements().getSuperTypes().visit(this);
      }
    }
    classDeclaration.getBody().visit(this);
    visitAll(classDeclaration.getSecondaryDeclarations());

    if (classDeclaration.isPrimaryDeclaration()) {
      if (classDeclaration.hasAnyExtConfig()) {
        String primaryDeclarationName = classDeclaration.getName();
        out.write("\nnamespace ");
        out.write(primaryDeclarationName);
        out.write(" {\n");
        if (classDeclaration.getOptImplements() != null) {
          CommaSeparatedList<Ide> superTypes = classDeclaration.getOptImplements().getSuperTypes();
          List<String> mixins = new ArrayList<>();
          do {
            IdeDeclaration interfaceIdeDeclaration = superTypes.getHead().getDeclaration(false);
            CompilationUnit mixinCompilationUnit = CompilationUnit.getMixinCompilationUnit(interfaceIdeDeclaration);
            if (mixinCompilationUnit != null
                    && ((ClassDeclaration) mixinCompilationUnit.getPrimaryDeclaration()).hasAnyExtConfig()) {
              mixins.add(mixinCompilationUnit.getPrimaryDeclaration().getName() + ".Config");
            }
            superTypes = superTypes.getTail();
          } while (superTypes != null);
          if (!mixins.isEmpty()) {
            out.write(String.format("  export interface Config extends %s {}\n", String.join(", ", mixins)));
          }
        }
        out.write("  export class Config ");
        ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
        if (superTypeDeclaration.hasAnyExtConfig()) {
          out.write("extends " + superTypeDeclaration.getName() + ".Config");
        }
        out.write(" {\n");
        for (TypedIdeDeclaration configDeclaration : configs.values()) {
          out.write("    " + configDeclaration.getName() + "?");
          visitIfNotNull(configDeclaration.getOptTypeRelation());
          out.write(";\n");
        }
        out.write("  }\n");
        out.write("}\n\n");

        out.write("export function " + primaryDeclarationName + "_(config?: " + primaryDeclarationName + ".Config): " + primaryDeclarationName + ".Config { return config; }\n");
      }
    }
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

  private String getTypeScriptTypeForActionScriptType(Type type) {
    TypeDeclaration declaration = type.getDeclaration();
    String as3TypeName = declaration.getIde().getName();
    AS3Type as3Type = AS3Type.typeByName(as3TypeName);
    if (as3Type == null) {
      String localName = getLocalName(declaration, true);
      if (type.getParentNode() instanceof TypeRelation
              && type.getParentNode().getParentNode() instanceof Parameter
              && "config".equals(((Parameter) type.getParentNode().getParentNode()).getName())) {
        localName += ".Config";
      }
      return localName;
    }
    switch (as3Type) {
      case ANY:
      case OBJECT:
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
        return as3Type.name.toLowerCase();
      case FUNCTION:
        return "AnyFunction";
    }
    return as3Type.name;
  }

  protected String getNativeAnnotationRequireValue(Annotation nativeAnnotation) {
    // exception: Ext.Base does not need to be "required", but for TypeScript, it needs to be imported!
    if ("Ext.Base".equals(getNativeAnnotationValue(nativeAnnotation))) {
      return "";
    }
    return super.getNativeAnnotationRequireValue(nativeAnnotation);
  }

  private String getRequireModuleName(IdeDeclaration declaration) {
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation == null) {
      return String.join("/", declaration.getQualifiedName());
    } else {
      String nativeAnnotationValue = getNativeAnnotationValue(nativeAnnotation);
      if (nativeAnnotationValue == null) {
        nativeAnnotationValue = declaration.getQualifiedNameStr();
      }
      if (getNativeAnnotationRequireValue(nativeAnnotation) != null) {
        return nativeAnnotationValue.replace('.', '/');
      }
      return null;
    }
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymRest());
    parameter.getIde().visit(this);
    boolean hasInitializer = parameter.getOptInitializer() != null;
    if (hasInitializer && isAmbientOrInterface(compilationUnit)) {
      out.write("?");
    }
    visitIfNotNull(parameter.getOptTypeRelation());
    if (hasInitializer && !isAmbientOrInterface(compilationUnit)) {
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
    out.writeToken("AS3.");
    out.writeSymbolToken(infixOpExpr.getOp());
    out.write('(');
    infixOpExpr.getArg1().visit(this);
    out.write(',');
    out.writeSymbolWhitespace(infixOpExpr.getOp());
    infixOpExpr.getArg2().visit(this);
    out.write(')');
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    vectorLiteral.getArrayLiteral().visit(this);
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    // ignore all explicit imports. They already have been rendered as transitive dependencies.
  }

  @Override
  public void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException {
    // suppress empty statements inside ECMAScript classes:
    if (!emptyStatement.isClassMember()) {
      super.visitEmptyStatement(emptyStatement);
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
      if (variableDeclaration.isExtConfig()) {
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
    } else if (variableDeclaration.isClassMember() && variableDeclaration.isPrivate()) {
      writeSymbolReplacement(variableDeclaration.getIde().getSymbol(), getDefinitionName(variableDeclaration));
      visitIfNotNull(variableDeclaration.getOptTypeRelation());
      visitIfNotNull(variableDeclaration.getOptInitializer());
    } else {
      super.visitVariableDeclarationBase(variableDeclaration);
    }
  }

  private String getDefinitionName(IdeDeclaration variableDeclaration) {
    return String.format(variableDeclaration.isStatic() ? "[$%s]" : "#%s", variableDeclaration.getIde().getName());
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    if (!isCompilationUnitAmbient()) {
      super.visitBlockStatement(blockStatement);
    }
  }

  private static final CodeGenerator ALIAS_THIS_CODE_GENERATOR = (out, first) -> out.write("const this$=this;");

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    FunctionExpr functionExpr = functionDeclaration.getFun();
    if (functionDeclaration.isClassMember()) {
      if (functionDeclaration.isGetter() &&
              (functionDeclaration.getAnnotation(Jooc.BINDABLE_ANNOTATION_NAME) != null
                      || functionDeclaration.getAnnotation(Jooc.EXT_CONFIG_ANNOTATION_NAME) != null)) {
        configs.put(functionDeclaration.getName(), functionDeclaration);
      }
      boolean isNativeGetter = false;
      if (functionDeclaration.isNative() || isAmbientOrInterface(compilationUnit)) {
        if (functionDeclaration.isSetter()) {
          return; // completely suppress native setter class members!
        }
        isNativeGetter = functionDeclaration.isGetter();
      }
      visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      if (functionDeclaration.getClassDeclaration().isInterface()) {
        out.writeSymbolWhitespace(functionDeclaration.getSymbol());
        out.writeToken("abstract");
      }
      // leave out "function" symbol for class members!
      writeOptSymbolWhitespace(functionDeclaration.getSymbol());
      if (!isNativeGetter) {
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
      }
      if (functionDeclaration.isConstructor()) {
        writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), "constructor");
      } else {
        if (functionDeclaration.isPrivate()) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), getDefinitionName(functionDeclaration));
          if (!functionDeclaration.isStatic()) {
            out.writeToken("=");
          }
        } else {
          functionDeclaration.getIde().visit(this);
        }
      }
      if (!isNativeGetter) {
        out.writeSymbol(functionExpr.getLParen());
        visitIfNotNull(functionExpr.getParams());
        out.writeSymbol(functionExpr.getRParen());
      }
      // in TypeScript, constructors and setters may not declare a return type, not even "void":
      if (!functionDeclaration.isConstructor() && !functionDeclaration.isSetter()) {
        visitIfNotNull(functionExpr.getOptTypeRelation());
      }
      if (functionDeclaration.isConstructor()
              && !functionDeclaration.containsSuperConstructorCall()
              && functionDeclaration.getClassDeclaration().notExtendsObject()) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), (out, first) -> out.write("super();"));
      }
      if (functionDeclaration.isThisAliased()
              && (!functionDeclaration.containsSuperConstructorCall()
              || !functionDeclaration.getClassDeclaration().notExtendsObject())) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), ALIAS_THIS_CODE_GENERATOR);
      } // else:
      // The super() call takes care of adding the this-alias, because TypeScript does not allow access
      // to "this" before super constructor call.

      if (functionDeclaration.isPrivate() && !functionDeclaration.isStatic()) {
        out.writeToken("=>");
      }
      visitIfNotNull(functionExpr.getBody());
      writeOptSymbol(functionDeclaration.getOptSymSemicolon());
    } else {
      // rewrite named function declaration function foo to const foo = function() {} or const foo = () => {}:
      writeSymbolReplacement(functionDeclaration.getSymbol(), "const");
      functionDeclaration.getIde().visit(this);
      out.write(" = ");
      functionExpr.visit(this);
      writeOptSymbol(functionDeclaration.getOptSymSemicolon(), ";");
    }
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    FunctionDeclaration functionDeclaration = findFunctionDeclaration(superConstructorCallStatement);
    // TypeScript does not allow super() constructor call when inheriting from Object, but ActionScript does,
    // so leave out the ActionScript call:
    if (functionDeclaration == null || functionDeclaration.getClassDeclaration().notExtendsObject()) {
      super.visitSuperConstructorCallStatement(superConstructorCallStatement);
      if (functionDeclaration != null && functionDeclaration.isThisAliased()) {
        ALIAS_THIS_CODE_GENERATOR.generate(out, false);
      }
    }
  }

  private static FunctionDeclaration findFunctionDeclaration(AstNode node) {
    AstNode parent = node;
    do {
      parent = parent.getParentNode();
      if (parent instanceof FunctionDeclaration) {
        return (FunctionDeclaration) parent;
      }
    } while (parent != null);
    return null;
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    if (functionExpr.mayRewriteToArrowFunction()) {
      out.writeSymbolWhitespace(functionExpr.getFunSymbol());
      out.suppressWhitespace(functionExpr.getLParen());
      // rewrite anonymous function expression that does *not* use "this" to arrow function:
      generateFunctionExprSignature(functionExpr);
      out.write(" =>");
      // special case: function body consists of exactly one statement, a return <expr>
      // then just render this expression:
      List<Directive> statements = functionExpr.getBody().getDirectives();
      if (statements.size() == 1) {
        Directive firstStatement = statements.get(0);
        if (firstStatement instanceof ReturnStatement) {
          Expr expr = ((ReturnStatement) firstStatement).getOptExpr();
          if (expr != null) {
            expr.visit(this);
            return;
          }
        }
      }
    } else {
      out.writeSymbol(functionExpr.getSymFunction());
      visitIfNotNull(functionExpr.getIde());
      generateFunctionExprSignature(functionExpr);
    }
    visitIfNotNull(functionExpr.getBody());
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    if (applyExpr.isTypeCast()) {
      ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
      if (isConfigFactory(applyExpr)) {
        out.writeSymbol(((IdeExpr)applyExpr.getFun()).getIde().getIde());
        out.write("_"); // use config factory function instead of the class itself!
        args.visit(this);
      } else {
        out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
        out.writeToken("AS3.cast");
        out.writeSymbol(args.getLParen());
        applyExpr.getFun().visit(this);
        out.writeToken(",");
        // isTypeCast() ensures that there is exactly one parameter:
        args.getExpr().getHead().visit(this);
        out.writeSymbol(args.getRParen());
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
    ExpressionType exprType = forInStatement.getExpr().getType();
    out.writeSymbol(forInStatement.getSymKeyword());
    out.writeSymbol(forInStatement.getLParen());
    if (decl != null) {
      // suppress declaration type: no type allowed in TypeScript here!
      writeOptSymbol(decl.getOptSymConstOrVar());
      decl.getIde().visit(this);
    } else if (forInStatement.getLValue() != null) {
      forInStatement.getLValue().visit(this);
    }
    if (forInStatement.getSymEach() != null) {
      // In ECMAScript 6, "for each (... in ...)" is replaced by "for (... of ...)":
      writeSymbolReplacement(forInStatement.getSymIn(), "of");
      Type declType = decl == null ? null : decl.getOptTypeRelation().getType();
      if (exprType != null && exprType.isArrayLike()) {
        forInStatement.getExpr().visit(this);
        // as decl cannot have a type, cast to specific Array, but only if the array's element type is not the exact declared type:
        if (declType != null && !new ExpressionType(declType).equals(exprType.getTypeParameter())) {
          out.write(" as Array<");
          declType.visit(this);
          out.write(">");
        }
      } else {
        // If the expression is not iterable, Object.values() must be used.
        // Note that it must be polyfilled in IE, even IE11!
        out.writeSymbolWhitespace(forInStatement.getExpr().getSymbol());
        out.write("Object.values");
        if (declType != null) {
          out.write("<");
          declType.visit(this);
          out.write(">");
        }
        out.write("(");
        forInStatement.getExpr().visit(this);
        out.write(")");
      }
    } else {
      out.writeSymbol(forInStatement.getSymIn());
      forInStatement.getExpr().visit(this);
    }
    out.writeSymbol(forInStatement.getRParen());
    forInStatement.getBody().visit(this);
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    Ide ide = dotExpr.getIde();
    if (ide.isBound()) {
      // found access to a method without applying it immediately: bind!
      out.writeToken("AS3.bind(");
      dotExpr.getArg().visit(this);
      writeSymbolReplacement(dotExpr.getOp(), ",");
      //writeSymbolReplacement(ide.getIde(), CompilerUtils.quote(ide.getName()));
      internalVisitDotExpr(dotExpr);
      out.writeToken(")");
    } else {
      internalVisitDotExpr(dotExpr);
    }
  }

  private void internalVisitDotExpr(DotExpr dotExpr) throws IOException {
    Expr arg = dotExpr.getArg();
    if (arg instanceof IdeExpr) {
      arg = ((IdeExpr)arg).getNormalizedExpr();
    }
    ExpressionType type = arg.getType();
    if (type != null) {
      Ide ide = dotExpr.getIde();
      IdeDeclaration memberDeclaration = type.resolvePropertyDeclaration(ide.getName());
      if (memberDeclaration != null && memberDeclaration.isPrivate()) {
        arg.visit(this);
        if (memberDeclaration.isStatic()) {
          writeSymbolReplacement(dotExpr.getOp(), "[");
          writeSymbolReplacement(ide.getSymbol(), "$" + ide.getName());
          out.write("]");
        } else {
          out.writeSymbol(dotExpr.getOp());
          writeSymbolReplacement(ide.getSymbol(), "#" + ide.getName());
        }
        return;
      }
    }

    super.visitDotExpr(dotExpr);
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    // this can only be "Vector$object", so we can always replace it by "Array":
    writeSymbolReplacement(ideWithTypeParam.getIde(), "Array");
    writeSymbolReplacement(ideWithTypeParam.getSymDotLt(), "<");
    ideWithTypeParam.getType().visit(this);
    out.writeSymbol(ideWithTypeParam.getSymGt());
  }

  private String getDefaultImportName(IdeDeclaration declaration) {
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null && getNativeAnnotationRequireValue(nativeAnnotation) == null) {
      String nativeName = getNativeAnnotationValue(nativeAnnotation);
      return nativeName == null ? declaration.getQualifiedNameStr() : nativeName;
    }
    return declaration.getName();
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    if (out.isWritingComment()) {
      super.visitQualifiedIde(qualifiedIde);
    } else {
      out.writeSymbolWhitespace(qualifiedIde.getQualifier().getSymbol());
      writeSymbolReplacement(qualifiedIde.getSymbol(), getLocalName(qualifiedIde, true));
    }
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    if (out.isWritingComment()) {
      super.visitIde(ide);
    } else {
      writeSymbolReplacement(ide.getIde(), getLocalName(ide, false));
    }
  }

  private String getLocalName(Ide ide, boolean useQualifiedName) {
    if (rewriteThis(ide)) {
      return "this$";
    } else if (ide.getScope() != null) {
      IdeDeclaration declaration = ide.getDeclaration(false);
      if (declaration != null) {
        return getLocalName(declaration, useQualifiedName);
      }
    }
    return useQualifiedName ? toLocalName(ide.getQualifiedName()) : ide.getName();
  }

  @Override
  String compilationUnitAccessCode(IdeDeclaration declaration) {
    return getLocalName(declaration, false);
  }

  private String getLocalName(IdeDeclaration declaration, boolean useQualifiedName) {
    String localName = null;
    if (declaration.isPrimaryDeclaration()) {
      declaration = CompilationUnit.mapMixinInterface(declaration.getCompilationUnit()).getPrimaryDeclaration();
      localName = imports.get(declaration.getQualifiedNameStr());
      if (localName == null) {
        System.err.println("*** not found in imports: " + declaration.getQualifiedNameStr());
      }
    }
    if (localName == null) {
      return useQualifiedName ? toLocalName(declaration.getQualifiedName()) : declaration.getName();
    }
    return localName;
  }

  private static String toLocalName(String[] qualifiedName) {
    return String.join("_", qualifiedName);
  }

  private static boolean rewriteThis(Ide ide) {
    return ide.isThis() && ide.isRewriteThis() && !ide.getScope().getFunctionExpr().mayRewriteToArrowFunction();
  }

  private int staticCodeCounter = 0;

  @Override
  void generateStaticInitializer(List<Directive> directives) throws IOException {
    if (directives.isEmpty()) {
      return;
    }
    Directive firstDirective = directives.get(0);
    out.writeSymbolWhitespace(firstDirective.getSymbol());
    out.writeToken("// noinspection JSUnusedLocalSymbols");
    String uniqueName = ((ClassDeclaration)firstDirective.getParentNode().getParentNode()).getQualifiedNameHash();
    if (staticCodeCounter > 0) {
      uniqueName += "$" + staticCodeCounter;
    }
    ++staticCodeCounter;
    out.writeToken(String.format("\n  private static static$%s = (() =>", uniqueName));

    // is static code already wrapped in a block?
    if (directives.size() == 1 && firstDirective instanceof BlockStatement) {
      // static block already has curly braces: reuse these!
      firstDirective.visit(this);
    } else {
      // surround statements by curly braces:
      out.writeToken(" {\n    ");
      for (Directive directive : directives) {
        directive.visit(this);
      }
      out.writeToken("\n  }");
    }
    out.writeToken(")();");
  }
}
