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
import net.jangaroo.jooc.ast.ForInStatement;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.FunctionExpr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.PropertyDeclaration;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private CompilationUnit compilationUnit;
  private Map<String, String> imports;
  private boolean companionInterfaceMode;
  private boolean needsCompanionInterface;

  TypeScriptCodeGenerator(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
  }

  @Override
  protected void writeModifiers(JsWriter out, IdeDeclaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration.isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbolWhitespace(modifier);
      if (!isPrimaryDeclaration && !companionInterfaceMode &&
              (modifier.sym == sym.PUBLIC
              || modifier.sym == sym.PROTECTED
              || modifier.sym == sym.IDE && SyntacticKeywords.STATIC.equals(modifier.getText())
      )) {
        out.writeSymbol(modifier, false);
      }
    }
    if (isPrimaryDeclaration) {
      Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotation != null) {
        if (getNativeAnnotationRequireValue(nativeAnnotation) == null
                && !declaration.getPackageDeclaration().isTopLevel()) {
          out.writeToken("export");
        }
        if (!isInterface(declaration)) {
          out.writeToken("declare");
        }
      }
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    this.compilationUnit = compilationUnit;
    this.imports = new HashMap<>();

    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();

    // initialize with name of current compilation unit:
    String primaryLocalName = primaryDeclaration.getName();
    imports.put(primaryDeclaration.getQualifiedNameStr(), primaryLocalName);

    boolean isModule = getRequireModuleName(primaryDeclaration) != null;
    if (isModule) {
      out.beginComment();
      compilationUnit.getPackageDeclaration().visit(this);
      out.endComment();
      out.write("import * as AS3 from 'AS3';");
    } else {
      Ide packageIde = compilationUnit.getPackageDeclaration().getIde();
      // if global namespace, simply leave it out
      if (packageIde != null) {
        writeSymbolReplacement(compilationUnit.getPackageDeclaration().getSymbol(), "namespace");
        writeSymbolReplacement(packageIde.getSymbol(), packageIde.getQualifiedNameStr());
        out.writeSymbol(compilationUnit.getLBrace());
      }
    }

    Set<String> localNames = new HashSet<>();
    localNames.add(primaryLocalName);

    // generate imports
    // first pass: detect import local name clashes:
    Set<String> localNameClashes = new HashSet<>();
    if (isModule) {
      for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
        CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
        String localName = getDefaultImportName(dependentCompilationUnitModel.getPrimaryDeclaration());
        localName = localName.split("\\.")[0]; // may be a native fully qualified name which "occupies" its first namespace!
        if (!localNames.add(localName)) {
          localNameClashes.add(localName);
        }
      }
    }

    // second pass: generate imports, using fully-qualified names for local name clashes:
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      IdeDeclaration dependentPrimaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = getRequireModuleName(dependentPrimaryDeclaration);
      String localName = getDefaultImportName(dependentPrimaryDeclaration);
      if (requireModuleName != null) {
        if (!isModule) {
          // Non-modules do not allow import directives, or they'd become modules.
          // Unfortunately, TypeScript's new "import type" syntax also turns the importing file to a module,
          // which I consider a (design) bug.
          // Workaround: use special ad-hoc import syntax. This must be repeated for every usage, because
          // we cannot define a local type, as in a non-module, there is no "local" scope (at least not in a
          // top-level non-module, using no namespace).
          localName = String.format("import('%s').default", requireModuleName);
          requireModuleName = null;
        } else {
          if (localNameClashes.contains(localName)) {
            localName = toLocalName(dependentPrimaryDeclaration.getQualifiedName());
          }
        }
      }
      imports.put(dependentPrimaryDeclaration.getQualifiedNameStr(), localName);
      if (requireModuleName != null) {
        out.write(String.format("import %s from '%s';\n", localName, requireModuleName));
      }
    }

    primaryDeclaration.visit(this);

    if (isModule) {
      out.write("\nexport default " + primaryDeclaration.getName() + ";\n");
    } else if (compilationUnit.getPackageDeclaration().getIde() != null) {
      // close namespace:
      out.writeSymbol(compilationUnit.getRBrace());
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    needsCompanionInterface = false;
    List<Ide> mixins = new ArrayList<>();
    List<TypedIdeDeclaration> configs = classDeclaration.getMembers().stream()
            .filter(TypedIdeDeclaration::isExtConfig)
            .collect(Collectors.toList());
    String ownConfigsClassName = null;
    if (!configs.isEmpty()) {
      ownConfigsClassName = classDeclaration.getName() + "Configs";
      out.write(String.format("class %s {", ownConfigsClassName));
      for (TypedIdeDeclaration configDeclaration : configs) {
        visitAsConfig(configDeclaration);
      }
      out.write("}\n");
      out.write(String.format("type PropsFromConfigs = Required<%s>;", ownConfigsClassName));
      mixins.add(new Ide("PropsFromConfigs"));
      needsCompanionInterface = true;
    }
    for (TypedIdeDeclaration member : classDeclaration.getStaticMembers().values()) {
      if (member.isPrivate()) {
        out.write(MessageFormat.format("const ${0} = Symbol(\"{0}\");\n", member.getName()));
      }
    }
    for (TypedIdeDeclaration member : classDeclaration.getMembers()) {
      if (member.isPrivate() && useSymbolForPrivateMemeber(member)) {
        out.write(MessageFormat.format("const ${0} = Symbol(\"{0}\");\n", member.getName()));
      }
    }

    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    String configClassName = null;
    FunctionDeclaration constructor = classDeclaration.getConstructor();
    if (constructor != null && constructor.getParams() != null) {
      Parameter firstParam = constructor.getParams().getHead();
      if ("config".equals(firstParam.getName()) && firstParam.getOptTypeRelation() != null) {
        configClassName = compilationUnitAccessCode(firstParam.getOptTypeRelation().getType().getDeclaration()) + "._";
      }
    }
    if (configClassName == null && classDeclaration.hasAnyExtConfig()) {
      configClassName = classDeclaration.getName() + "._";
    }
    String configTypeParameterDeclaration = "";
    if (configClassName != null) {
      configTypeParameterDeclaration = String.format("<Cfg extends %s = %s>", configClassName, configClassName);
      out.write(configTypeParameterDeclaration);
    }
    if (classDeclaration.getOptExtends() != null) {
      classDeclaration.getOptExtends().visit(this);
      if (configClassName != null) {
        // *always* hand through Cfg type parameter to super class if using config system:
        out.write("<Cfg>");
      }
    }

    List<String> configMixins = new ArrayList<>();
    List<Ide> realInterfaces = new ArrayList<>();
    if (classDeclaration.getOptImplements() != null) {
      CommaSeparatedList<Ide> superTypes = classDeclaration.getOptImplements().getSuperTypes();
      do {
        ClassDeclaration maybeMixinDeclaration = (ClassDeclaration) superTypes.getHead().getDeclaration(false);
        CompilationUnit mixinCompilationUnit = CompilationUnit.getMixinCompilationUnit(maybeMixinDeclaration);
        if (mixinCompilationUnit != null
                && mixinCompilationUnit != compilationUnit) { // prevent circular inheritance between mixin and its own interface!
          mixins.add(superTypes.getHead());
          if (maybeMixinDeclaration.hasAnyExtConfig()) {
            configMixins.add(compilationUnitAccessCode(maybeMixinDeclaration) + "._");
          }
        } else {
          realInterfaces.add(superTypes.getHead());
        }
        superTypes = superTypes.getTail();
      } while (superTypes != null);

      visitImplementsFiltered(
              classDeclaration.getOptImplements().getSymImplements(),
              null,
              classDeclaration.getOptImplements(),
              configClassName != null,
              realInterfaces);
    }

    classDeclaration.getBody().visit(this);

    if (needsCompanionInterface) {
      out.write("\ninterface " + classDeclaration.getName() + configTypeParameterDeclaration);
      // output "extends <mixin-interfaces>[, PropsFromConfigs]"
      visitImplementsFiltered(
              new JooSymbol("extends"),
              configs.isEmpty() ? null : "PropsFromConfigs",
              classDeclaration.getOptImplements(),
              configClassName != null,
              mixins);
      // visit class body again in "companion interface" mode
      companionInterfaceMode = true;
      classDeclaration.getBody().visit(this);
      companionInterfaceMode = false;
      out.write("\n");
    }

    if (classDeclaration.isPrimaryDeclaration()) {
      visitAll(classDeclaration.getSecondaryDeclarations());

      if (classDeclaration.isInterface() && classDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null
              || classDeclaration.getOptImplements() != null) {
        out.write(MessageFormat.format(classDeclaration.isInterface()
                ? "\nconst {0} = AS3.createInterface<{0}>(\"{0}\""
                : "\nAS3.implementsInterfaces({0}", classDeclaration.getName()));
        if (classDeclaration.getOptImplements() != null) {
          out.write(", ");
          classDeclaration.getOptImplements().getSuperTypes().visit(this);
        }
        out.write(");\n");
      }
      if (configClassName != null) {
        out.write("\ndeclare namespace ");
        out.write(classDeclaration.getName());
        out.write(" {\n");
        List<String> configExtends = new ArrayList<>();
        ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
        if (superTypeDeclaration != null) {
          configExtends.add(compilationUnitAccessCode(superTypeDeclaration) + "._");
        }
        configExtends.addAll(configMixins);
        if (ownConfigsClassName != null) {
          configExtends.add(ownConfigsClassName);
        }
        out.write(String.format("  export interface _ extends %s {}\n", String.join(", ", configExtends)));
        out.write("  export function _(config?: _):_;\n");
        out.write("}\n\n");
      }
    }
  }

  private void visitImplementsFiltered(JooSymbol symImplementsOrExtends,
                                       String additionalIde,
                                       Implements optImplements,
                                       boolean useCfgTypeParameter,
                                       List<Ide> filter) throws IOException {
    JooSymbol lastSym = symImplementsOrExtends;
    if (additionalIde != null) {
      out.writeSymbol(lastSym);
      out.writeToken(additionalIde);
      lastSym = new JooSymbol(",");
    }
    if (optImplements != null) {
      CommaSeparatedList<Ide> current = optImplements.getSuperTypes();
      do {
        Ide head = current.getHead();
        if (filter.contains(head)) {
          out.writeSymbol(lastSym);
          head.visit(this);
          if (useCfgTypeParameter && useCfgTypeParameter(head)) {
            // hand through my Config class type parameter:
            out.write("<Cfg>");
          }
        }
        lastSym = current.getSymComma();
        current = current.getTail();
      } while (current != null);
    }
  }

  private void visitAsConfig(TypedIdeDeclaration configDeclaration) throws IOException {
    JooSymbol optSymSemicolon = null;
    if (configDeclaration instanceof FunctionDeclaration) {
      FunctionDeclaration functionDeclaration = (FunctionDeclaration) configDeclaration;
      if (functionDeclaration.isGetter()) {
        optSymSemicolon = functionDeclaration.getOptSymSemicolon();
      } else {
        // completely suppress set accessors (there are no write-only configs):
        return;
      }
    }
    // output all comments & white-space:
    visitDeclarationAnnotationsAndModifiers(configDeclaration);
    configDeclaration.getIde().visit(this);
    // we want all configs optional (even those documented as "required"):
    out.write("?");
    visitIfNotNull(configDeclaration.getOptTypeRelation());
    if (configDeclaration instanceof VariableDeclaration) {
      VariableDeclaration variableDeclaration = (VariableDeclaration) configDeclaration;
      visitIfNotNull(variableDeclaration.getOptInitializer());
      optSymSemicolon = variableDeclaration.getOptSymSemicolon();
    } else if (configDeclaration instanceof PropertyDeclaration) {
      PropertyDeclaration propertyDeclaration = (PropertyDeclaration) configDeclaration;
      if (propertyDeclaration.getGetter() != null) {
        optSymSemicolon = propertyDeclaration.getGetter().getOptSymSemicolon();
      }
    }
    writeOptSymbol(optSymSemicolon, ";");
  }

  @Override
  public void visitClassBodyDirectives(List<Directive> classBodyDirectives) throws IOException {
    super.visitClassBodyDirectives(!companionInterfaceMode ? classBodyDirectives
            : classBodyDirectives.stream()
            .filter(FunctionDeclaration.class::isInstance)
            .collect(Collectors.toList()));
  }

  private boolean useCfgTypeParameter(Ide superType) {
    IdeDeclaration declaration = superType.getDeclaration();
    if (declaration instanceof ClassDeclaration) {
      if (((ClassDeclaration) declaration).hasAnyExtConfig()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    if (typeRelation.getParentNode() instanceof IdeDeclaration) {
      out.writeSymbol(typeRelation.getSymbol());
      IdeDeclaration ideDeclaration = (IdeDeclaration) typeRelation.getParentNode();
      ExpressionType expressionType = ideDeclaration.getIde().getScope().getExpressionType(ideDeclaration);
      String tsType = getTypeScriptTypeForActionScriptType(expressionType);
      if ("config".equals(ideDeclaration.getName())) {
        TypeDeclaration maybeExtConfigClassDeclaration = expressionType.getDeclaration();
        if (maybeExtConfigClassDeclaration instanceof ClassDeclaration
                && ((ClassDeclaration) maybeExtConfigClassDeclaration).hasAnyExtConfig()) {
          tsType += "._"; // use config class instead!
        }
      }
      writeSymbolReplacement(typeRelation.getType().getSymbol(), tsType);
    } else {
      super.visitTypeRelation(typeRelation);
    }
  }

  @Override
  public void visitType(Type type) throws IOException {
    String tsType = getTypeScriptTypeForActionScriptType(type);
    writeSymbolReplacement(type.getSymbol(), tsType);
  }

  private String getTypeScriptTypeForActionScriptType(Type type) {
    return getTypeScriptTypeForActionScriptType(new ExpressionType(type));
  }

  private String getTypeScriptTypeForActionScriptType(ExpressionType expressionType) {
    AS3Type as3Type = expressionType == null ? AS3Type.ANY : expressionType.getAS3Type();
    switch (as3Type) {
      case OBJECT:
        TypeDeclaration declaration = expressionType.getDeclaration();
        if (!"Object".equals(declaration.getQualifiedNameStr())) {
          return getLocalName(declaration, true);
        }
        // fall-through
      case ANY:
        return "any";
      case UINT:
      case INT:
        return "number";
      case VECTOR:
      case ARRAY:
        return "Array<" + getTypeScriptTypeForActionScriptType(expressionType.getTypeParameter()) + ">";
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
        // never render [ExtConfig]s in a normal "visit":
        return;
      }
      visitDeclarationAnnotationsAndModifiers(variableDeclaration);
      for (VariableDeclaration currentVariableDeclaration = variableDeclaration;
           currentVariableDeclaration != null;
           currentVariableDeclaration = currentVariableDeclaration.getOptNextVariableDeclaration()) {
        if (currentVariableDeclaration != variableDeclaration) {
          // re-render annotations:
          visitAll(variableDeclaration.getAnnotations());
          // pull ide's white-space before the modifiers, as declarations are white-space sensitive:
          out.writeSymbolWhitespace(currentVariableDeclaration.getIde().getSymbol());
          // re-render modifiers:
          writeModifiers(out, variableDeclaration);
        }
        // for class members, leave out "var", replace "const" by "readonly":
        if (variableDeclaration.isConst()) {
          writeReadonlySuppressWhitespace(currentVariableDeclaration.getSymbol());
        }
        visitVariableDeclarationBase(currentVariableDeclaration);
        writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");
      }
    } else {
      super.visitVariableDeclaration(variableDeclaration);
    }
  }

  private void writeReadonlySuppressWhitespace(JooSymbol suppressWhitespaceOf) throws IOException {
    out.writeToken("readonly");
    // take care to not render new-lines after 'readonly', or TypeScript will interpret
    // it as the variable name and start a new variable declaration on the next line,
    // missing all modifiers!
    out.write(" ");
    out.suppressWhitespace(suppressWhitespaceOf);
  }

  @Override
  void visitVariableDeclarationBase(VariableDeclaration variableDeclaration) throws IOException {
    TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();
    Initializer initializer = variableDeclaration.getOptInitializer();
    Ide ide = variableDeclaration.getIde();
    if (variableDeclaration.isClassMember()) {
      if (variableDeclaration.isPrivate()) {
        writeSymbolReplacement(ide.getSymbol(), getDefinitionName(variableDeclaration));
      } else {
        ide.visit(this);
      }
      visitIfNotNull(typeRelation);
      visitIfNotNull(initializer);
    } else if (variableDeclaration.isPrimaryDeclaration()
            && !variableDeclaration.isConst()
            && getRequireModuleName(variableDeclaration) != null) {
      out.writeSymbol(ide.getSymbol()); // do not rewrite var name (no underscore)!
      if (typeRelation != null) {
        out.writeSymbol(typeRelation.getSymRelation());
        // wrap type by simple object:
        out.write("{_: ");
        typeRelation.getType().visit(this);
        out.write("}");
      }
      if (!isCompilationUnitAmbient()) {
        // ambient CUs allow no initializers, while non-ambient CUs must have one, so
        // that the _ property at least exists.
        if (initializer != null) {
          out.writeSymbol(initializer.getSymEq());
        } else {
          out.write("=");
        }
        out.write("{_: ");
        if (initializer != null) {
          initializer.getValue().visit(this);
        } else {
          out.write(VariableDeclaration.getDefaultValue(typeRelation));
        }
        out.write("}");
      }
    } else {
      ide.visit(this);
      if (typeRelation != null) {
        // TypeScript supports type inference! And since the rhs might be changed to a config type,
        // leaving TypeScript to chose the same type leads to better results than to declaring it again!
        // So leave out type declaration if it just re-declares the exact type of the initializer:
        if (initializer == null
                || initializer.getValue().getType() == null
                || !initializer.getValue().getType().equals(new ExpressionType(typeRelation.getType()))) {
          typeRelation.visit(this);
        }
      }
      visitIfNotNull(initializer);
    }
  }

  private String getDefinitionName(IdeDeclaration varOrFunDeclaration) {
    return String.format(useSymbolForPrivateMemeber(varOrFunDeclaration)
            ? "[$%s]" : "#%s", varOrFunDeclaration.getIde().getName());
  }

  private boolean useSymbolForPrivateMemeber(IdeDeclaration varOrFunDeclaration) {
    return varOrFunDeclaration.isStatic()
            || !varOrFunDeclaration.isNative()
            && (varOrFunDeclaration instanceof PropertyDeclaration
            || varOrFunDeclaration instanceof FunctionDeclaration
            && ((FunctionDeclaration) varOrFunDeclaration).isGetterOrSetter());
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
      if (functionDeclaration.isExtConfig()) {
        // never render [ExtConfig]s in a normal "visit":
        return;
      }
      boolean isAmbientOrInterface = isAmbientOrInterface(functionDeclaration.getCompilationUnit());
      boolean convertToProperty = functionDeclaration.isGetterOrSetter() &&
              (functionDeclaration.isNative() || isAmbientOrInterface);
      if (convertToProperty && functionDeclaration.isSetter()) {
        // completely suppress native setter class members!
        return;
      }
      // any other native members in a non-ambient/interface compilation unit are moved
      // to its companion interface declaration:
      boolean renderIntoInterface = !convertToProperty
              && !isAmbientOrInterface
              && functionDeclaration.isNative();
      if (renderIntoInterface) {
        needsCompanionInterface = true;
      }
      if (companionInterfaceMode != renderIntoInterface) {
        return;
      }
      visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      // leave out "function" symbol for class members!
      writeOptSymbolWhitespace(functionDeclaration.getSymbol());
      if (convertToProperty) {
        if (!hasSetter(functionDeclaration)) { // a native getter without setter => readonly property!
          writeReadonlySuppressWhitespace(functionDeclaration.getIde().getSymbol());
        }
      } else {
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
      }
      if (functionDeclaration.isConstructor()) {
        writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), "constructor");
      } else {
        if (functionDeclaration.isPrivate()) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), getDefinitionName(functionDeclaration));
          if (!useSymbolForPrivateMemeber(functionDeclaration)) {
            out.writeToken("=");
          }
        } else {
          functionDeclaration.getIde().visit(this);
        }
      }
      if (convertToProperty) {
        if (functionDeclaration.isExtConfig()) {
          out.write("?");
        }
      } else {
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
        addBlockStartCodeGenerator(functionDeclaration.getBody(), (out, first) -> out.write("\n    super();"));
      }
      if (functionDeclaration.isThisAliased()) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), ALIAS_THIS_CODE_GENERATOR);
      }

      if (functionDeclaration.isPrivate() && !useSymbolForPrivateMemeber(functionDeclaration)) {
        out.writeToken("=>");
      }
      visitIfNotNull(functionExpr.getBody());
      writeOptSymbol(functionDeclaration.getOptSymSemicolon());
    } else {
      if (functionDeclaration.isPrimaryDeclaration()) {
        visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      }
      if (functionExpr.rewriteToArrowFunction()) {
        // rewrite named function declaration function foo to var foo = () => {}:
        // (We could use const, but only if there are no forward references.)
        writeSymbolReplacement(functionDeclaration.getSymbol(), "var");
        functionDeclaration.getIde().visit(this);
        out.write(" = ");
        functionExpr.visit(this);
        writeOptSymbol(functionDeclaration.getOptSymSemicolon(), ";");
      } else {
        // if there is no implicit outer this access, don't bother to rewrite to an arrow function:
        functionExpr.visit(this);
        writeOptSymbolWhitespace(functionDeclaration.getOptSymSemicolon());
      }
    }
  }

  protected void visitBlockStatementDirectives(BlockStatement body) throws IOException {
    if (!(body.usesInstanceThis()
            && body.getParentNode() instanceof FunctionExpr
            && body.getParentNode().getParentNode() instanceof FunctionDeclaration
            && ((FunctionDeclaration) body.getParentNode().getParentNode()).containsSuperConstructorCall())) {
      super.visitBlockStatementDirectives(body);
      return;
    }
    Iterator<Directive> iterator = body.getDirectives().iterator();
    List<Directive> directivesToWrap = null;
    while (iterator.hasNext()) {
      Directive directive = iterator.next();
      if (directivesToWrap == null && directive.usesInstanceThis()) {
        // start recording:
        directivesToWrap = new ArrayList<>();
      }
      if (directive instanceof SuperConstructorCallStatement) {
        if (directivesToWrap == null) {
          directive.visit(this);
        } else {
          visitSuperCallWithWrappedDirectives((SuperConstructorCallStatement) directive, directivesToWrap);
        }
        break;
      }
      if (directivesToWrap == null) {
        directive.visit(this);
      } else {
        directivesToWrap.add(directive);
      }
    }
    while (iterator.hasNext()) {
      iterator.next().visit(this);
    }
  }

  private void visitSuperCallWithWrappedDirectives(SuperConstructorCallStatement superCall, List<Directive> directivesToWrap) throws IOException {
    superCall.getFun().visit(this);
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = superCall.getArgs();
    out.writeSymbol(args.getLParen());
    CommaSeparatedList<Expr> superCallParams = args.getExpr();
    // not exactly one parameter?
    boolean hasOneParameter = superCallParams != null && superCallParams.getTail() == null;
    if (!hasOneParameter) {
      // use spread operator on the returned array:
      out.write("...");
    }
    // declare as immediately-evaluating function (IEF), so that TypeScript does not complaing about
    // usage of `this` before calling `super()`:
    out.write("(()=>");
    if (!directivesToWrap.isEmpty()) {
      out.write("{");
      visitAll(directivesToWrap);
      out.write("\n    return ");
    }
    if (!hasOneParameter) {
      out.write("[");
    }
    visitIfNotNull(superCallParams);
    if (!hasOneParameter) {
      out.write("]");
    }
    if (!directivesToWrap.isEmpty()) {
      out.write(";}");
    }
    // evaluate IEF:
    out.write(")()");
    out.writeSymbol(args.getRParen());
    writeOptSymbol(superCall.getSymSemicolon());
  }

  private static boolean hasSetter(FunctionDeclaration getter) {
    TypedIdeDeclaration maybePropertyDeclaration = getter.getClassDeclaration().getMemberDeclaration(getter.getName());
    return maybePropertyDeclaration instanceof PropertyDeclaration &&
            ((PropertyDeclaration) maybePropertyDeclaration).getSetter() != null;
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
    if (functionExpr.rewriteToArrowFunction()) {
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
        out.write("._"); // use config factory function instead of the class itself!
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
        if (useSymbolForPrivateMemeber(memberDeclaration)) {
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
      } else {
        if (declaration instanceof VariableDeclaration && !((VariableDeclaration) declaration).isConst()
                && getRequireModuleName(declaration) != null) {
          localName += "._";
        }
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
    return ide.isThis() && ide.isRewriteThis() && !ide.getScope().getFunctionExpr().rewriteToArrowFunction();
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