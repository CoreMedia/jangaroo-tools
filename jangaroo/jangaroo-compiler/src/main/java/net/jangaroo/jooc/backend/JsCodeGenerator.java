package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.Debug;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.*;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.jooc.util.MessageFormat;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A visitor of the AST that generates executable JavaScript code on
 * a {@link net.jangaroo.jooc.JsWriter}.
 */
public class JsCodeGenerator extends CodeGeneratorBase {
  private static final JooSymbol SYM_VAR = new JooSymbol(sym.VAR, "var"); // NOSONAR introducing a constant for "var" would obscure the generated output
  private static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");
  private static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  private static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  public static final Set<String> PRIMITIVES = new HashSet<String>(4);
  public static final List<String> ANNOTATIONS_TO_TRIGGER_AT_RUNTIME = Arrays.asList("SWF", "ExtConfig"); // TODO: inject / make configurable
  public static final List<String> ANNOTATIONS_FOR_COMPILER_ONLY = Arrays.asList(
          Jooc.NATIVE_ANNOTATION_NAME,
          Jooc.RENAME_ANNOTATION_NAME,
          Jooc.EMBED_ANNOTATION_NAME,
          Jooc.BINDABLE_ANNOTATION_NAME,
          Jooc.ARRAY_ELEMENT_TYPE_ANNOTATION_NAME,
          Jooc.EXT_CONFIG_ANNOTATION_NAME,
          Jooc.RESOURCE_BUNDLE_ANNOTATION_NAME,
          Jooc.MIXIN_ANNOTATION_NAME,
          Jooc.MIXIN_HOOK_ANNOTATION_NAME,
          Jooc.EXT_PRIVATE_ANNOTATION_NAME,
          Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME,
          Jooc.PUBLIC_API_EXCLUSION_ANNOTATION_NAME,
          Jooc.EVENT_ANNOTATION_NAME,
          Jooc.LAZY_ANNOTATION_NAME,
          Jooc.PARAMETER_ANNOTATION_NAME,
          Jooc.RETURN_ANNOTATION_NAME
  );
  public static final String DEFAULT_ANNOTATION_PARAMETER_NAME = "";
  public static final String INIT_STATICS = "__initStatics__";

  static {
    PRIMITIVES.add("Boolean");
    PRIMITIVES.add("String");
    PRIMITIVES.add("Number");
    PRIMITIVES.add("int");
    PRIMITIVES.add("uint");
    PRIMITIVES.add("Object");
    PRIMITIVES.add("RegExp");
    PRIMITIVES.add("Date");
    PRIMITIVES.add("Array");
    PRIMITIVES.add("Error");
    PRIMITIVES.add("Vector");
    PRIMITIVES.add("Class");
    PRIMITIVES.add("Function");
    PRIMITIVES.add("XML");
  }

