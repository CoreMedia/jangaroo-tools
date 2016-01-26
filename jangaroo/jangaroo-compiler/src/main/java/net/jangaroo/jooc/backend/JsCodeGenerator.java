package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.Debug;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AsExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.BreakStatement;
import net.jangaroo.jooc.ast.CaseStatement;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.ContinueStatement;
import net.jangaroo.jooc.ast.Declaration;
import net.jangaroo.jooc.ast.DefaultStatement;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DoStatement;
import net.jangaroo.jooc.ast.DotExpr;
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
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.IfStatement;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.InfixOpExpr;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NamespaceDeclaration;
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
import net.jangaroo.jooc.ast.Statement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.SwitchStatement;
import net.jangaroo.jooc.ast.ThrowStatement;
import net.jangaroo.jooc.ast.TryStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.Typed;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.UseNamespaceDirective;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.ast.WhileStatement;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.jooc.util.MessageFormat;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  public static final List<String> ANNOTATIONS_FOR_COMPILER_ONLY = Arrays.asList("Embed", "Native", "Bindable", "AllowConstructorParameters");
  public static final String DEFAULT_ANNOTATION_PARAMETER_NAME = "";
  public static final String PROPERTIES_CLASS_SUFFIX = "_properties";
  public static final String INIT_STATICS = "__initStatics__";
  public static final String AS3_NAMESPACE_DOT = "AS3.";

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

  private boolean expressionMode = false;
  private Map<String,String> imports = new HashMap<String,String>();
  private ClassDefinitionBuilder primaryClassDefinitionBuilder = new ClassDefinitionBuilder();
  private int staticCodeCounter = 0;
  private ClassDefinitionBuilder secondaryClassDefinitionBuilder;
  private CompilationUnit compilationUnit;
  private String factory;
  private LinkedList<Metadata> currentMetadata = new LinkedList<Metadata>();
  private final MessageFormat VAR_$NAME_EQUALS_ARGUMENTS_SLICE_$INDEX =
    new MessageFormat("var {0}=Array.prototype.slice.call(arguments{1,choice,0#|0<,{1}});");

  private void generateToArrayCode(String paramName, int paramIndex) throws IOException {
    out.write(VAR_$NAME_EQUALS_ARGUMENTS_SLICE_$INDEX.format(paramName, paramIndex));
  }

  private final CodeGenerator ARGUMENT_TO_ARRAY_CODE_GENERATOR = new CodeGenerator() {
    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      generateToArrayCode(FunctionExpr.ARGUMENTS, 0);
    }
  };

  public JsCodeGenerator(JsWriter out) {
    super(out);
  }

  private Map<String,PropertyDefinition> membersOrStaticMembers(Declaration memberDeclaration) {
    ClassDefinitionBuilder classDefinitionBuilder = getClassDefinitionBuilder(memberDeclaration);
    return memberDeclaration.isStatic() ? classDefinitionBuilder.staticMembers : classDefinitionBuilder.members;
  }

  private ClassDefinitionBuilder getClassDefinitionBuilder(Declaration memberDeclaration) {
    return memberDeclaration.getClassDeclaration() == null || memberDeclaration.getClassDeclaration().isPrimaryDeclaration() ? primaryClassDefinitionBuilder : secondaryClassDefinitionBuilder;
  }

  private static boolean isAssignmentLHS(Ide ide) {
    AstNode parentNode = ide.getParentNode();
    if (parentNode instanceof IdeExpr || (parentNode instanceof DotExpr && ((DotExpr) parentNode).getIde() == ide)) {
      AstNode containingExpr = parentNode.getParentNode();
      if (containingExpr instanceof AssignmentOpExpr) {
        Expr arg1 = ((AssignmentOpExpr) containingExpr).getArg1();
        if (arg1 instanceof IdeExpr) {
          arg1 = ((IdeExpr) arg1).getNormalizedExpr();
        }
        if (arg1 == parentNode) {
          return true;
        }
      }
    }
    return false;
  }

  private String compilationUnitAccessCode(IdeDeclaration primaryDeclaration) {
    if (primaryDeclaration.getCompilationUnit() == compilationUnit) {
      return primaryDeclaration.getName();
    } else {
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
    IdeDeclaration type = arg.getType();
    if (type == null && arg instanceof IdeExpr) {
      Ide argIde = ((IdeExpr) arg).getIde();
      IdeDeclaration ideDeclaration = argIde.getDeclaration(false);
      if (ideDeclaration instanceof ClassDeclaration) {
        memberDeclaration = ((ClassDeclaration)ideDeclaration).getStaticMemberDeclaration(ide.getName());
      } else {
        type = ideDeclaration;
      }
    }
    if (type != null) {
      memberDeclaration = Ide.resolveMember(type, ide);
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
        memberName = memberName + "$" + ide.getScope().getClassDeclaration().getInheritanceLevel();
      } else if (isSuperCall(ide)) {
        memberName = "callParent";
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
    } else if (memberDeclaration != null && !isAssignmentLHS(ide)) {
      MemberModel getter = findMemberWithBindableAnnotation(ide, MethodType.GET, memberDeclaration.getClassDeclaration());
      if (getter != null) {
        // found usage of an [Bindable]-annotated get function: call it via AS3.getBindable()!
        out.writeToken("AS3.getBindable(");
        memberName = CompilerUtils.quote(getter.getName());
        separatorToken = ",";
        closingToken = ")";
        String bindableEvent = getBindableEventName(getter);
        if (bindableEvent != null) {
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

  private static boolean isSuperCall(Ide ide) {
    return ide.isQualifiedBySuper()
            && ide.getScope().getClassDeclaration().getMemberDeclaration(ide.getName()) != null;
  }
  
  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    out.beginCommentWriteSymbol(typeRelation.getSymRelation());
    typeRelation.getType().getIde().visit(this);
    out.endComment();
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    Ide optName = annotationParameter.getOptName();
    visitIfNotNull(optName);
    writeOptSymbol(annotationParameter.getOptSymEq());
    AstNode optValue = annotationParameter.getValue();
    visitIfNotNull(optValue);
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

    currentMetadata.getLast().args.add(new MetadataArgument(name, value));
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    anExtends.getSuperClass().visit(this);
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
    out.writeSymbol(initializer.getSymEq());
    initializer.getValue().visit(this);
  }

  @Override
  public void visitObjectField(ObjectField objectField) throws IOException {
    objectField.getLabel().visit(this);
    out.writeSymbol(objectField.getSymColon());
    objectField.getValue().visit(this);
  }

  @Override
  public void visitForInitializer(ForInitializer forInitializer) throws IOException {
    if (forInitializer.getDecl() != null) {
      forInitializer.getDecl().visit(this);
    } else {
      visitIfNotNull(forInitializer.getExpr());
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    String[] dependencies = collectDependencies(compilationUnit, null);
    String[] requires = collectDependencies(compilationUnit, true);
    String[] uses = collectDependencies(compilationUnit, false);
    String moduleName = CompilerUtils.quote(getModuleName(compilationUnit));
    PackageDeclaration packageDeclaration = compilationUnit.getPackageDeclaration();
    out.write("Ext.define(");
    out.write(moduleName);
    out.write(", function(" + primaryDeclaration.getName() + ") {");
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
    if (primaryClassDefinitionBuilder.staticCode.length() > 0) {
      out.write(", function(clazz) {\n");
      out.write(String.format("  clazz.%s();\n", INIT_STATICS));
      out.write("}");
    }
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
        classDefinition.set("__factory__", JsonObject.code(factory));
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
          superInterfaces.add(compilationUnitAccessCode(superInterface));
        }
        superTypes = superTypes.getTail();
      }
      classDefinition.set("mixins", new JsonArray(superInterfaces.toArray()));
    }
  }

  private String[] collectDependencies(CompilationUnit compilationUnit, Boolean required) throws IOException {
    List<String> requires = new ArrayList<String>();
    Collection<CompilationUnit> dependentCompilationUnits = compilationUnit.getDependenciesAsCompilationUnits();
    for (CompilationUnit dependentCU : dependentCompilationUnits) {
      if (required != null && compilationUnit.isRequiredDependency(dependentCU) != required) {
        continue;
      }
      IdeDeclaration dependentDeclaration = dependentCU.getPrimaryDeclaration();
      String javaScriptName;
      String javaScriptNameToRequire;
      Annotation nativeAnnotation = dependentCU.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
      if (nativeAnnotation == null) {
        javaScriptName = getModuleName(dependentDeclaration);
        javaScriptNameToRequire = javaScriptName;
      } else {
        String javaScriptAlias = getNativeAnnotationValue(nativeAnnotation);
        if (javaScriptAlias != null) {
          javaScriptName = javaScriptAlias;
        } else {
          javaScriptName = dependentDeclaration.getQualifiedNameStr();
        }
        javaScriptNameToRequire = getNativeAnnotationRequireValue(nativeAnnotation);
        if ("".equals(javaScriptNameToRequire)) {
          javaScriptNameToRequire = javaScriptName;
        }
      }
      if (javaScriptNameToRequire != null) {
        requires.add(javaScriptNameToRequire);
      }
      imports.put(dependentCU.getPrimaryDeclaration().getQualifiedNameStr(), javaScriptName);
    }

    return requires.toArray(new String[requires.size()]);
  }

  private String getNativeAnnotationValue(Annotation nativeAnnotation) {
    return (String) getAnnotationParameterValue(nativeAnnotation, null, null);
  }

  private String getNativeAnnotationRequireValue(Annotation nativeAnnotation) {
    return (String) getAnnotationParameterValue(nativeAnnotation, Jooc.NATIVE_ANNOTATION_REQUIRE_PROPERTY, "");
  }

  private static Object getAnnotationParameterValue(Annotation nativeAnnotation, String name,
                                                    Object defaultValue) {
    CommaSeparatedList<AnnotationParameter> annotationParameters = nativeAnnotation.getOptAnnotationParameters();
    while (annotationParameters != null) {
      AnnotationParameter annotationParameter = annotationParameters.getHead();
      Ide optName = annotationParameter.getOptName();
      if (optName != null && name != null && name.equals(optName.getName())
              || optName == null && name == null) {
        AstNode value = annotationParameter.getValue();
        return value == null ? defaultValue : value.getSymbol().getJooValue();
      }
      annotationParameters = annotationParameters.getTail();
    }
    return null;
  }

  private static String getModuleName(CompilationUnit compilationUnit) {
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    return getModuleName(primaryDeclaration);
  }

  private static String getModuleName(IdeDeclaration primaryDeclaration) {
    return AS3_NAMESPACE_DOT + primaryDeclaration.getQualifiedNameStr();
  }

  private JsonObject createClassDefinition(ClassDeclaration classDeclaration) throws IOException {
    JsonObject classDefinition = new JsonObject();
    if (classDeclaration.getInheritanceLevel() > 1) {
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
    JsonObject members = convertMembers(classDefinitionBuilder.members);
    JsonObject bindables = convertBindables(classDefinitionBuilder.members);
    if (!bindables.isEmpty()) {
      members.set("config", bindables);
    }
    if (!members.isEmpty()) {
      classDefinition.add(members);
    }
    JsonObject staticMembers = convertMembers(classDefinitionBuilder.staticMembers);
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

  private JsonObject convertMembers(Map<String, PropertyDefinition> members) {
    JsonObject membersDefinition = new JsonObject();
    for (Map.Entry<String, PropertyDefinition> entry : members.entrySet()) {
      if (entry.getValue().isValueOnly() && !entry.getValue().bindable) {
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
          ideText = "this";
        }
        if ("this".equals(ideText) && ide.isRewriteThis()) {
          ideText = "this$";
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
      qualifiedIde.getQualifier().visit(this);
      out.writeSymbol(qualifiedIde.getSymDot());
      visitIde(qualifiedIde);
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
    visitIde(ideWithTypeParam);
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
  public void visitImplements(Implements anImplements) throws IOException {
    out.writeSymbol(anImplements.getSymImplements());
    generateImplements(anImplements.getSuperTypes());
  }

  private void generateImplements(CommaSeparatedList<Ide> superTypes) throws IOException {
    out.writeSymbol(superTypes.getHead().getSymbol());
    if (superTypes.getSymComma() != null) {
      out.writeSymbol(superTypes.getSymComma());
      generateImplements(superTypes.getTail());
    }
  }

  @Override
  public void visitType(Type type) throws IOException {
    type.getIde().visit(this);
  }

  @Override
  public void visitObjectLiteral(ObjectLiteral objectLiteral) throws IOException {
    out.writeSymbol(objectLiteral.getLBrace());
    visitIfNotNull(objectLiteral.getFields());
    writeOptSymbol(objectLiteral.getOptComma());
    out.writeSymbol(objectLiteral.getRBrace());
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
  public <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException {
    out.writeSymbol(parenthesizedExpr.getLParen());
    visitIfNotNull(parenthesizedExpr.getExpr());
    out.writeSymbol(parenthesizedExpr.getRParen());
  }

  @Override
  public void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException {
    visitParenthesizedExpr(arrayLiteral);
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

  private static String resolveBindable(DotExpr dotExpr, MethodType methodType) throws IOException {
    //System.err.println("*#*#*#* trying to find " + methodType + "ter for qIde " + qIde.getQualifiedNameStr());
    IdeDeclaration lhsType = dotExpr.getArg().getType();
    if (lhsType instanceof ClassDeclaration) {
      MemberModel member = findMemberWithBindableAnnotation(dotExpr.getIde(), methodType, (ClassDeclaration) lhsType);
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

  private static MemberModel findMemberWithBindableAnnotation(Ide qIde, MethodType methodType, ClassDeclaration typeDeclaration) throws IOException {
    String memberName = qIde.getIde().getText();
    MemberModel member = lookupPropertyDeclaration(typeDeclaration, memberName, methodType);
//      System.err.println("*#*#*#* found member " + member + " for " + typeDeclaration.getQualifiedNameStr()
//              + "#" + memberName + " for qIde " + qIde.getQualifiedNameStr());
    if (member != null) {
      List<AnnotationModel> bindableAnnotations = member.getAnnotations(Jooc.BINDABLE_ANNOTATION_NAME);
      if (bindableAnnotations.size() > 0) {
        return member;
      }
    }
    return null;
  }

  private static Map<String, AnnotationPropertyModel> getBindablePropertiesByName(MemberModel member) {
    return member.getAnnotations(Jooc.BINDABLE_ANNOTATION_NAME).get(0).getPropertiesByName();
  }

  private static String resolveBindable(Ide qIde, MethodType methodType, ClassDeclaration typeDeclaration) throws IOException {
    MemberModel member = findMemberWithBindableAnnotation(qIde, methodType, typeDeclaration);
    return member == null ? null : getBindablePropertyName(methodType, member);
  }

  private static String getBindablePropertyName(MethodType methodType, MemberModel member) {
    AnnotationPropertyModel bindableAnnotationValue = getBindablePropertiesByName(member).get(null);
    if (bindableAnnotationValue == null) {
      return (methodType == MethodType.GET &&
              "Boolean".equals(((MethodModel) member).getReturnModel().getType())
              ? "is" : methodType) + MxmlUtils.capitalize(member.getName());
    } else {
      return bindableAnnotationValue.getStringValue();
    }
  }

  private static String getBindableEventName(MemberModel member) {
    AnnotationPropertyModel eventAnnotation = getBindablePropertiesByName(member).get("event");
    return eventAnnotation == null ? null : eventAnnotation.getStringValue();
  }


  private static MemberModel lookupPropertyDeclaration(ClassDeclaration classDeclaration, String memberName,
                                                       MethodType methodType) throws IOException {
    MemberModel member;
    ClassDeclaration superDeclaration = classDeclaration.getSuperTypeDeclaration();
    if (superDeclaration != null) {
      member = lookupPropertyDeclaration(superDeclaration, memberName, methodType);
      if (member != null) {
        return member;
      }
    }
    // TODO: also look in implemented interfaces first!

    CompilationUnitModel compilationUnitModel = new ApiModelGenerator(false).generateModel(classDeclaration.getCompilationUnit()); // TODO: Evil! Must cache models in registry!
    member = compilationUnitModel.getClassModel().getMember(memberName);
    if (member instanceof PropertyModel) {
      member = ((PropertyModel) member).getMethod(methodType);
    } else if (!(member instanceof FieldModel) ||
            methodType == MethodType.SET && ((FieldModel) member).isConst()) {
      member = null;
    }
    return member;
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
  public void visitAsExpr(AsExpr asExpr) throws IOException {
    visitInfixOpExpr(asExpr);
  }

  @Override
  public void visitArrayIndexExpr(ArrayIndexExpr arrayIndexExpr) throws IOException {
    arrayIndexExpr.getArray().visit(this);
    arrayIndexExpr.getIndexExpr().visit(this);
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
    generateFunTailCode(functionExpr);
  }

  public void generateFunTailCode(FunctionExpr functionExpr) throws IOException {
    Parameters params = functionExpr.getParams();
    if (functionExpr.hasBody()) {
      if (functionExpr.isArgumentsUsedAsArray()) {
        functionExpr.getBody().addBlockStartCodeGenerator(ARGUMENT_TO_ARRAY_CODE_GENERATOR);
      }
      if (params != null) {
        // inject into body for generating initializers later:
        functionExpr.getBody().addBlockStartCodeGenerator(getParameterInitializerCodeGenerator(params));
      }
    }
    generateSignatureJsCode(functionExpr);
    if (functionExpr.hasBody()) {
      functionExpr.getBody().visit(this);
    }
  }

  public CodeGenerator getParameterInitializerCodeGenerator(final Parameters params) {
    return new CodeGenerator() {
      @Override
      public void generate(JsWriter out, boolean first) throws IOException {
        // collect the ... (rest) parameter and all optional parameters with their position index:
        int restParamIndex = -1;
        Parameter restParam = null;
        Map<Integer,Parameter> paramByIndex = new HashMap<Integer, Parameter>();
        Parameters parameters = params;
        for (int paramIndex = 0; parameters != null; parameters = parameters.getTail()) {
          Parameter param = parameters.getHead();
          if (param.isRest()) {
            restParamIndex = paramIndex;
            restParam = param;
            break;
          }
          if (param.hasInitializer()) {
            paramByIndex.put(paramIndex, param);
          }
          ++paramIndex;
        }
        generateParameterInitializers(out, paramByIndex);
        if (restParam != null) {
          generateRestParamCode(restParam, restParamIndex);
        }
      }

    };
  }

  private final MessageFormat IF_ARGUMENT_LENGTH_LTE_$N = new MessageFormat("if(arguments.length<={0})");
  private final MessageFormat SWITCH_$INDEX = new MessageFormat("switch({0,choice,0#arguments.length|0<Math.max(arguments.length,{0})})");
  private final MessageFormat CASE_$N = new MessageFormat("case {0}:");

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

  public void generateRestParamCode(Parameter param, int paramIndex) throws IOException {
    String paramName = param.getName();
    if (paramName != null && !(paramName.equals(FunctionExpr.ARGUMENTS) && paramIndex == 0)) {
      generateToArrayCode(paramName, paramIndex);
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

  public void generateSignatureJsCode(FunctionExpr functionExpr) throws IOException {
    out.writeSymbol(functionExpr.getLParen());
    visitIfNotNull(functionExpr.getParams());
    out.writeSymbol(functionExpr.getRParen());
    visitIfNotNull(functionExpr.getOptTypeRelation());
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
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    if (applyExpr.getArgs() != null && applyExpr.getFun() instanceof IdeExpr &&
            SyntacticKeywords.ASSERT.equals(applyExpr.getFun().getSymbol().getText())) {
      applyExpr.getFun().visit(this);
      JooSymbol symKeyword = applyExpr.getFun().getSymbol();
      out.writeSymbol(applyExpr.getArgs().getLParen());
      applyExpr.getArgs().getExpr().visit(this);
      out.writeToken(", ");
      out.writeString(new File(symKeyword.getFileName()).getName());
      out.writeToken(", ");
      out.writeInt(symKeyword.getLine());
      out.write(", ");
      out.writeInt(symKeyword.getColumn());
      out.writeSymbol(applyExpr.getArgs().getRParen());
    } else {
      generateFunJsCode(applyExpr);
    }
  }

  private void generateFunJsCode(ApplyExpr applyExpr) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (applyExpr.isTypeCast()) {
      out.writeToken("AS3.cast");
      out.writeSymbol(args.getLParen());
      applyExpr.getFun().visit(this);
      out.writeToken(",");
      // isTypeCast() ensures that there is exactly one parameter:
      args.getExpr().getHead().visit(this);
      out.writeSymbol(args.getRParen());
    } else {
      applyExpr.getFun().visit(this);
      // check for super call:
      if (args != null && applyExpr.getFun() instanceof IdeExpr && isSuperCall(((IdeExpr) applyExpr.getFun()).getIde())) {
        out.writeSymbol(args.getLParen());
        out.writeToken("[");
        visitIfNotNull(args.getExpr());
        out.writeToken("]");
        out.writeSymbol(args.getRParen());
      } else {
        visitIfNotNull(args);
      }
    }
  }

  @Override
  public void visitNewExpr(NewExpr newExpr) throws IOException {
    out.writeSymbol(newExpr.getSymNew());
    newExpr.getApplyConstructor().visit(this);
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    out.beginComment();
    out.writeSymbol(classBody.getLBrace());
    out.endComment();
    boolean inStaticInitializerBlock = false;
    for (Directive directive : classBody.getDirectives()) {
      final boolean isStaticInitializer = directive instanceof Statement && !(directive instanceof Declaration);
      if (isStaticInitializer) {
        inStaticInitializerBlock = beginStaticInitializer(out, inStaticInitializerBlock);
      } else {
        inStaticInitializerBlock = endStaticInitializer(out, inStaticInitializerBlock);
      }
      directive.visit(this);
    }
    endStaticInitializer(out, inStaticInitializerBlock);
    out.beginComment();
    out.writeSymbol(classBody.getRBrace());
    out.endComment();
  }

  private boolean beginStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (!inStaticInitializerBlock) {
      String staticFunctionName = "static$" + staticCodeCounter++;
      out.writeToken(String.format("function %s(){", staticFunctionName));
      primaryClassDefinitionBuilder.staticCode.append("          ").append(staticFunctionName).append("();\n");
    }
    return true;
  }

  private boolean endStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (inStaticInitializerBlock) {
      out.writeToken("}");
    }
    return false;
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    out.writeSymbol(blockStatement.getLBrace());
    boolean first = true;
    for (CodeGenerator codeGenerator : blockStatement.getBlockStartCodeGenerators()) {
      codeGenerator.generate(out, first);
      first = false;
    }
    visitAll(blockStatement.getDirectives());
    out.writeSymbol(blockStatement.getRBrace());
  }

  @Override
  public void visitDefaultStatement(DefaultStatement defaultStatement) throws IOException {
    out.writeSymbol(defaultStatement.getSymDefault());
    out.writeSymbol(defaultStatement.getSymColon());
  }

  @Override
  public void visitLabeledStatement(LabeledStatement labeledStatement) throws IOException {
    labeledStatement.getIde().visit(this);
    out.writeSymbol(labeledStatement.getSymColon());
    labeledStatement.getStatement().visit(this);
  }

  @Override
  public void visitIfStatement(IfStatement ifStatement) throws IOException {
    out.writeSymbol(ifStatement.getSymKeyword());
    ifStatement.getCond().visit(this);
    ifStatement.getIfTrue().visit(this);
    if (ifStatement.getSymElse() != null) {
      out.writeSymbol(ifStatement.getSymElse());
      ifStatement.getIfFalse().visit(this);
    }
  }

  @Override
  public void visitCaseStatement(CaseStatement caseStatement) throws IOException {
    out.writeSymbol(caseStatement.getSymKeyword());
    caseStatement.getExpr().visit(this);
    out.writeSymbol(caseStatement.getSymColon());
  }

  @Override
  public void visitTryStatement(TryStatement tryStatement) throws IOException {
    out.writeSymbol(tryStatement.getSymKeyword());
    tryStatement.getBlock().visit(this);
    visitAll(tryStatement.getCatches());
    if (tryStatement.getSymFinally() != null) {
      out.writeSymbol(tryStatement.getSymFinally());
      tryStatement.getFinallyBlock().visit(this);
    }
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void visitCatch(Catch aCatch) throws IOException {
    List<Catch> catches = aCatch.getParentTryStatement().getCatches();
    Catch firstCatch = catches.get(0);
    boolean isFirst = aCatch.equals(firstCatch);
    boolean isLast = aCatch.equals(catches.get(catches.size() - 1));
    TypeRelation typeRelation = aCatch.getParam().getOptTypeRelation();
    boolean hasCondition = aCatch.hasCondition();
    if (!hasCondition && !isLast) {
      throw Jooc.error(aCatch.getRParen(), "Only last catch clause may be untyped.");
    }
    final JooSymbol errorVar = firstCatch.getParam().getIde().getIde();
    final JooSymbol localErrorVar = aCatch.getParam().getIde().getIde();
    // in the following, always take care to write whitespace only once!
    out.writeSymbolWhitespace(aCatch.getSymKeyword());
    if (isFirst) {
      out.writeSymbolToken(aCatch.getSymKeyword()); // "catch"
      // "(localErrorVar)":
      out.writeSymbol(aCatch.getLParen(), !hasCondition);
      out.writeSymbol(errorVar, !hasCondition);
      if (!hasCondition && typeRelation != null) {
        // can only be ": *", add as comment:
        typeRelation.visit(this);
      }
      out.writeSymbol(aCatch.getRParen(), !hasCondition);
      if (hasCondition || !isLast) {
        // a catch block always needs a brace, so generate one for conditions:
        out.writeToken("{");
      }
    } else {
      // transform catch(ide:Type){...} into else if is(e,Type)){var ide=e;...}
      out.writeToken("else");
    }
    if (hasCondition) {
      out.writeToken("if(AS3.is");
      out.writeSymbol(aCatch.getLParen());
      out.writeSymbolWhitespace(localErrorVar);
      out.writeSymbolToken(errorVar);
      out.writeSymbolWhitespace(typeRelation.getSymRelation());
      out.writeToken(",");
      Ide typeIde = typeRelation.getType().getIde();
      out.writeSymbolWhitespace(typeIde.getIde());
      out.writeToken(compilationUnitAccessCode(typeIde.getDeclaration()));
      out.writeSymbol(aCatch.getRParen());
      out.writeToken(")");
    }
    if (!localErrorVar.getText().equals(errorVar.getText())) {
      aCatch.getBlock().addBlockStartCodeGenerator(new VarCodeGenerator(localErrorVar, errorVar));
    }
    aCatch.getBlock().visit(this);
    if (isLast) {
      if (hasCondition) {
        out.writeToken("else throw");
        out.writeSymbolToken(errorVar);
        out.writeToken(";");
      }
      if (!(isFirst && !hasCondition)) {
        // last catch clause closes the JS catch block:
        out.writeToken("}");
      }
    }
  }

  private static class VarCodeGenerator implements CodeGenerator {
    private final JooSymbol localErrorVar;
    private final JooSymbol errorVar;

    public VarCodeGenerator(JooSymbol localErrorVar, JooSymbol errorVar) {
      this.localErrorVar = localErrorVar;
      this.errorVar = errorVar;
    }

    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      out.writeToken("var");
      out.writeSymbolToken(localErrorVar);
      out.writeToken("=");
      out.writeSymbolToken(errorVar);
      out.writeToken(";");
    }
  }

  @Override
  public void visitForInStatement(final ForInStatement forInStatement) throws IOException {
    final Ide exprAuxIde = forInStatement.getExprAuxIde();
    IdeDeclaration exprType = forInStatement.getExpr().getType();
    String exprTypeName = exprType != null  ? exprType.getQualifiedNameStr() : "";
    boolean iterateArrayMode = "Array".equals(exprTypeName) || "Vector$object".equals(exprTypeName);
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
      ((BlockStatement) forInStatement.getBody()).addBlockStartCodeGenerator(new CodeGenerator() {
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
  public void visitWhileStatement(WhileStatement whileStatement) throws IOException {
    out.writeSymbol(whileStatement.getSymKeyword());
    visitIfNotNull(whileStatement.getOptCond());
    whileStatement.getBody().visit(this);
  }

  @Override
  public void visitForStatement(ForStatement forStatement) throws IOException {
    out.writeSymbol(forStatement.getSymKeyword());
    out.writeSymbol(forStatement.getLParen());
    visitIfNotNull(forStatement.getForInit());
    out.writeSymbol(forStatement.getSymSemicolon1());
    visitIfNotNull(forStatement.getOptCond());
    out.writeSymbol(forStatement.getSymSemicolon2());
    visitIfNotNull(forStatement.getOptStep());
    out.writeSymbol(forStatement.getRParen());
    forStatement.getBody().visit(this);
  }

  @Override
  public void visitDoStatement(DoStatement doStatement) throws IOException {
    out.writeSymbol(doStatement.getSymKeyword());
    doStatement.getBody().visit(this);
    out.writeSymbol(doStatement.getSymWhile());
    doStatement.getOptCond().visit(this);
    out.writeSymbol(doStatement.getSymSemicolon());
  }

  @Override
  public void visitSwitchStatement(SwitchStatement switchStatement) throws IOException {
    out.writeSymbol(switchStatement.getSymKeyword());
    switchStatement.getCond().visit(this);
    switchStatement.getBlock().visit(this);
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void visitSemicolonTerminatedStatement(SemicolonTerminatedStatement semicolonTerminatedStatement) throws IOException {
    visitIfNotNull(semicolonTerminatedStatement.getOptStatement());
    writeOptSymbol(semicolonTerminatedStatement.getOptSymSemicolon());
  }

  @Override
  public void visitContinueStatement(ContinueStatement continueStatement) throws IOException {
    out.writeSymbol(continueStatement.getSymKeyword());
    visitIfNotNull(continueStatement.getOptStatement());
    visitIfNotNull(continueStatement.getOptLabel());
    writeOptSymbol(continueStatement.getOptSymSemicolon());
  }

  @Override
  public void visitBreakStatement(BreakStatement breakStatement) throws IOException {
    out.writeSymbol(breakStatement.getSymKeyword());
    visitIfNotNull(breakStatement.getOptStatement());
    visitIfNotNull(breakStatement.getOptLabel());
    writeOptSymbol(breakStatement.getOptSymSemicolon());
  }

  @Override
  public void visitThrowStatement(ThrowStatement throwStatement) throws IOException {
    out.writeSymbol(throwStatement.getSymKeyword());
    visitIfNotNull(throwStatement.getOptStatement());
    writeOptSymbol(throwStatement.getOptSymSemicolon());
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
    out.writeSymbol(returnStatement.getSymKeyword());
    visitIfNotNull(returnStatement.getOptStatement());
    writeOptSymbol(returnStatement.getOptSymSemicolon());
  }

  @Override
  public void visitEmptyStatement(EmptyStatement emptyStatement) throws IOException {
    visitSemicolonTerminatedStatement(emptyStatement);
  }

  @Override
  public void visitEmptyDeclaration(EmptyDeclaration emptyDeclaration) throws IOException {
    out.writeSymbolWhitespace(emptyDeclaration.getSymSemicolon());
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    Debug.assertTrue(parameter.getModifiers() == 0, "Parameters must not have any modifiers");
    boolean isRest = parameter.isRest();
    if (parameter.getOptSymConstOrRest() != null) {
      out.beginCommentWriteSymbol(parameter.getOptSymConstOrRest());
      if (isRest) {
        parameter.getIde().visit(this);
      }
      out.endComment();
    }
    if (!isRest) {
      parameter.getIde().visit(this);
    }
    visitIfNotNull(parameter.getOptTypeRelation());
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
    if ((variableDeclaration.isClassMember() || variableDeclaration.isPrimaryDeclaration()) && !variableDeclaration.isPrivateStatic()) {
      if (!variableDeclaration.isPrimaryDeclaration() && !currentMetadata.isEmpty()) {
        getClassDefinitionBuilder(variableDeclaration).storeCurrentMetadata(
                variableDeclaration.getIde().getName() + (variableDeclaration.isPrivate() ? "$" + variableDeclaration.getClassDeclaration().getInheritanceLevel() : ""),
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
      registerField(variableDeclaration);
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
          String value = getValueFromEmbedMetadata();
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
          registerField(variableDeclaration);
        } else {
          optInitializer.visit(this);
        }
      }
      visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
      writeOptSymbol(variableDeclaration.getOptSymSemicolon());
    }
    resetCurrentMetadata(variableDeclaration);
  }

  private void resetCurrentMetadata(IdeDeclaration declaration) {
    if (declaration.isClassMember() || declaration.isPrimaryDeclaration()) {
      currentMetadata = new LinkedList<Metadata>();
    }
  }

  private String getValueFromEmbedMetadata() {
    Metadata embedMetadata = Metadata.find(currentMetadata, "Embed");
    if (embedMetadata != null) {
      String source = (String) embedMetadata.getArgumentValue("source");
      String assetType = CompilationUnit.guessAssetType(source);
      int index = compilationUnit.getResourceDependencies().indexOf(assetType + "!" + source);
      String assetFactory = "new String";
      if ("image".equals(assetType)) {
        assetFactory = imports.get("flash.display.Bitmap") + ".fromImg";
      }
      return String.format("function(){return %s($resource_%d)}", assetFactory, index);
    }
    return null;
  }

  private void registerField(VariableDeclaration variableDeclaration) {
    String variableName = variableDeclaration.getName();

    boolean isBindable = Metadata.find(currentMetadata, "Bindable") != null;
    String value = null;
    if (mustInitializeInStaticCode(variableDeclaration)) {
      if (variableDeclaration.isStatic()) {
        primaryClassDefinitionBuilder.staticCode.append("          ").append(variableName).append("$static_();\n");
      }
      if (isBindable) {
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
        value = getValueFromEmbedMetadata();
        if (value == null) {
          TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();
          value = VariableDeclaration.getDefaultValue(typeRelation);
        }
      }
      if (variableDeclaration.isPrivate() && !variableDeclaration.isStatic()) {
        variableName += "$" + ((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).getInheritanceLevel();
      }
    }
    if (variableDeclaration.isPrimaryDeclaration()) {
      factory = value == null
              ? variableName + "_"
              : String.format("function() {\n        return(%s);\n      }", value);
      return;
    }
    if (value != null) {
      membersOrStaticMembers(variableDeclaration).put(variableName,
              new PropertyDefinition(value, !variableDeclaration.isConst(), isBindable));
    }
    // special Ext magic: when declaring a public static const xtype:String, add an alias: "widget." + Clazz.xtype:
    if (variableDeclaration.isPublic() && variableDeclaration.isStatic() && variableDeclaration.isConst()
            && "xtype".equals(variableDeclaration.getName())
            && variableDeclaration.getOptInitializer() != null
            && variableDeclaration.getOptInitializer().getValue() instanceof LiteralExpr) {
      getClassDefinitionBuilder(variableDeclaration).members.put("alias",
              new PropertyDefinition("\"widget." + ((LiteralExpr) variableDeclaration.getOptInitializer().getValue()).getValue().getJooValue() + "\""));
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
      String slotName = variableName + (variableDeclaration.isPrivate() ? "$" + variableDeclaration.getClassDeclaration().getInheritanceLevel() : "");
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

  private static final CodeGenerator ALIAS_THIS_CODE_GENERATOR = new CodeGenerator() {
    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      out.write("var this$=this;");
    }
  };

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    boolean isPrimaryDeclaration = functionDeclaration.equals(compilationUnit.getPrimaryDeclaration());
    assert functionDeclaration.isClassMember() || (!functionDeclaration.isNative() && !functionDeclaration.isAbstract());
    if (isPrimaryDeclaration) {
      factory = "function() {\n        return " + functionDeclaration.getName() + ";\n      }";
    }
    if (functionDeclaration.isThisAliased()) {
      functionDeclaration.getBody().addBlockStartCodeGenerator(ALIAS_THIS_CODE_GENERATOR);
    }
    if (functionDeclaration.isConstructor() && !functionDeclaration.containsSuperConstructorCall() && functionDeclaration.hasBody()) {
      functionDeclaration.getBody().addBlockStartCodeGenerator(new SuperCallCodeGenerator(functionDeclaration.getClassDeclaration()));
    }
    if (!functionDeclaration.isClassMember() && !isPrimaryDeclaration) {
      functionDeclaration.getFun().visit(this);
    } else {
      if (!isPrimaryDeclaration && !currentMetadata.isEmpty()) {
        getClassDefinitionBuilder(functionDeclaration).storeCurrentMetadata(
                functionDeclaration.getIde().getName(),
                currentMetadata
        );
      }
      out.beginComment();
      writeModifiers(out, functionDeclaration);
      if (functionDeclaration.isAbstract() || functionDeclaration.isNative()) {
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
        writeOptSymbol(functionDeclaration.getSymGetOrSet());
        functionDeclaration.getIde().visit(this);
        generateSignatureJsCode(functionDeclaration.getFun());
        writeOptSymbol(functionDeclaration.getOptSymSemicolon());
        out.endComment();
      } else {
        out.endComment();
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
        JooSymbol functionSymbol = functionDeclaration.getIde().getSymbol();
        String functionName = convertIdentifier(functionSymbol.getText());
        String methodName = functionName;

        boolean isAccessor = functionDeclaration.isGetterOrSetter();
        if (isAccessor) {
          Metadata bindableAnnotation = Metadata.find(currentMetadata, Jooc.BINDABLE_ANNOTATION_NAME);
          if (bindableAnnotation != null) {
            String accessorPrefix = functionDeclaration.getSymGetOrSet().getText();
            String accessorName = (String) bindableAnnotation.getArgumentValue(DEFAULT_ANNOTATION_PARAMETER_NAME);
            if (accessorName != null) {
              methodName = accessorName;
            } else {
              TypeRelation typeRelation = functionDeclaration.getOptTypeRelation();
              String methodPrefix = functionDeclaration.isGetter() && typeRelation != null &&
                      "Boolean".equals(typeRelation.getType().getIde().getName()) ? "is" : accessorPrefix;
              methodName = methodPrefix + MxmlUtils.capitalize(functionName);
            }
            functionName = accessorPrefix + "$" + functionName;
            isAccessor = false;
          }
        } else if (functionDeclaration.isConstructor()) {
          functionName += "$";
        }

        String overriddenMethodName = null;
        PropertyDefinition overriddenPropertyDefinition = null;
        if (functionDeclaration.isPrivate() && !functionDeclaration.isStatic()) {
          String privateMethodName = methodName + "$" + functionDeclaration.getClassDeclaration().getInheritanceLevel();
          if (functionDeclaration.isOverride()) {
            overriddenMethodName = privateMethodName;
            getClassDefinitionBuilder(functionDeclaration).super$Used = true;
          } else {
            methodName = privateMethodName;
          }
        } else if (functionDeclaration.isStatic()) {
          functionName += "$static";
        }
        Map<String, PropertyDefinition> members = membersOrStaticMembers(functionDeclaration);
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
                    new PropertyDefinition(functionName));
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
    }
    resetCurrentMetadata(functionDeclaration);
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    ClassDefinitionBuilder classDefinitionBuilder = classDeclaration.isPrimaryDeclaration()
            ? primaryClassDefinitionBuilder : (secondaryClassDefinitionBuilder = new ClassDefinitionBuilder());
    classDefinitionBuilder.storeCurrentMetadata("", currentMetadata);
    currentMetadata = new LinkedList<Metadata>();
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

    if (!classDeclaration.isInterface() && classDeclaration.getConstructor() == null) {
      // generate default constructor that calls field initializers:
      String constructorName = classDeclaration.getName() + "$";
      out.write("function " + constructorName + "() {");
      new SuperCallCodeGenerator(classDeclaration).generate(out, true);
      out.write("}");
      classDefinitionBuilder.members.put("constructor", new PropertyDefinition(constructorName));
    }
  }

  private void generateFieldInitCode(ClassDeclaration classDeclaration, boolean startWithSemicolon) throws IOException {
    Iterator<VariableDeclaration> iterator = classDeclaration.getFieldsWithInitializer().iterator();
    if (iterator.hasNext()) {
      if (startWithSemicolon) {
        out.write(";");
      }
      do {
        VariableDeclaration field = iterator.next();
        generateInitCode(field, true);
      } while (iterator.hasNext());
    }
  }

  public void generateInitCode(VariableDeclaration field, boolean endWithSemicolon) throws IOException {
    out.write(field.getName() + "_.call(this)");
    if (endWithSemicolon) {
      out.write(";");
    }
  }

  private class SuperCallCodeGenerator implements CodeGenerator {
    private ClassDeclaration classDeclaration;

    public SuperCallCodeGenerator(ClassDeclaration classDeclaration) {
      this.classDeclaration = classDeclaration;
    }

    @Override
    public void generate(JsWriter out, boolean first) throws IOException {
      int inheritanceLevel = classDeclaration.getInheritanceLevel();
      if (inheritanceLevel > 1) { // suppress for classes extending Object
        generateSuperConstructorCallCode(null);
        out.writeToken(";");
      }
      generateFieldInitCode(classDeclaration, false);
    }
  }

  @Override
  public void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException {
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
    out.writeSymbol(packageDeclaration.getSymPackage());
    visitIfNotNull(packageDeclaration.getIde());
    out.endComment();
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    if (superConstructorCallStatement.getClassDeclaration().getInheritanceLevel() > 1) {
      out.writeSymbolWhitespace(superConstructorCallStatement.getSymbol());
      generateSuperConstructorCallCode(superConstructorCallStatement.getArgs());
      generateFieldInitCode(superConstructorCallStatement.getClassDeclaration(), true);
    } else { // suppress for classes extending Object
      // Object super call does nothing anyway:
      out.beginComment();
      out.writeSymbol(superConstructorCallStatement.getSymbol());
      visitIfNotNull(superConstructorCallStatement.getArgs());
      out.endComment();
      generateFieldInitCode(superConstructorCallStatement.getClassDeclaration(), false);
    }
    out.writeSymbol(superConstructorCallStatement.getSymSemicolon());
  }

  private void generateSuperConstructorCallCode(ParenthesizedExpr<CommaSeparatedList<Expr>> args) throws IOException {
    out.write("this.callParent");
    if (args == null) {
      out.writeToken("([])");
    } else {
      out.writeSymbol(args.getLParen());
      out.writeToken("[");
      visitIfNotNull(args.getExpr());
      out.writeToken("]");
      out.writeSymbol(args.getRParen());
    }
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    currentMetadata.add(new Metadata(annotation.getIde().getName()));
    out.beginComment();
    out.writeSymbol(annotation.getLeftBracket());
    annotation.getIde().visit(this);
    writeOptSymbol(annotation.getOptLeftParen());
    visitIfNotNull(annotation.getOptAnnotationParameters());
    writeOptSymbol(annotation.getOptRightParen());
    out.writeSymbol(annotation.getRightBracket());
    out.endComment();
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    out.beginComment();
    out.writeSymbol(useNamespaceDirective.getUseKeyword());
    out.writeSymbol(useNamespaceDirective.getNamespaceKeyword());
    useNamespaceDirective.getNamespace().visit(this);
    out.writeSymbol(useNamespaceDirective.getSymSemicolon());
    out.endComment();
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    if (importDirective.isExplicit()) {
      out.beginComment();
      out.writeSymbol(importDirective.getImportKeyword());
      importDirective.getIde().visit(this);
      out.writeSymbol(importDirective.getSymSemicolon());
      out.endComment();
    }
  }

  private static class PropertyDefinition {
    String value;
    boolean writable;
    boolean configurable;
    String get;
    String set;
    boolean bindable;

    private PropertyDefinition() {
    }

    private PropertyDefinition(String value) {
      this.value = value;
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
    List<MetadataArgument> args = new ArrayList<MetadataArgument>();

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
    Map<String,PropertyDefinition> members = new LinkedHashMap<String, PropertyDefinition>();
    Map<String,PropertyDefinition> staticMembers = new LinkedHashMap<String, PropertyDefinition>();
    StringBuilder staticCode = new StringBuilder();
    boolean super$Used = false;

    void storeCurrentMetadata(String memberName, LinkedList<Metadata> currentMetadata) {
      Object memberMetadata = metadata.get(memberName);
      List<Object> allMetadata = memberMetadata instanceof JsonArray
              ? ((JsonArray) memberMetadata).getItems()
              : new LinkedList<Object>();
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
