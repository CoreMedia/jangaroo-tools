package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.AsExpr;
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
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NamespaceDeclaration;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectFieldOrSpread;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
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
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.config.TypeScriptTargetSourceFormatFeature.SIMPLIFIED_AS_EXPRESSIONS;
import static net.jangaroo.jooc.config.TypeScriptTargetSourceFormatFeature.SIMPLIFIED_THIS_USAGE_BEFORE_SUPER_CONSTRUCTOR_CALL;
import static net.jangaroo.jooc.config.TypeScriptTargetSourceFormatFeature.STATIC_BLOCKS;
import static net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit.AS_STRING;
import static net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit.NET_JANGAROO_EXT_EXML;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private static final Collection<String> TYPESCRIPT_BUILT_IN_TYPES = Arrays.asList(
          "Object",
          "Array",
          "Vector$object"
  );
  public static final List<AS3Type> TYPES_ALLOWED_AS_INDEX = Arrays.asList(AS3Type.ANY, AS3Type.STRING, AS3Type.NUMBER, AS3Type.INT, AS3Type.UINT);

  private static final String I_RESOURCE_MANAGER_QUALIFIED_NAME = "mx.resources.IResourceManager";
  private static final String GET_STRING_METHOD_NAME = "getString";
  private static final String RESOURCE_MANAGER_QUALIFIED_NAME = "resourceManager";
  private static final String RESOURCE_MANAGER_IMPL_QUALIFIED_NAME = "mx.resources.ResourceManager";
  private static final String GET_INSTANCE_METHOD_NAME = "getInstance";
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
    // and classes inheriting from FlExtEvent (they are converted to an ...Events interface)
    // and namespace declarations
    // and [Mixin] interfaces:
    return !TYPESCRIPT_BUILT_IN_TYPES.contains(primaryDeclaration.getQualifiedNameStr())
            && !(primaryDeclaration instanceof ClassDeclaration && ((ClassDeclaration) primaryDeclaration).inheritsFromFlExtEvent())
            && !(primaryDeclaration instanceof NamespaceDeclaration)
            && primaryDeclaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME) == null;
  }

  private final TypeScriptModuleResolver typeScriptModuleResolver;
  private CompilationUnit compilationUnit;
  private Map<String, String> imports;
  private boolean companionInterfaceMode;
  private boolean needsCompanionInterface;
  private List<ClassDeclaration> mixinClasses;
  private List<Ide> mixins;
  private List<ClassDeclaration> configSupers;
  private List<ClassDeclaration> eventsSupers;
  private List<Annotation> ownEvents;
  private List<Ide> realInterfaces;
  private boolean hasOwnConfigClass;
  private boolean hasOwnEventsClass;
  private List<TypedIdeDeclaration> ownConfigs;
  private FunctionExpr constructorFun;

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
      // Suppress ASDoc of [Event]s, they are rendered separately:
      if (!Jooc.EVENT_ANNOTATION_NAME.equals(annotation.getMetaName())) {
        whitespaceSymbols.add(annotation.getSymbol());
        if (ANNOTATION_NAME_TO_TSDOC_TAG_RENDERER.containsKey(annotation.getMetaName())) {
          tsdocTags.add(annotation);
        }
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
    if (!companionInterfaceMode && declaration.isClassMember()) {
      // Generate in strict order required by TypeScript: protected -> static -> override

      // Relevant whitespace has already been processed in TypeScript mode, so just use the tokens
      if (declaration.isProtected()) {
        out.writeToken("protected");
      }
      if (declaration.isStatic() && !(declaration instanceof ClassDeclaration)) {
        out.writeToken(SyntacticKeywords.STATIC);
      }
      if (declaration.getSuperDeclaration() != null) {
        out.writeToken(SyntacticKeywords.OVERRIDE);
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

    needsCompanionInterface = false;
    mixinClasses = new ArrayList<>();
    if (primaryDeclaration instanceof ClassDeclaration) {
      determineConfigAndEventsInterfaces((ClassDeclaration) primaryDeclaration);
    }

    boolean isModule = typeScriptModuleResolver.getRequireModuleName(compilationUnit, primaryDeclaration) != null;

    String targetNamespace = null;
    Set<String> localNames = new HashSet<>();
    if (isModule) {
      if (!getMetadata(primaryDeclaration).isEmpty()) {
        compilationUnit.addBuiltInIdentifierUsage("metadata");
      }
      if (primaryDeclaration instanceof VariableDeclaration
              && isLazy((VariableDeclaration) primaryDeclaration)) {
        compilationUnit.addBuiltInIdentifierUsage(getLazyFactoryFunctionName((VariableDeclaration) primaryDeclaration));
      }
      if (primaryDeclaration instanceof ClassDeclaration && (
              ((ClassDeclaration) primaryDeclaration).getConstructorConfigParameterType() != null
                      || ((ClassDeclaration) primaryDeclaration).hasConfigClass()
                      && ((ClassDeclaration) primaryDeclaration).getSuperTypeDeclaration().hasConfigClass()
      )) {
        compilationUnit.addBuiltInIdentifierUsage("Config");
      }

      Set<String> usedBuiltInIdentifiers = new TreeSet<>(compilationUnit.getUsedBuiltInIdentifiers());
      if (!usedBuiltInIdentifiers.isEmpty()) {
        localNames.addAll(usedBuiltInIdentifiers);
        // special case 'Config' is imported from its own ES module:
        if (usedBuiltInIdentifiers.remove("Config")) {
          out.write("import Config from \"@jangaroo/runtime/Config\";\n");
        }
        if (usedBuiltInIdentifiers.remove("Events")) {
          out.write("import Events from \"@jangaroo/ext-ts/Events\";\n");
        }
        if (!usedBuiltInIdentifiers.isEmpty()) {
          out.write(String.format("import { %s } from \"@jangaroo/runtime\";\n",
                  String.join(", ", usedBuiltInIdentifiers)));
        }
      }
      if (compilationUnit.getCompileDependencies().contains("js.KeyEvent")) {
        out.write("import KeyEvent from \"@jangaroo/runtime/KeyEvent\";\n");
      }
      if (compilationUnit.getCompileDependencies().contains("Function")) {
        out.write("import { AnyFunction } from \"@jangaroo/runtime/types\";\n");
      }

    } else { // !isModule
      targetNamespace = CompilerUtils.packageName(targetQualifiedNameStr);
      // if global namespace, simply leave it out
      if (!targetNamespace.isEmpty()) {
        out.writeToken("declare namespace");
        out.writeToken(targetNamespace);
        out.writeSymbol(compilationUnit.getLBrace());
      }
    }

    localNames.add(primaryLocalName);
    if (primaryDeclaration instanceof ClassDeclaration) {
      ClassDeclaration classDeclaration = (ClassDeclaration) primaryDeclaration;
      ClassDeclaration configClassDeclaration = classDeclaration.getConfigClassDeclaration();
      if (configClassDeclaration != null && !configClassDeclaration.equals(classDeclaration.getSuperTypeDeclaration())) {
        localNames.add(primaryLocalName + "Config");
      }
    }

    // generate imports
    // first pass: detect import local name clashes:
    Set<String> localNameClashes = new HashSet<>();
    Collection<CompilationUnit> dependentCompilationUnitModels = compilationUnit.getCompileDependencies().stream()
            .map(dependencyName -> RESOURCE_MANAGER_IMPL_QUALIFIED_NAME.equals(dependencyName) ? RESOURCE_MANAGER_QUALIFIED_NAME : dependencyName)
            .map(compilationUnitModelResolver::resolveCompilationUnit)
            .filter(TypeScriptCodeGenerator::isNoFlExtEventClass)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    boolean jooImported = false;
    List<String> usedRemovedNames = new ArrayList<>();
    for (CompilationUnit dependentCompilationUnitModel : dependentCompilationUnitModels) {
      String requireModuleName = typeScriptModuleResolver.getRequireModuleName(compilationUnit, dependentCompilationUnitModel.getPrimaryDeclaration());
      boolean isInJooPackage = "joo".equals(dependentCompilationUnitModel.getPackageDeclaration().getQualifiedNameStr());
      if (isInJooPackage) {
        Annotation deprecatedAnnotation = dependentCompilationUnitModel.getPrimaryDeclaration().getAnnotation(Jooc.DEPRECATED_ANNOTATION_NAME);
        if (deprecatedAnnotation != null) {
          Map<String, Object> annotationProperties = deprecatedAnnotation.getPropertiesByName();
          if ("none".equals(annotationProperties.get("replacement"))) {
            usedRemovedNames.add(dependentCompilationUnitModel.getQualifiedNameStr());
          }
        }
      }
      if (!jooImported && requireModuleName == null && isInJooPackage) {
        // found formerly native name that is now imported from "@jangaroo/runtime/joo":
        out.write("import joo from \"@jangaroo/runtime/joo\";\n");
        jooImported = true; // only import "joo" once!
      } else if (requireModuleName != null ||
              !dependentCompilationUnitModel.getPrimaryDeclaration().getTargetQualifiedNameStr().contains(".")) {
        CompilationUnit compilationUnitToRequire = getCompilationUnitToRequire(dependentCompilationUnitModel);
        if (compilationUnitToRequire != null) {
          // dependentCompilationUnitModel is an Ext pseudo singleton:
          if (dependentCompilationUnitModels.contains(compilationUnitToRequire)) {
            // other compilation unit is already used directly: skip to avoid false name clash!
            continue;
          }
          dependentCompilationUnitModel = compilationUnitToRequire;
        }
        String localName = typeScriptModuleResolver.getDefaultImportName(dependentCompilationUnitModel.getPrimaryDeclaration());
        localName = localName.split("\\.")[0]; // may be a native fully qualified name which "occupies" its first namespace!
        if (!localNames.add(localName)) {
          localNameClashes.add(localName);
        }
      }
    }
    if (!usedRemovedNames.isEmpty()) {
      throw new CompilerError(compilationUnit.getSymbol(), String.format("Compilation unit uses one or more removed jangaroo-runtime API. Please remove the following usages before migrating to TypeScript:\n%s", String.join("\n", usedRemovedNames)));
    }

    for (String resourceDependency : compilationUnit.getResourceDependencies()) {
      String localName = getLocalNameOfResourceDependency(resourceDependency);
      if (!localNames.add(localName)) {
        localNameClashes.add(localName);
      }
    }

    // second pass: generate imports, using fully-qualified names for local name clashes:
    Map<String, String> moduleNameToLocalName = new TreeMap<>();
    for (CompilationUnit dependentCompilationUnitModel : dependentCompilationUnitModels) {
      CompilationUnit pseudoSingletonCompilationUnit = null;
      CompilationUnit compilationUnitToRequire = getCompilationUnitToRequire(dependentCompilationUnitModel);
      if (compilationUnitToRequire != null) {
        // dependentCompilationUnitModel is an Ext pseudo singleton:
        pseudoSingletonCompilationUnit = dependentCompilationUnitModel;
        dependentCompilationUnitModel = compilationUnitToRequire;
      }
      IdeDeclaration dependentPrimaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = typeScriptModuleResolver.getRequireModuleName(compilationUnit, dependentPrimaryDeclaration);
      String localName;
      if (requireModuleName == null) {
        localName = TypeScriptModuleResolver.getNonRequireNativeName(dependentPrimaryDeclaration);
      } else {
        // re-map all @jangaroo/runtime/AS3** compilation units to @jangaroo/runtime**:
        requireModuleName = requireModuleName.replace("@jangaroo/runtime/AS3", "@jangaroo/runtime");

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
          // handle special pseudo-singletons that in Ext TS are accessed via getInstance():
          if (pseudoSingletonCompilationUnit != null) {
            imports.put(pseudoSingletonCompilationUnit.getQualifiedNameStr(), localName + ".getInstance()");
          }
        }
      }
      imports.put(dependentPrimaryDeclaration.getQualifiedNameStr(), localName);
    }

    for (String resourceDependency : compilationUnit.getResourceDependencies()) {
      String localName = getLocalNameOfResourceDependency(resourceDependency);
      if (localNameClashes.contains(localName)) {
        localName = toIdentifier(resourceDependency.replaceAll("[.][.]?/", ""));
      }
      imports.put("!" + resourceDependency, localName);
      moduleNameToLocalName.put(transformEmbedPath(compilationUnit, resourceDependency), localName);
    }

    // now generate the import directives:
    for (Map.Entry<String, String> importEntry : moduleNameToLocalName.entrySet()) {
      out.write(String.format("import %s from \"%s\";\n", importEntry.getValue(), importEntry.getKey()));
    }

    primaryDeclaration.visit(this);

    if (isModule) {
      if (!(primaryDeclaration instanceof ClassDeclaration && ((ClassDeclaration) primaryDeclaration).isPropertiesSubclass())
              && !isInitFunction(primaryDeclaration)) {
        out.write("\nexport default " + primaryLocalName + ";\n");
      }
    } else if (!targetNamespace.isEmpty()) {
      // close namespace:
      out.writeSymbol(compilationUnit.getRBrace());
      out.write("\n");
    }
  }

  private static String transformEmbedPath(CompilationUnit compilationUnit, String resourceDependency) {
    FileInputSource resourceInputSource = (FileInputSource) compilationUnit.getInputSource();
    File resourceFile = new File(resourceInputSource.getFile().getParentFile(), resourceDependency);
    if (CompilerUtils.getRelativePath(resourceInputSource.getSourceDir(), resourceFile, true) == null) {
      // embedded resource file is not below source directory: assume it is copied over, so strip all "../":
      resourceFile = new File(resourceInputSource.getSourceDir(),
              resourceDependency.replaceFirst("^([.][.]/)*", ""));
    }
    // adjust to cut-off extNamespace in target directory:
    resourceDependency = CompilerUtils.getRelativePath(
            CompilerUtils.fileFromQName(compilationUnit.getPrimaryDeclaration()
                    .getExtNamespaceRelativeTargetQualifiedNameStr(), resourceInputSource.getSourceDir(), "").getParentFile(),
            resourceFile,
            false).replaceAll("\\\\", "/");

    // always start with ./ or ../ :
    if (!(resourceDependency.startsWith("./") || resourceDependency.startsWith(".//"))) {
      resourceDependency = "./" + resourceDependency;
    }
    return resourceDependency;
  }

  private static String getLocalNameOfResourceDependency(String resourceDependency) {
    int lastDotPos = resourceDependency.lastIndexOf('/');
    String localResourceName = lastDotPos == -1 ? resourceDependency : resourceDependency.substring(lastDotPos + 1);
    return toIdentifier(localResourceName);
  }

  private static String toIdentifier(String string) {
    return string.replaceAll("[^a-zA-Z0-9$_]", "_");
  }

  private CompilationUnit getCompilationUnitToRequire(CompilationUnit compilationUnit) {
    Annotation nativeAnnotation = compilationUnit.getPrimaryDeclaration().getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null) {
      String requireValue = typeScriptModuleResolver.getNativeAnnotationRequireValue(nativeAnnotation);
      if (requireValue != null && !requireValue.isEmpty()) {
        // dependentCompilationUnitModel is an Ext pseudo singleton:
        return compilationUnit.getPrimaryDeclaration().getType().getDeclaration().getCompilationUnit();
      }
    }
    return null;
  }

  private static boolean isNoFlExtEventClass(CompilationUnit compilationUnit) {
    return !(compilationUnit.getPrimaryDeclaration() instanceof ClassDeclaration
            && ((ClassDeclaration) compilationUnit.getPrimaryDeclaration()).inheritsFromFlExtEvent());
  }

  private boolean isObservable(ClassDeclaration classDeclaration) {
    CompilationUnit observableInterface = ((JangarooParser) compilationUnitModelResolver).getCompilationUnit("ext.mixin.IObservable");
    return observableInterface != null &&
            classDeclaration.isAssignableTo((ClassDeclaration) observableInterface.getPrimaryDeclaration());
  }

  private void determineConfigAndEventsInterfaces(ClassDeclaration classDeclaration) {
    mixins = new ArrayList<>();

    configSupers = new ArrayList<>();
    ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
    eventsSupers = new ArrayList<>();
    realInterfaces = new ArrayList<>();
    ownEvents = Collections.emptyList();
    if (classDeclaration.getOptImplements() != null) {
      CommaSeparatedList<Ide> superTypes = classDeclaration.getOptImplements().getSuperTypes();
      do {
        ClassDeclaration maybeMixinDeclaration = (ClassDeclaration) superTypes.getHead().getDeclaration(false);
        CompilationUnit mixinCompilationUnit = CompilationUnit.getMixinCompilationUnit(maybeMixinDeclaration);
        if (mixinCompilationUnit != null
                && mixinCompilationUnit != classDeclaration.getCompilationUnit()) { // prevent circular inheritance between mixin and its own interface!
          mixinClasses.add(maybeMixinDeclaration);
          mixins.add(superTypes.getHead());
          if (maybeMixinDeclaration.hasConfigClass()) {
            compilationUnit.addBuiltInIdentifierUsage("Config");
            configSupers.add(maybeMixinDeclaration);
          }
          if (!maybeMixinDeclaration.getAnnotations(Jooc.EVENT_ANNOTATION_NAME).isEmpty()) {
            compilationUnit.addBuiltInIdentifierUsage("Events");
            eventsSupers.add(maybeMixinDeclaration);
          }
        } else {
          realInterfaces.add(superTypes.getHead());
        }
        superTypes = superTypes.getTail();
      } while (superTypes != null);
    }

    ClassDeclaration myMixinInterface = classDeclaration.getMyMixinInterface();
    // class is itself a mixin or an Ext Observable or a mixin client: consider its declared events!
    ownEvents = (myMixinInterface != null ? myMixinInterface : classDeclaration).getAnnotations(Jooc.EVENT_ANNOTATION_NAME);
    hasOwnEventsClass = (myMixinInterface != null || isObservable(classDeclaration) || !eventsSupers.isEmpty())
            && !(eventsSupers.isEmpty() && ownEvents.isEmpty());
    if (hasOwnEventsClass) {
      if (isObservable(superTypeDeclaration)) {
        eventsSupers.add(0, superTypeDeclaration);
      }
      if (!eventsSupers.isEmpty()) {
        compilationUnit.addBuiltInIdentifierUsage("Events");
      }
      for (Annotation ownEvent : ownEvents) {
        CompilationUnit eventTypeCompilationUnit = getEventTypeCompilationUnit(ownEvent);
        if (eventTypeCompilationUnit != null) {
          if (isNoFlExtEventClass(eventTypeCompilationUnit)) {
            compilationUnit.addDependency(eventTypeCompilationUnit, false);
          } else {
            List<TypedIdeDeclaration> eventParameters = getEventParameters(eventTypeCompilationUnit);
            for (TypedIdeDeclaration eventParameter : eventParameters) {
              TypeRelation optTypeRelation = eventParameter.getOptTypeRelation();
              if (optTypeRelation != null) {
                TypeDeclaration declaration = optTypeRelation.getType().getDeclaration();
                if (declaration != null) {
                  compilationUnit.addDependency(declaration.getCompilationUnit(), false);
                }
              }
            }
          }
        }
      }
    }

    ClassDeclaration configClassDeclaration = classDeclaration.getConfigClassDeclaration();
    hasOwnConfigClass = configClassDeclaration != null || hasOwnEventsClass;
    if (hasOwnConfigClass) {
      if (configClassDeclaration == null) {
        configClassDeclaration = classDeclaration;
      }
      ownConfigs = classDeclaration.getMembers().stream()
              .filter(typedIdeDeclaration -> !typedIdeDeclaration.isMixinMemberRedeclaration() && typedIdeDeclaration.isExtConfigOrBindable())
              .collect(Collectors.toList());
      if (configClassDeclaration.equals(superTypeDeclaration)) {
        if (ownConfigs.isEmpty() && configSupers.isEmpty() && ownEvents.isEmpty() && eventsSupers.isEmpty()) {
          hasOwnConfigClass = false;
        } else {
          getCompiler().getLog().warning(
                  classDeclaration.getConstructor().getParams().getHead().getSymbol(),
                  "A class reusing the Config type of its superclass in its config constructor parameter " +
                          "may not define own Configs or add Configs from mixins."
                          + " Please change the constructor parameter type or (re)move the additional Configs.");
        }
      }
      if (superTypeDeclaration != null && superTypeDeclaration.hasConfigClass()) {
        configSupers.add(0, superTypeDeclaration);
      }
    }
    if (!configSupers.isEmpty()) {
      compilationUnit.addBuiltInIdentifierUsage("Config");
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    if (classDeclaration.isPropertiesClass()) {
      visitPropertiesClassDeclaration(classDeclaration);
      return;
    }

    String classDeclarationLocalName = compilationUnitAccessCode(classDeclaration);
    String eventsInterfaceName = renderEventsInterface(classDeclaration);
    if (hasOwnConfigClass) {
      List<String> configExtends = configSupers.stream()
              .map(this::configType)
              .collect(Collectors.toList());
      if (!ownConfigs.isEmpty()) {
        String configNamesType = ownConfigs.stream().map(config -> CompilerUtils.quote(config.getName())).collect(Collectors.joining(" |\n  "));
        configExtends.add(String.format("Partial<Pick<%s,\n  %s\n>>", classDeclarationLocalName, configNamesType));
      }
      out.write(String.format("interface %s%s {", classDeclarationLocalName + "Config", configExtends.isEmpty() ? "" : " extends " + String.join(", ", configExtends)));
      if (eventsInterfaceName != null) {
        out.write(String.format("\n  listeners?: %s;", eventsInterfaceName));
      }
      out.write("\n}\n\n");
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
      for (IdeDeclaration node : classDeclaration.getSecondaryDeclarations()) {
        if (node instanceof ClassDeclaration) {
          needsCompanionInterface = false;
          mixinClasses = new ArrayList<>();
          determineConfigAndEventsInterfaces((ClassDeclaration) node);
          node.visit(this);
        }
      }
    }
  }

  private String renderEventsInterface(ClassDeclaration classDeclaration) throws IOException {
    if (!hasOwnEventsClass) {
      // no (additional) events: automatically inherits the events from its super class, nothing to do here
      // Do not (yet) use [Event] annotations of other classes, only render their documentation.
      // To suppress the Event interface output, set eventMixins and ownEvents to empty list:
      for (Annotation ownEvent : ownEvents) {
        if (containsASDoc(ownEvent.getSymbol())) {
          out.writeSymbolWhitespace(ownEvent.getSymbol());
        }
      }
      return null;
    }
    List<String> eventsExtends = eventsSupers.stream()
            .map(this::eventsType)
            .collect(Collectors.toList());
    if (ownEvents.isEmpty()) {
      // special case: no own events, only combine the events of (optional) super class and mixins:
      return String.join(" & ", eventsExtends);
    }
    String classDeclarationLocalName = compilationUnitAccessCode(classDeclaration);
    String eventsInterfaceName = classDeclarationLocalName + "Events";
    out.write("interface " + eventsInterfaceName);
    if (!eventsExtends.isEmpty()) {
      out.write(" extends " + String.join(", ", eventsExtends));
    }
    out.write(" {");
    for (Annotation ownEvent : ownEvents) {
      String eventName = ownEvent.getEventName();
      if (eventName == null) {
        throw new CompilerError(ownEvent.getSymbol(), "Event must have a name.");
      }
      eventName = normalizeExtEventName(eventName);
      String eventASDoc = ownEvent.getSymbol().getWhitespace();
      String eventParametersCode = "";
      CompilationUnit eventTypeCompilationUnit = getEventTypeCompilationUnit(ownEvent);
      if (eventTypeCompilationUnit != null) {
        String eventParametersASDoc;
        if (isNoFlExtEventClass(eventTypeCompilationUnit)) {
          eventParametersASDoc = "\n * @param event";
          eventParametersCode = "event: " + compilationUnitAccessCode(eventTypeCompilationUnit.getPrimaryDeclaration());
        } else {
          List<TypedIdeDeclaration> eventParameters = getEventParameters(eventTypeCompilationUnit);
          eventParametersASDoc = eventParameters.stream()
                  .map(this::memberToParamASDoc)
                  .collect(Collectors.joining());
          eventParametersCode = eventParameters.stream()
                  .map(this::getEventParameterCode)
                  .collect(Collectors.joining(", "));
        }

        // insert parameter ASDoc:
        Matcher matcher = Pattern.compile("(\\s*[*]/)").matcher(eventASDoc);
        if (matcher.find()) {
          eventASDoc = matcher.replaceFirst(eventParametersASDoc + "$1");
        } else {
          eventASDoc = "\n/**" + eventParametersASDoc + "\n */";
        }

        // remove all @eventType lines:
        eventASDoc = eventASDoc.replaceAll("\n[*\\s]*@eventType .*\n", "\n");
      }
      out.write(reIndentASDocToMemberLevel(eventASDoc));
      if (!eventASDoc.endsWith("\n")) {
        out.write("\n");
      }
      out.write(String.format("  %s?(%s):any;", eventName, eventParametersCode));
    }
    out.write("\n}\n\n");
    return eventsInterfaceName;
  }

  private String getEventParameterCode(TypedIdeDeclaration eventParameter) {
    ExpressionType type = eventParameter.getType();
    return eventParameter.getName() + ": " + getTypeScriptTypeForActionScriptType(type == null ? null : type.getTypeParameter());
  }

  private List<TypedIdeDeclaration> getEventParameters(CompilationUnit eventTypeCompilationUnit) {
    return ((ClassDeclaration) eventTypeCompilationUnit.getPrimaryDeclaration()).getMembers()
            .stream()
            .filter(eventParameter ->
                    !eventParameter.isStatic()
                            && eventParameter instanceof FunctionDeclaration
                            && ((FunctionDeclaration) eventParameter).isGetter())
            .collect(Collectors.toList());
  }

  private CompilationUnit getEventTypeCompilationUnit(Annotation ownEvent) {
    CompilationUnit eventTypeCompilationUnit = null;
    Object eventTypeName = ownEvent.getPropertiesByName().get(Jooc.EVENT_ANNOTATION_TYPE_ATTRIBUTE_NAME);
    if (eventTypeName instanceof String) {
      eventTypeCompilationUnit = compilationUnitModelResolver.resolveCompilationUnit((String) eventTypeName);
    }
    return eventTypeCompilationUnit;
  }

  private static final Pattern ASDOC_PATTERN = Pattern.compile("\\s*/[*][*]\\s*[*]?([\\s\\S]*)[*]/\\s*");

  private String memberToParamASDoc(TypedIdeDeclaration eventParameter) {
    String asDoc = "\n * @param " + eventParameter.getName();
    JooSymbol symbolWithASDoc = findSymbolWithASDoc(eventParameter);
    if (symbolWithASDoc != null) {
      Matcher matcher = ASDOC_PATTERN.matcher(symbolWithASDoc.getWhitespace());
      if (matcher.matches()) {
        asDoc += " " + matcher.group(1).trim();
      }
    }
    return asDoc;
  }

  private static final Pattern ASDOC_WITHOUT_INDENTATION_PATTERN = Pattern.compile("\\s*(( [*]|/[*][*]).*)");

  private static String reIndentASDocToMemberLevel(String asdoc) {
    String[] lines = asdoc.split("\n", -1);
    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      Matcher matcher = ASDOC_WITHOUT_INDENTATION_PATTERN.matcher(line);
      if (matcher.matches()) {
        lines[i] = "  " + matcher.group(1);
      }
    }
    return String.join("\n", lines);
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
        out.write("\nmetadata(" + classDeclarationLocalName + ", ");
        boolean firstAnnotation = true;
        for (Annotation runtimeAnnotation : metadata) {
          if (firstAnnotation) {
            firstAnnotation = false;
          } else {
            out.write(",\n    ");
          }
          out.write("[" + CompilerUtils.quote(runtimeAnnotation.getMetaName()));
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
    if (classDeclaration.isPropertiesSubclass()) {
      out.write("import ResourceBundleUtil from \"@jangaroo/runtime/l10n/ResourceBundleUtil\";\n");
    }
    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbolWhitespace(classDeclaration.getSymClass());
    FunctionDeclaration constructorDeclaration = classDeclaration.getConstructor();
    List<AssignmentOpExpr> propertyAssignments = getPropertiesClassAssignments(constructorDeclaration, true, true);
    if (classDeclaration.isPropertiesSubclass()) {
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
        // special case: legacy type js.KeyEvent used as a type becomes TS lib.dom KeyboardEvent:
        if ("js.KeyEvent".equals(qualifiedNameStr)) {
          return "KeyboardEvent";
        }
        // use class name:
        String tsType = getLocalName(declaration, true);
        return expressionType.isConfigType() ? configType(tsType) : tsType;
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
    boolean isOptional = initializer != null && (isAmbientOrInterface(compilationUnit) || isUndefined(initializer.getValue()) || companionInterfaceMode);
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
    Expr fieldValue = objectField.getValue();
    JooSymbol symbol = fieldValue.getSymbol();
    if (Pattern.matches("[\\s]+", symbol.getWhitespace())) {
      out.suppressWhitespace(symbol);
      out.write(" ");
    }
    if (fieldValue instanceof ApplyExpr
            && isApiCall((ApplyExpr) fieldValue, NET_JANGAROO_EXT_EXML, "eventHandler", true)) {
      // rewrite Exml.eventHandler(SomeFlExtEventClass.SOME_EVENT_NAME, flExtEventHandler) to just flExtEventHandler:
      ((ApplyExpr) fieldValue).getArgs().getExpr().getTail().getTail().getHead().visit(this);
    } else {
      super.visitObjectFieldValue(objectField);
    }
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
    writeSymbolReplacement(annotationParameter.getOptSymEq(), ":");
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
          boolean compilationUnitAmbient = isCompilationUnitAmbient();
          if (!compilationUnitAmbient) {
            // we want the ASDoc at the generated accessor, so render (private) field first:
            out.write("\n\n ");
            visitVariableDeclarationBase(currentVariableDeclaration);
            writeOptSymbol(variableDeclaration.getOptSymSemicolon(), "\n");
          }

          // generate accessors (field has been transformed to #-private)
          String accessor = currentVariableDeclaration.getName();

          visitDeclarationAnnotationsAndModifiers(variableDeclaration);
          out.writeToken("get");
          out.write(" " + accessor + "()");
          visitIfNotNull(currentVariableDeclaration.getOptTypeRelation());
          if (compilationUnitAmbient) {
            out.write(";");
          } else {
            out.write(String.format(" { return this.#%s; }", accessor));
          }

          // generate a set accessor, but only if no custom set...() method exists:
          String setMethodName = MethodType.SET + MxmlUtils.capitalize(accessor);
          TypedIdeDeclaration setMethodDeclaration = classDeclaration.getMemberDeclaration(setMethodName);
          if (setMethodDeclaration == null || setMethodDeclaration.isPrivate()) {
            out.write("\n  ");
            if (currentVariableDeclaration.getSuperDeclaration() != null) {
              out.write("override ");
            }
            out.write(String.format("set %s(value", accessor));
            visitIfNotNull(currentVariableDeclaration.getOptTypeRelation());
            out.write(")");
            if (compilationUnitAmbient) {
              out.write(";");
            } else {
              out.write(String.format(" { this.#%s = value; }", accessor));
            }
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
      if (variableDeclaration.isPrivate() || (variableDeclaration.isBindable() && !isAmbientOrInterface(compilationUnit))) {
        writeSymbolReplacement(ide.getSymbol(), getHashPrivateName(variableDeclaration));
      } else {
        ide.visit(this);
      }
      if (typeRelation != null) {
        typeRelation.visit(this);
      }
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
          if (initializer != null) {
            visitExprWithParenthesisIfObjectLiteral(initializer.getValue());
          } else {
            out.write(VariableDeclaration.getDefaultValue(typeRelation));
          }
          out.write(")");
        } else {
          out.write("{_: ");
          if (initializer != null) {
            initializer.getValue().visit(this);
          } else {
            out.write(VariableDeclaration.getDefaultValue(typeRelation));
          }
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
      Annotation embedAnnotation = variableDeclaration.getAnnotation(Jooc.EMBED_ANNOTATION_NAME);
      if (embedAnnotation != null) {
        out.write(" = Embed({");
        CommaSeparatedList<AnnotationParameter> annotationParameters = embedAnnotation.getOptAnnotationParameters();
        while (annotationParameters != null) {
          AnnotationParameter annotationParameter = annotationParameters.getHead();
          Ide optName = annotationParameter.getOptName();
          if (optName != null && Jooc.EMBED_ANNOTATION_SOURCE_PROPERTY.equals(optName.getName())) {
            AstNode value = annotationParameter.getValue();
            out.write(optName.getName() + ":" + imports.get("!" + ((LiteralExpr) value).getValue().getJooValue()));
          } else {
            annotationParameter.visit(this);
          }
          writeOptSymbol(annotationParameters.getSymComma());
          annotationParameters = annotationParameters.getTail();
        }
        out.write("})");
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
              !functionDeclaration.isBindable() && (functionDeclaration.isNative() || isAmbientOrInterface);
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
                getCompiler().getLog().warning(implMethodSymbolWithASDoc,
                        "Mixin method implementation has non-inheriting ASDoc. " +
                                "Please move such documentation to the mixin interface before TypeScript conversion.");
              }
              out.suppressWhitespace(implMethodSymbolWithASDoc);
            }
          }
        }
      }
      TypedIdeDeclaration setAccessor = getAccessorNameFromSetMethod(functionDeclaration);
      if (functionDeclaration.isNative() && functionDeclaration.isBindable() && !companionInterfaceMode && functionDeclaration.isGetter()
              && !isAmbientOrInterface) {
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

      if (functionDeclaration.isNative() && functionDeclaration.isBindable() && !companionInterfaceMode
              && !isAmbientOrInterface) {
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

      // In ActionScript, if you override an accessor, but not its complementary accessor (i.e. override
      // set, but not get), the complementary accessor is inherited. In ECMAScript, it is not, so we must
      // generate TypeScript code to also override it and simply delegate to the super property.
      if (functionDeclaration.isGetterOrSetter() && functionDeclaration.isOverride() && !functionDeclaration.isNative()) {
        // try to find complementary accessor:
        ClassDeclaration classDeclaration = functionDeclaration.getClassDeclaration();
        MethodType complementaryAccessorType = functionDeclaration.isGetter() ? MethodType.SET : MethodType.GET;
        String propertyName = functionDeclaration.getName();
        TypedIdeDeclaration complementaryAccessor =
                lookupPropertyDeclaration(classDeclaration, propertyName, complementaryAccessorType);
        if (complementaryAccessor instanceof FunctionDeclaration
                && !complementaryAccessor.getClassDeclaration().equals(classDeclaration)) {
          // found complementary accessor, and it was not in the same class, so it must be in the super class:
          if (complementaryAccessorType.equals(MethodType.GET)) {
            out.write(String.format("\n  override get %s()", propertyName));
            visitIfNotNull(complementaryAccessor.getOptTypeRelation());
            out.write(String.format(" {\n    return super.%s;\n  }\n", propertyName));
          } else {
            out.write(String.format("\n  override set %s(value", propertyName));
            visitIfNotNull(((FunctionDeclaration) complementaryAccessor).getFun().getParams().getHead().getOptTypeRelation());
            out.write(String.format(") {\n    super.%s = value;\n  }\n", propertyName));
          }
        }
      }
    } else {
      if (functionDeclaration.isPrimaryDeclaration()) {
        if (isInitFunction(functionDeclaration)) {
          // unwrap the init() function body to top-level ES module code that serves as npm package autoload:
          visitAll(functionDeclaration.getBody().getDirectives());
          out.writeSymbolWhitespace(functionDeclaration.getBody().getRBrace());
          return;
        } else {
          visitDeclarationAnnotationsAndModifiers(functionDeclaration);
        }
      }
      functionExpr.visit(this);
      writeOptSymbolWhitespace(functionDeclaration.getOptSymSemicolon());
    }
  }

  private boolean isInitFunction(IdeDeclaration ideDeclaration) {
    if ("init".equals(ideDeclaration.getExtNamespaceRelativeTargetQualifiedNameStr())
            && ideDeclaration instanceof FunctionDeclaration) {
      FunctionDeclaration functionDeclaration = (FunctionDeclaration) ideDeclaration;
      return functionDeclaration.getParams() == null &&
              (functionDeclaration.getOptTypeRelation() == null || AS3Type.VOID.name.equals(functionDeclaration.getOptTypeRelation().getType().getIde().getName()));
    }
    return false;
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
    if (!(compilationUnit.getPrimaryDeclaration() instanceof ClassDeclaration
            && body.usesInstanceThis()
            && body.getParentNode() instanceof FunctionExpr
            && body.getParentNode().getParentNode() instanceof FunctionDeclaration
            && ((FunctionDeclaration) body.getParentNode().getParentNode()).containsSuperConstructorCall()
            && ((FunctionDeclaration) body.getParentNode().getParentNode()).getClassDeclaration().notExtendsObject())) {
      super.visitBlockStatementDirectives(body);
      return;
    }
    boolean isExtClass = ((ClassDeclaration) compilationUnit.getPrimaryDeclaration()).inheritsFromExtBaseExplicitly();
    boolean useThisBeforeSuperViaIgnore = isFeatureEnabled(SIMPLIFIED_THIS_USAGE_BEFORE_SUPER_CONSTRUCTOR_CALL);
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
          if (!isExtClass) {
            getCompiler().getLog().warning(
                    (directivesToWrap.isEmpty() ? directive : directivesToWrap.get(0)).getSymbol(),
                    "Constructor code of non-Ext class may not access 'this' before calling 'super()'. "
                            + "Either move code or make this class an Ext class by inheriting from ext.Base.");
          }
          if (useThisBeforeSuperViaIgnore) {
            out.write("\n    // @ts-expect-error Ext JS semantics"
                    + "\n    const this$ = this;");
            constructorFun = ((FunctionDeclaration) body.getParentNode().getParentNode()).getFun();
            for (Directive directiveToWrap : directivesToWrap) {
              directiveToWrap.visit(this);
            }
            directive.visit(this);
            constructorFun = null;
            break;
          } else {
            visitSuperCallWithWrappedDirectives((SuperConstructorCallStatement) directive, directivesToWrap);
          }
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

  private Jooc getCompiler() {
    return (Jooc) compilationUnit.getPrimaryDeclaration().getIde().getScope().getCompiler();
  }

  private boolean isFeatureEnabled(long feature) {
    return (getCompiler().getConfig().getTypeScriptTargetSourceFormatFeatures() & feature) != 0L;
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
    if (superCall.getClassDeclaration().getConstructor().isThisAliased(true)) {
      ALIAS_THIS_CODE_GENERATOR.generate(out, false);
    }
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
      if (functionDeclaration != null && functionDeclaration.isThisAliased(true)
              && functionDeclaration.getFun() != constructorFun) {
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
          visitExprWithParenthesisIfObjectLiteral(returnExpr);
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

  private void visitExprWithParenthesisIfObjectLiteral(Expr returnExpr) throws IOException {
    boolean needsInnerParenthesis = returnExpr instanceof ObjectLiteral;
    if (needsInnerParenthesis) {
      out.writeSymbolWhitespace(returnExpr.getSymbol());
      out.write("(");
    }
    returnExpr.visit(this);
    if (needsInnerParenthesis) {
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
      // __typeCheckObjectLiteral__(type, { O }) -> Config<type>({ O })
      CommaSeparatedList<Expr> typeAndObjectLiteral = args.getExpr();
      Expr typeExpr = typeAndObjectLiteral.getHead();
      Expr objectLiteral = typeAndObjectLiteral.getTail().getHead();
      ExpressionType typeParameter = typeExpr.getType().getTypeParameter();
      if (!renderSingleSpreadValue(objectLiteral, typeParameter)) {
        writeSymbolReplacement(applyExpr.getSymbol(), "Config");
        out.writeToken("<");
        out.write(getTypeScriptTypeForActionScriptType(typeParameter));
        out.writeToken(">");
        out.writeSymbol(args.getLParen());
        objectLiteral.visit(this);
        out.writeSymbol(args.getRParen());
      }
    } else if (applyExpr.isTypeCast()) {
      IdeDeclaration declaration = ((IdeExpr) applyExpr.getFun()).getIde().getDeclaration();
      if (declaration instanceof ClassDeclaration) {
        ClassDeclaration castToClass = (ClassDeclaration) declaration;
        Expr firstParameter = args.getExpr().getHead();
        boolean isExtConfig = castToClass.inheritsFromExtBaseExplicitly();
        if (!castToClass.hasConfigClass() && !isExtConfig
                && firstParameter instanceof ObjectLiteral) {
          // Found a cast of an object literal into a non-config, non-Ext class or interface.
          // This has been used in Jangaroo-ActionScript to construct simple, typed objects that do *not*
          // inherit from Ext.Base. In TypeScript, they won't, anyway, so we can construct them on the fly.
          // For a (runtime) interface, we create an ad-hoc class that mixes in the interface,
          // instantiate it, then (if not empty) assign all given properties:
          //    IFoo({ ... }) =AS-TS=> Object.setPrototypeOf({ ... }, mixin(class {}, IFoo).prototype)
          // For a class, we simply set the class' prototype:
          //    Foo({ ... }) =AS-TS=> Object.setPrototypeOf({ ... }, Foo.prototype)
          out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
          out.write("Object.setPrototypeOf");
          out.writeSymbol(args.getLParen());
          firstParameter.visit(this);
          out.writeToken(", ");
          if (castToClass.isInterface()) {
            out.writeToken("mixin(");
            out.writeToken("class {}, ");
            applyExpr.getFun().visit(this);
            out.writeToken(")");
          } else {
            applyExpr.getFun().visit(this);
          }
          out.write(".prototype");
          out.writeSymbol(args.getRParen());
          return;
        }
        ExpressionType castToType = applyExpr.getFun().getType().getTypeParameter();
        if (castToType != null && castToType.isConfigType() ||
                isOfConfigType(firstParameter) && castToClass.hasConfigClass()) {
          writeSymbolReplacement(applyExpr.getSymbol(), "Config");
          if (!isExtConfig) {
            out.write("<" + compilationUnitAccessCode(declaration) + ">");
            out.writeSymbolWhitespace(args.getLParen());
          }
          out.write("(");
          boolean doRenderArg = true;
          if (isExtConfig) {
            // use generic Config factory function with the class as first parameter:
            out.write(compilationUnitAccessCode(declaration));
            if (firstParameter instanceof ObjectLiteral && ((ObjectLiteral) firstParameter).getFields() == null) {
              // suppress second parameter if it is just an empty object:
              doRenderArg = false;
            } else {
              writeSymbolReplacement(args.getLParen(), ", ");
            }
          }
          if (doRenderArg &&
                  !renderSingleSpreadValue(firstParameter, castToType)) {
            firstParameter.visit(this);
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
    } else if (isResourceManager_getInstance(applyExpr)) {
      CompilationUnit resourceManager = compilationUnitModelResolver.resolveCompilationUnit(RESOURCE_MANAGER_QUALIFIED_NAME);
      writeSymbolReplacement(applyExpr.getSymbol(), getLocalName(resourceManager.getPrimaryDeclaration(), false));
    } else if (applyExpr.isFlexAddEventListener()) {
      Expr eventNameExpr = args.getExpr().getHead();
      if (eventNameExpr instanceof IdeExpr) {
        eventNameExpr = ((IdeExpr) eventNameExpr).getNormalizedExpr();
      }
      if (eventNameExpr instanceof DotExpr) {
        DotExpr eventNameDotExpr = (DotExpr) eventNameExpr;
        ExpressionType eventClass = eventNameDotExpr.getArg().getType().getTypeParameter();
        if (eventClass != null) {
          VariableDeclaration eventNameDeclaration = (VariableDeclaration) eventClass.getDeclaration().getStaticMemberDeclaration(eventNameDotExpr.getIde().getName());
          String eventOnName = (String) ((LiteralExpr) eventNameDeclaration.getOptInitializer().getValue()).getValue().getJooValue();
          String eventName = normalizeExtEventName(eventOnName);
          Expr fun = applyExpr.getFun();
          DotExpr funDotExpr = (DotExpr) (fun instanceof IdeExpr ? ((IdeExpr) fun).getNormalizedExpr() : fun);
          funDotExpr.getArg().visit(this);
          out.writeSymbol(funDotExpr.getOp());
          out.write("addListener");
          out.writeSymbol(args.getLParen());
          out.write(CompilerUtils.quote(eventName));
          out.writeSymbol(args.getExpr().getSymComma());
          args.getExpr().getTail().getHead().visit(this);
          out.writeSymbol(args.getRParen());
        }
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

  private static String normalizeExtEventName(String eventOnName) {
    return eventOnName.startsWith("on") ? eventOnName.substring(2).toLowerCase() : eventOnName;
  }

  @Override
  boolean renderSingleSpreadValue(Expr argument, ExpressionType parameterType) throws IOException {
    // We use nested object literals + spread operator for assigning untyped Config properties,
    // but the special case that there are _only_ untyped properties results in an outer object
    // that only contains _one_ spread inner object ({...{ untyped: "foo"}}), and that does _not_
    // prevent the type error as originally intended. It seems TypeScript cannot accurately type
    // spread expression, _only_ the special case { ...T } => T.
    // Thus, Config objects with _only_ untyped properties must be represented differently. We chose
    // to use a type assertion on the object literal, which, in contrast to a typed function call
    // parameter, allows additional untyped properties. So
    //   <Foo u:untyped="foo"/>
    // becomes
    //   new Foo(<Config<Foo>>{ untyped: "foo" })

    // If the parameter has a type and the argument is an object literal...
    if (parameterType != null) {
      if (argument instanceof ObjectLiteral) {
        ObjectLiteral objectLiteral = (ObjectLiteral) argument;
        CommaSeparatedList<ObjectFieldOrSpread> fields = objectLiteral.getFields();
        // ...and the argument object literal only consists of one spread...
        if (fields != null && fields.getTail() == null && fields.getHead() instanceof Spread) {
          writeOptSymbolWhitespace(objectLiteral.getSymbol());
          // Add a type assertion to match the parameter type (as Config type):
          parameterType.markAsConfigTypeIfPossible();
          out.write("<" + getTypeScriptTypeForActionScriptType(parameterType) + ">");
          // Skip the outer, obsolete object literal, in other words, visit only the inner object:
          ((Spread) fields.getHead()).getArg().visit(this);
          return true;
        }
      } else if (argument instanceof ApplyExpr && ((ApplyExpr) argument).isTypeCast()) {
        // check for Config or even obsolete type cast:
        if (parameterType.isConfigType()) {
          if (parameterType.getDeclaration() instanceof ClassDeclaration &&
                  !((ClassDeclaration) parameterType.getDeclaration()).inheritsFromExtBaseExplicitly()) {
            // found pattern new Foo(Foo({ ... })), where Foo is no Ext class: simply suppress the type cast!
            ((ApplyExpr) argument).getArgs().getExpr().getHead().visit(this);
            return true;
          }
          if (argument.getType() != null) {
            // When parameter type is a Config type, so must be the argument type:
            argument.getType().markAsConfigTypeIfPossible();
            // This leads to better code being generated for the inner type cast.
          }
        }
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

  private static boolean isResourceManager_getInstance(ApplyExpr applyExpr) {
    if (isApiCall(applyExpr, RESOURCE_MANAGER_IMPL_QUALIFIED_NAME, GET_INSTANCE_METHOD_NAME, true)) {
      CommaSeparatedList<Expr> argsExpressions = applyExpr.getArgs().getExpr();
      return argsExpressions == null || argsExpressions.getHead() == null; // call uses no arguments
    }
    return false;
  }

  private static String getPackageNameFromReflectionCall(ApplyExpr applyExpr) {
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (args.getExpr() != null && args.getExpr().getHead() instanceof LiteralExpr
            && args.getExpr().getTail() == null
            && applyExpr.getFun() instanceof IdeExpr) {
      IdeDeclaration declaration = ((IdeExpr) applyExpr.getFun()).getIde().getDeclaration(false);
      if (declaration != null && "joo.getOrCreatePackage".equals(declaration.getQualifiedNameStr())) {
        return (String) ((LiteralExpr) args.getExpr().getHead()).getValue().getJooValue();
      }
    }
    return null;
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
      if (dotExpr.getArg() instanceof ApplyExpr) {
        String packageNameFromReflectionCall = getPackageNameFromReflectionCall((ApplyExpr) dotExpr.getArg());
        if (packageNameFromReflectionCall != null) {
          String localName = dotExpr.getIde().getName();
          String qName = CompilerUtils.qName(packageNameFromReflectionCall, localName);
          CompilationUnit targetCompilationUnit = ide.getScope().getCompiler().getCompilationUnit(qName);
          if (targetCompilationUnit == null) {
            // try again with UPPERCASE identifier (session -> SESSION):
            String guessedRenamedQName = CompilerUtils.qName(packageNameFromReflectionCall, localName.toUpperCase());
            CompilationUnit renamedTargetCompilationUnit = ide.getScope().getCompiler().getCompilationUnit(guessedRenamedQName);
            if (renamedTargetCompilationUnit != null) {
              // check that the found CU is really renamed to the desired name:
              Annotation renameAnnotation = renamedTargetCompilationUnit.getPrimaryDeclaration().getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
              if (renameAnnotation != null) {
                Object renamedQName = renameAnnotation.getPropertiesByName().get(null);
                if (qName.equals(renamedQName)) {
                  targetCompilationUnit = renamedTargetCompilationUnit;
                }
              }
            }
          }
          if (targetCompilationUnit != null && targetCompilationUnit.getPrimaryDeclaration() instanceof VariableDeclaration) {
            VariableDeclaration targetPrimaryDeclaration = (VariableDeclaration) targetCompilationUnit.getPrimaryDeclaration();
            if (!targetPrimaryDeclaration.isConst() || targetPrimaryDeclaration.getAnnotation(Jooc.LAZY_ANNOTATION_NAME) != null) {
              out.write("._");
            }
          }
        }
      }
    }
  }

  private static boolean mayNotBeNull(Expr firstArg) {
    AstNode parentNode = firstArg.getParentNode();
    return parentNode instanceof DotExpr && ((DotExpr) parentNode).getArg() == firstArg
            || parentNode instanceof ArrayIndexExpr && ((ArrayIndexExpr) parentNode).getArray() == firstArg
            || parentNode instanceof ApplyExpr && ((ApplyExpr) parentNode).getFun() == firstArg;
  }

  @Override
  boolean convertToFunctionCall(InfixOpExpr expr) {
    return !(isFeatureEnabled(SIMPLIFIED_AS_EXPRESSIONS)
            && expr instanceof AsExpr
            && expr.getArg2() instanceof IdeExpr
            && expr.getParentNode() instanceof ParenthesizedExpr
            && mayNotBeNull((Expr) expr.getParentNode()));
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
    if (parameters.getHead().getOptTypeRelation() != null && parameters.getTail() == null) {
      TypeDeclaration declaration = parameters.getHead().getOptTypeRelation().getType().getDeclaration(false);
      if (declaration instanceof ClassDeclaration && ((ClassDeclaration) declaration).inheritsFromFlExtEvent()) {
        return;
      }
    }
    super.visitParameters(parameters);
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
                type.getTypeParameter().getDeclaration() instanceof ClassDeclaration &&
                ((ClassDeclaration) type.getTypeParameter().getDeclaration()).isPropertiesClass()) {
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
    // so we must rewrite this to (obj as unknown)["typedMember"]
    Expr indexedExpr = arrayIndexExpr.getArray();
    ExpressionType type = indexedExpr.getType();
    ParenthesizedExpr<Expr> indexExpr = arrayIndexExpr.getIndexExpr();
    if (type != null && !AS3Type.ANY.equals(type.getAS3Type())) {
      Expr innerIndexExpr = indexExpr.getExpr();
      if (innerIndexExpr instanceof LiteralExpr) {
        LiteralExpr innerIndexLiteralExpr = (LiteralExpr) innerIndexExpr;
        Object stringValue = innerIndexLiteralExpr.getValue().getJooValue();
        if (stringValue instanceof String &&
                renderSquareBracketMemberAccess(arrayIndexExpr, type.resolvePropertyDeclaration((String) stringValue))) {
          return;
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

  private boolean renderSquareBracketMemberAccess(ArrayIndexExpr arrayIndexExpr, IdeDeclaration memberDeclaration) throws IOException {
    if (memberDeclaration == null) {
      return false;
    }
    Expr indexedExpr = arrayIndexExpr.getArray();
    ParenthesizedExpr<Expr> indexExpr = arrayIndexExpr.getIndexExpr();
    // found a typed member
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    ClassDeclaration memberClassDeclaration = memberDeclaration.getClassDeclaration();
    String dotProperty = null;
    if (memberDeclaration instanceof TypedIdeDeclaration &&
            ((TypedIdeDeclaration) memberDeclaration).isBindable()) {
      if (primaryDeclaration.equals(memberClassDeclaration) &&
              (!(memberDeclaration instanceof PropertyDeclaration) || memberDeclaration.isNative())) {
        // untyped access to bindables in AS is used to prevent rewriting to AS3.get/setBindable(), but
        // instead access the internal field directly, so map it to TypeScript private field access:
        dotProperty = indexedExpr.getType().isConfigType() ? memberDeclaration.getName() : getHashPrivateName(memberDeclaration);
      } else if (!isBindableWithoutAccessor((TypedIdeDeclaration) memberDeclaration)) {
        // member is known to support runtime accessors, so even untyped access ends up in the set method,
        // thus we can safely replace by typed access:
        dotProperty = memberDeclaration.getName();
      }
      // In any other case, when a [Bindable] is accessed via square brackets, the motivation is usually to prevent
      // Jangaroo from converting it to get/setBindable(). In TypeScript, this might be possible to express via dot
      // property access, but usually, a [Bindable] Config is not available as a public property of the target class.
      // Thus, we better keep untyped access, including a type assertion for TypeScript, by keeping dotProperty null.
    } else {
      if (!memberDeclaration.isWritable() && arrayIndexExpr.isAssignmentLHS()) {
        // The untyped access is used to allow writing a read-only property
        FunctionDeclaration functionDeclaration = findFunctionDeclaration(arrayIndexExpr);
        if (functionDeclaration != null && functionDeclaration.isConstructor()) {
          // writing a read-only property is allowed (only) in the constructor, no need for untyped access:
          dotProperty = memberDeclaration.getName();
        }
      } else if (!memberDeclaration.isPrivate()) {
        // if square brackets were used to bypass protected access...
        if (memberDeclaration.isProtected() && !isProtectedAccessAllowed(indexedExpr, primaryDeclaration, memberClassDeclaration)) {
          // ...this still works in TypeScript w/o type assertion, so just keep the code as-is:
          return false;
        } else {
          IdeDeclaration memberTypeHolder = memberDeclaration instanceof PropertyDeclaration
                  ? ((PropertyDeclaration) memberDeclaration).getAccessor(arrayIndexExpr.isAssignmentLHS())
                  : memberDeclaration;
          ExpressionType memberType = memberTypeHolder == null || memberTypeHolder.getType() == null ? null
                  : memberTypeHolder.getType().getEvalType();
          getCompiler().getLog().warning(
                  indexExpr.getSymbol(),
                  String.format("A declaration of member '%s' of type '%s' was found, assuming the untyped square-brackets access is not necessary.",
                          memberDeclaration.getName(), memberType));
          dotProperty = memberDeclaration.getName();
        }
      }
    }

    if (dotProperty != null) {
      indexedExpr.visit(this);
      writeSymbolReplacement(indexExpr.getLParen(), ".");
      writeSymbolReplacement(indexExpr.getExpr().getSymbol(), dotProperty);
    } else {
      // need to cast to 'unknown':
      out.writeSymbolWhitespace(indexedExpr.getSymbol());
      out.write("(");
      indexedExpr.visit(this);
      out.write(" as unknown)");
      indexExpr.visit(this);
    }
    return true;
  }

  private boolean isProtectedAccessAllowed(Expr objExpr,
                                           IdeDeclaration fromPrimaryDeclaration,
                                           ClassDeclaration protectedMemberClassDeclaration) {
    return fromPrimaryDeclaration instanceof ClassDeclaration &&
            objExpr.getType() != null &&
            fromPrimaryDeclaration.equals(objExpr.getType().getDeclaration()) &&
            (fromPrimaryDeclaration.equals(protectedMemberClassDeclaration) ||
            ((ClassDeclaration) fromPrimaryDeclaration).isSubclassOf(protectedMemberClassDeclaration));
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

  private String eventsType(ClassDeclaration targetClass) {
    return eventsType(compilationUnitAccessCode(targetClass));
  }

  private String eventsType(String targetClass) {
    return String.format("Events<%s>", targetClass);
  }

  private String configType(ClassDeclaration targetClass) {
    return configType(compilationUnitAccessCode(targetClass));
  }

  private String configType(String targetClass) {
    return String.format("Config<%s>", targetClass);
  }

  private boolean rewriteThis(Ide ide) {
    // rewrite to this$ if either local flag on ide is set OR we are in constructor-this-rewrite-mode
    if (ide.isThis() && (constructorFun != null || ide.isRewriteThis())) {
      // starting from current scope, look for enclosing non-arrow/non-class-member function:
      Scope scope = ide.getScope();
      FunctionExpr functionExpr = scope.getFunctionExpr();
      while (functionExpr != null &&
              (functionExpr.getFunctionDeclaration() == null ||
                      !functionExpr.getFunctionDeclaration().isClassMember())) {
        if (!functionExpr.rewriteToArrowFunction()) {
          return ide.isRewriteThis();
        }
        scope = scope.getParentScope();
        functionExpr = scope.getFunctionExpr();
      }
      // if in constructor-this-rewrite-mode...
      if (constructorFun != null) {
        // ...did we reach the constructor function? If so, rewrite to this$!
        return functionExpr == constructorFun;
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

      // add reference to current Config type as 'declare Config: ...Config;':
      if (!companionInterfaceMode && hasOwnConfigClass) {
        TypeDeclaration configClassDeclaration = classDeclaration.getConfigClassDeclaration();
        if (configClassDeclaration != null) {
          if (!(classDeclaration.equals(configClassDeclaration)
                  // allow to deviate from the same class for some special cases:
                  // some MXML base classes do not use their own config type, but the one of their MXML subclass :(
                  || classDeclaration.equals(configClassDeclaration.getSuperTypeDeclaration())
                  // some (base) classes simply reuse their super class as their config type :(
                  || configClassDeclaration.equals(classDeclaration.getSuperTypeDeclaration()))) {
            TypeRelation configParameterType = classDeclaration.getConstructorConfigParameterType();
            getCompiler().getLog().warning(configParameterType.getSymbol(),
                    String.format("Class extends ext.Base, has 'config' constructor parameter, but its type '%s' is not a valid Config type for this class. " +
                                    "Still generating a TypeScript Config class, but please fix this.",
                            configParameterType.getType().getIde().getQualifiedNameStr()));
          }
        }
        out.write(String.format("\n  declare Config: %sConfig;", compilationUnitAccessCode(classDeclaration)));
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
            out.write(String.format("\n  static%s readonly REST_RESOURCE_URI_TEMPLATE: string = %s;",
                    isRestResourceOverride((ClassDeclaration) compilationUnit.getPrimaryDeclaration()) ? " override" : "",
                    annotationParameter.getValue().getSymbol().getText()));
          }
        }
      }
    }
  }

  private static boolean isRestResourceOverride(ClassDeclaration classDeclaration) {
    ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
    while (superTypeDeclaration != null) {
      if (superTypeDeclaration.getAnnotation(REST_RESOURCE_ANNOTATION_NAME) != null) {
        return true;
      }
      superTypeDeclaration = superTypeDeclaration.getSuperTypeDeclaration();
    }
    return false;
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
    boolean generateStaticBlocks = isFeatureEnabled(STATIC_BLOCKS);
    if (generateStaticBlocks) {
      out.writeToken("static");
    } else {
      // simulate static blocks by declaring a unique #private static field with an immediately-evaluating
      // arrow function expression:
      String uniqueName = "";
      if (staticCodeCounter > 0) {
        uniqueName = String.valueOf(staticCodeCounter);
      }
      ++staticCodeCounter;
      out.writeToken(String.format("static #static%s = (() =>", uniqueName));
    }

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
    if (!generateStaticBlocks) {
      out.writeToken(")();");
    }
  }

  @Override
  protected String builtInIdentifierCode(String builtInIdentifier) {
    assert compilationUnit.getUsedBuiltInIdentifiers().contains(builtInIdentifier) : "Usage of built-in identifier '" + builtInIdentifier + "' has not been analyzed.";
    return builtInIdentifier;
  }
}