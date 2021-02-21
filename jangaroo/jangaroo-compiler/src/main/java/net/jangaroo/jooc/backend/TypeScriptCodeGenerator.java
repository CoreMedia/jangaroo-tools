package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
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
import net.jangaroo.jooc.model.MethodType;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TypeScriptCodeGenerator extends CodeGeneratorBase {

  private static final Collection<String> TYPESCRIPT_BUILT_IN_TYPES = Arrays.asList(
          "Object",
          "Array",
          "Vector$object"
  );
  public static final List<AS3Type> TYPES_ALLOWED_AS_INDEX = Arrays.asList(AS3Type.ANY, AS3Type.STRING, AS3Type.NUMBER, AS3Type.INT, AS3Type.UINT);

  private static final Pattern INDENTATION_PATTERN = Pattern.compile("\n *\\z");

  /**
   * The TypeScript compiler directive to suppress errors regarding unsupported usage of #private names.
   * The error number 18022 does not have any effect on the compiler, but we add it to easily identify the
   * corresponding ts-expect-error lines to remove as soon as the feature is fully supported.
   */
  private static final String TS_EXPECT_ERROR_18022 = "//@ts-expect-error 18022";

  private static final String I_RESOURCE_MANAGER_QUALIFIED_NAME = "mx.resources.IResourceManager";
  private static final String GET_STRING_METHOD_NAME = "getString";

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

  TypeScriptCodeGenerator(TypeScriptModuleResolver typeScriptModuleResolver, JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
    this.typeScriptModuleResolver = typeScriptModuleResolver;
  }

  @Override
  protected void writeModifiers(JsWriter out, IdeDeclaration declaration) throws IOException {
    boolean isPrimaryDeclaration = declaration.isPrimaryDeclaration();
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbolWhitespace(modifier);
      if (!isPrimaryDeclaration && !companionInterfaceMode) {
        if (modifier.sym == sym.PROTECTED
                || modifier.sym == sym.IDE && SyntacticKeywords.STATIC.equals(modifier.getText())) {
          out.writeSymbol(modifier, false);
        } else if (modifier.sym == sym.PRIVATE && noSupportForHashPrivate(declaration)) {
          // As long as tsc does not yet support private members other than instance fields,
          // insert @ts-expect-error compiler directive, always as a separate line that can easily be removed later.
          // So repeat the same indentation if possible:
          Matcher indentationMatcher = INDENTATION_PATTERN.matcher(modifier.getWhitespace());
          out.write(indentationMatcher.find()
                  ? TS_EXPECT_ERROR_18022 + indentationMatcher.group()
                  : "\n  " + TS_EXPECT_ERROR_18022 + "\n");
        }
      }
    }
    if (isPrimaryDeclaration) {
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

    if (!compilationUnit.getUsedBuiltInIdentifiers().isEmpty()) {
      out.write(String.format("import {%s} from '@jangaroo/joo/AS3';\n",
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
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
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
    for (String dependentCUId : compilationUnit.getTransitiveDependencies()) {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);
      IdeDeclaration dependentPrimaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = typeScriptModuleResolver.getRequireModuleName(compilationUnit, dependentPrimaryDeclaration);
      String localName = typeScriptModuleResolver.getDefaultImportName(dependentPrimaryDeclaration);
      if (requireModuleName != null) {
        if (!isModule) {
          // import from non-module to module must be inlined:
          localName = String.format("import('%s').default", requireModuleName);
        } else if (localNameClashes.contains(localName)) {
          // resolve name clashes by using transformed fully-qualified name ('.' -> '_'):
          localName = TypeScriptModuleResolver.toLocalName(dependentPrimaryDeclaration.getQualifiedName());
        }
      }
      imports.put(dependentPrimaryDeclaration.getQualifiedNameStr(), localName);
      if (isModule && requireModuleName != null) {
        out.write(String.format("import %s from '%s';\n", localName, requireModuleName));
      }
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

    // pull out private static VariableDeclarations so that they can be used in Properties / Config class
    // initializers, too:
    for (TypedIdeDeclaration staticMember : classDeclaration.getStaticMembers().values()) {
      if (staticMember.isPrivate() && staticMember.isDeclaringStandAloneConstant()) {
        visitPrivateStaticVarWithSimpleInitializer((VariableDeclaration) staticMember);
      }
    }

    needsCompanionInterface = false;
    List<Ide> mixins = new ArrayList<>();
    ClassDeclaration configClass = classDeclaration.getConfigClassDeclaration();
    String configClassName = null;
    String ownPropertiesClassName = null;
    String ownConfigsClassName = null;
    String configsFromProps = null;
    String classDeclarationLocalName = getLocalName(classDeclaration, false);
    if (configClass != null) {
      configClassName = compilationUnitAccessCode(configClass) + "._";
      List<TypedIdeDeclaration> properties = classDeclaration.getMembers().stream()
              .filter(typedIdeDeclaration -> !typedIdeDeclaration.isMixinMemberRedeclaration() && typedIdeDeclaration.isExtConfig())
              .collect(Collectors.toList());
      List<TypedIdeDeclaration> configs = classDeclaration.getMembers().stream()
              .filter(typedIdeDeclaration -> !typedIdeDeclaration.isMixinMemberRedeclaration() && typedIdeDeclaration.isBindable())
              .collect(Collectors.toList());
      if (!properties.isEmpty()) {
        ownPropertiesClassName = classDeclarationLocalName + "Properties";
        out.write(String.format("\nclass %s {", ownPropertiesClassName));
        for (TypedIdeDeclaration propertiesDeclaration : properties) {
          visitAsConfig(propertiesDeclaration);
        }
        out.write("}\n");
        configsFromProps = String.format("Partial<%s>", ownPropertiesClassName);
        mixins.add(new Ide(ownPropertiesClassName));
        needsCompanionInterface = true;
      }
      if (!configs.isEmpty()) {
        ownConfigsClassName = classDeclarationLocalName + "Configs";
        out.write(String.format("\nclass %s {", ownConfigsClassName));
        for (TypedIdeDeclaration configDeclaration : configs) {
          visitAsConfig(configDeclaration);
        }
        out.write("}\n");
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
          if (maybeMixinDeclaration.hasConfigClass()) {
            configMixins.add(compilationUnitAccessCode(maybeMixinDeclaration) + "._");
          }
        } else {
          realInterfaces.add(superTypes.getHead());
        }
        superTypes = superTypes.getTail();
      } while (superTypes != null);
    }

    if (configClassName != null) {
      List<String> configExtends = new ArrayList<>(configMixins);
      ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
      if (superTypeDeclaration != null && superTypeDeclaration.hasConfigClass()) {
        configExtends.add(compilationUnitAccessCode(superTypeDeclaration) + "._");
      }
      configExtends.addAll(configMixins);
      if (configsFromProps != null) {
        configExtends.add(configsFromProps);
      }
      if (ownConfigsClassName != null) {
        configExtends.add(String.format("Partial<%s>", ownConfigsClassName));
      }
      out.write("interface " + classDeclarationLocalName + "_");
      if (!configExtends.isEmpty()) {
        out.write(" extends " + String.join(", ", configExtends));
      }
      out.write(" {\n");
      out.write("}\n\n");
    }

    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbolWhitespace(classDeclaration.getSymClass());
    if (isAmbientInterface(classDeclaration.getCompilationUnit())) {
      out.writeToken("interface");
    } else {
      if (classDeclaration.isInterface()) {
        out.writeToken("abstract");
      }
      out.writeToken("class");
    }
    writeSymbolReplacement(classDeclaration.getIde().getSymbol(), getLocalName(classDeclaration, false));
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
              null,
              classDeclaration.getOptImplements(),
              configClassName != null,
              realInterfaces);
    }

    classDeclaration.getBody().visit(this);

    if (needsCompanionInterface) {
      out.write("\ninterface " + classDeclarationLocalName + configTypeParameterDeclaration);
      // output "extends [Required<...Configs>,] [<mixin-interfaces>]"
      visitImplementsFiltered(
              new JooSymbol("extends"),
              ownPropertiesClassName,
              classDeclaration.getOptImplements(),
              configClassName != null,
              mixins);
      // visit class body again in "companion interface" mode
      companionInterfaceMode = true;
      classDeclaration.getBody().visit(this);
      companionInterfaceMode = false;
      out.write("\n");
    }

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

      List<Annotation> metadata = classDeclaration.getMetadata();
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

    if (classDeclaration.isPrimaryDeclaration()) {
      visitAll(classDeclaration.getSecondaryDeclarations());

      if (configClassName != null) {
        out.write(String.format("\ndeclare namespace %s {\n", classDeclarationLocalName));
        out.write(String.format("  export type _ = %s_;\n", classDeclarationLocalName));
        out.write("  export const _: { new(config?: _): _; };\n");
        out.write("}\n\n");
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

  private void visitPrivateStaticVarWithSimpleInitializer(VariableDeclaration privateStaticVar) throws IOException {
    out.writeSymbolWhitespace(privateStaticVar.getSymbol());
    out.writeSymbol(privateStaticVar.getOptSymConstOrVar());
    privateStaticVar.getIde().visit(this);
    visitIfNotNull(privateStaticVar.getOptTypeRelation());
    generateInitializer(privateStaticVar);
    writeOptSymbol(privateStaticVar.getOptSymSemicolon());
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isCurrentMixinInterface(Ide head) {
    return CompilationUnit.mapMixinInterface(head.getDeclaration().getCompilationUnit()).equals(compilationUnit);
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
        // head must be included in filter and
        // must not be a mixin class's mixin interface: 
        if (filter.contains(head) && !isCurrentMixinInterface(head)) {
          out.writeSymbol(lastSym);
          lastSym = current.getSymComma();
          head.visit(this);
          if (useCfgTypeParameter && useCfgTypeParameter(head)) {
            // hand through my Config class type parameter:
            out.write("<Cfg>");
          }
        }
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
    visitIfNotNull(configDeclaration.getOptTypeRelation());
    if (configDeclaration instanceof VariableDeclaration) {
      VariableDeclaration variableDeclaration = (VariableDeclaration) configDeclaration;
      generateInitializer(variableDeclaration);
      optSymSemicolon = variableDeclaration.getOptSymSemicolon();
    } else if (configDeclaration instanceof PropertyDeclaration) {
      PropertyDeclaration propertyDeclaration = (PropertyDeclaration) configDeclaration;
      if (propertyDeclaration.getGetter() != null) {
        optSymSemicolon = propertyDeclaration.getGetter().getOptSymSemicolon();
      }
    }
    writeOptSymbol(optSymSemicolon, ";");
  }

  private boolean useCfgTypeParameter(Ide superType) {
    IdeDeclaration declaration = superType.getDeclaration();
    return declaration instanceof ClassDeclaration && ((ClassDeclaration) declaration).hasConfigClass();
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
      if (variableDeclaration.isExtConfigOrBindable() ||
              variableDeclaration.isPrivateStatic() && variableDeclaration.isDeclaringStandAloneConstant()) {
        // never render [ExtConfig]s or private statics with "simple" initializers in a normal "visit":
        return;
      }
      if (isNonAmbientInterface(variableDeclaration.getClassDeclaration().getCompilationUnit())) {
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
        writeSymbolReplacement(ide.getSymbol(), getHashPrivateName(variableDeclaration));
      } else {
        ide.visit(this);
      }
      visitIfNotNull(typeRelation);
      if (!isAmbientOrInterface(compilationUnit)) {
        generateInitializer(variableDeclaration);
      }
    } else if (variableDeclaration.isPrimaryDeclaration()
            && !variableDeclaration.isConst()
            && typeScriptModuleResolver.getRequireModuleName(compilationUnit, variableDeclaration) != null) {
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
                || initializer.getValue() instanceof ObjectLiteral   // do not suppress type for object literals
                || !initializer.getValue().getType().equals(variableDeclaration.getType())) {
          typeRelation.visit(this);
        }
      }
      visitIfNotNull(initializer);
    }
  }

  private void generateInitializer(VariableDeclaration variableDeclaration) throws IOException {
    Initializer initializer = variableDeclaration.getOptInitializer();
    if (initializer != null) {
      initializer.visit(this);
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

  private boolean noSupportForHashPrivate(IdeDeclaration varOrFunDeclaration) {
    return varOrFunDeclaration.isStatic()
            || !varOrFunDeclaration.isNative()
            && (varOrFunDeclaration instanceof PropertyDeclaration
            || varOrFunDeclaration instanceof FunctionDeclaration);
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
      boolean isAmbientOrInterface = isAmbientOrInterface(functionDeclaration.getCompilationUnit());
      boolean convertToProperty = functionDeclaration.isGetterOrSetter() &&
              (functionDeclaration.isNative() || isAmbientOrInterface);
      if (convertToProperty) {
        if (functionDeclaration.isSetter()) {
          // completely suppress (native) setter class members, they are covered by the writable property declaration
          return;
        }
        if (!functionDeclaration.isStatic() && functionDeclaration.isPublic()) {
          // may be an Ext Config / Bindable:
          IdeDeclaration memberDeclaration = functionDeclaration.getClassDeclaration().getMemberDeclaration(functionDeclaration.getName());
          if (memberDeclaration instanceof PropertyDeclaration && ((PropertyDeclaration) memberDeclaration).isExtConfigOrBindable()) {
            // never render [ExtConfig]s in a normal "visit":
            return;
          }
        }
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
      if (isNonAmbientInterface(functionDeclaration.getClassDeclaration().getCompilationUnit())) {
        out.writeSymbolWhitespace(functionDeclaration.getSymbol());
        out.writeToken("abstract");
      }
      // leave out "function" symbol for class members!
      writeOptSymbolWhitespace(functionDeclaration.getSymbol());
      boolean convertAccessorToMethod = false;
      if (convertToProperty) {
        if (!hasSetter(functionDeclaration)) { // a native getter without setter => readonly property!
          writeReadonlySuppressWhitespace(functionDeclaration.getIde().getSymbol());
        }
      } else {
        convertAccessorToMethod = functionDeclaration.isGetterOrSetter()
                && functionDeclaration.getAnnotation(Jooc.BINDABLE_ANNOTATION_NAME) != null;
        if (!convertAccessorToMethod) {
          writeOptSymbol(functionDeclaration.getSymGetOrSet());
        }
      }
      if (functionDeclaration.isConstructor()) {
        writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), "constructor");
      } else {
        if (functionDeclaration.isPrivate()) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(), getHashPrivateName(functionDeclaration));
        } else if (convertAccessorToMethod) {
          writeSymbolReplacement(functionDeclaration.getIde().getSymbol(),
                  getBindablePropertyName(functionDeclaration.isGetter() ? MethodType.GET : MethodType.SET,
                          functionDeclaration));
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

      visitIfNotNull(functionExpr.getBody());
      writeOptSymbol(functionDeclaration.getOptSymSemicolon());
    } else {
      if (functionDeclaration.isPrimaryDeclaration()) {
        visitDeclarationAnnotationsAndModifiers(functionDeclaration);
      }
      functionExpr.visit(this);
      writeOptSymbolWhitespace(functionDeclaration.getOptSymSemicolon());
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
          out.writeSymbolWhitespace(functionExpr.getBody().getLBrace());
          ReturnStatement returnStatement = (ReturnStatement) firstStatement;
          out.writeSymbolWhitespace(returnStatement.getSymbol());
          Expr expr = returnStatement.getOptExpr();
          if (expr != null) {
            boolean needsInnerParenthesis = expr instanceof ObjectLiteral;
            if (needsInnerParenthesis) {
              out.writeSymbolWhitespace(expr.getSymbol());
              out.write("(");
            }
            expr.visit(this);
            if (needsInnerParenthesis) {
              out.write(")");
            }
          } else {
            // a sole return without and value should be a rare case, but who knows:
            out.writeToken("undefined");
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
      IdeDeclaration declaration = ((IdeExpr) applyExpr.getFun()).getIde().getDeclaration();
      if (declaration instanceof ClassDeclaration && ((ClassDeclaration) declaration).hasConfigClass()) {
        if (isOfConfigType(args.getExpr().getHead())) {
          // use config factory function instead of the class itself:
          writeSymbolReplacement(applyExpr.getSymbol(), "new " + compilationUnitAccessCode(declaration) + "._");
          args.visit(this);
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
          key  = (String) jooValue;
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
    } else {
      super.visitApplyExpr(applyExpr);
    }
  }

  private static boolean isIResourceManager_getString(ApplyExpr applyExpr) {
    Expr fun = applyExpr.getFun();
    if (fun instanceof IdeExpr) {
      fun = ((IdeExpr) fun).getNormalizedExpr();
    }
    if (fun instanceof DotExpr && GET_STRING_METHOD_NAME.equals(((DotExpr) fun).getIde().getName())) {
      ExpressionType type = ((DotExpr) fun).getArg().getType();
      CommaSeparatedList<Expr> argsExpressions = applyExpr.getArgs().getExpr();
      return type != null && I_RESOURCE_MANAGER_QUALIFIED_NAME.equals(type.getDeclaration().getQualifiedNameStr())
              && argsExpressions != null && argsExpressions.getTail() != null && argsExpressions.getTail().getTail() == null // call uses exactly 2 arguments
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
        if (!"any".equals(getTypeScriptTypeForActionScriptType(type))) {
          // dynamic property access on typed objects must be converted to ["<ide>"] in TypeScript:
          arg.visit(this);
          writeSymbolReplacement(dotExpr.getOp(), "[");
          writeSymbolReplacement(ide.getSymbol(), "'" + ide.getName() + "'");
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
        if (memberDeclaration.isPrivateStatic() && memberDeclaration.isDeclaringStandAloneConstant()) {
          // suppress "<Class>." (and do not use "#" prefix) for private statics with simple initializer:
          out.writeSymbolWhitespace(arg.getSymbol());
          out.writeSymbolWhitespace(dotExpr.getOp());
          out.writeSymbol(dotExpr.getIde().getIde());
          return;
        }
        if (!ide.isAssignmentLHS()) {
          IdeDeclaration bindableConfigDeclarationCandidate = getBindableConfigDeclarationCandidate(type, ide);
          if (bindableConfigDeclarationCandidate != null) {
            TypedIdeDeclaration getter = findMemberWithBindableAnnotation(ide, MethodType.GET, bindableConfigDeclarationCandidate.getClassDeclaration());
            if (getter != null) {
              // found usage of a [Bindable]-annotated property: replace property access by arg.getConfig("memberName"):
              memberName = "getConfig(" + CompilerUtils.quote(ide.getName()) + ")";
            }
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

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
    Expr lhs = assignmentOpExpr.getArg1();
    if (lhs instanceof IdeExpr) {
      lhs = ((IdeExpr) lhs).getNormalizedExpr();
    }
    if (lhs instanceof DotExpr) {
      DotExpr dotExpr = (DotExpr) lhs;
      Expr arg = dotExpr.getArg();
      ExpressionType type = arg.getType();
      Ide ide = dotExpr.getIde();
      IdeDeclaration memberDeclaration = getBindableConfigDeclarationCandidate(type, ide);
      if (memberDeclaration != null) {
        TypedIdeDeclaration setter = findMemberWithBindableAnnotation(ide, MethodType.SET, memberDeclaration.getClassDeclaration());
        if (setter != null) {
          // found usage of a [Bindable]-annotated property: replace property write by lhsArg.setConfig("memberName", rhsExpr):
          arg.visit(this);
          out.writeSymbol(dotExpr.getOp());
          out.write("setConfig(");
          writeSymbolReplacement(ide.getSymbol(), CompilerUtils.quote(ide.getName()));
          writeSymbolReplacement(assignmentOpExpr.getOp(), ",");
          assignmentOpExpr.getArg2().visit(this);
          out.write( ")");
          return;
        }
      }
    }
    super.visitAssignmentOpExpr(assignmentOpExpr);
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
            // found a typed member, need to downcast to 'object':
            out.writeSymbolWhitespace(indexedExpr.getSymbol());
            out.write("(");
            indexedExpr.visit(this);
            out.write(" as unknown)");
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
        if (declaration instanceof VariableDeclaration && !((VariableDeclaration) declaration).isConst()
                && typeScriptModuleResolver.getRequireModuleName(compilationUnit, declaration) != null) {
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
