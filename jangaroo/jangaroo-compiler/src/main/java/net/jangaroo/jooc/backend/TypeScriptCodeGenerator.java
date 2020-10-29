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
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BinaryOpExpr;
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
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.ObjectField;
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
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;
import net.jangaroo.jooc.input.ZipFileInputSource;
import net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.types.FunctionSignature;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private static final Collection<String> TYPESCRIPT_BUILT_IN_TYPES = Arrays.asList(
          "Object",
          "Array",
          "Vector$object"
  );
  public static final List<AS3Type> TYPES_ALLOWED_AS_INDEX = Arrays.asList(AS3Type.ANY, AS3Type.STRING, AS3Type.NUMBER, AS3Type.INT, AS3Type.UINT);

  public static boolean generatesCode(IdeDeclaration primaryDeclaration) {
    // generate TypeScript for almost everything *except* some built-in classes which would fail to compile
    // and [Mixin] interfaces:
    return !TYPESCRIPT_BUILT_IN_TYPES.contains(primaryDeclaration.getQualifiedNameStr())
            && primaryDeclaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME) == null;
  }

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
                && declaration.getTargetQualifiedNameStr().contains(".")) {
          out.writeToken("export");
        } else if (!isInterface(declaration)) {
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

    out.writeSymbolWhitespace(compilationUnit.getPackageDeclaration().getSymbol());

    if (!compilationUnit.getUsedBuiltInIdentifiers().isEmpty()) {
      out.write(String.format("import {%s} from '@jangaroo/joo/AS3';\n",
              String.join(", ", compilationUnit.getUsedBuiltInIdentifiers())));
    }

    boolean isModule = getRequireModuleName(primaryDeclaration) != null;
    String targetNamespace = null;
    if (!isModule) {
      targetNamespace = CompilerUtils.packageName(primaryDeclaration.getTargetQualifiedNameStr());
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
    } else if (!targetNamespace.isEmpty()) {
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
      if (member.isPrivate() && useSymbolForPrivateMember(member)) {
        out.write(MessageFormat.format("const ${0} = Symbol(\"{0}\");\n", member.getName()));
      }
    }

    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbolWhitespace(classDeclaration.getSymClass());
    if (classDeclaration.isInterface()) {
      out.writeToken("abstract");
    }
    out.writeToken("class");
    classDeclaration.getIde().visit(this);
    String configClassName = null;
    FunctionDeclaration constructor = classDeclaration.getConstructor();
    if (constructor != null && constructor.getParams() != null) {
      Parameter firstParam = constructor.getParams().getHead();
      if ("config".equals(firstParam.getName()) && firstParam.getOptTypeRelation() != null
              && classDeclaration.getName().startsWith(firstParam.getOptTypeRelation().getType().getIde().getName())) {
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

      if (classDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null
              && classDeclaration.getOptImplements() != null
              && (!classDeclaration.isInterface() || classDeclaration.getOptImplements().getSuperTypes().getTail() != null)) {
        out.write("\nmixin(" + classDeclaration.getName());
        out.write(", ");
        classDeclaration.getOptImplements().getSuperTypes().visit(this);
        out.write(");\n");
      }
      if (configClassName != null) {
        out.write("\ndeclare namespace ");
        out.write(classDeclaration.getName());
        out.write(" {\n");
        out.write("  export class _");
        ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
        if (superTypeDeclaration != null && !superTypeDeclaration.isObject()) {
          out.write(" extends " + compilationUnitAccessCode(superTypeDeclaration) + "._");
        }
        out.write(" {\n");
        out.write("    constructor(config?: _);\n");
        out.write("  }\n");
        List<String> configExtends = new ArrayList<>(configMixins);
        if (ownConfigsClassName != null) {
          configExtends.add(ownConfigsClassName);
        }
        if (!configExtends.isEmpty()) {
          out.write(String.format("  export interface _ extends %s {}\n", String.join(", ", configExtends)));
        }
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
    AstNode parentNode = typeRelation.getParentNode();
    if (parentNode instanceof FunctionExpr) {
      parentNode = parentNode.getParentNode();
    }
    if (parentNode instanceof IdeDeclaration) {
      IdeDeclaration ideDeclaration =  (IdeDeclaration) parentNode;
      out.writeSymbol(typeRelation.getSymbol());
      ExpressionType expressionType = ideDeclaration.getIde().getScope().getExpressionType(ideDeclaration);
      // a non-getter-setter function declaration returns its function signature, but we are just interested 
      // in its return value, which is contained in the type parameter:
      if (expressionType instanceof FunctionSignature) {
        expressionType = expressionType.getTypeParameter();
      }
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
        if (as3Type.name.equals(declaration.getQualifiedNameStr())) {
          // it is really "Object", use TypeScript "Object":
          return as3Type.name;
        }
        // use class name:
        return getLocalName(declaration, true);
      case ANY:
        return "any";
      case VECTOR:
      case ARRAY:
        return "Array<" + getTypeScriptTypeForActionScriptType(expressionType.getTypeParameter()) + ">";
      case UINT:
      case INT:
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
    String moduleName = getRequireModulePath(declaration);
    if (moduleName == null) {
      return null;
    }
    InputSource importedInputSource = declaration.getCompilationUnit().getInputSource();
    FileInputSource currentInputSource = (FileInputSource) compilationUnit.getInputSource();
    if (importedInputSource instanceof FileInputSource &&
            ((FileInputSource) importedInputSource).getSourceDir().equals(currentInputSource.getSourceDir())) {
      // same input source: relativize against current file
      moduleName = CompilerUtils.removeExtension(computeRelativeModulePath(currentInputSource.getFile(),
              new File(currentInputSource.getSourceDir(), moduleName + ".ts")));
    } else {
      // compute target npm package name
      String npmPackageName;
      if (importedInputSource instanceof FileInputSource) {
        // When using Maven, only test code uses code from another source directory.
        // We know that in the target TypeScript workspace, the relative path from the test source root
        // directory to the source root directory is "../src". This is achieved by creating
        // two absolute paths, one in the dummy root directory "/tests" (name is arbitrary)
        // and one which is just the root directory "/src". The 'modulePath' is added later.
        npmPackageName = computeRelativeModulePath(new File("/tests/" + currentInputSource.getRelativePath()),
                new File("/src"));
      } else if (importedInputSource instanceof ZipEntryInputSource) {
        npmPackageName = findSenchaPackageName((ZipEntryInputSource) importedInputSource);
      } else {
        throw new IllegalStateException("The input source for a compilation unit was not a file");
      }
      if (npmPackageName.startsWith("net.jangaroo__")) {
        // well-known vendor prefix net.jangaroo -> @jangaroo
        npmPackageName = npmPackageName.replace("net.jangaroo__", "@jangaroo/");
        // very special case jangaroo-runtime -> joo
        npmPackageName = npmPackageName.replace("/jangaroo-runtime", "/joo");
        // another special case: 'ext-as' is replaced by 'ext-ts' for everything in namespace 'Ext' and
        // by 'joo' for everything else:
        if (npmPackageName.endsWith("ext-as")) {
          npmPackageName = npmPackageName.replace("/ext-as", moduleName.startsWith("Ext") ? "/ext-ts" : "/joo");
        }
      } else if (npmPackageName.startsWith("com.coremedia")) {
        //npmPackageName = npmPackageName.replaceFirst("^com[.]coremedia[^_]*__", "@coremedia/");
        npmPackageName = "@coremedia/" + npmPackageName;
      }
      // prepend target npm package in front
      moduleName = npmPackageName + "/" + moduleName;
    }
    return moduleName;
  }

  private String computeRelativeModulePath(File currentFile, File importedFile) {
    File currentDir = currentFile.getParentFile();
    String relativeModulePath = CompilerUtils.getRelativePath(currentDir,
            importedFile, false);
    relativeModulePath = relativeModulePath.replace(File.separatorChar, '/');
    if (!relativeModulePath.startsWith(".")) {
      relativeModulePath = "./" + relativeModulePath;
    }
    return relativeModulePath;
  }

  private String findSenchaPackageName(ZipEntryInputSource zipEntryInputSource) {
    String npmPackageName = null;
    ZipFileInputSource zipFileInputSource = zipEntryInputSource.getZipFileInputSource();
    List<? extends InputSource> swcPkgFiles = zipFileInputSource.getChild("META-INF/pkg").list();
    for (InputSource swcPkgFile : swcPkgFiles) {
      String swcPkgFileName = swcPkgFile.getName();
      if (swcPkgFileName.endsWith(".json") && !swcPkgFileName.equals("package.json")
              && !swcPkgFileName.contains("-overrides")) {
        npmPackageName = CompilerUtils.removeExtension(swcPkgFileName);
        break;
      }
    }
    if (npmPackageName == null) {
      throw new IllegalStateException("SWC " + zipFileInputSource.getZipFile().getName() + " does not contain a /META-INF/pkg/<package-name>.json file");
    }
    return npmPackageName;
  }

  private String getRequireModulePath(IdeDeclaration declaration) {
    // exception: In TypeScript, "AS3.Error" is directly mapped to native "Error":
    String qualifiedName = declaration.getQualifiedNameStr();
    if ("Error".equals(qualifiedName)) {
      return null;
    }
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation == null) {
      Annotation renameAnnotation = declaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
      if (renameAnnotation != null) {
        qualifiedName = getNativeAnnotationValue(renameAnnotation);
      }
    } else {
      if (getNativeAnnotationRequireValue(nativeAnnotation) == null) {
        return null;
      }
      String nativeAnnotationValue = getNativeAnnotationValue(nativeAnnotation);
      if (nativeAnnotationValue != null) {
        qualifiedName = nativeAnnotationValue;
      }
    }
    return qualifiedName.replace('.', '/');
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
            Jooc.RENAME_ANNOTATION_NAME.equals(annotation.getMetaName()) ||
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
      if (variableDeclaration.getClassDeclaration().isInterface()) {
        out.writeSymbolWhitespace(variableDeclaration.getSymbol());
        out.writeToken("abstract");
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
          writeReadonlySuppressWhitespace(currentVariableDeclaration.getIde().getSymbol());
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
      if (!isAmbientOrInterface(compilationUnit)) {
        if (initializer != null) {
          initializer.visit(this);
        } else {
          // While AS3 automatically assigns default values to fields, TypeScript/ECMAScript don't,
          // so we have to add an explicit initializer to keep semantics:
          String implicitDefaultValue = VariableDeclaration.getDefaultValue(typeRelation);
          // no need to explicitly set a field to "undefined":
          if (!"undefined".equals(implicitDefaultValue)) {
            out.write(" = " + implicitDefaultValue);
          }
        }
      }
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
                || isAmbientOrInterface(compilationUnit)
                || initializer.getValue().getType() == null
                || !initializer.getValue().getType().equals(new ExpressionType(typeRelation.getType()))) {
          typeRelation.visit(this);
        }
      }
      visitIfNotNull(initializer);
    }
  }

  private String getDefinitionName(IdeDeclaration varOrFunDeclaration) {
    return String.format(useSymbolForPrivateMember(varOrFunDeclaration)
            ? "[$%s]" : "#%s", varOrFunDeclaration.getIde().getName());
  }

  private boolean useSymbolForPrivateMember(IdeDeclaration varOrFunDeclaration) {
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
      if (functionDeclaration.getClassDeclaration().isInterface()) {
        out.writeSymbolWhitespace(functionDeclaration.getSymbol());
        out.writeToken("abstract");
      }
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
          if (!useSymbolForPrivateMember(functionDeclaration)) {
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
        generateFunctionExprReturnTypeRelation(functionExpr);
      }
      if (functionDeclaration.isConstructor()
              && !functionDeclaration.containsSuperConstructorCall()
              && functionDeclaration.getClassDeclaration().getOptExtends() != null) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), (out, first) -> out.write("\n    super();"));
      }
      if (functionDeclaration.isThisAliased() && !functionDeclaration.isContainsSuperConstructorCall()) {
        addBlockStartCodeGenerator(functionDeclaration.getBody(), ALIAS_THIS_CODE_GENERATOR);
      }

      if (functionDeclaration.isPrivate() && !useSymbolForPrivateMember(functionDeclaration)) {
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
        Directive firstStatement = statements.get(0);
        if (firstStatement instanceof ReturnStatement) {
          Expr expr = ((ReturnStatement) firstStatement).getOptExpr();
          if (expr != null) {
            expr.visit(this);
            if (needsParenthesis) {
              out.write(")");
            }
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
    if (needsParenthesis) {
      out.write(")");
    }
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (applyExpr.isTypeCheckObjectLiteralFunctionCall()) {
      // it is an object literal type check call: transform
      // __typeCheckObjectLiteral__([t1, ..., tn], { O }) -> AS3._<t1 & ... & tn>({ O })
      CommaSeparatedList<Expr> typesAndObjectLiteral = args.getExpr();
      ArrayLiteral typesArray = (ArrayLiteral) typesAndObjectLiteral.getHead();
      Expr objectLiteral = typesAndObjectLiteral.getTail().getHead();
      writeSymbolReplacement(applyExpr.getSymbol(), "_");
      writeSymbolReplacement(typesArray.getLParen(), "<");
      for (CommaSeparatedList<Expr> current = typesArray.getExpr(); current != null; current = current.getTail()) {
        Expr typeExpr = current.getHead();
        if (typeExpr instanceof IdeExpr) {
          out.write(((IdeExpr) typeExpr).getIde().getName() + "._");
        } else {
          ArrayLiteral untypedProperties = (ArrayLiteral) typeExpr;
          writeSymbolReplacement(untypedProperties.getLParen(), "{");
          for (CommaSeparatedList<Expr> currentProperty = untypedProperties.getExpr();
               currentProperty != null;
               currentProperty = currentProperty.getTail()) {
            String propertyName = (String) ((LiteralExpr) currentProperty.getHead()).getValue().getJooValue();
            out.write(Ide.isValidIdentifier(propertyName) ? propertyName : CompilerUtils.quote(propertyName));
            writeOptSymbol(currentProperty.getSymComma());
          }
          writeSymbolReplacement(untypedProperties.getRParen(), "}");
        }
        if (current.getSymComma() != null) {
          writeSymbolReplacement(current.getSymComma(), "&");
        }
      }
      writeSymbolReplacement(typesArray.getRParen(), ">");
      out.writeSymbol(args.getLParen());
      objectLiteral.visit(this);
      out.writeSymbol(args.getRParen());
    } else if (applyExpr.isTypeCast()) {
      Expr typeCastedExpr = args.getExpr().getHead();
      if (typeCastedExpr instanceof ObjectLiteral) {
        // use config factory function instead of the class itself:
        writeSymbolReplacement(applyExpr.getSymbol(), "new " + ((IdeExpr)applyExpr.getFun()).getIde().getIde().getText() + "._");
        args.visit(this);
      } else if (isExtApply(typeCastedExpr)) {
        // If you type-cast the result of Ext.apply(), you are surely using config objects.
        // Since in TypeScript, Ext.apply() hands through the types of its parameters, it
        // should work without the type cast:
        out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
        out.writeSymbolWhitespace(args.getLParen());
        typeCastedExpr.visit(this);
        out.writeSymbolWhitespace(args.getRParen());
      } else {
        out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
        out.writeToken(builtInIdentifierCode("cast"));
        out.writeSymbol(args.getLParen());
        applyExpr.getFun().visit(this);
        out.writeToken(",");
        // isTypeCast() ensures that there is exactly one parameter:
        typeCastedExpr.visit(this);
        out.writeSymbol(args.getRParen());
      }
    } else {
      super.visitApplyExpr(applyExpr);
    }
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
    exmlAppendOrPrepend.getSymbol().setWhitespace("");
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
        if (!AS3Type.ANY.equals(type.getAS3Type())) {
          // dynamic property access on typed objects must be converted to ["<ide>"] in TypeScript:
          arg.visit(this);
          writeSymbolReplacement(dotExpr.getOp(), "[");
          writeSymbolReplacement(ide.getSymbol(), "'" + ide.getName() + "'");
          out.write("]");
          return;
        }
      } else {
        Annotation nativeAnnotation = memberDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
        String memberName = ide.getName();
        if (nativeAnnotation != null) {
          Object nativeMemberName = nativeAnnotation.getPropertiesByName().get(null);
          if (nativeMemberName instanceof String) {
            memberName = (String) nativeMemberName;
          }
        }
        if (!memberName.equals(ide.getName()) || memberDeclaration.isPrivate()) {
          arg.visit(this);
          if (memberDeclaration.isPrivate()) {
            if (useSymbolForPrivateMember(memberDeclaration)) {
              writeSymbolReplacement(dotExpr.getOp(), "[");
              writeSymbolReplacement(ide.getSymbol(), "$" + memberName);
              out.write("]");
            } else {
              out.writeSymbol(dotExpr.getOp());
              writeSymbolReplacement(ide.getSymbol(), "#" + memberName);
            }
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
            // found a typed member, need to downcast to 'object':
            out.writeSymbolWhitespace(indexedExpr.getSymbol());
            out.write("(");
            indexedExpr.visit(this);
            out.write(" as object)");
            indexExpr.visit(this);
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
//    writeSymbolReplacement(ideWithTypeParam.getSymDotLt(), "<");
//    ideWithTypeParam.getType().visit(this);
//    out.writeSymbol(ideWithTypeParam.getSymGt());
  }

  private String getDefaultImportName(IdeDeclaration declaration) {
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null && getNativeAnnotationRequireValue(nativeAnnotation) == null) {
      String nativeName = getNativeAnnotationValue(nativeAnnotation);
      return nativeName == null ? declaration.getQualifiedNameStr() : nativeName;
    }
    Annotation renameAnnotation = declaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
    if (renameAnnotation != null) {
      String targetName = getNativeAnnotationValue(renameAnnotation);
      if (targetName != null && !targetName.isEmpty()) {
        return CompilerUtils.className(targetName);
      }
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
    } else if (declaration instanceof Parameter && ((Parameter) declaration).isRest()
            && FunctionExpr.ARGUMENTS.equals(declaration.getName())) {
      // parameter name "arguments" is not allowed in ECMAScript strict to avoid confusion with the built-in
      // "arguments", so let's rename this:
      localName = FunctionExpr.ARGUMENTS + "$";
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

  @Override
  protected String builtInIdentifierCode(String builtInIdentifier) {
    assert compilationUnit.getUsedBuiltInIdentifiers().contains(builtInIdentifier) : "Usage of built-in identifier '" + builtInIdentifier + "' has not been analyzed.";
    return builtInIdentifier;
  }
}