  public static boolean generatesCode(IdeDeclaration primaryDeclaration) {
    // only generate JavaScript if [Native] / [Mixin] annotation and 'native' modifier on primary compilationUnit are not present:
    return primaryDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null
            && !primaryDeclaration.isNative()
            && primaryDeclaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME) == null;
  }

  private boolean expressionMode = false;
  private Map<String,String> imports = new HashMap<String,String>();
  private ClassDefinitionBuilder primaryClassDefinitionBuilder = new ClassDefinitionBuilder();
  private int staticCodeCounter = 0;
  private ClassDefinitionBuilder secondaryClassDefinitionBuilder;
  private CompilationUnit compilationUnit;
  private String factory;
  private final MessageFormat $NAME_EQUALS_ARGUMENTS_SLICE_$INDEX =
    new MessageFormat("{0}=Array.prototype.slice.call(arguments{1,choice,0#|0<,{1}});");

  private void generateToArrayCode(String paramName, int paramIndex) throws IOException {
    if (!FunctionExpr.ARGUMENTS.equals(paramName)) {
      out.writeToken("var");
    }
    out.writeToken($NAME_EQUALS_ARGUMENTS_SLICE_$INDEX.format(paramName, paramIndex));
  }

  private final CodeGenerator ARGUMENT_TO_ARRAY_CODE_GENERATOR = new CodeGenerator() {
    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      generateToArrayCode(FunctionExpr.ARGUMENTS, 0);
    }
  };

  public JsCodeGenerator(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    super(out, compilationUnitModelResolver);
  }

  private Map<String,PropertyDefinition> membersOrStaticMembers(Declaration memberDeclaration) {
    ClassDefinitionBuilder classDefinitionBuilder = getClassDefinitionBuilder(memberDeclaration);
    return memberDeclaration.isStatic() ? classDefinitionBuilder.staticMembers : classDefinitionBuilder.members;
  }

  private ClassDefinitionBuilder getClassDefinitionBuilder(Declaration memberDeclaration) {
    return memberDeclaration.getClassDeclaration() == null || memberDeclaration.getClassDeclaration().isPrimaryDeclaration() ? primaryClassDefinitionBuilder : secondaryClassDefinitionBuilder;
  }

  String compilationUnitAccessCode(IdeDeclaration primaryDeclaration) {
    CompilationUnit otherUnit = primaryDeclaration.getCompilationUnit();
    if (otherUnit == compilationUnit) {
      return primaryDeclaration.getName();
    } else {
      primaryDeclaration = compilationUnit.mapMixinInterface(otherUnit).getPrimaryDeclaration();
      String primaryDeclarationName = imports.get(primaryDeclaration.getQualifiedNameStr());
      Debug.assertTrue(primaryDeclarationName != null, "QName not found in imports: " + primaryDeclaration.getQualifiedNameStr());
      return primaryDeclarationName;
    }
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    IdeDeclaration memberDeclaration = null;
    Expr arg = dotExpr.getArg();
    JooSymbol symDot = dotExpr.getOp();
    Ide ide = dotExpr.getIde();

    if (arg instanceof IdeExpr) {
      arg = ((IdeExpr)arg).getNormalizedExpr();
    }
    ExpressionType type = arg.getType();
    if (type != null) {
      memberDeclaration = type.resolvePropertyDeclaration(ide.getName());
    }

    String memberName = ide.getName();

    if (memberDeclaration != null && memberDeclaration.isPrivateStatic()) {
      // comment out explicit reference to class for private static access:
      out.beginComment();
      arg.visit(this);
      out.writeSymbol(symDot);
      out.endComment();
      // add "$static" suffix to private static members:
      writeSymbolReplacement(ide.getIde(), memberName + "$static");
      return;
    }

    if (memberDeclaration != null) {
      if (memberDeclaration.isPrivate()) {
        memberName = memberName + "$" + ide.getScope().getClassDeclaration().getQualifiedNameHash();
      }
    }

    String separatorToken = ".";
    String closingToken = "";

    if (ide.isBound()) {
      // found access to a method without applying it immediately: bind!
      out.writeToken("AS3.bind(");
      separatorToken = ",";
      memberName = CompilerUtils.quote(memberName);
      closingToken = ")";
    } else if (memberDeclaration != null && !ide.isAssignmentLHS()) {
      TypedIdeDeclaration getter = findMemberWithBindableAnnotation(ide, MethodType.GET, memberDeclaration.getClassDeclaration());
      if (getter != null) {
        // found usage of an [Bindable]-annotated get function: call it via AS3.getBindable()!
        Expr normalizedArg = arg instanceof IdeExpr ? ((IdeExpr) arg).getNormalizedExpr() : arg;
        out.writeSymbolWhitespace(normalizedArg.getSymbol());
        out.writeToken("AS3.getBindable(");
        memberName = CompilerUtils.quote(getter.getName());
        separatorToken = ",";
        closingToken = ")";
        String bindableEvent = getBindableEventName(getter);
        if (bindableEvent != null && !"DUMMY".equals(bindableEvent)) {
          closingToken = "," + CompilerUtils.quote(bindableEvent) + closingToken;
        }
      }
    } else if (memberName.startsWith("@")) {
      // escape @... property names:
      separatorToken = "[";
      memberName = CompilerUtils.quote(memberName);
      closingToken = "]";
    }

    arg.visit(this);
    writeSymbolReplacement(symDot, separatorToken);
    writeSymbolReplacement(ide.getIde(), memberName);
    if (!closingToken.isEmpty()) {
      out.writeToken(closingToken);
    }
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    out.beginCommentWriteSymbol(typeRelation.getSymRelation());
    typeRelation.getType().getIde().visit(this);
    out.endComment();
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    out.writeSymbol(anExtends.getSuperClass().getIde());
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    boolean isClassDeclaration = primaryDeclaration instanceof ClassDeclaration;
    String[] requires = collectDependencies(compilationUnit, isClassDeclaration ? Boolean.TRUE : null);
    String[] uses = isClassDeclaration ? collectDependencies(compilationUnit, false) : new String[0];
    PackageDeclaration packageDeclaration = compilationUnit.getPackageDeclaration();
    this.compilationUnit = compilationUnit;
    out.beginComment();
    packageDeclaration.visit(this);
    out.writeSymbol(compilationUnit.getLBrace());
    visitAll(compilationUnit.getDirectives());
    out.endComment();
    primaryDeclaration.visit(this);
    out.beginComment();
    out.writeSymbol(compilationUnit.getRBrace());
    out.write("\n");
    out.write("\n============================================== Jangaroo part ==============================================");
    out.endComment();
    JsonObject classDefinition = createClassDefinition(primaryDeclaration, primaryClassDefinitionBuilder);
    if (requires.length > 0) {
      classDefinition.set("requires", new JsonArray(requires));
    }
    if (uses.length > 0) {
      classDefinition.set("uses", new JsonArray(uses));
    }
    out.write("\n    return " + classDefinition.toString(2, 4) + ";\n}");
    out.write(");\n");
  }

  private JsonObject createClassDefinition(IdeDeclaration declaration, ClassDefinitionBuilder classDefinitionBuilder) throws IOException {
    JsonObject classDefinition;
    if (declaration instanceof ClassDeclaration && ((ClassDeclaration) declaration).isInterface()) {
      classDefinition = createInterfaceDefinition((ClassDeclaration) declaration);
    } else {
      if (declaration instanceof ClassDeclaration) {
        classDefinition = createClassDefinition((ClassDeclaration) declaration);
      } else {
        classDefinition = new JsonObject();
        boolean isLazy = declaration.getAnnotation(Jooc.LAZY_ANNOTATION_NAME) != null;
        classDefinition.set(isLazy ? "__lazyFactory__" : "__factory__", JsonObject.code(factory));
      }
      fillClassDefinition(classDefinition, classDefinitionBuilder);
    }
    return classDefinition;
  }

  private JsonObject createInterfaceDefinition(ClassDeclaration interfaceDeclaration) {
    JsonObject interfaceDefinition = new JsonObject();
    addOptImplements(interfaceDeclaration, interfaceDefinition);
    return interfaceDefinition;
  }

  private void addOptImplements(ClassDeclaration classDeclaration, JsonObject classDefinition) {
    Implements optImplements = classDeclaration.getOptImplements();
    if (optImplements != null) {
      List<String> superInterfaces = new ArrayList<String>();
      CommaSeparatedList<Ide> superTypes = optImplements.getSuperTypes();
      while (superTypes != null) {
        IdeDeclaration superInterface = superTypes.getHead().getDeclaration(false);
        if (superInterface == null) {
          System.err.println("ignoring unresolvable interface " + superTypes.getHead().getQualifiedNameStr());
        } else {
          CompilationUnit mixinCompilationUnit = compilationUnit.mapMixinInterface(superInterface.getCompilationUnit());
          if (!compilationUnit.equals(mixinCompilationUnit)) {
            superInterfaces.add(compilationUnitAccessCode(superInterface));
          }
        }
        superTypes = superTypes.getTail();
      }
      if (!superInterfaces.isEmpty()) {
        if (classDeclaration.isInterface() && superInterfaces.size() == 1) {
          // if interface has just one super interface, let Ext class extend super interface Ext class:
          classDefinition.set("extend", superInterfaces.get(0));
        } else {
          // is class or has more than one super interface: mix-in interfaces Ext classes
          classDefinition.set("mixins", new JsonArray(superInterfaces.toArray()));
        }
      }
    }
  }

  private String[] collectDependencies(CompilationUnit compilationUnit, Boolean required) throws IOException {
    Set<String> requires = new TreeSet<>();
    Collection<String> dependentCompilationUnits = compilationUnit.getTransitiveDependencies();
    for (String dependentCUId : dependentCompilationUnits) {
      if (required != null && compilationUnit.isRequiredDependency(dependentCUId) != required) {
        continue;
      }

      String javaScriptName;
      String javaScriptNameToRequire;
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(dependentCUId);

      IdeDeclaration primaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();

      Annotation nativeAnnotation = primaryDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotation == null) {
        Annotation renameAnnotation = primaryDeclaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
        javaScriptName = renameAnnotation == null ? null : getNativeAnnotationValue(renameAnnotation);
        if (javaScriptName == null) {
          javaScriptName = dependentCUId;
        }
        javaScriptNameToRequire = javaScriptName;
      } else {
        String javaScriptAlias = getNativeAnnotationValue(nativeAnnotation);
        if (javaScriptAlias != null) {
          javaScriptName = javaScriptAlias;
        } else {
          javaScriptName = dependentCUId;
        }
        javaScriptNameToRequire = getNativeAnnotationRequireValue(nativeAnnotation);
        if ("".equals(javaScriptNameToRequire)) {
          javaScriptNameToRequire = javaScriptName;
        }
      }
      if (javaScriptNameToRequire != null) {
        requires.add(javaScriptNameToRequire);
      }
      imports.put(dependentCUId, javaScriptName);
    }

    return requires.toArray(new String[requires.size()]);
  }

  private JsonObject createClassDefinition(ClassDeclaration classDeclaration) throws IOException {
    JsonObject classDefinition = new JsonObject();
    if (classDeclaration.notExtendsObject()) {
      ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
      classDefinition.set("extend", compilationUnitAccessCode(superTypeDeclaration));
    }
    addOptImplements(classDeclaration, classDefinition);
    return classDefinition;
  }

  private void fillClassDefinition(JsonObject classDefinition, ClassDefinitionBuilder classDefinitionBuilder) throws IOException {
    if (!classDefinitionBuilder.metadata.isEmpty()) {
      classDefinition.set("metadata", classDefinitionBuilder.metadata);
    }
    if (!classDefinitionBuilder.mixinConfig.isEmpty()) {
      classDefinition.set("mixinConfig", classDefinitionBuilder.mixinConfig);
    }
    JsonObject members = convertMembers(classDefinitionBuilder.members, false);
    JsonObject bindables = convertBindables(classDefinitionBuilder.members);
    if (!bindables.isEmpty()) {
      members.set("config", bindables);
    }
    JsonObject extPrivateMembers = convertMembers(classDefinitionBuilder.members, true);
    if (!extPrivateMembers.isEmpty()) {
      members.set("privates", extPrivateMembers);
    }
    if (!members.isEmpty()) {
      classDefinition.add(members);
    }
    JsonObject staticMembers = convertMembers(classDefinitionBuilder.staticMembers, false);
    if (!staticMembers.isEmpty() || classDefinitionBuilder.staticCode.length() > 0) {
      if (classDefinitionBuilder.staticCode.length() > 0) {
        String staticInitializer = String.format("function() {\n%s        }",
                classDefinitionBuilder.staticCode.toString());
        staticMembers.set(INIT_STATICS, JsonObject.code(staticInitializer));
      }
      classDefinition.set("statics", staticMembers);
    }
    JsonObject accessors = convertAccessors(classDefinitionBuilder.members);
    JsonObject staticAccessors = convertAccessors(classDefinitionBuilder.staticMembers);
    if (!staticAccessors.isEmpty()) {
      accessors.set("statics", staticAccessors);
    }
    if (!accessors.isEmpty()) {
      classDefinition.set("__accessors__", accessors);
    }
  }

  private JsonObject convertMembers(Map<String, PropertyDefinition> members, boolean extPrivate) {
    JsonObject membersDefinition = new JsonObject();
    for (Map.Entry<String, PropertyDefinition> entry : members.entrySet()) {
      if (entry.getValue().isValueOnly() && !entry.getValue().bindable && entry.getValue().extPrivate == extPrivate) {
        membersDefinition.set(entry.getKey(), JsonObject.code(entry.getValue().value));
      }
    }
    return membersDefinition;
  }

  private JsonObject convertBindables(Map<String, PropertyDefinition> members) {
    JsonObject bindables = new JsonObject();
    for (Map.Entry<String, PropertyDefinition> entry : members.entrySet()) {
      PropertyDefinition member = entry.getValue();
      if (member.bindable) {
        bindables.set(entry.getKey(), JsonObject.code(member.isValueOnly() ? member.value : "null"));
      }
    }
    return bindables;
  }

  private JsonObject convertAccessors(Map<String, PropertyDefinition> members) {
    JsonObject accessors = new JsonObject();
    for (Map.Entry<String, PropertyDefinition> entry : members.entrySet()) {
      if (!entry.getValue().isValueOnly()) {
        accessors.set(entry.getKey(), entry.getValue().asJson());
      }
    }
    return accessors;
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    out.writeSymbolWhitespace(ide.getIde());
    // take care of reserved words called as functions (Rhino does not like):
    String ideText = ide.getIde().getText();
    if (!out.isWritingComment()) {
      if (expressionMode) {
        if (ide.isSuper()) {
          ideText = getSuperClassPrototypeAccessCode();
        }
        if ("this".equals(ideText) && ide.isRewriteThis()) {
          ideText = "_this";
        } else {
          ideText = convertIdentifier(ideText);
          IdeDeclaration ideDeclaration = ide.getDeclaration(false);
          if (ideDeclaration != null) {
            if (ideDeclaration.isPrimaryDeclaration()) {
              ideText = compilationUnitAccessCode(ideDeclaration);
            } else if (ideDeclaration.isPrivateStatic()) {
              if (ideDeclaration instanceof FunctionDeclaration && ((FunctionDeclaration)ideDeclaration).isGetterOrSetter()) {
                ideText = "get$" + ideText + "$static()";
              } else {
                ideText += "$static";
              }
            }
          }
        }
      } else {
        ideText = convertIdentifier(ideText);
      }
    }
    out.writeTokenForSymbol(ideText, ide.getSymbol());
  }

  // take care of reserved words called as functions (Rhino does not like):
  private String convertIdentifier(String identifier) {
    return SyntacticKeywords.RESERVED_WORDS.contains(identifier) ? identifier + "_" : identifier;
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    if (out.isWritingComment()) {
      super.visitQualifiedIde(qualifiedIde);
    } else {
//      out.beginComment();
//      qualifiedIde.getQualifier().visit(this);
//      out.endComment();
      out.writeSymbolWhitespace(qualifiedIde.getQualifier().getSymbol());
      IdeDeclaration ideDeclaration = qualifiedIde.getDeclaration();
      String compilationUnitAccessCode = compilationUnitAccessCode(ideDeclaration);
      String ideName = qualifiedIde.getName();
      if (!qualifiedIde.getScope().lookupDeclaration(new Ide(ideName), false).isPrimaryDeclaration()) {
        // something non-imported (primary declaration) hides the short name used for import, so rather use the fully qualified name:
        out.writeTokenForSymbol(qualifiedIde.getQualifiedNameStr(), qualifiedIde.getSymbol());
      } else {
        out.writeTokenForSymbol(compilationUnitAccessCode, qualifiedIde.getSymbol());
      }
    }
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    if (out.isWritingComment()) {
      out.writeSymbol(ideWithTypeParam.getOriginalIde());
    } else {
      visitIde(ideWithTypeParam);
    }
    out.beginComment();
    out.writeSymbol(ideWithTypeParam.getSymDotLt());
    ideWithTypeParam.getType().visit(this);
    out.writeSymbol(ideWithTypeParam.getSymGt());
    out.endComment();
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    // so far, namespaces are only comments:
    out.beginComment();
    out.writeSymbol(namespacedIde.getNamespace().getSymbol());
    out.writeSymbol(namespacedIde.getSymNamespaceSep());
    out.endComment();
    visitIde(namespacedIde);
  }

  @Override
  public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
    visitInExpressionMode(ideExpr.getIde());
  }

  private void visitInExpressionMode(AstNode expr) throws IOException {
    boolean oldExpressionMode = expressionMode;
    expressionMode = !out.isWritingComment();
    try {
      expr.visit(this);
    } finally {
      expressionMode = oldExpressionMode;
    }
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
    Expr leftHandSide = assignmentOpExpr.getArg1();
    if (assignmentOpExpr.getOp().sym == sym.ANDANDEQ || assignmentOpExpr.getOp().sym == sym.OROREQ) {
      leftHandSide.visit(this);
      writeSymbolReplacement(assignmentOpExpr.getOp(), "=");
      // TODO: refactor for a simpler way to switch off white-space temporarily:
      JoocConfiguration options = (JoocConfiguration) out.getOptions();
      DebugMode mode = options.getDebugMode();
      options.setDebugMode(null);
      leftHandSide.visit(this);
      options.setDebugMode(mode);
      out.writeToken(assignmentOpExpr.getOp().sym == sym.ANDANDEQ ? "&&" : "||");
      out.writeToken("(");
      assignmentOpExpr.getArg2().visit(this);
      out.writeToken(")");
    } else {
      String setter = null;
      AstNode dotExprArg = null;
      JooSymbol symDot = null;
      if (leftHandSide instanceof IdeExpr) {
        leftHandSide = ((IdeExpr)leftHandSide).getNormalizedExpr();
      }
      if (leftHandSide instanceof IdeExpr) {
        Ide ide = ((IdeExpr) leftHandSide).getIde();
        IdeDeclaration ideDeclaration = ide.getDeclaration(false);
        if (ideDeclaration != null && ideDeclaration.isPrivateStatic() && ideDeclaration instanceof FunctionDeclaration
                && ((FunctionDeclaration)ideDeclaration).isGetterOrSetter()) {
          writeSymbolReplacement(ide.getSymbol(), "set$" + ide.getName() + "$static");
          writeSymbolReplacement(assignmentOpExpr.getOp(), "(");
          assignmentOpExpr.getArg2().visit(this);
          out.writeToken(")");
          return;
        }
      } else if (leftHandSide instanceof DotExpr) {
        DotExpr dotExpr = (DotExpr) leftHandSide;
        setter = resolveBindable(dotExpr, MethodType.SET);
        dotExprArg = dotExpr.getArg();
        symDot = dotExpr.getOp();
      }
      if (setter != null && dotExprArg != null) {
        out.writeSymbolWhitespace(dotExprArg.getSymbol());
        out.write("AS3.setBindable(");
        visitInExpressionMode(dotExprArg);
        writeSymbolReplacement(symDot, ",");
        out.write(CompilerUtils.quote(setter));
        writeSymbolReplacement(assignmentOpExpr.getOp(), ",");
        assignmentOpExpr.getArg2().visit(this);
        out.writeToken(")");
        return;
      }
      
      visitBinaryOpExpr(assignmentOpExpr);
    }
  }

  private String resolveBindable(DotExpr dotExpr, MethodType methodType) throws IOException {
    //System.err.println("*#*#*#* trying to find " + methodType + "ter for qIde " + qIde.getQualifiedNameStr());
    ExpressionType lhsType = dotExpr.getArg().getType();
    if (lhsType != null && lhsType.getAS3Type() == AS3Type.OBJECT) {
      TypedIdeDeclaration member = findMemberWithBindableAnnotation(dotExpr.getIde(), methodType, lhsType.getDeclaration());
      return member == null ? null : member.getName();
    }
    if (lhsType instanceof Typed) {
      TypeRelation typeRelation = ((Typed) lhsType).getOptTypeRelation();
      if (typeRelation != null) {
        IdeDeclaration typeDeclaration = typeRelation.getType().resolveDeclaration();
        if (typeDeclaration instanceof ClassDeclaration) {
          return resolveBindable(dotExpr.getIde(), methodType, (ClassDeclaration) typeDeclaration);
        }
      }
    }
    return null;
  }

  private String resolveBindable(Ide qIde, MethodType methodType, ClassDeclaration typeDeclaration) throws IOException {
    TypedIdeDeclaration member = findMemberWithBindableAnnotation(qIde, methodType, typeDeclaration);
    return member == null ? null : getBindablePropertyName(methodType, member);
  }

  private static String getBindableEventName(TypedIdeDeclaration member) {
    Object eventAnnotation = getBindablePropertiesByName(member).get("event");
    return eventAnnotation instanceof String ? (String) eventAnnotation : null;
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
    visitIfNotNull(parameters.getHead());
    if (parameters.getSymComma() != null) {
      if (parameters.getTail().getHead().isRest()) {
        out.beginCommentWriteSymbol(parameters.getSymComma());
        parameters.getTail().visit(this);
        out.endComment();
      } else {
        out.writeSymbol(parameters.getSymComma());
        parameters.getTail().visit(this);
      }
    }
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    out.writeSymbol(functionExpr.getSymFunction());
    if (functionExpr.getIde() != null) {
      out.writeSymbol(functionExpr.getIde().getIde());
    }
    handleParameters(functionExpr);
    generateFunTailCode(functionExpr);
  }

  public void handleParameters(FunctionExpr functionExpr) throws IOException {
    Parameters params = functionExpr.getParams();
    if (functionExpr.hasBody()) {
      if (functionExpr.isArgumentsUsedAsArray()) {
        addBlockStartCodeGenerator(functionExpr.getBody(), ARGUMENT_TO_ARRAY_CODE_GENERATOR);
      }
      if (params != null) {
        // inject into body for generating initializers later:
        JoocConfiguration config = ((Jooc) params.getHead().getIde().getScope().getCompiler()).getConfig();
        addBlockStartCodeGenerator(functionExpr.getBody(),
                config.isUseEcmaParameterInitializerSemantics()
                        ? getEcmaParameterInitializerCodeGenerator(params)
                        : getParameterInitializerCodeGenerator(params));
      }
    }
  }

  public void generateFunTailCode(FunctionExpr functionExpr) throws IOException {
    generateFunctionExprSignature(functionExpr);
    if (functionExpr.hasBody()) {
      functionExpr.getBody().visit(this);
    }
  }

  public CodeGenerator getParameterInitializerCodeGenerator(final Parameters params) {
    return new CodeGenerator() {
      @Override
      public void generate(JsWriter out, boolean first) throws IOException {
        // collect all optional parameters with their position index:
        Map<Integer,Parameter> paramByIndex = new HashMap<Integer, Parameter>();
        int paramIndex = 0;
        for (Parameters parameters = params; parameters != null; parameters = parameters.getTail()) {
          Parameter param = parameters.getHead();
          if (param.hasInitializer()) {
            paramByIndex.put(paramIndex, param);
          }
          ++paramIndex;
        }
        generateParameterInitializers(out, paramByIndex);
        generateRestParamCode(params);
      }

    };
  }

  public CodeGenerator getEcmaParameterInitializerCodeGenerator(final Parameters params) {
    return new CodeGenerator() {
      @Override
      public void generate(JsWriter out, boolean first) throws IOException {
        for (Parameters parameters = params; parameters != null; parameters = parameters.getTail()) {
          Parameter param = parameters.getHead();
          if (param.hasInitializer()) {
            out.write(ASSIGN_DEFAULT_IF_PARAMETER_IS_UNDEFINED.format(param.getName()));
            generateBodyInitializerCode(param);
            out.write("}");
          }
        }
        generateRestParamCode(params);
      }
    };
  }

  private final MessageFormat IF_ARGUMENT_LENGTH_LTE_$N = new MessageFormat("if(arguments.length<={0})");
  private final MessageFormat SWITCH_$INDEX = new MessageFormat("switch({0,choice,0#arguments.length|0<Math.max(arguments.length,{0})})");
  private final MessageFormat CASE_$N = new MessageFormat("case {0}:");

  private final MessageFormat ASSIGN_DEFAULT_IF_PARAMETER_IS_UNDEFINED = new MessageFormat("if ({0} === void 0) '{' ");

  private void generateParameterInitializers(JsWriter out, Map<Integer, Parameter> paramByIndex) throws IOException {
    Iterator<Map.Entry<Integer, Parameter>> paramByIndexIterator = paramByIndex.entrySet().iterator();
    if (paramByIndexIterator.hasNext()) {
      Map.Entry<Integer, Parameter> indexAndParam = paramByIndexIterator.next();
      Integer firstParamIndex = indexAndParam.getKey();
      if (!paramByIndexIterator.hasNext()) {
        // only one parameter initializer: use "if"
        out.write(IF_ARGUMENT_LENGTH_LTE_$N.format(firstParamIndex));
        generateBodyInitializerCode(indexAndParam.getValue());
      } else {
        // more than one parameter initializer: use "switch"
        out.write(SWITCH_$INDEX.format(firstParamIndex));
        out.write("{");

        while (true) {
          out.write(CASE_$N.format(indexAndParam.getKey()));
          generateBodyInitializerCode(indexAndParam.getValue());
          if (!paramByIndexIterator.hasNext()) {
            break;
          }
          indexAndParam = paramByIndexIterator.next();
        }

        out.write("}");
      }
    }
  }

  public void generateRestParamCode(final Parameters params) throws IOException {
    if (params != null) {
      // determine the last parameter and its index:
      int lastParamIndex = 0;
      Parameters parameters = params;
      while (parameters.getTail() != null) {
        ++lastParamIndex;
        parameters = parameters.getTail();
      }
      // now check for ...rest parameter:
      Parameter lastParam = parameters.getHead();
      if (lastParam.isRest()) {
        String lastParamName = lastParam.getName();
        if (lastParamName != null && !(lastParamName.equals(FunctionExpr.ARGUMENTS) && lastParamIndex == 0)) {
          generateToArrayCode(lastParamName, lastParamIndex);
        }
      }
    }
  }

  public void generateBodyInitializerCode(Parameter param) throws IOException {
    out.setSuppressWhitespace(true); // do not output whitespace twice!
    try {
      out.writeToken(param.getName());
      out.writeSymbol(param.getOptInitializer().getSymEq());
      param.getOptInitializer().getValue().visit(this);
      out.write(";");
    } finally {
      out.setSuppressWhitespace(false);
    }
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    out.beginComment();
    out.writeSymbol(vectorLiteral.getSymNew());
    out.writeSymbol(vectorLiteral.getSymLt());
    vectorLiteral.getVectorType().visit(this);
    out.writeSymbol(vectorLiteral.getSymGt());
    out.endComment();
    vectorLiteral.getArrayLiteral().visit(this);
  }

  @Override
  protected void handleExmlAppendPrepend(ObjectField objectField, DotExpr exmlAppendOrPrepend) throws IOException {
    JooSymbol propertySymbol = objectField.getLabel().getSymbol();
    out.writeTokenForSymbol(propertySymbol.getText() + "$at", propertySymbol);
    out.write(":");
    // net.jangaroo.ext.Exml:
    exmlAppendOrPrepend.getArg().visit(this);
    // '.'
    out.writeSymbol(exmlAppendOrPrepend.getOp());
    JooSymbol appendOrPrependSymbol = exmlAppendOrPrepend.getIde().getSymbol();
    // append -> APPEND, prepend -> PREPEND
    out.writeTokenForSymbol(appendOrPrependSymbol.getText().toUpperCase(), appendOrPrependSymbol);
    out.write(", ");
    objectField.getLabel().visit(this);
    out.writeSymbol(objectField.getSymColon());
    // suppress the Exml.append / prepend function name, only render its argument (leave the unnecessary parenthesis):
    ((ApplyExpr) objectField.getValue()).getArgs().visit(this);
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    if (applyExpr.getArgs() != null && applyExpr.getFun() instanceof IdeExpr) {
      Ide funIde = ((IdeExpr) applyExpr.getFun()).getIde();
      JooSymbol lParen = applyExpr.getArgs().getLParen();
      CommaSeparatedList<Expr> arguments = applyExpr.getArgs().getExpr();
      if (applyExpr.isAssert()) {
        writeSymbolReplacement(funIde.getSymbol(), builtInIdentifierCode(SyntacticKeywords.ASSERT));
        JooSymbol symKeyword = applyExpr.getFun().getSymbol();
        out.writeSymbol(lParen);
        arguments.visit(this);
        out.writeToken(", ");
        out.writeString(new File(symKeyword.getFileName()).getName());
        out.writeToken(", ");
        out.writeInt(symKeyword.getLine());
        out.write(", ");
        out.writeInt(symKeyword.getColumn());
        out.writeSymbol(applyExpr.getArgs().getRParen());
        return;
      } else if (isAddEventListenerMethod(funIde)) {
        out.writeSymbolWhitespace(funIde.getSymbol());
        out.writeToken("AS3.");
        out.writeSymbol(funIde.getIde());
        out.writeSymbol(lParen, false);
        funIde.getQualifier().visit(this);
        writeSymbolReplacement(((QualifiedIde) funIde).getSymDot(), ",");
        out.writeSymbolWhitespace(lParen);
        Expr eventConstant = arguments.getHead();
        if (!(eventConstant instanceof IdeExpr)) {
          throw Jooc.error(eventConstant, String.format("'%s' must be used with event constant.", MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME));
        }
        Ide eventConstantIde = ((IdeExpr) eventConstant).getIde();
        if (eventConstantIde.getQualifier() == null) {
          throw Jooc.error(eventConstant, String.format("'%s' must be used with event constant from event class.", MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME));
        }
        visitInExpressionMode(eventConstantIde.getQualifier());
        writeSymbolReplacement(((QualifiedIde) eventConstantIde).getSymDot(), ",");
        out.writeToken("\"");
        out.writeSymbol(eventConstantIde.getIde());
        out.writeToken("\"");
        out.writeSymbol(arguments.getSymComma());
        arguments.getTail().visit(this);
        out.writeSymbol(applyExpr.getArgs().getRParen());
        return;
      }
    }
    generateFunJsCode(applyExpr);
  }

  private boolean isAddEventListenerMethod(Ide funIde) {
    if (MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME.equals(funIde.getName())) {
      IdeDeclaration qualifierDeclaration = funIde.getQualifier().resolveDeclaration();
      if (qualifierDeclaration instanceof ClassDeclaration) {
        CompilationUnit type = compilationUnitModelResolver.resolveCompilationUnit(qualifierDeclaration.getQualifiedNameStr());
        if (type != null && type.getPrimaryDeclaration() != null) {
          // check whether the type implements IObservable:
          return compilationUnitModelResolver.implementsInterface(type, MxmlUtils.EVENT_DISPATCHER_INTERFACE);
        }
      }
    }
    return false;
  }

  private void generateFunJsCode(ApplyExpr applyExpr) throws IOException {
    // handle constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (applyExpr.isTypeCast()) {
      out.writeSymbolWhitespace(applyExpr.getFun().getSymbol());
      out.writeToken("AS3.cast");
      out.writeSymbol(args.getLParen());
      applyExpr.getFun().visit(this);
      out.writeToken(",");
      // isTypeCast() ensures that there is exactly one parameter:
      args.getExpr().getHead().visit(this);
      out.writeSymbol(args.getRParen());
    } else if (applyExpr.isTypeCheckObjectLiteralFunctionCall()) {
      // suppress virtual object literal type check function call:
      args.getExpr().getTail().getHead().visit(this);
    } else {
      applyExpr.getFun().visit(this);
      // check for super call:
      if (args != null && applyExpr.getFun() instanceof IdeExpr && ((IdeExpr) applyExpr.getFun()).getIde().isQualifiedBySuper()) {
        generateSuperCallParameters(args);
      } else {
        visitApplyExprArguments(applyExpr);
      }
    }
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    out.beginComment();
    out.writeSymbol(classBody.getLBrace());
    out.endComment();
    visitClassBodyDirectives(classBody.getDirectives());
    out.beginComment();
    out.writeSymbol(classBody.getRBrace());
    out.endComment();
  }

  void generateStaticInitializer(List<Directive> directives) throws IOException {
    String staticFunctionName = "static$" + staticCodeCounter++;

    out.writeToken(String.format("function %s(){", staticFunctionName));
    for (Directive directive : directives) {
      directive.visit(this);
    }
    out.writeToken("}");

    primaryClassDefinitionBuilder.staticCode.append("          ").append(staticFunctionName).append("();\n");
  }

  @Override
  protected String builtInIdentifierCode(String builtInIdentifier) {
    return "AS3." + builtInIdentifier;
  }

  @Override
  public void visitForInStatement(final ForInStatement forInStatement) throws IOException {
    final Ide exprAuxIde = forInStatement.getExprAuxIde();
    ExpressionType exprType = forInStatement.getExpr().getType();
    boolean iterateArrayMode = exprType != null && exprType.isArrayLike();
    if (exprAuxIde != null && !iterateArrayMode) {
      new SemicolonTerminatedStatement(new VariableDeclaration(SYM_VAR, exprAuxIde, null, null), SYM_SEMICOLON).visit(this);
    }
    out.writeSymbol(forInStatement.getSymKeyword());
    final boolean isForEach = forInStatement.getSymEach() != null;
    if (isForEach) {
      out.beginComment();
      out.writeSymbol(forInStatement.getSymEach());
      out.endComment();
    }
    out.writeSymbol(forInStatement.getLParen());
    if (isForEach || iterateArrayMode) {
      new VariableDeclaration(SYM_VAR, forInStatement.getAuxIde(), null, null).visit(this);
    } else {
      if (forInStatement.getDecl() != null) {
        forInStatement.getDecl().visit(this);
      } else {
        forInStatement.getLValue().visit(this);
      }
    }
    if (iterateArrayMode) {
      String indexVarName = forInStatement.getAuxIde().getName();
      out.write("=0");
      if (exprAuxIde != null) {
        out.write(",");
        out.writeToken(exprAuxIde.getName());
        out.writeToken("=");
        out.beginComment();
        out.writeSymbol(forInStatement.getSymIn());
        out.endComment();
        forInStatement.getExpr().visit(this);
      }
      out.write(";");
      out.write(indexVarName);
      out.write("<");
      if (exprAuxIde != null) {
        out.writeToken(exprAuxIde.getName());
      } else {
        out.beginComment();
        out.writeSymbol(forInStatement.getSymIn());
        out.endComment();
        forInStatement.getExpr().visit(this);
      }
      out.write(".length;");
      out.write("++" + indexVarName);
    } else {
      out.writeSymbol(forInStatement.getSymIn());
      if (exprAuxIde != null) {
        // assign the ^ value to the auxiliary expression value variable once:
        out.writeToken(exprAuxIde.getName());
        out.writeToken("=");
      }
      forInStatement.getExpr().visit(this);
    }
    out.writeSymbol(forInStatement.getRParen());
    if (isForEach || iterateArrayMode) {
      // inject synthesized statement into loop body:
      if (!(forInStatement.getBody() instanceof BlockStatement)) {
        forInStatement.setBody(new BlockStatement(SYM_LBRACE, Arrays.<Directive>asList(forInStatement.getBody()), SYM_RBRACE));
      }
      addBlockStartCodeGenerator((BlockStatement) forInStatement.getBody(), new CodeGenerator() {
        @Override
        public void generate(JsWriter out, boolean first) throws IOException {
          // synthesize assigning the correct index to the variable given in the original for each statement:
          if (forInStatement.getDecl() != null) {
            forInStatement.getDecl().visit(JsCodeGenerator.this);
          } else {
            forInStatement.getLValue().visit(JsCodeGenerator.this);
          }
          out.writeToken("=");
          if (!isForEach) {
            out.write("String(" + forInStatement.getAuxIde().getName() + ")");
          } else {
            if (exprAuxIde == null) {
              forInStatement.getExpr().visit(JsCodeGenerator.this);
            } else {
              out.write(exprAuxIde.getName());
            }
            out.write("[" + forInStatement.getAuxIde().getName() + "]");
          }
          out.write(";");
        }
      });
    }
    forInStatement.getBody().visit(this);
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    Debug.assertTrue(parameter.getModifiers() == 0, "Parameters must not have any modifiers");
    if (parameter.isRest()) {
      out.beginCommentWriteSymbol(parameter.getOptSymRest());
      parameter.getIde().visit(this);
      out.endComment();
    } else {
      parameter.getIde().visit(this);
    }
    visitParameterTypeRelation(parameter);
    // in the method signature, comment out initializer code.
    if (parameter.getOptInitializer() != null) {
      out.beginComment();
      parameter.getOptInitializer().visit(this);
      out.endComment();
    }
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.hasPreviousVariableDeclaration()) {
      Debug.assertTrue(variableDeclaration.getOptSymConstOrVar() != null && variableDeclaration.getOptSymConstOrVar().sym == sym.COMMA, "Additional variable declarations must start with a COMMA.");
    }
    visitAll(variableDeclaration.getAnnotations());
    writeExtDefineCodePrefix(variableDeclaration);
    List<Metadata> currentMetadata = buildMetadata(variableDeclaration);
    if ((variableDeclaration.isClassMember() || variableDeclaration.isPrimaryDeclaration()) && !variableDeclaration.isPrivateStatic()) {
      if (!variableDeclaration.isPrimaryDeclaration() && !currentMetadata.isEmpty()) {
        getClassDefinitionBuilder(variableDeclaration).storeCurrentMetadata(
                variableDeclaration.getIde().getName() + (variableDeclaration.isPrivate() ? "$" + variableDeclaration.getClassDeclaration().getQualifiedNameHash() : ""),
                currentMetadata
        );
      }
      out.beginComment();
      writeModifiers(out, variableDeclaration);
      writeOptSymbol(variableDeclaration.getOptSymConstOrVar());
      variableDeclaration.getIde().visit(this);
      visitIfNotNull(variableDeclaration.getOptTypeRelation());
      if (mustInitializeInStaticCode(variableDeclaration)) {
        out.endComment();
        generateFieldInitializerCode(variableDeclaration);
      } else {
        visitIfNotNull(variableDeclaration.getOptInitializer());
        out.endComment();
      }
      registerField(variableDeclaration, currentMetadata);
      visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
      generateFieldEndCode(variableDeclaration);
    } else {
      if (variableDeclaration.hasPreviousVariableDeclaration()) {
        writeOptSymbol(variableDeclaration.getOptSymConstOrVar());
      } else {
        generateVarStartCode(variableDeclaration);
      }
      visitInExpressionMode(variableDeclaration.getIde());
      visitIfNotNull(variableDeclaration.getOptTypeRelation());
      Initializer optInitializer = variableDeclaration.getOptInitializer();
      if (optInitializer == null) {
        if (variableDeclaration.isPrimaryDeclaration() || variableDeclaration.isPrivateStatic()) {
          String value = getValueFromEmbedMetadata(currentMetadata);
          if (value == null) {
            TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();
            value = VariableDeclaration.getDefaultValue(typeRelation);
          }
          if (value != null) {
            out.write("=" + value);
          }
        }
      } else {
        if (variableDeclaration.isPrivateStatic() && mustInitializeInStaticCode(variableDeclaration)) {
          out.writeToken(";");
          generateFieldInitializerCode(variableDeclaration);
          registerField(variableDeclaration, currentMetadata);
          visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
          // suppress redundant ';' after field initializer:
          writeOptSymbolWhitespace(variableDeclaration.getOptSymSemicolon());
          return;
        } else {
          optInitializer.visit(this);
        }
      }
      visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
      writeOptSymbol(variableDeclaration.getOptSymSemicolon());
    }
  }

  private String getValueFromEmbedMetadata(List<Metadata> currentMetadata) {
    Metadata embedMetadata = Metadata.find(currentMetadata, Jooc.EMBED_ANNOTATION_NAME);
    if (embedMetadata != null) {
      String source = (String) embedMetadata.getArgumentValue(Jooc.EMBED_ANNOTATION_SOURCE_PROPERTY);
      String assetType = EmbeddedAssetResolver.guessAssetType(source);
      int index = compilationUnit.getResourceDependencies().indexOf(assetType + "!" + source);
      String assetFactory = "new String";
      if ("image".equals(assetType)) {
        assetFactory = imports.get("flash.display.Bitmap") + ".fromImg";
      }
      return String.format("function(){return %s($resource_%d)}", assetFactory, index);
    }
    return null;
  }

  private void registerField(VariableDeclaration variableDeclaration, List<Metadata> currentMetadata) {
    String variableName = variableDeclaration.getName();
    boolean isBindable = variableDeclaration.getAnnotation(Jooc.BINDABLE_ANNOTATION_NAME) != null;
    String value = null;
    if (mustInitializeInStaticCode(variableDeclaration)) {
      if (variableDeclaration.isStatic()) {
        primaryClassDefinitionBuilder.staticCode.append("          ").append(variableName).append("$static_();\n");
      }
      if (isBindable || variableDeclaration.isStatic() && !variableDeclaration.isPrivate()) {
        // make sure that configs are always declared, even with dynamic initializer, so that Ext magic is applied:
        value = "undefined";
      }
    } else {
      if (variableDeclaration.getOptInitializer() != null) {
        Expr initialValue = variableDeclaration.getOptInitializer().getValue();
        JsWriter originalOut = out;
        StringWriter initialValueWriter = new StringWriter();
        out = new JsWriter(initialValueWriter);
        out.setOptions(originalOut.getOptions());
        try {
          initialValue.visit(this);
        } catch (IOException e) {
          // cannot happen
        } finally {
          out = originalOut;
        }
        value = initialValueWriter.toString().trim();
      } else {
        value = getValueFromEmbedMetadata(currentMetadata);
        if (value == null) {
          TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();
          value = VariableDeclaration.getDefaultValue(typeRelation);
        }
      }
      if (variableDeclaration.isPrivate() && !variableDeclaration.isStatic()) {
        variableName += "$" + ((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).getQualifiedNameHash();
      }
    }
    if (variableDeclaration.isPrimaryDeclaration()) {
      factory = value == null
              ? variableName + "_"
              : String.format("function() {\n        return(%s);\n      }", value);
      return;
    }
    if (value != null) {
      // special Ext magic: when declaring a public static const xtype:String, replace it by an alias: "widget." + Clazz.xtype:
      if (variableDeclaration.isPublic() && variableDeclaration.isStatic() && variableDeclaration.isConst()
              && "xtype".equals(variableDeclaration.getName())
              && variableDeclaration.getOptInitializer() != null
              && variableDeclaration.getOptInitializer().getValue() instanceof LiteralExpr) {
        getClassDefinitionBuilder(variableDeclaration).members.put("alias",
                new PropertyDefinition(CompilerUtils.quote("widget." + CompilerUtils.unquote(value))));
      } else {
        membersOrStaticMembers(variableDeclaration).put(variableName,
                new PropertyDefinition(value, !variableDeclaration.isConst(), isBindable));
      }
    }

  }

  protected void generateVarStartCode(VariableDeclaration variableDeclaration) throws IOException {
    out.beginComment();
    writeModifiers(out, variableDeclaration);
    out.endComment();
    if (variableDeclaration.getOptSymConstOrVar() != null) {
      if (variableDeclaration.isConst()) {
        out.beginCommentWriteSymbol(variableDeclaration.getOptSymConstOrVar());
        out.endComment();
        out.writeToken("var");
      } else {
        out.writeSymbol(variableDeclaration.getOptSymConstOrVar());
      }
    }
  }

  protected void generateFieldInitializerCode(VariableDeclaration variableDeclaration) throws IOException {
    Initializer initializer = variableDeclaration.getOptInitializer();
    out.beginComment();
    out.writeSymbol(initializer.getSymEq());
    out.endComment();
    String variableName = variableDeclaration.getName();
    out.writeToken("function");
    out.writeToken(variableName + (variableDeclaration.isStatic() ? "$static" : "") + "_");
    out.writeToken("(){");
    if (variableDeclaration.isPrivateStatic()) {
      out.write(variableName + "$static=(");
      initializer.getValue().visit(this);
    } else {
      String target = variableDeclaration.isStatic() ? variableDeclaration.getClassDeclaration().getName() : "this";
      String slotName = variableName + (variableDeclaration.isPrivate() ? "$" + variableDeclaration.getClassDeclaration().getQualifiedNameHash() : "");
      if (false /* variableDeclaration.isConst() */) { // keep it compatible, even for constants! 
        out.write("Object.defineProperty(" + target + ",\"" + slotName + "\",{value:");
        initializer.getValue().visit(this);
        out.writeToken("}");
      } else {
        if (variableDeclaration.isPrimaryDeclaration()) {
          out.writeToken("return");
        } else {
          out.write(target + "." + slotName + "=");
        }
        out.writeToken("(");
        initializer.getValue().visit(this);
      }
    }
    out.writeToken(");}");
  }

  private boolean mustInitializeInStaticCode(VariableDeclaration variableDeclaration) {
    return variableDeclaration.getOptInitializer() != null && !variableDeclaration.getOptInitializer().getValue().isRuntimeConstant();
  }

  protected void generateFieldEndCode(VariableDeclaration variableDeclaration) throws IOException {
    if (!variableDeclaration.hasPreviousVariableDeclaration()) {
      Debug.assertTrue(variableDeclaration.getOptSymSemicolon() != null, "optSymSemicolon != null");
      out.beginComment();
      out.writeSymbol(variableDeclaration.getOptSymSemicolon());
      out.endComment();
    }
  }

  private static final CodeGenerator ALIAS_THIS_CODE_GENERATOR = (out, first) -> out.write("var _this=this;");

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    visitAll(functionDeclaration.getAnnotations());
    boolean isPrimaryDeclaration = functionDeclaration.equals(compilationUnit.getPrimaryDeclaration());
    assert functionDeclaration.isClassMember() || (!functionDeclaration.isNative() && !functionDeclaration.isAbstract());
    if (isPrimaryDeclaration) {
      writeExtDefineCodePrefix(functionDeclaration);
      factory = "function() {\n        return " + functionDeclaration.getName() + ";\n      }";
    }
    handleParameters(functionDeclaration.getFun());
    if (functionDeclaration.isClassMember() && functionDeclaration.isThisAliased()) {
      addBlockStartCodeGenerator(functionDeclaration.getBody(), ALIAS_THIS_CODE_GENERATOR);
    }
    if (functionDeclaration.isConstructor() && !functionDeclaration.containsSuperConstructorCall() && functionDeclaration.hasBody()
       && needsSuperCallCodeGenerator(functionDeclaration.getClassDeclaration())) {
      addBlockStartCodeGenerator(functionDeclaration.getBody(), new SuperCallCodeGenerator(functionDeclaration.getClassDeclaration()));
    }
    if (!functionDeclaration.isClassMember() && !isPrimaryDeclaration) {
      functionDeclaration.getFun().visit(this);
    } else {
      JooSymbol functionSymbol = functionDeclaration.getIde().getSymbol();
      String functionName = convertIdentifier(functionSymbol.getText());
      String methodName = functionName;
      List<Metadata> currentMetadata = buildMetadata(functionDeclaration);
      if (!isPrimaryDeclaration && !currentMetadata.isEmpty()) {
        getClassDefinitionBuilder(functionDeclaration).storeCurrentMetadata(
                functionName,
                currentMetadata
        );
      }
      out.beginComment();
      writeModifiers(out, functionDeclaration);
      Map<String, PropertyDefinition> members = membersOrStaticMembers(functionDeclaration);
      if (functionDeclaration.isAbstract() || functionDeclaration.isNative()) {
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
        functionDeclaration.getIde().visit(this);
        generateFunctionExprSignature(functionDeclaration.getFun());
        writeOptSymbol(functionDeclaration.getOptSymSemicolon());
        out.endComment();
      } else {
        out.endComment();
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());

        boolean isAccessor = functionDeclaration.isGetterOrSetter();
        if (isAccessor) {
          Metadata bindableAnnotation = Metadata.find(currentMetadata, Jooc.BINDABLE_ANNOTATION_NAME);
          if (bindableAnnotation != null) {
            String accessorPrefix = functionDeclaration.getSymGetOrSet().getText();
            String accessorName = (String) bindableAnnotation.getArgumentValue(DEFAULT_ANNOTATION_PARAMETER_NAME);
            members.put(functionName, new PropertyDefinition("undefined", true, true));
            methodName = accessorName != null ? accessorName : accessorPrefix + MxmlUtils.capitalize(functionName);
            functionName = accessorPrefix + "$" + functionName;
            isAccessor = false;
          }
        } else if (functionDeclaration.isConstructor()) {
          functionName += "$";
        }

        String overriddenMethodName = null;
        PropertyDefinition overriddenPropertyDefinition = null;
        if (functionDeclaration.isPrivate() && !functionDeclaration.isStatic()) {
          String privateMethodName = methodName + "$" + functionDeclaration.getClassDeclaration().getQualifiedNameHash();
          if (functionDeclaration.isOverride()) {
            overriddenMethodName = privateMethodName;
            getClassDefinitionBuilder(functionDeclaration).super$Used = true;
          } else {
            methodName = privateMethodName;
          }
        } else if (functionDeclaration.isStatic()) {
          functionName += "$static";
        }
        if (isAccessor) {
          out.writeSymbolWhitespace(functionDeclaration.getIde().getSymbol());
          out.writeSymbolWhitespace(functionDeclaration.getSymGetOrSet());
          String accessorPrefix = functionDeclaration.getSymGetOrSet().getText() + "$";
          String accessorName = accessorPrefix + functionName;
          out.writeToken(accessorName);
          if (!functionDeclaration.isPrivateStatic()) { // TODO: simulate private static getter when called!
            PropertyDefinition accessorDefinition;
            accessorDefinition = members.get(methodName);
            if (accessorDefinition == null) {
              accessorDefinition = new PropertyDefinition();
              members.put(methodName, accessorDefinition);
            }
            if (functionDeclaration.isGetter()) {
              accessorDefinition.get = accessorName;
              if (functionDeclaration.isOverride() && accessorDefinition.set == null) {
                // inherit non-overwritten counterpart (may be undefined, and may be overwritten later):
                accessorDefinition.set = "super$.__lookupSetter__('" + methodName + "')";
              }
            } else {
              accessorDefinition.set = accessorName;
              if (functionDeclaration.isOverride() && accessorDefinition.get == null) {
                // inherit non-overwritten counterpart (may be undefined, and may be overwritten later):
                accessorDefinition.get = "super$.__lookupGetter__('" + methodName + "')";
              }
            }
            if (overriddenMethodName != null) {
              overriddenPropertyDefinition = new PropertyDefinition();
              overriddenPropertyDefinition.get = "super$.__lookupGetter__('" + methodName + "')";
              overriddenPropertyDefinition.set = "super$.__lookupSetter__('" + methodName + "')";
            }
          }
        } else {
          writeSymbolReplacement(functionSymbol, functionName);
          if (!functionDeclaration.isPrimaryDeclaration() && !functionDeclaration.isPrivateStatic()) {
            members.put(functionDeclaration.isConstructor() ? "constructor" : methodName,
                    new PropertyDefinition(functionName, functionDeclaration.getAnnotation(Jooc.EXT_PRIVATE_ANNOTATION_NAME) != null));
            if (overriddenMethodName != null) {
              overriddenPropertyDefinition = new PropertyDefinition("super$." + methodName);
            }
          }
        }
        if (overriddenMethodName != null) {
          members.put(overriddenMethodName, overriddenPropertyDefinition);
        }
        generateFunTailCode(functionDeclaration.getFun());
      }
      processMixinAnnotations(functionDeclaration, functionName, methodName);
    }
  }

  // See https://docs.sencha.com/extjs/6.5.3/classic/Ext.Mixin.html
  private void processMixinAnnotations(FunctionDeclaration functionDeclaration, String functionName, String jsMethodName) {
    for (Annotation annotation : functionDeclaration.getAnnotations(Jooc.MIXIN_HOOK_ANNOTATION_NAME)) {
      Map<String, Object> propertiesByName = annotation.getPropertiesByName();
      for (Map.Entry<String, Object> propertyWithValues : propertiesByName.entrySet()) {
        String mixinHookType = propertyWithValues.getKey();
        if (mixinHookType == null) {
          mixinHookType = Jooc.MIXIN_HOOK_ANNOTATION_DEFAULT_ATTRIBUTE_NAME;
        }
        if (Jooc.MIXIN_HOOK_ANNOTATION_ATTRIBUTE_NAMES.contains(mixinHookType)) {
          JsonObject mixinConfig = getClassDefinitionBuilder(functionDeclaration).mixinConfig;
          if (Jooc.MIXIN_HOOK_ANNOTATION_EXTENDED_ATTRIBUTE_NAME.equals(mixinHookType)) {
            mixinConfig.set(mixinHookType, JsonObject.code(functionName));
          } else {
            JsonObject mixinKeyConfig = (JsonObject) mixinConfig.get(mixinHookType);
            if (mixinKeyConfig == null) {
              mixinKeyConfig = new JsonObject();
              mixinConfig.set(mixinHookType, mixinKeyConfig);
            }
            Object value = propertyWithValues.getValue();
            if (value instanceof String) {
              mixinKeyConfig.set((String) value, jsMethodName);
            } else if (value instanceof List) {
              @SuppressWarnings("unchecked")
              List<String> values = (List) value;
              for (String item : values) {
                mixinKeyConfig.set(item, jsMethodName);
              }
            }
          }
        } else {
          throw Jooc.error(annotation, "Invalid [MixinHook] attribute '" + mixinHookType + "'.");
        }
      }
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitAll(classDeclaration.getAnnotations());
    writeExtDefineCodePrefix(classDeclaration);
    List<Metadata> currentMetadata = buildMetadata(classDeclaration);
    ClassDefinitionBuilder classDefinitionBuilder = classDeclaration.isPrimaryDeclaration()
            ? primaryClassDefinitionBuilder : (secondaryClassDefinitionBuilder = new ClassDefinitionBuilder());
    classDefinitionBuilder.storeCurrentMetadata("", currentMetadata);
    out.beginComment();
    writeModifiers(out, classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    out.endComment();
    classDeclaration.getBody().visit(this);

    for (IdeDeclaration secondaryDeclaration : classDeclaration.getSecondaryDeclarations()) {
      String secondaryDeclarationName = secondaryDeclaration.getName();
      secondaryDeclaration.visit(this);
      JsonObject secondaryClassDefinition = createClassDefinition(secondaryDeclaration, secondaryClassDefinitionBuilder);
      out.write(new MessageFormat("var {0}$static = Ext.define(null, ").format(secondaryDeclarationName));
      out.write(secondaryClassDefinition.toString(-1, -1));
      out.write(");");
    }

    if (!classDeclaration.isInterface() && classDeclaration.getConstructor() == null
            && needsSuperCallCodeGenerator(classDeclaration)) {
      // generate default constructor that calls field initializers:
      String constructorName = classDeclaration.getName() + "$";
      out.write("function " + constructorName + "() {");
      new SuperCallCodeGenerator(classDeclaration).generate(out, true);
      out.write("}");
      classDefinitionBuilder.members.put("constructor", new PropertyDefinition(constructorName));
    }
  }

  private void writeExtDefineCodePrefix(IdeDeclaration declaration) throws IOException {
    if (declaration.isPrimaryDeclaration()) {
      out.writeSymbolWhitespace(declaration.getSymbol());
      out.write("Ext.define(");
      out.write(CompilerUtils.quote(declaration.getTargetQualifiedNameStr()));
      out.write(", function(" + declaration.getName() + ") {");
    }
  }

  private List<Metadata> buildMetadata(Declaration declaration) {
    List<Metadata> metadata = new LinkedList<>();
    for (Annotation annotation : declaration.getAnnotations()) {
      final Metadata m = new Metadata(annotation.getIde().getName());
      m.args = buildMetadataArgs(annotation);
      metadata.add(m);
    }
    return metadata;
  }

  private List<MetadataArgument> buildMetadataArgs(Annotation annotation) {
    CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
    if (annotationParameters == null) {
      return Collections.emptyList();
    }
    List<MetadataArgument> args = new LinkedList<>();
    for (; annotationParameters != null; annotationParameters = annotationParameters.getTail()) {
      final AnnotationParameter annotationParameter = annotationParameters.getHead();
      Ide optName = annotationParameter.getOptName();
      AstNode optValue = annotationParameter.getValue();
      String name = optName == null ? DEFAULT_ANNOTATION_PARAMETER_NAME : optName.getName();
      Object value;
      if (optValue instanceof LiteralExpr) {
        value = ((LiteralExpr) optValue).getValue().getJooValue();
      } else if (optValue instanceof Ide) {
        IdeDeclaration ideDeclaration = ((Ide) optValue).getDeclaration();
        value = JsonObject.code(compilationUnitAccessCode(ideDeclaration));
      } else {
        value = null;
      }
      args.add(new MetadataArgument(name, value));
    }
    return args;
  }

  public static boolean needsSuperCallCodeGenerator(ClassDeclaration classDeclaration) {
    return classDeclaration.notExtendsObject()                         // only if class has a "real" superclass
            || !classDeclaration.getFieldsWithInitializer().isEmpty(); // or there are field initializers
  }

  private class SuperCallCodeGenerator implements CodeGenerator {
    private ClassDeclaration classDeclaration;

    public SuperCallCodeGenerator(ClassDeclaration classDeclaration) {
      this.classDeclaration = classDeclaration;
    }

    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      generateSuperConstructorCallCode(classDeclaration, null);
      out.writeToken(";");
    }
  }

  @Override
  public void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException {
    visitAll(namespaceDeclaration.getAnnotations());
    if (namespaceDeclaration.isPrimaryDeclaration()) {
      writeExtDefineCodePrefix(namespaceDeclaration);
    }
    out.beginString();
    writeModifiers(out, namespaceDeclaration);
    out.writeSymbol(namespaceDeclaration.getSymNamespace());
    namespaceDeclaration.getIde().visit(this);
    out.endString();
    out.writeSymbolWhitespace(namespaceDeclaration.getOptInitializer().getSymEq());
    out.writeToken(",");
    namespaceDeclaration.getOptInitializer().getValue().visit(this);
    writeSymbolReplacement(namespaceDeclaration.getOptSymSemicolon(), ",[]");
    if (namespaceDeclaration.isPrimaryDeclaration()) {
      factory = namespaceDeclaration.getName();
    }
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    out.beginComment();
    super.visitPackageDeclaration(packageDeclaration);
    out.endComment();
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    ClassDeclaration classDeclaration = superConstructorCallStatement.getClassDeclaration();
    if (classDeclaration.notExtendsObject() || !classDeclaration.getFieldsWithInitializer().isEmpty()) {
      out.writeSymbolWhitespace(superConstructorCallStatement.getSymbol());
      generateSuperConstructorCallCode(classDeclaration, superConstructorCallStatement.getArgs());
    } else { // suppress for classes extending Object
      // Object super call does nothing anyway:
      out.beginComment();
      out.writeSymbol(superConstructorCallStatement.getSymbol());
      visitIfNotNull(superConstructorCallStatement.getArgs());
      out.endComment();
    }
    out.writeSymbol(superConstructorCallStatement.getSymSemicolon());
  }

  private void generateSuperConstructorCallCode(ClassDeclaration classDeclaration, ParenthesizedExpr<CommaSeparatedList<Expr>> args) throws IOException {
    String superWithLevel = "super$" + classDeclaration.getQualifiedNameHash();
    out.write("this." + superWithLevel);
    if (args == null) {
      out.writeToken("()");
    } else {
      args.visit(this);
    }
    List<String> callSuperCode = new ArrayList<>();
    callSuperCode.add("function() {\n");
    if (classDeclaration.notExtendsObject()) {
      callSuperCode.add("        " + getSuperClassPrototypeAccessCode() + ".constructor.apply(this, arguments);\n");
    }
    for (VariableDeclaration field : classDeclaration.getFieldsWithInitializer()) {
      callSuperCode.add("        " + field.getName() + "_.call(this);\n");
    }
    callSuperCode.add("      }");
    ClassDefinitionBuilder classDefinitionBuilder;
    if (classDeclaration.isPrimaryDeclaration()) {
      classDefinitionBuilder = primaryClassDefinitionBuilder;
    } else {
      classDefinitionBuilder = secondaryClassDefinitionBuilder;
      // secondary class definition does not allow new-lines:
      callSuperCode = callSuperCode.stream()
              .map(String::trim)
              .collect(Collectors.toList());

    }
    classDefinitionBuilder.members.put(superWithLevel, new PropertyDefinition(String.join("", callSuperCode)));
  }

  private String getSuperClassPrototypeAccessCode() {
    return compilationUnitAccessCode(((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).getSuperTypeDeclaration()) + ".prototype";
  }

  private void generateSuperCallParameters(ParenthesizedExpr<CommaSeparatedList<Expr>> args) throws IOException {
    out.writeToken(".call");
    if (args == null) {
      out.writeToken("(this)");
    } else {
      out.writeSymbolToken(args.getLParen());
      out.writeToken("this");
      out.writeSymbolWhitespace(args.getLParen());
      CommaSeparatedList<Expr> parameters = args.getExpr();
      if (parameters != null && parameters.getHead() != null) {
        out.writeToken(",");
        parameters.visit(this);
      }
      out.writeSymbol(args.getRParen());
    }
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    out.beginComment();
    super.visitAnnotation(annotation);
    out.endComment();
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    out.beginComment();
    super.visitUseNamespaceDirective(useNamespaceDirective);
    out.endComment();
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    out.beginComment();
    super.visitImportDirective(importDirective);
    out.endComment();
  }

  private static class PropertyDefinition {
    String value;
    boolean writable;
    boolean configurable;
    String get;
    String set;
    boolean bindable;
    boolean extPrivate;

    private PropertyDefinition() {
    }

    private PropertyDefinition(String value) {
      this.value = value;
    }

    private PropertyDefinition(String value, boolean extPrivate) {
      this.value = value;
      this.extPrivate = extPrivate;
    }

    private PropertyDefinition(String value, boolean writable, boolean bindable) {
      this.value = value;
      this.writable = writable;
      this.bindable = bindable;
    }

    JsonObject asJson() {
      JsonObject result = new JsonObject();
      if (value != null) {
        result.set("value", JsonObject.code(value));
      }
      if (get != null) {
        result.set("get", JsonObject.code(get));
      }
      if (set != null) {
        result.set("set", JsonObject.code(set));
      }
      if (writable) {
        result.set("writable", true);
      }
      if (configurable) {
        result.set("configurable", true);
      }
      return result;
    }

    Object asAbbreviatedJson() {
      if (isValueOnly()) {
        return JsonObject.code(value);
      }
      return asJson();
    }

    boolean isValueOnly() {
      return /*!writable && !configurable && */ get == null && set == null;
    }
  }

  private static class Metadata {
    String name;
    List<MetadataArgument> args = new ArrayList<>();

    static Metadata find(List<Metadata> metadataList, String name) {
      for (Metadata metadata : metadataList) {
        if (metadata.name.equals(name)) {
          return metadata;
        }
      }
      return null;
    }

    private Metadata(String name) {
      this.name = name;
    }

    public Object getArgumentValue(String argumentName) {
      for (MetadataArgument arg : args) {
        if (arg.name.equals(argumentName)) {
          return arg.value;
        }
      }
      return null;
    }
  }

  private static class MetadataArgument {
    String name;
    Object value;

    private MetadataArgument(String name, Object value) {
      this.name = name;
      this.value = value;
    }
  }

  private static class ClassDefinitionBuilder {
    JsonObject metadata = new JsonObject();
    JsonObject mixinConfig = new JsonObject();
    Map<String,PropertyDefinition> members = new LinkedHashMap<String, PropertyDefinition>();
    Map<String,PropertyDefinition> staticMembers = new LinkedHashMap<String, PropertyDefinition>();
    StringBuilder staticCode = new StringBuilder();
    boolean super$Used = false;

    void storeCurrentMetadata(String memberName, List<Metadata> currentMetadata) {
      Object memberMetadata = metadata.get(memberName);
      List<Object> allMetadata = memberMetadata instanceof JsonArray
              ? ((JsonArray) memberMetadata).getItems()
              : new LinkedList<>();
      allMetadata.addAll(compress(currentMetadata));
      if (!allMetadata.isEmpty()) {
        metadata.set(memberName, new JsonArray(allMetadata.toArray()));
      }
    }

    public List<Object> compress(List<Metadata> metadataList) {
      List<Object> compressedMetadataList = new ArrayList<Object>();
      for (Metadata metadata : metadataList) {
        if (!ANNOTATIONS_FOR_COMPILER_ONLY.contains(metadata.name)) {
          compressedMetadataList.add(metadata.name);
          if (!metadata.args.isEmpty()) {
            ArrayList<Object> argNameValues = new ArrayList<Object>();
            for (MetadataArgument metadataArgument : metadata.args) {
              argNameValues.add(metadataArgument.name);
              argNameValues.add(metadataArgument.value);
            }
            compressedMetadataList.add(new JsonArray(argNameValues.toArray()));
          }
        }
      }
      return compressedMetadataList;
    }
  }
}
