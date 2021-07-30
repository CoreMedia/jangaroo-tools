package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Declaration;
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
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectFieldOrSpread;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.Spread;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit.NET_JANGAROO_EXT_EXML;
import static net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit.AS_STRING;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private static final Collection<String> TYPESCRIPT_BUILT_IN_TYPES = Arrays.asList(
          "Object",
          "Array",
          "Vector$object"
  );
  public static final List<AS3Type> TYPES_ALLOWED_AS_INDEX = Arrays.asList(AS3Type.ANY, AS3Type.STRING, AS3Type.NUMBER, AS3Type.INT, AS3Type.UINT);

  private static final String I_RESOURCE_MANAGER_QUALIFIED_NAME = "mx.resources.IResourceManager";
  private static final String GET_STRING_METHOD_NAME = "getString";
  private static final String REST_RESOURCE_ANNOTATION_NAME = "RestResource";
  private static final String REST_RESOURCE_URI_TEMPLATE_PARAMETER_NAME = "uriTemplate";
  private static final Map<String, Function<Annotation, String>> ANNOTATION_NAME_TO_TSDOC_TAG_RENDERER = new HashMap<String, Function<Annotation, String>>() {{
    put(Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME, annotation -> "\n * @public");
    put(Jooc.DEPRECATED_ANNOTATION_NAME, annotation -> "\n * @deprecated" + renderDeprecatedParameters(annotation));
  }};

  private static String renderDeprecatedParameters(Annotation annotation) {
    List<String> parts = new ArrayList<>();
    Map<String, Object> propertiesByName = annotation.getPropertiesByName();
    Object since = propertiesByName.get("since");
    if (since instanceof String) {
      parts.add(" since " + since);
    }
    Object replacements = propertiesByName.get("replacement");
    if (replacements instanceof String) {
      parts.add(" Use {@link " + replacements + "} instead.");
    }
    return String.join(".", parts);
  }

  public static boolean generatesCode(IdeDeclaration primaryDeclaration) {
    // generate TypeScript for almost everything *except* some built-in classes which would fail to compile
    // and [Mixin] interfaces:
    return !TYPESCRIPT_BUILT_IN_TYPES.contains(primaryDeclaration.getQualifiedNameStr())
            && primaryDeclaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME) == null;
  }

  private final TypeScriptModuleResolver typeScriptModuleResolver;
  private CompilationUnit compilationUnit;
  private Map<String, String> imports;
  private boolean companionInterfaceMode;
  private boolean needsCompanionInterface;
  private List<ClassDeclaration> mixinClasses;

  TypeScriptCodeGenerator(TypeScriptModuleResolver typeScriptModuleResolver, JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
    this.typeScriptModuleResolver = typeScriptModuleResolver;
  }

  @Override
  void visitDeclarationAnnotationsAndModifiers(IdeDeclaration declaration) throws IOException {
    List<Annotation> annotations = declaration.getAnnotations();
    /* ASDoc comments may come before annotations, after annotations, or even *both*.
     * The latter case is only for annotations like [Event] that have their own ASDoc.
     * Whitespace-only "whitespace" may be ignored, but *not* the initial one if no
     * other whitespace follows.
     */
    List<JooSymbol> whitespaceSymbols = new ArrayList<>();
    List<Annotation> tsdocTags = new ArrayList<>();
    for (Annotation annotation : annotations) {
      whitespaceSymbols.add(annotation.getSymbol());
      if (ANNOTATION_NAME_TO_TSDOC_TAG_RENDERER.containsKey(annotation.getMetaName())) {
        tsdocTags.add(annotation);
      }
    }
    Collections.addAll(whitespaceSymbols, declaration.getSymModifiers());
    whitespaceSymbols.add(declaration.getDeclarationSymbol());
    whitespaceSymbols.add(declaration.getIde().getSymbol());
    if (!tsdocTags.isEmpty()) {
      String tsDoc = toTsdoc(tsdocTags);
      int lastSymbolWithASDocIndex;
      String newWhitespace;
      // find last symbol with ASDoc (if any):
      JooSymbol lastSymbolWithASDoc = whitespaceSymbols.stream()
              .reduce(null, (symbolWithASDoc, currentSymbol) -> containsASDoc(currentSymbol) ? currentSymbol : symbolWithASDoc);
      if (lastSymbolWithASDoc == null) {
        lastSymbolWithASDocIndex = whitespaceSymbols.size();
        newWhitespace = "/**" + tsDoc + "\n */\n";
      } else {
        String whitespace = lastSymbolWithASDoc.getWhitespace();
        Matcher matcher = Pattern.compile("\n? *\\*/").matcher(whitespace);
        if (!matcher.find()) {
          throw new CompilerError(declaration.getSymbol(), "Internal error: End of ASDoc not found.");
        }
        StringBuffer builder = new StringBuffer();
        matcher.appendReplacement(builder, tsDoc + Matcher.quoteReplacement(matcher.group()));
        newWhitespace = matcher.appendTail(builder).toString();
        // To not modify the AST, in the whitespace symbols list, replace the symbol containing the ASDoc
        // by a new one with the modified ASDoc:
        out.suppressWhitespace(lastSymbolWithASDoc);
        lastSymbolWithASDocIndex = whitespaceSymbols.indexOf(lastSymbolWithASDoc);
        whitespaceSymbols.remove(lastSymbolWithASDoc);
      }
      whitespaceSymbols.add(lastSymbolWithASDocIndex,
              new JooSymbol(sym.SEMICOLON, "", -1, -1, newWhitespace, ""));
    }
    out.writeNonTrivialWhitespace(whitespaceSymbols);

    writeModifiers(declaration);
    if (declaration.isPrimaryDeclaration()) {
      Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotation != null && !isInterface(declaration)) {
        if (typeScriptModuleResolver.getNativeAnnotationRequireValue(nativeAnnotation) == null
                && declaration.getTargetQualifiedNameStr().contains(".")) {
          out.writeToken("export");
        } else {
          out.writeToken("declare");
        }
      }
    }
  }

  private static String toTsdoc(List<Annotation> tsdocTags) {
    return tsdocTags.stream()
            .map(tsdocTag -> ANNOTATION_NAME_TO_TSDOC_TAG_RENDERER.get(tsdocTag.getMetaName()).apply(tsdocTag))
            .collect(Collectors.joining());
  }

  @Override
  protected void writeModifiers(IdeDeclaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration.isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      if (!isPrimaryDeclaration && !companionInterfaceMode) {
        if (modifier.sym == sym.PROTECTED
                || modifier.sym == sym.IDE &&
                (SyntacticKeywords.STATIC.equals(modifier.getText())
//                        || SyntacticKeywords.OVERRIDE.equals(modifier.getText())
                )) {
          out.writeSymbol(modifier);
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
    String targetQualifiedNameStr = primaryDeclaration.getTargetQualifiedNameStr();
    String primaryLocalName = CompilerUtils.className(targetQualifiedNameStr);
    imports.put(primaryDeclaration.getQualifiedNameStr(), primaryLocalName);

    out.writeSymbolWhitespace(compilationUnit.getPackageDeclaration().getSymbol());

    if (!getMetadata(primaryDeclaration).isEmpty()) {
      compilationUnit.addBuiltInIdentifierUsage("metadata");
    }
    if (primaryDeclaration instanceof VariableDeclaration
            && isLazy((VariableDeclaration) primaryDeclaration)) {
      compilationUnit.addBuiltInIdentifierUsage(getLazyFactoryFunctionName((VariableDeclaration) primaryDeclaration));
    }

    if (!compilationUnit.getUsedBuiltInIdentifiers().isEmpty()) {
      out.write(String.format("import { %s } from \"@jangaroo/runtime/AS3\";\n",
              String.join(", ", compilationUnit.getUsedBuiltInIdentifiers())));
    }

    boolean isModule = typeScriptModuleResolver.getRequireModuleName(compilationUnit, primaryDeclaration) != null;
    String targetNamespace = null;
    if (!isModule) {
      targetNamespace = CompilerUtils.packageName(targetQualifiedNameStr);
      // if global namespace, simply leave it out
      if (!targetNamespace.isEmpty()) {
        out.writeToken("declare namespace");
        out.writeToken(targetNamespace);
        out.writeSymbol(compilationUnit.getLBrace());
      }
    }

    Set<String> localNames = new HashSet<>();
    localNames.add(primaryLocalName);

    // generate imports
    // first pass: detect import local name clashes:
    Set<String> localNameClashes = new HashSet<>();
    for (String dependentCUId : compilationUnit.getCompileDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      if (typeScriptModuleResolver.getRequireModuleName(compilationUnit, dependentCompilationUnitModel.getPrimaryDeclaration()) != null ||
              !dependentCompilationUnitModel.getPrimaryDeclaration().getTargetQualifiedNameStr().contains(".")) {
        String localName = typeScriptModuleResolver.getDefaultImportName(dependentCompilationUnitModel.getPrimaryDeclaration());
        localName = localName.split("\\.")[0]; // may be a native fully qualified name which "occupies" its first namespace!
        if (!localNames.add(localName)) {
          localNameClashes.add(localName);
        }
      }
    }

    // second pass: generate imports, using fully-qualified names for local name clashes:
    Map<String, String> moduleNameToLocalName = new TreeMap<>();
    for (String dependentCUId : compilationUnit.getCompileDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      IdeDeclaration dependentPrimaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = typeScriptModuleResolver.getRequireModuleName(compilationUnit, dependentPrimaryDeclaration);
      String localName;
      if (requireModuleName == null) {
        localName = TypeScriptModuleResolver.getNonRequireNativeName(dependentPrimaryDeclaration);
      } else {
        if (!isModule) {
          // import from non-module to module must be inlined:
          localName = String.format("import(\"%s\").default", requireModuleName);
        } else {
          // for rare occasions that two ActionScript identifiers map to the same JavaScript/TypeScript name,
          // do not import twice, but reuse the previous local name, found by the module name:
          localName = moduleNameToLocalName.get(requireModuleName);
          if (localName == null) {
            localName = typeScriptModuleResolver.getDefaultImportName(dependentPrimaryDeclaration);
            if (localNameClashes.contains(localName)) {
              // resolve name clashes by using transformed fully-qualified name ('.' -> '_'):
              localName = TypeScriptModuleResolver.toLocalName(dependentPrimaryDeclaration.getQualifiedName());
            }
            moduleNameToLocalName.put(requireModuleName, localName);
          }
        }
      }
      imports.put(dependentPrimaryDeclaration.getQualifiedNameStr(), localName);
    }

    // now generate the import directives:
    for (Map.Entry<String, String> importEntry : moduleNameToLocalName.entrySet()) {
      out.write(String.format("import %s from \"%s\";\n", importEntry.getValue(), importEntry.getKey()));
    }

    primaryDeclaration.visit(this);

    if (isModule) {
      if (!isPropertiesSubclass(primaryDeclaration)) {
        out.write("\nexport default " + primaryLocalName + ";\n");
      }
    } else if (!targetNamespace.isEmpty()) {
      // close namespace:
      out.writeSymbol(compilationUnit.getRBrace());
      out.write("\n");
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    if (isPropertiesClass(classDeclaration)) {
      visitPropertiesClassDeclaration(classDeclaration);
      return;
    }

    needsCompanionInterface = false;
    List<Ide> mixins = new ArrayList<>();
    mixinClasses = new ArrayList<>();
    String classDeclarationLocalName = compilationUnitAccessCode(classDeclaration);

    List<String> configMixins = new ArrayList<>();
    List<Ide> realInterfaces = new ArrayList<>();
    if (classDeclaration.getOptImplements() != null) {
      CommaSeparatedList<Ide> superTypes = classDeclaration.getOptImplements().getSuperTypes();
      do {
        ClassDeclaration maybeMixinDeclaration = (ClassDeclaration) superTypes.getHead().getDeclaration(false);
        CompilationUnit mixinCompilationUnit = CompilationUnit.getMixinCompilationUnit(maybeMixinDeclaration);
        if (mixinCompilationUnit != null
                && mixinCompilationUnit != compilationUnit) { // prevent circular inheritance between mixin and its own interface!
          mixinClasses.add(maybeMixinDeclaration);
          mixins.add(superTypes.getHead());
          if (maybeMixinDeclaration.hasConfigClass()) {
            configMixins.add(compilationUnitAccessCode(maybeMixinDeclaration) + "._");
          }
        } else {
          realInterfaces.add(superTypes.getHead());
        }
        superTypes = superTypes.getTail();
      } while (superTypes != null);
    }

    boolean hasConfigClass = classDeclaration.hasConfigClass();
    if (hasConfigClass) {
      List<String> configExtends = new ArrayList<>();
      ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
      if (superTypeDeclaration != null && superTypeDeclaration.hasConfigClass()) {
        configExtends.add(compilationUnitAccessCode(superTypeDeclaration) + "._");
      }
      configExtends.addAll(configMixins);
      List<TypedIdeDeclaration> configs = classDeclaration.getMembers().stream()
              .filter(typedIdeDeclaration -> !typedIdeDeclaration.isMixinMemberRedeclaration() && typedIdeDeclaration.isExtConfigOrBindable())
              .collect(Collectors.toList());
      if (!configs.isEmpty()) {
        String configNamesType = configs.stream().map(config -> CompilerUtils.quote(config.getName())).collect(Collectors.joining(" |\n  "));
        configExtends.add(String.format("Partial<Pick<%s,\n  %s\n>>", classDeclarationLocalName, configNamesType));
      }
      out.write(String.format("interface %s_%s {\n}\n\n", classDeclarationLocalName, configExtends.isEmpty() ? "" : " extends " + String.join(", ", configExtends)));
    }

    ClassDeclaration myMixinInterface = classDeclaration.getMyMixinInterface();
    if (myMixinInterface != null) {
      JooSymbol myMixinSymbolWithASDoc = findSymbolWithASDoc(myMixinInterface);
      if (myMixinSymbolWithASDoc != null) {
        out.writeSymbolWhitespace(myMixinSymbolWithASDoc);
        JooSymbol classSymbolWithASDoc = findSymbolWithASDoc(classDeclaration);
        if (classSymbolWithASDoc != null) {
          out.suppressWhitespace(classSymbolWithASDoc);
        }
      }
    }
    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    if (isAmbientInterface(classDeclaration.getCompilationUnit())) {
      out.writeToken("interface");
    } else {
      if (classDeclaration.isInterface()) {
        out.writeToken("abstract");
      }
      out.writeToken("class");
    }
    writeSymbolReplacement(classDeclaration.getIde().getSymbol(), getLocalName(classDeclaration, false));
    visitIfNotNull(classDeclaration.getOptExtends());

    if (classDeclaration.getOptImplements() != null) {
      JooSymbol extendsOrImplements;
      if (classDeclaration.isInterface() && classDeclaration.getOptImplements().getSuperTypes().getTail() != null) {
        extendsOrImplements = new JooSymbol("implements");
        needsCompanionInterface = true;
        mixins = realInterfaces;
      } else {
        extendsOrImplements = classDeclaration.getOptImplements().getSymImplements();
      }
      visitImplementsFiltered(
              extendsOrImplements,
              classDeclaration.getOptImplements(),
              realInterfaces);
    }

    classDeclaration.getBody().visit(this);

    if (needsCompanionInterface) {
      out.write("\ninterface " + classDeclarationLocalName);
      // output "extends [Required<...Configs>,] [<mixin-interfaces>]"
      visitImplementsFiltered(
              new JooSymbol("extends"),
              classDeclaration.getOptImplements(),
              mixins);
      // visit class body again in "companion interface" mode
      companionInterfaceMode = true;
      classDeclaration.getBody().visit(this);
      companionInterfaceMode = false;
      out.write("\n");
    }

    generateClassMetadata(classDeclaration);

    if (classDeclaration.isPrimaryDeclaration()) {
      visitAll(classDeclaration.getSecondaryDeclarations());

      if (hasConfigClass) {
        out.write(String.format("\ndeclare namespace %s {\n", classDeclarationLocalName));
        out.write(String.format("  export type _ = %s_;\n", classDeclarationLocalName));
        out.write("  export const _: { new(config?: _): _; };\n");
        out.write("}\n\n");
      }
    }
  }

  private static List<Annotation> getMetadata(IdeDeclaration declaration) {
    // use all runtime retention metadata, but not [RestResource], which gets special treatment:
    return declaration.getAnnotations().stream()
            .filter(annotation ->
                    !REST_RESOURCE_ANNOTATION_NAME.equals(annotation.getMetaName()) &&
                    !Jooc.ANNOTATIONS_FOR_COMPILER_ONLY.contains(annotation.getMetaName()))
            .collect(Collectors.toList());
  }

  private void generateClassMetadata(ClassDeclaration classDeclaration) throws IOException {
    String classDeclarationLocalName = compilationUnitAccessCode(classDeclaration);
    if (classDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null) {
      if (classDeclaration.getOptImplements() != null
              && (!classDeclaration.isInterface() || classDeclaration.getOptImplements().getSuperTypes().getTail() != null)) {
        CommaSeparatedList<Ide> superTypes = classDeclaration.getOptImplements().getSuperTypes();
        boolean foundNonMixinInterface = false;
        while (superTypes != null) {
          if (!isCurrentMixinInterface(superTypes.getHead())) {
            if (!foundNonMixinInterface) {
              out.write("\nmixin(" + classDeclarationLocalName);
              foundNonMixinInterface = true;
            }
            out.write(", ");
            superTypes.getHead().visit(this);
          }
          superTypes = superTypes.getTail();
        }
        if (foundNonMixinInterface) {
          out.write(");\n");
        }
      }

      List<Annotation> metadata = getMetadata(classDeclaration);
      if (!metadata.isEmpty()) {
        out.write("\nmetadata(" + classDeclarationLocalName + ", [");
        boolean firstAnnotation = true;
        for (Annotation runtimeAnnotation : metadata) {
          if (firstAnnotation) {
            firstAnnotation = false;
          } else {
            out.write(",\n    ");
          }
          out.write(CompilerUtils.quote(runtimeAnnotation.getMetaName()));
          CommaSeparatedList<AnnotationParameter> annotationParameters = runtimeAnnotation.getOptAnnotationParameters();
          if (annotationParameters != null) {
            out.write(", {");
            boolean firstParameter = true;
            while (annotationParameters != null) {
              AnnotationParameter annotationParameter = annotationParameters.getHead();
              if (firstParameter) {
                firstParameter = false;
              } else {
                out.write(", ");
              }
              visitIfNotNull(annotationParameter.getOptName(), "\"\"");
              out.write(": ");
              visitIfNotNull(annotationParameter.getValue(), "true");
              annotationParameters = annotationParameters.getTail();
            }
            out.write("}");
          }
          out.write("]");
        }
        out.write(");\n");
      }
    }
  }

  private void visitPropertiesClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbolWhitespace(classDeclaration.getSymClass());
    FunctionDeclaration constructorDeclaration = classDeclaration.getConstructor();
    List<AssignmentOpExpr> propertyAssignments = getPropertiesClassAssignments(constructorDeclaration, true, true);
    if (isPropertiesSubclass(classDeclaration)) {
      out.write("ResourceBundleUtil.override(" + compilationUnitAccessCode(classDeclaration.getSuperTypeDeclaration()) + ", {");
      renderPropertiesClassValues(propertyAssignments, true, false, false);
      out.write("\n});\n");
    } else {
      String classDeclarationLocalName = getLocalName(classDeclaration, false);
      out.write("interface " + classDeclarationLocalName + " {");
      for (AssignmentOpExpr propertyAssignment : propertyAssignments) {
        AstNode index = getObjectAndProperty(propertyAssignment).getValue();
        if (index instanceof Ide && ((Ide) index).getDeclaration(false) != null) {
          IdeDeclaration declaration = ((Ide) index).getDeclaration();
          out.writeSymbolWhitespace(declaration.getSymbol());
          out.write("  ");
        } else {
          out.writeSymbolWhitespace(propertyAssignment.getSymbol());
        }
        index.visit(this);
        out.writeToken(": string;");
      }
      out.write("\n}");
      TypedIdeDeclaration instanceDeclaration = classDeclaration.getStaticMemberDeclaration(PROPERTY_CLASS_INSTANCE);
      out.writeSymbolWhitespace(instanceDeclaration != null ? instanceDeclaration.getSymbol()
              : constructorDeclaration != null ? constructorDeclaration.getSymbol() : new JooSymbol("\n"));
      out.write(String.format("const %s: %s = {", classDeclarationLocalName, classDeclarationLocalName));
      renderPropertiesClassValues(propertyAssignments, false, false, false);
      out.write("\n};\n");
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isCurrentMixinInterface(Ide head) {
    return CompilationUnit.mapMixinInterface(head.getDeclaration().getCompilationUnit()).equals(compilationUnit);
  }

  private void visitImplementsFiltered(JooSymbol symImplementsOrExtends,
                                       Implements optImplements,
                                       List<Ide> filter) throws IOException {
    JooSymbol lastSym = symImplementsOrExtends;
    if (optImplements != null) {
      CommaSeparatedList<Ide> current = optImplements.getSuperTypes();
      do {
        Ide head = current.getHead();
        // head must be included in filter and
        // must not be a mixin class's mixin interface:
        if (filter.contains(head) && !isCurrentMixinInterface(head)) {
          out.writeSymbol(lastSym);
          lastSym = current.getSymComma();
          head.visit(this);
        }
        current = current.getTail();
      } while (current != null);
    }
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    AstNode parentNode = typeRelation.getParentNode();
    if (parentNode instanceof FunctionExpr) {
      parentNode = parentNode.getParentNode();
    }
    if (parentNode instanceof IdeDeclaration) {
      out.writeSymbol(typeRelation.getSymbol());
      ExpressionType expressionType = ((IdeDeclaration) parentNode).getType();
      // a non-getter-setter function declaration returns its function signature, but we are just interested
      // in its return value, which is contained in the type parameter:
      if (expressionType instanceof FunctionSignature) {
        expressionType = expressionType.getTypeParameter();
      }
      String tsType = getTypeScriptTypeForActionScriptType(expressionType);
      if ("any".equals(tsType)
              && parentNode instanceof VariableDeclaration
              && hasObjectLiteralInitializer((VariableDeclaration) parentNode)) {
        tsType = "Record<string,any>";
      }
      writeSymbolReplacement(typeRelation.getType().getSymbol(), tsType);
    } else {
      super.visitTypeRelation(typeRelation);
    }
  }

  private boolean hasObjectLiteralInitializer(VariableDeclaration variableDeclaration) {
    return variableDeclaration.getOptInitializer() != null
            && variableDeclaration.getOptInitializer().getValue() instanceof ObjectLiteral;
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
        String qualifiedNameStr = declaration.getQualifiedNameStr();
        if (as3Type.name.equals(qualifiedNameStr)) {
          // it is really "Object", use TypeScript "any":
          return "any";
        }
        // special case: built-in TypeScript 'Promise' type needs a type parameter:
        if ("js.Promise".equals(qualifiedNameStr)) {
          return "Promise<any>";
        }
        // special case: built-in TypeScript 'Map' type needs type parameters:
        if ("js.Map".equals(qualifiedNameStr)) {
          return "Map<any, any>";
        }
        // use class name:
        String tsType = getLocalName(declaration, true);
        return expressionType.isConfigType() ? tsType + "._" : tsType;
      case ANY:
        return "any";
      case VECTOR:
      case ARRAY:
        return "Array<" + getTypeScriptTypeForActionScriptType(expressionType.getTypeParameter()) + ">";
      case UINT:
      case INT:
        // in non-modules, these may be mapped to import('...').default:
        return imports.get(expressionType.getDeclaration().getQualifiedNameStr());
      case BOOLEAN:
      case NUMBER:
      case STRING:
        return as3Type.name.toLowerCase();
      case FUNCTION:
        return "AnyFunction";
    }
    return as3Type.name;
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymRest());
    parameter.getIde().visit(this);
    Initializer initializer = parameter.getOptInitializer();
    boolean isOptional = initializer != null && (isAmbientOrInterface(compilationUnit) || isUndefined(initializer.getValue()));
    if (isOptional) {
      out.write("?");
    }
    visitParameterTypeRelation(parameter);
    if (initializer != null && !isOptional) {
        initializer.visit(this);
    }
  }

  private static boolean isUndefined(Expr expr) {
     return expr instanceof IdeExpr && "undefined".equals(((IdeExpr) expr).getIde().getName());
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isCompilationUnitAmbient() {
    return isAmbient(compilationUnit);
  }

  private static boolean isAmbientOrInterface(CompilationUnit compilationUnit) {
    return isAmbient(compilationUnit)
            || isInterface(compilationUnit.getPrimaryDeclaration());
  }

  private static boolean isAmbientInterface(CompilationUnit compilationUnit) {
    return isAmbient(compilationUnit)
            && isInterface(compilationUnit.getPrimaryDeclaration());
  }

  private static boolean isNonAmbientInterface(CompilationUnit compilationUnit) {
    return !isAmbient(compilationUnit)
            && isInterface(compilationUnit.getPrimaryDeclaration());
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
  protected void visitObjectFieldValue(ObjectField objectField) throws IOException {
    JooSymbol symbol = objectField.getValue().getSymbol();
    if (Pattern.matches("[\\s]+", symbol.getWhitespace())) {
      out.suppressWhitespace(symbol);
      out.write(" ");
    }
    super.visitObjectFieldValue(objectField);
  }


  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    vectorLiteral.getArrayLiteral().visit(this);
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) {
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
    if (Jooc.ANNOTATIONS_FOR_COMPILER_ONLY.contains(annotation.getMetaName())) {
      out.writeSymbolWhitespace(annotation.getSymbol());
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
    if (companionInterfaceMode) {
      return;
    }
    if (variableDeclaration.isClassMember()) {
      ClassDeclaration classDeclaration = variableDeclaration.getClassDeclaration();
      if (isNonAmbientInterface(classDeclaration.getCompilationUnit())) {
        out.writeSymbolWhitespace(variableDeclaration.getSymbol());
        out.writeToken("abstract");
      }
      boolean bindable = variableDeclaration.isBindable();
      for (VariableDeclaration currentVariableDeclaration = variableDeclaration;
           currentVariableDeclaration != null;
           currentVariableDeclaration = currentVariableDeclaration.getOptNextVariableDeclaration()) {
        if (bindable) {
          // we want the ASDoc at the generated accessor, so render (private) field first:
          out.write("\n\n ");
          visitVariableDeclarationBase(currentVariableDeclaration);
          writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");

          // generate accessors (field has been transformed to #-private)
          String accessor = currentVariableDeclaration.getName();

          visitDeclarationAnnotationsAndModifiers(variableDeclaration);
          out.write(String.format("get %s()", accessor));
          visitIfNotNull(currentVariableDeclaration.getOptTypeRelation());
          out.write(String.format(" { return this.#%s; }", accessor));

          // generate a set accessor, but only if no custom set...() method exists:
          String setMethodName = MethodType.SET + MxmlUtils.capitalize(accessor);
          TypedIdeDeclaration setMethodDeclaration = classDeclaration.getMemberDeclaration(setMethodName);
          if (setMethodDeclaration == null || setMethodDeclaration.isPrivate()) {
            out.write("\n  ");
            out.write(String.format("set %s(value", accessor));
            visitIfNotNull(currentVariableDeclaration.getOptTypeRelation());
            out.write(String.format(") { this.#%s = value; }", accessor));
          }
        } else {
          if (currentVariableDeclaration == variableDeclaration) {
            visitDeclarationAnnotationsAndModifiers(variableDeclaration);
          } else {
            // re-render annotations:
            visitAll(variableDeclaration.getAnnotations());
            // pull ide's white-space before the modifiers, as declarations are white-space sensitive:
            out.writeSymbolWhitespace(currentVariableDeclaration.getIde().getSymbol());
            // re-render modifiers:
            writeModifiers(variableDeclaration);
          }
          // for class members, leave out "var", replace "const" by "readonly":
          if (variableDeclaration.isConst()) {
            writeReadonlySuppressWhitespace(currentVariableDeclaration.getIde().getSymbol());
          }
          visitVariableDeclarationBase(currentVariableDeclaration);
          writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");
        }
      }
    } else {
      super.visitVariableDeclaration(variableDeclaration);
    }
  }

  @Override
  void writeVarOrConst(VariableDeclaration variableDeclaration) throws IOException {
    if (isPrimaryVariableDeclaration(variableDeclaration)) {
      writeSymbolReplacement(variableDeclaration.getOptSymConstOrVar(), "const");
    } else {
      super.writeVarOrConst(variableDeclaration);
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
      if (variableDeclaration.isPrivate() || variableDeclaration.isBindable()) {
        writeSymbolReplacement(ide.getSymbol(), getHashPrivateName(variableDeclaration));
      } else {
        ide.visit(this);
      }
      visitIfNotNull(typeRelation);
      if (!isAmbientOrInterface(compilationUnit)) {
        generateInitializer(variableDeclaration);
      }
    } else if (isPrimaryVariableDeclaration(variableDeclaration)) {
      // do not rewrite var name (no underscore)!
      writeSymbolReplacement(ide.getSymbol(), CompilerUtils.className(variableDeclaration.getTargetQualifiedNameStr()));
      if (typeRelation != null) {
        out.writeSymbol(typeRelation.getSymRelation());
        // wrap type by simple object:
        out.write("{");
        if (variableDeclaration.isConst()) {
          out.write("readonly ");
        }
        out.write("_: ");
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
        if (isLazy(variableDeclaration)) {
          out.write(getLazyFactoryFunctionName(variableDeclaration) + "(() =>");
        } else {
          out.write("{_: ");
        }
        if (initializer != null) {
          initializer.getValue().visit(this);
        } else {
          out.write(VariableDeclaration.getDefaultValue(typeRelation));
        }
        if (isLazy(variableDeclaration)) {
          out.write(")");
        } else {
          out.write("}");
        }
      }
    } else {
      ide.visit(this);
      if (typeRelation != null) {
        // TypeScript supports type inference! And since the rhs might be changed to a config type,
        // leaving TypeScript to chose the same type leads to better results than to declaring it again!
        // So leave out type declaration if it just re-declares the exact type of the initializer:
        if (initializer == null
                || isAmbientOrInterface(compilationUnit)
                || initializer.getValue().getType() == null
                || initializer.getValue() instanceof ObjectLiteral   // do not suppress type for object literals
                || !initializer.getValue().getType().equals(variableDeclaration.getType())) {
          typeRelation.visit(this);
        }
      }
      visitIfNotNull(initializer);
    }
  }

  // is it a primary modifiable (var, not const) _or_ [Lazy] variable declaration?
  private boolean isPrimaryVariableDeclaration(VariableDeclaration variableDeclaration) {
    return variableDeclaration.isPrimaryDeclaration()
            && (!variableDeclaration.isConst() || isLazy(variableDeclaration))
            && typeScriptModuleResolver.getRequireModuleName(compilationUnit, variableDeclaration) != null;
  }

  private boolean isLazy(VariableDeclaration variableDeclaration) {
    return variableDeclaration.getAnnotation(Jooc.LAZY_ANNOTATION_NAME) != null;
  }

  private String getLazyFactoryFunctionName(VariableDeclaration variableDeclaration) {
    return variableDeclaration.isConst() ? "lazyConst" : "lazyVar";
  }

  private void generateInitializer(VariableDeclaration variableDeclaration) throws IOException {
    Initializer initializer = variableDeclaration.getOptInitializer();
    if (initializer != null) {
      if (!variableDeclaration.isStatic() && initializer.getValue() instanceof FunctionExpr
              && ((FunctionExpr) initializer.getValue()).isThisAliased(true)) {
        out.writeSymbol(initializer.getSymEq());
        out.write(" (this$ =>");
        initializer.getValue().visit(this);
        out.write(")(this)");
      } else {
        initializer.visit(this);
      }
    } else {
      // While AS3 automatically assigns default values to fields, TypeScript/ECMAScript don't,
      // so we have to add an explicit initializer to keep semantics:
      String implicitDefaultValue = VariableDeclaration.getDefaultValue(variableDeclaration.getOptTypeRelation());
      // no need to explicitly set a field to "undefined":
      if (!"undefined".equals(implicitDefaultValue)) {
        out.write(" = " + implicitDefaultValue);
      }
    }
  }

  private String getHashPrivateName(IdeDeclaration varOrFunDeclaration) {
    return "#" + varOrFunDeclaration.getIde().getName();
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    if (!isCompilationUnitAmbient()) {
      super.visitBlockStatement(blockStatement);
    }
  }

  private static final CodeGenerator ALIAS_THIS_CODE_GENERATOR = (out, first) -> out.write("const this$=this;");

  private static final Pattern SET_METHOD_NAME_PATTERN = Pattern.compile("set[A-Z].*");

  private static TypedIdeDeclaration getAccessorNameFromSetMethod(FunctionDeclaration functionDeclaration) {
    if (!functionDeclaration.isPrivate() && functionDeclaration.getParams() != null && functionDeclaration.getParams().getTail() == null) {
      String methodName = functionDeclaration.getName();
      if (SET_METHOD_NAME_PATTERN.matcher(methodName).matches()) {
        String setAccessorName = CompilerUtils.uncapitalize(methodName.substring(3));
        if (setAccessorName != null) {
          TypedIdeDeclaration maybeBindablePropertyDeclaration = functionDeclaration.getClassDeclaration().getMemberDeclaration(setAccessorName);
          if ((maybeBindablePropertyDeclaration instanceof PropertyDeclaration ||
                  maybeBindablePropertyDeclaration instanceof VariableDeclaration) &&
                  maybeBindablePropertyDeclaration.isBindable()) {
            return maybeBindablePropertyDeclaration;
          }
        }
      }
    }
    return null;
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    FunctionExpr functionExpr = functionDeclaration.getFun();
    if (functionDeclaration.isClassMember()) {
      boolean isAmbientOrInterface = isAmbientOrInterface(functionDeclaration.getCompilationUnit());
      boolean convertToProperty = functionDeclaration.isGetterOrSetter() &&
              (functionDeclaration.isNative() && !functionDeclaration.isBindable() || isAmbientOrInterface);
      if (convertToProperty && functionDeclaration.isSetter()) {
        // completely suppress (native) setter class members, they are covered by the writable property declaration
        return;
      }
      // In ActionScript, native declarations often redeclare Mixin methods.
      // Try to find such a declaration in all mixins of this class:
      if (functionDeclaration.isNative()) {
        for (ClassDeclaration mixinClass : mixinClasses) {
          if (mixinClass.resolvePropertyDeclaration(functionDeclaration.getName()) != null) {
            needsCompanionInterface = true;
            return;
          }
        }
      }
      // any other native members in a non-ambient/interface compilation unit are moved
      // to its companion interface declaration:
      boolean renderIntoInterface = !convertToProperty
              && !isAmbientOrInterface
              && functionDeclaration.isNative()
              && !functionDeclaration.isBindable();
      if (renderIntoInterface) {
        needsCompanionInterface = true;
      }
      if (companionInterfaceMode != renderIntoInterface) {
        return;
      }

      ClassDeclaration myMixinInterface = functionDeclaration.getClassDeclaration().getMyMixinInterface();
      if (myMixinInterface != null) {
        // only look for direct interface members:
        IdeDeclaration interfaceMethod = myMixinInterface.getMemberDeclaration(functionDeclaration.getName());
        if (interfaceMethod instanceof PropertyDeclaration) {
          interfaceMethod = ((PropertyDeclaration) interfaceMethod).getAccessor(functionDeclaration.isSetter());
        }
        if (interfaceMethod != null) {
          JooSymbol interfaceSymbolWithASDoc = findSymbolWithASDoc(interfaceMethod);
          if (interfaceSymbolWithASDoc != null) {
            out.writeSymbolWhitespace(interfaceSymbolWithASDoc);
            JooSymbol implMethodSymbolWithASDoc = findSymbolWithASDoc(functionDeclaration);
            if (implMethodSymbolWithASDoc != null) {
              String implMethodWhitespace = implMethodSymbolWithASDoc.getWhitespace();
              if (!(implMethodWhitespace.contains("@inheritDoc") || implMethodWhitespace.contains("@private"))) {
                functionDeclaration.getIde().getScope().getCompiler().getLog().warning(implMethodSymbolWithASDoc,
                        "Mixin method implementation has non-inheriting ASDoc. " +
                                "Please move such documentation to the mixin interface before TypeScript conversion.");
              }
              out.suppressWhitespace(implMethodSymbolWithASDoc);
            }
          }
        }
      }
      TypedIdeDeclaration setAccessor = getAccessorNameFromSetMethod(functionDeclaration);
      if (functionDeclaration.isNative() && functionDeclaration.isBindable() && !companionInterfaceMode && functionDeclaration.isGetter()) {
        // it must be a [Bindable] native get function, so complement a private field for the default implementation:
        out.write("\n  #" + functionDeclaration.getName());
        visitIfNotNull(functionDeclaration.getOptTypeRelation());
        out.write(";\n");
      }
      visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      if (isNonAmbientInterface(functionDeclaration.getClassDeclaration().getCompilationUnit())) {
        out.writeSymbolWhitespace(functionDeclaration.getSymbol());
        out.writeToken("abstract");
      }
      // leave out "function" symbol for class members!
      writeOptSymbolWhitespace(functionDeclaration.getSymbol());
      if (convertToProperty) {
        if (!hasSetter(functionDeclaration)) { // a native getter without setter => readonly property!
          writeReadonlySuppressWhitespace(functionDeclaration.getIde().getSymbol());
        }
      } else if (setAccessor != null) {
        out.writeToken(MethodType.SET.toString());
      } else {
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
      }
      if (functionDeclaration.isConstructor()) {
        writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), "constructor");
      } else {
        if (functionDeclaration.isPrivate()) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), getHashPrivateName(functionDeclaration));
        } else if (setAccessor != null) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), setAccessor.getName());
        } else {
          functionDeclaration.getIde().visit(this);
        }
      }
      if (!convertToProperty) {
        out.writeSymbol(functionExpr.getLParen());
        visitIfNotNull(functionExpr.getParams());
        out.writeSymbol(functionExpr.getRParen());
      }
      // in TypeScript, constructors and setters may not declare a return type, not even "void":
      if (!functionDeclaration.isConstructor() && !functionDeclaration.isSetter() && setAccessor == null) {
        generateFunctionExprReturnTypeRelation(functionExpr);
      }
      if (functionDeclaration.isConstructor()
              && !functionDeclaration.containsSuperConstructorCall()
              && functionDeclaration.getClassDeclaration().getOptExtends() != null) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), (out, first) -> out.write("\n    super();"));
      }
      if (functionDeclaration.isThisAliased(true) && !functionDeclaration.isContainsSuperConstructorCall()) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), ALIAS_THIS_CODE_GENERATOR);
      }

      if (functionDeclaration.isNative() && functionDeclaration.isBindable() && !companionInterfaceMode) {
        // it must be a [Bindable] native, so complement the default implementation:
        if (functionDeclaration.isGetter()) {
          out.write(" { return this.#" + functionDeclaration.getName() + "; }");
        } else {
          out.write(" { this.#" + functionDeclaration.getName() + " = " + functionDeclaration.getParams().getHead().getName() + "; }");
        }
      } else {
        visitIfNotNull(functionExpr.getBody());
        writeOptSymbol(functionDeclaration.getOptSymSemicolon());
      }
    } else {
      if (functionDeclaration.isPrimaryDeclaration()) {
        visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      }
      functionExpr.visit(this);
      writeOptSymbolWhitespace(functionDeclaration.getOptSymSemicolon());
    }
  }

  JooSymbol findSymbolWithASDoc(IdeDeclaration declaration) {
    // look in modifier symbols and the declaration's symbol first, as sometimes, annotations like [Event]
    // have their own ASDoc:
    if (containsASDoc(declaration.getSymbol())) {
      return declaration.getSymbol();
    }
    for (JooSymbol symModifier : declaration.getSymModifiers()) {
      if (containsASDoc(symModifier)) {
        return symModifier;
      }
    }
    for (Annotation annotation : declaration.getAnnotations()) {
      if (containsASDoc(annotation.getSymbol())) {
        return annotation.getSymbol();
      }
    }
    return null;
  }

  private static boolean containsASDoc(JooSymbol symbol) {
    return symbol.getWhitespace().contains("/**");
  }

  void generateFunctionExprReturnTypeRelation(FunctionExpr functionExpr) throws IOException {
    TypeRelation optTypeRelation = functionExpr.getOptTypeRelation();
    String returnTypeFromAnnotation = functionExpr.getReturnTypeFromAnnotation();
    if (returnTypeFromAnnotation != null) {
      writeOptSymbol(optTypeRelation == null ? null : optTypeRelation.getSymRelation(), ": ");
      out.write(returnTypeFromAnnotation);
    } else {
      visitIfNotNull(optTypeRelation);
    }
  }

  protected void visitBlockStatementDirectives(BlockStatement body) throws IOException {
    if (!(body.usesInstanceThis()
            && body.getParentNode() instanceof FunctionExpr
            && body.getParentNode().getParentNode() instanceof FunctionDeclaration
            && ((FunctionDeclaration) body.getParentNode().getParentNode()).containsSuperConstructorCall()
            && ((FunctionDeclaration) body.getParentNode().getParentNode()).getClassDeclaration().notExtendsObject())) {
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
    List<VariableDeclaration> pulledOutVariableDeclarations = new ArrayList<>();
    for (Iterator<Directive> iterator = directivesToWrap.iterator(); iterator.hasNext(); ) {
      Directive directive = iterator.next();
      if (directive instanceof VariableDeclaration) {
        VariableDeclaration variableDeclaration = (VariableDeclaration) directive;
        // Only pull declarations out of directivesToWrap that are used outside directivesToWrap!
        AstNode body = directive.getParentNode();
        if (variableDeclaration.getUsages().stream()
                .anyMatch(usage -> !directivesToWrap.contains(getParentDirective(usage, body)))) {
          pulledOutVariableDeclarations.add(variableDeclaration);

          visitDeclarationAnnotationsAndModifiers(variableDeclaration);
          writeSymbolReplacement(variableDeclaration.getOptSymConstOrVar(), "var");
          variableDeclaration.getIde().visit(this);
          variableDeclaration.getOptTypeRelation().visit(this);
          visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
          writeOptSymbol(variableDeclaration.getOptSymSemicolon());

          if (variableDeclaration.getOptInitializer() == null) {
            iterator.remove();
          }
        }
      }
    }

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
    // declare as immediately-evaluating function (IEF), so that TypeScript does not complain about
    // usage of `this` before calling `super()`:
    out.write("(()");
    if (superCallParams != null && superCallParams.getTail() != null) {
      List<String> types = new ArrayList<>();
      for (CommaSeparatedList<Expr> current = superCallParams; current != null; current = current.getTail()) {
        types.add("any"); // as the corresponding imports would be complicated to generate, we just use "any"
      }
      out.write(":[" + String.join(",", types) + "]");
    }
    out.write("=>");
    if (!directivesToWrap.isEmpty()) {
      out.write("{");
      for (AstNode directive : directivesToWrap) {
        if (directive instanceof VariableDeclaration && pulledOutVariableDeclarations.contains(directive)) {
          renderVariableDeclarationAsAssignment((VariableDeclaration) directive);
        } else {
          directive.visit(this);
        }
      }
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

  private static Directive getParentDirective(IdeExpr usage, AstNode container) {
    AstNode current = usage;
    while (current != null && !container.equals(current.getParentNode())) {
      current = current.getParentNode();
    }
    return current instanceof Directive ? (Directive) current : null;
  }

  private void renderVariableDeclarationAsAssignment(VariableDeclaration variableDeclaration) throws IOException {
    out.write(variableDeclaration.getOptSymConstOrVar().getWhitespace());
    variableDeclaration.getIde().visit(this);
    variableDeclaration.getOptInitializer().visit(this);
    writeOptSymbol(variableDeclaration.getOptSymSemicolon());
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
      if (functionDeclaration != null && functionDeclaration.isThisAliased(true)) {
        ALIAS_THIS_CODE_GENERATOR.generate(out, false);
      }
    }
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    boolean needsParenthesis = false;
    if (functionExpr.rewriteToArrowFunction()) {
      out.writeSymbolWhitespace(functionExpr.getFunSymbol());
      needsParenthesis = functionExpr.getParentNode() instanceof BinaryOpExpr; // TODO: what else?
      if (needsParenthesis) {
        out.write("(");
      }
      out.suppressWhitespace(functionExpr.getLParen());
      // rewrite anonymous function expression that does *not* use "this" to arrow function:
      generateFunctionExprSignature(functionExpr);
      out.write(" =>");
      // special case: function body consists of exactly one statement, a return <expr>
      // then just render this expression:
      List<Directive> statements = functionExpr.getBody().getDirectives();
      if (statements.size() == 1) {
        Expr returnExpr;
        ReturnStatement returnStatement = null;
        Directive firstStatement = statements.get(0);
        if (firstStatement instanceof ReturnStatement) {
          returnStatement = (ReturnStatement) firstStatement;
          returnExpr = returnStatement.getOptExpr();
          if (returnExpr == null) {
            // a sole return without any value should be a rare case, but who knows:
            returnExpr = new IdeExpr(new JooSymbol(sym.IDE, "undefined"));
          }
        } else {
          // check whether a void expression is evaluated, then "return" it to avoid the block:
          returnExpr = getExpressionIfVoid(firstStatement);
        }
        if (returnExpr != null) {
          out.writeSymbolWhitespace(functionExpr.getBody().getLBrace());
          if (returnStatement != null) {
            out.writeSymbolWhitespace(returnStatement.getSymKeyword());
          }
          boolean needsInnerParenthesis = returnExpr instanceof ObjectLiteral;
          if (needsInnerParenthesis) {
            out.writeSymbolWhitespace(returnExpr.getSymbol());
            out.write("(");
          }
          returnExpr.visit(this);
          if (needsInnerParenthesis) {
            out.write(")");
          }
          out.writeSymbolWhitespace(functionExpr.getBody().getRBrace());
          if (needsParenthesis) {
            out.write(")");
          }
          return;
        }
      }
    } else {
      out.writeSymbol(functionExpr.getSymFunction());
      visitIfNotNull(functionExpr.getIde());
      generateFunctionExprSignature(functionExpr);
    }
    visitIfNotNull(functionExpr.getBody());
    if (needsParenthesis) {
      out.write(")");
    }
  }

  private Expr getExpressionIfVoid(Directive firstStatement) {
    if (firstStatement.getClass().equals(SemicolonTerminatedStatement.class)) {
      AstNode statement = ((SemicolonTerminatedStatement) firstStatement).getOptStatement();
      if (statement instanceof Expr) {
        Expr expr = (Expr) statement;
        if (expr.isOfAS3Type(AS3Type.VOID)) {
          return expr;
        }
      }
    }
    return null;
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (applyExpr.isTypeCheckObjectLiteralFunctionCall()) {
      // it is an object literal type check call: transform
      // __typeCheckObjectLiteral__(type, { O }) -> AS3._<type>({ O })
      CommaSeparatedList<Expr> typeAndObjectLiteral = args.getExpr();
      Expr typeExpr = typeAndObjectLiteral.getHead();
      Expr objectLiteral = typeAndObjectLiteral.getTail().getHead();
      ExpressionType typeParameter = typeExpr.getType().getTypeParameter();
      if (!renderSingleSpreadValue(objectLiteral, typeParameter)) {
        writeSymbolReplacement(applyExpr.getSymbol(), "_");
        out.writeToken("<");
        out.write(getTypeScriptTypeForActionScriptType(typeParameter));
        out.writeToken(">");
        out.writeSymbol(args.getLParen());
        objectLiteral.visit(this);
        out.writeSymbol(args.getRParen());
      }
    } else if (applyExpr.isTypeCast()) {
      IdeDeclaration declaration = ((IdeExpr) applyExpr.getFun()).getIde().getDeclaration();
      if (declaration instanceof ClassDeclaration && ((ClassDeclaration) declaration).hasConfigClass()) {
        if (isOfConfigType(args.getExpr().getHead())) {
          // use config factory function instead of the class itself:
          writeSymbolReplacement(applyExpr.getSymbol(), "new " + compilationUnitAccessCode(declaration) + "._");
          out.writeSymbol(args.getLParen());
          if (!renderSingleSpreadValue(args.getExpr().getHead(), applyExpr.getFun().getType().getTypeParameter())) {
            args.getExpr().getHead().visit(this);
          }
          out.writeSymbol(args.getRParen());
          return;
        }
      }
      out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
      out.writeToken(builtInIdentifierCode("cast"));
      out.writeSymbol(args.getLParen());
      applyExpr.getFun().visit(this);
      out.writeToken(",");
      args.getExpr().visit(this);
      out.writeSymbol(args.getRParen());
    } else if (isIResourceManager_getString(applyExpr)) {
      out.writeSymbolWhitespace(applyExpr.getSymbol());
      CommaSeparatedList<Expr> argExpressions = args.getExpr();
      ClassDeclaration propertiesClass = applyExpr.getPropertiesClass(argExpressions.getHead());
      out.write(compilationUnitAccessCode(propertiesClass));
      Expr propertyKey = argExpressions.getTail().getHead();
      String key = null;
      if (propertyKey instanceof LiteralExpr) {
        Object jooValue = propertyKey.getSymbol().getJooValue();
        if (jooValue instanceof String && Ide.isValidIdentifier((String) jooValue)) {
          key = (String) jooValue;
        }
      }
      if (key != null) {
        out.write("." + key);
      } else {
        out.writeToken("[");
        out.suppressWhitespace(propertyKey.getSymbol());
        propertyKey.visit(this);
        out.writeToken("]");
      }
    } else if (args != null &&
            args.getExpr() != null &&
            args.getExpr().getTail() == null &&
            isApiCall(applyExpr, NET_JANGAROO_EXT_EXML, AS_STRING, true) &&
            args.getExpr().getHead().isOfAS3Type(AS3Type.STRING)) {
      // suppress obsolete net.jangaroo.ext.Exml.asString(<someString>):
      args.getExpr().getHead().visit(this);
    } else if (args != null) {
      // check for set-Method call (within current class) that is really a Config write:
      CommaSeparatedList<Expr> expr = args.getExpr();
      if (expr != null && expr.getTail() == null) {
        Expr fun = applyExpr.getFun();
        if (fun instanceof IdeExpr) {
          fun = ((IdeExpr) fun).getNormalizedExpr();
        }
        if (fun instanceof DotExpr) {
          DotExpr dotExpr = (DotExpr) fun;
          IdeDeclaration declaration = dotExpr.getIde().getDeclaration(false);
          if (declaration instanceof FunctionDeclaration &&
                  compilationUnit.getPrimaryDeclaration().equals(declaration.getClassDeclaration())) {
            TypedIdeDeclaration accessor = getAccessorNameFromSetMethod((FunctionDeclaration) declaration);
            if (accessor != null) {
              // rewrite obj.setFoo(value) to obj.foo = value:
              if (!isBindableWithoutAccessor(accessor)) {
                dotExpr.getArg().visit(this);
              } else {
                // rewrite obj to asConfig(obj):
                out.writeSymbolWhitespace(applyExpr.getSymbol());
                out.write("asConfig");
                out.writeSymbol(args.getLParen());
                dotExpr.getArg().visit(this);
                out.writeSymbol(args.getRParen());
              }
              out.writeSymbol(dotExpr.getOp());
              writeSymbolReplacement(dotExpr.getIde().getSymbol(), accessor.getName());
              out.write(" = ");
              expr.getHead().visit(this);
              return;
            }
          }
        }
      }
      super.visitApplyExpr(applyExpr);
    }
  }

  @Override
  boolean renderSingleSpreadValue(Expr argument, ExpressionType parameterType) throws IOException {
    // We use nested object literals + spread operator for assigning untyped Config properties,
    // but the special case that there are _only_ untyped properties results in an outer object
    // that only contains _one_ spread inner object ({...{ untyped: "foo"}}), and that does _not_
    // prevent the type error as originally intended. It seems TypeScript cannot accurately type
    // spread expression, _only_ the special case { ...T } => T.
    // Thus, Config objects with _only_ untyped properties must be represent differently. We chose
    // to use a type assertion on the object literal, which, in contrast to a typed function call
    // parameter, allows additional untyped properties. So
    //   <Foo u:untyped="foo"/>
    // becomes
    //   new Foo(<Foo._>{ untyped: "foo" })

    // If the parameter has a type and the argument is an object literal...
    if (parameterType != null && argument instanceof ObjectLiteral) {
      ObjectLiteral objectLiteral = (ObjectLiteral) argument;
      CommaSeparatedList<ObjectFieldOrSpread> fields = objectLiteral.getFields();
      // ...and the argument object literal only consists of one spread...
      if (fields != null && fields.getTail() == null && fields.getHead() instanceof Spread) {
        // Skip the outer, obsolete object literal, in other words, visit only the inner object:
        writeOptSymbolWhitespace(objectLiteral.getSymbol());
        ((Spread) fields.getHead()).getArg().visit(this);
        // ...and insert a type assertion to match the parameter type:
        out.write(" as " + getTypeScriptTypeForActionScriptType(parameterType));
        return true;
      }
    }
    return false;
  }

  private static boolean isApiCall(ApplyExpr applyExpr, String qualifiedClassName, String methodName, boolean isStatic) {
    Expr fun = applyExpr.getFun();
    if (fun instanceof IdeExpr) {
      fun = ((IdeExpr) fun).getNormalizedExpr();
    }
    if (fun instanceof DotExpr && methodName.equals(((DotExpr) fun).getIde().getName())) {
      ExpressionType type = ((DotExpr) fun).getArg().getType();
      if (isStatic) {
        type = type.getTypeParameter();
      }
      return type != null && qualifiedClassName.equals(type.getDeclaration().getQualifiedNameStr());
    }
    return false;
  }

  private static boolean isIResourceManager_getString(ApplyExpr applyExpr) {
    if (isApiCall(applyExpr, I_RESOURCE_MANAGER_QUALIFIED_NAME, GET_STRING_METHOD_NAME, false)) {
      CommaSeparatedList<Expr> argsExpressions = applyExpr.getArgs().getExpr();
      return argsExpressions != null && argsExpressions.getTail() != null && argsExpressions.getTail().getTail() == null // call uses exactly 2 arguments
              && applyExpr.getPropertiesClass(argsExpressions.getHead()) != null; // bundle name resolves to properties class
    }
    return false;
  }

  private static boolean isOfConfigType(Expr expr) {
    if (expr instanceof ApplyExpr && ((ApplyExpr) expr).isTypeCheckObjectLiteralFunctionCall()) {
      expr = ((ApplyExpr) expr).getArgs().getExpr().getTail().getHead();
    }
    // for the following conditions, the type cast is rewritten to the config class:
    return expr instanceof ObjectLiteral  // an object literal can only be cast into a config type
            || expr.getType() != null && expr.getType().isConfigType() // argument has a config type
            // Ext.apply() hands-through its argument type(s), so if any is of config type, so is the result:
            || isExtApply(expr) && isAnyOfConfigType(((ApplyExpr) expr).getArgs().getExpr());
  }

  private static boolean isAnyOfConfigType(CommaSeparatedList<Expr> expr) {
    return expr != null && (isOfConfigType(expr.getHead()) || isAnyOfConfigType(expr.getTail()));
  }

  @Override
  void generateTypeAssertion(Expr argument, String type) throws IOException {
    argument.visit(this);
    out.write(" as " + type);
  }

  @Override
  protected void handleExmlAppendPrepend(ObjectField objectField, DotExpr exmlAppendOrPrepend) throws IOException {
    out.writeSymbolWhitespace(objectField.getSymbol());
    out.writeToken("...");
    String whitespace = exmlAppendOrPrepend.getSymbol().getWhitespace();
    out.suppressWhitespace(exmlAppendOrPrepend.getSymbol());
    exmlAppendOrPrepend.visit(this);
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = ((ApplyExpr) objectField.getValue()).getArgs();
    out.writeTokenForSymbol("({", args.getLParen());
    objectField.getLabel().visit(this);
    out.writeSymbol(objectField.getSymColon());
    out.write(whitespace);
    args.getExpr().visit(this);
    out.writeTokenForSymbol("})", args.getRParen());
  }

  private static boolean isExtApply(Expr expr) {
    if (expr instanceof ApplyExpr) {
      ApplyExpr applyExpr = (ApplyExpr) expr;
      Expr fun = applyExpr.getFun();
      if (fun instanceof IdeExpr) {
        fun = ((IdeExpr) fun).getNormalizedExpr();
      }
      if (fun instanceof DotExpr) {
        DotExpr dotExpr = (DotExpr) fun;
        if ("apply".equals(dotExpr.getIde().getName())) {
          String argFQN = getArgFQN(dotExpr);
          return MxmlCompilationUnit.NET_JANGAROO_EXT_EXML.equals(argFQN) || "ext.Ext".equals(argFQN);
        }
      }
    }
    return false;
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
      Type declType = decl == null || decl.getOptTypeRelation() == null ? null : decl.getOptTypeRelation().getType();
      if (exprType != null && exprType.isArrayLike()) {
        forInStatement.getExpr().visit(this);
        // as decl cannot have a type, cast to specific Array, but not if the array's element type is the exact declared type:
        if (declType != null && new ExpressionType(declType).equals(exprType.getTypeParameter())) {
          declType = null;
        }
      } else {
        // If the expression is not iterable, Object.values() must be used.
        // Note that it must be polyfilled in IE, even IE11!
        out.writeSymbolWhitespace(forInStatement.getExpr().getSymbol());
        out.write("Object.values");
        out.write("(");
        forInStatement.getExpr().visit(this);
        out.write(" || {})"); // add empty object default, because Object.values() does not like 'undefined'
      }
      if (declType != null) {
        String tsType = getTypeScriptTypeForActionScriptType(declType);
        if (!"any".equals(tsType)) {
          out.write(" as " + tsType + "[]");
        }
      }
    } else {
      out.writeSymbol(forInStatement.getSymIn());
      forInStatement.getExpr().visit(this);
    }
    out.writeSymbol(forInStatement.getRParen());
    forInStatement.getBody().visit(this);
  }

  @Override
  public final void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    JooSymbol value = literalExpr.getValue();
    if (value.getJooValue() instanceof String) {
      // normalize single quotes to double quotes, unless a double quote is used inside the string:
      String string = (String) value.getJooValue();
      if (value.getText().charAt(0) == '\'' && !string.contains("\"")) {
        writeSymbolReplacement(literalExpr.getSymbol(), CompilerUtils.quote(string, false));
        return;
      }
    }
    super.visitLiteralExpr(literalExpr);
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    Ide ide = dotExpr.getIde();
    if (ide.isBound()) {
      // found access to a method without applying it immediately: bind!
      out.writeToken("bind(");
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
    ExpressionType type = arg.getType();
    if (type != null) {
      Ide ide = dotExpr.getIde();
      IdeDeclaration memberDeclaration = type.resolvePropertyDeclaration(ide.getName());
      if (memberDeclaration == null) {
        if (type.getAS3Type() != AS3Type.ANY && !type.isObject()) {
          // dynamic property access on typed objects must be converted to ["<ide>"] in TypeScript:
          arg.visit(this);
          writeSymbolReplacement(dotExpr.getOp(), "[");
          writeSymbolReplacement(ide.getSymbol(), '"' + ide.getName() + '"');
          out.write("]");
          return;
        }
      } else {
        if (PROPERTY_CLASS_INSTANCE.equals(ide.getName()) && type.getAS3Type() == AS3Type.CLASS &&
                type.getTypeParameter() != null &&
                isPropertiesClass(type.getTypeParameter().getDeclaration())) {
          arg.visit(this);
          // *_properties classes become objects in TypeScript, thus suppress .INSTANCE:
          out.writeSymbolWhitespace(dotExpr.getOp());
          out.writeSymbolWhitespace(dotExpr.getIde().getIde());
          return;
        }
        Annotation nativeAnnotation = memberDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
        String memberName = ide.getName();
        if (nativeAnnotation != null) {
          Object nativeMemberName = nativeAnnotation.getPropertiesByName().get(null);
          if (nativeMemberName instanceof String) {
            memberName = (String) nativeMemberName;
          }
        }
        IdeDeclaration bindableConfigDeclarationCandidate = getBindableConfigDeclarationCandidate(type, ide);
        if (bindableConfigDeclarationCandidate != null) {
          TypedIdeDeclaration accessor = findMemberWithBindableAnnotation(ide,
                  ide.isAssignmentLHS() ? MethodType.SET : MethodType.GET,
                  bindableConfigDeclarationCandidate.getClassDeclaration());
          if (accessor != null) {
            // found usage of a [Bindable]-annotated property: replace property access by asConfig(arg).ide:
            out.writeSymbolWhitespace(arg.getSymbol());
            out.write("asConfig(");
            arg.visit(this);
            out.write(")");
            out.writeSymbol(dotExpr.getOp());
            ide.visit(this);
            return;
          }
        }
        if (!memberName.equals(ide.getName()) || memberDeclaration.isPrivate()) {
          arg.visit(this);
          if (memberDeclaration.isPrivate()) {
            out.writeSymbol(dotExpr.getOp());
            writeSymbolReplacement(ide.getSymbol(), "#" + memberName);
          } else {
            out.writeSymbol(dotExpr.getOp());
            writeSymbolReplacement(ide.getSymbol(), memberName);
          }
          return;
        }
      }
    }

    super.visitDotExpr(dotExpr);
  }

  private IdeDeclaration getBindableConfigDeclarationCandidate(ExpressionType type, Ide ide) {
    if (type != null && !type.isConfigType()) {
      IdeDeclaration memberDeclaration = type.getDeclaration().resolvePropertyDeclaration(ide.getName(), false);
      if ((memberDeclaration instanceof VariableDeclaration
              || memberDeclaration instanceof PropertyDeclaration
              || (memberDeclaration instanceof FunctionDeclaration && ((FunctionDeclaration) memberDeclaration).isGetterOrSetter()))
              && !memberDeclaration.isPrivate() && !memberDeclaration.isProtected()) {
        return memberDeclaration;
      }
    }
    return null;
  }

  @Override
  public void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException {
    // check for obj["typedMember"], as TypeScript will treat this like obj.typedMember,
    // so we must rewrite this to (obj as object)["typedMember"]
    // (alternative: (obj as unknown).typedMember))
    Expr indexedExpr = arrayIndexExpr.getArray();
    ExpressionType type = indexedExpr.getType();
    ParenthesizedExpr<Expr> indexExpr = arrayIndexExpr.getIndexExpr();
    if (type != null && !AS3Type.ANY.equals(type.getAS3Type())) {
      Expr innerIndexExpr = indexExpr.getExpr();
      if (innerIndexExpr instanceof LiteralExpr) {
        LiteralExpr innerIndexLiteralExpr = (LiteralExpr) innerIndexExpr;
        Object stringValue = innerIndexLiteralExpr.getValue().getJooValue();
        if (stringValue instanceof String) {
          IdeDeclaration memberDeclaration = type.resolvePropertyDeclaration((String) stringValue);
          if (memberDeclaration != null) {
            // found a typed member
            if (compilationUnit.getPrimaryDeclaration().equals(memberDeclaration.getClassDeclaration()) &&
                    memberDeclaration instanceof TypedIdeDeclaration &&
                    ((TypedIdeDeclaration) memberDeclaration).isBindable() &&
                    (!(memberDeclaration instanceof PropertyDeclaration) || memberDeclaration.isNative())) {
              // untyped access to bindables in AS is used to prevent rewriting to AS3.get/setBindable(), but
              // instead access the internal field directly, so map it so TypeScript private field access:
              indexedExpr.visit(this);
              writeSymbolReplacement(indexExpr.getLParen(), ".");
              writeSymbolReplacement(innerIndexExpr.getSymbol(), type.isConfigType() ? memberDeclaration.getName() : getHashPrivateName(memberDeclaration));
            } else {
              // found a typed member, need to downcast to 'object':
              out.writeSymbolWhitespace(indexedExpr.getSymbol());
              out.write("(");
              indexedExpr.visit(this);
              out.write(" as unknown)");
              indexExpr.visit(this);
            }
            return;
          }
        }
      }
    }
    ExpressionType indexExprType = indexExpr.getType();
    if (indexExprType != null && !(TYPES_ALLOWED_AS_INDEX.contains(indexExprType.getAS3Type()))) {
      indexedExpr.visit(this);
      out.writeSymbol(indexExpr.getLParen());
      out.write("String(");
      indexExpr.getExpr().visit(this);
      out.write(")");
      out.writeSymbol(indexExpr.getRParen());
    } else {
      super.visitArrayIndexExpr(arrayIndexExpr);
    }
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    // this can only be "Vector$object", so we can always replace it by "Array":
    writeSymbolReplacement(ideWithTypeParam.getIde(), "Array");
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
    if (out.isWritingComment() || ide.getParentNode() == null // comment or ObjectField label
            || ide.getParentNode() instanceof DotExpr) {      // or property/field
      super.visitIde(ide);
    } else {
      // "top-level" Ide access: rewrite!
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
    return useQualifiedName ? TypeScriptModuleResolver.toLocalName(ide.getQualifiedName()) : ide.getName();
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
        if (declaration instanceof VariableDeclaration && isPrimaryVariableDeclaration((VariableDeclaration) declaration)) {
          // Modifiable singleton access:
          localName += "._";
        }
      }
    } else if (declaration instanceof Parameter
            && !declaration.getIde().getSymbol().isVirtual() // it is *not* the implicit 'arguments' parameter!
            && FunctionExpr.ARGUMENTS.equals(declaration.getName())) {
      // parameter name "arguments" is not allowed in ECMAScript strict to avoid confusion with the built-in
      // "arguments", so let's rename this:
      localName = FunctionExpr.ARGUMENTS + "$";
    }
    if (localName == null) {
      return useQualifiedName ? TypeScriptModuleResolver.toLocalName(declaration.getQualifiedName()) : declaration.getName();
    }
    return localName;
  }

  private static boolean rewriteThis(Ide ide) {
    if (ide.isThis() && ide.isRewriteThis()) {
      // starting from current scope, look for enclosing non-arrow/non-class-member function:
      Scope scope = ide.getScope();
      FunctionExpr functionExpr = scope.getFunctionExpr();
      while (functionExpr != null &&
              (functionExpr.getFunctionDeclaration() == null ||
                      !functionExpr.getFunctionDeclaration().isClassMember())) {
        if (!functionExpr.rewriteToArrowFunction()) {
          return true;
        }
        scope = scope.getParentScope();
        functionExpr = scope.getFunctionExpr();
      }
    }
    return false;
  }

  private static final Pattern ENDS_WITH_4_SPACES_INDENTATION = Pattern.compile("^([\\s\\S]*)\n( ?| {3,})$");

  @Override
  public void visitClassBodyDirectives(List<Directive> classBodyDirectives) throws IOException {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    if (primaryDeclaration instanceof ClassDeclaration) {
      ClassDeclaration classDeclaration = (ClassDeclaration) primaryDeclaration;

      // generate REST Resource URI template from annotation:
      generateRestResourceUriTemplateConstant(primaryDeclaration.getAnnotation(REST_RESOURCE_ANNOTATION_NAME));

      // fix MXML ASDoc:
      if (compilationUnit instanceof MxmlCompilationUnit) {
        for (Directive directive : classBodyDirectives) {
          // Class body directives are indented by 4 spaces in MXML, but in TypeScript, as class members, they should
          // only have 2 spaces. In combination with the new # private member syntax, ESLint cannot fix this, so we
          // do it here.
          setIndentationToTwo(directive.getSymbol());
          if (directive instanceof Declaration) {
            for (Annotation annotation : ((Declaration) directive).getAnnotations()) {
              setIndentationToTwo(annotation.getSymbol());
            }
          }
        }
      }

      // complement override of 'initialConfig' with current Config type:
      if (!companionInterfaceMode) {
        TypedIdeDeclaration initialConfigDeclaration = classDeclaration.getMemberDeclaration("initialConfig");
        if (initialConfigDeclaration == null || initialConfigDeclaration.isPrivate()) {
          TypeDeclaration configClassDeclaration = classDeclaration.getConfigClassDeclaration();
          if (configClassDeclaration != null) {
            if (!(classDeclaration.equals(configClassDeclaration)
                    // allow to deviate from the same class for some special cases:
                    // some MXML base classes do not use their own config type, but the one of their MXML subclass :(
                    || classDeclaration.equals(configClassDeclaration.getSuperTypeDeclaration())
                    // some (base) classes simply reuse their super class as their config type :(
                    || configClassDeclaration.equals(classDeclaration.getSuperTypeDeclaration()))) {
              TypeRelation configParameterType = classDeclaration.getConstructorConfigParameterType();
              classDeclaration.getIde().getScope().getCompiler().getLog().warning(configParameterType.getSymbol(),
                      String.format("Class extends ext.Base, has 'config' constructor parameter, but its type '%s' is not a valid Config type for this class. " +
                                      "Still generating a TypeScript Config class, but please fix this.",
                              configParameterType.getType().getIde().getQualifiedNameStr()));
            }
            ExpressionType configType = new ExpressionType(configClassDeclaration);
            configType.markAsConfigTypeIfPossible();
            out.write(String.format("\n  declare readonly initialConfig: %s;", getTypeScriptTypeForActionScriptType(configType)));
          }
        }
      }
    }
    super.visitClassBodyDirectives(classBodyDirectives);
  }

  private void generateRestResourceUriTemplateConstant(Annotation restResourceAnnotation) throws IOException {
    if (restResourceAnnotation != null) {
      CommaSeparatedList<AnnotationParameter> annotationParameters = restResourceAnnotation.getOptAnnotationParameters();
      if (annotationParameters != null) {
        AnnotationParameter annotationParameter = annotationParameters.getHead();
        if (annotationParameter.getOptName() != null) {
          if (REST_RESOURCE_URI_TEMPLATE_PARAMETER_NAME.equals(annotationParameter.getOptName().getName())) {
            out.write(String.format("\n  static readonly REST_RESOURCE_URI_TEMPLATE = %s;",
                    annotationParameter.getValue().getSymbol().getText()));
          }
        }
      }
    }
  }

  private void setIndentationToTwo(JooSymbol symbol) {
    String whitespace = symbol.getWhitespace();
    Matcher indentationMatcher = ENDS_WITH_4_SPACES_INDENTATION.matcher(whitespace);
    if (indentationMatcher.matches()) {
      symbol.setWhitespace(indentationMatcher.group(1) + "\n  ");
    }
  }

  private int staticCodeCounter = 0;

  @Override
  void generateStaticInitializer(List<Directive> directives) throws IOException {
    if (directives.isEmpty() || companionInterfaceMode) {
      return;
    }
    Directive firstDirective = directives.get(0);
    out.writeSymbolWhitespace(firstDirective.getSymbol());
    String uniqueName = "";
    if (staticCodeCounter > 0) {
      uniqueName = String.valueOf(staticCodeCounter);
    }
    ++staticCodeCounter;
    out.writeToken(String.format("static #static%s = (() =>", uniqueName));

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

  @Override
  protected String builtInIdentifierCode(String builtInIdentifier) {
    assert compilationUnit.getUsedBuiltInIdentifiers().contains(builtInIdentifier) : "Usage of built-in identifier '" + builtInIdentifier + "' has not been analyzed.";
    return builtInIdentifier;
  }
}
