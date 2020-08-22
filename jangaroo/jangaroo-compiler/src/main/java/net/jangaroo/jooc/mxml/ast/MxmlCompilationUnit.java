package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.DeclarationScope;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * AST node for an MXML compilation unit, represented by its root node.
 */
public class MxmlCompilationUnit extends CompilationUnit {

  static final String DEFAULTS = "defaults";
  static final String NET_JANGAROO_EXT_EXML = "net.jangaroo.ext.Exml";
  static final String APPLY = "apply";
  private final RootElementProcessor rootElementProcessor = new RootElementProcessor();

  private final IsNativeConstructor isNativeConstructor = new IsNativeConstructor(this);
  private final IsInitMethod isInitMethod = new IsInitMethod();

  private final Collection<String> importedSymbols = new HashSet<>();

  private final InputSource source;
  private final XmlHeader optXmlHeader;
  private final XmlElement rootNode;
  private final MxmlParserHelper mxmlParserHelper;

  private final List<Directive> constructorBodyDirectives = new LinkedList<>();
  private final List<Directive> classBodyDirectives = new LinkedList<>();
  private final String classQName;

  private FunctionDeclaration initMethod;
  private Parameter constructorParam;
  private Scope constructorScope;

  private MxmlToModelParser mxmlToModelParser;
  private final Map<String, VariableDeclaration> classVariablesByName = new LinkedHashMap<>();

  public MxmlCompilationUnit(@Nonnull InputSource source, @Nullable XmlHeader optXmlHeader, @Nonnull XmlElement rootNode, @Nonnull MxmlParserHelper mxmlParserHelper) {
    // no secondary declarations: https://issues.apache.org/jira/browse/FLEX-21373
    super(null, MxmlAstUtils.SYM_LBRACE, new LinkedList<>(), null, MxmlAstUtils.SYM_RBRACE, Collections.<IdeDeclaration>emptyList());
    this.source = source;
    this.optXmlHeader = optXmlHeader;
    this.rootNode = rootNode;
    this.mxmlParserHelper = mxmlParserHelper;
    classQName = CompilerUtils.qNameFromRelativePath(source.getRelativePath());
  }

  @Override
  public void scope(Scope scope) {
    packageDeclaration = mxmlParserHelper.parsePackageDeclaration(classQName);

    JangarooParser parser = scope.getCompiler();
    constructorScope = new DeclarationScope(this, null, parser);
    mxmlToModelParser = new MxmlToModelParser(parser, mxmlParserHelper, this);

    rootElementProcessor.process(rootNode);

    // handle imports
    for (JooSymbol jooSymbol : rootElementProcessor.getImports()) {
      addImport(jooSymbol);
    }

    // init class declaration
    ClassDeclaration classDeclaration = new ClassDeclarationBuilder(parser, mxmlParserHelper, this).build();
    primaryDeclaration = classDeclaration;
    Ide superClassIde = classDeclaration.getOptExtends().getSuperClass();
    addImport(superClassIde);
    Implements impl = classDeclaration.getOptImplements();
    if(null != impl) {
      CommaSeparatedList<Ide> superTypes = impl.getSuperTypes();
      for (AstNode superType : superTypes.getChildren()) {
        if(superType instanceof Ide) {
          addImport((Ide)superType);
        }
      }
    }

    // handle annotations
    for (JooSymbol jooSymbol : rootElementProcessor.getMetadata()) {
      List<Annotation> annotations = mxmlParserHelper.parseMetadata(jooSymbol);
      if(null != annotations) {
        primaryDeclaration.getAnnotations().addAll(annotations);
      }
    }

    // find member variables
    for (Directive directive : classBodyDirectives) {
      if (directive instanceof VariableDeclaration) {
        VariableDeclaration variableDeclaration = (VariableDeclaration) directive;
        classVariablesByName.put(variableDeclaration.getName(), variableDeclaration);
        ((ClassDeclaration) primaryDeclaration).registerMember(variableDeclaration);
      }
    }

    preProcessClassBodyDirectives();

    // Check whether the super constructor has a 'config' param to construct the right super call:
    boolean useSuperConfig = CompilationUnitUtils.constructorSupportsConfigOptionsParameter(superClassIde.getQualifiedNameStr(), parser);

    ObjectLiteral defaultsConfig = createFields(useSuperConfig);
    // all kinds of (weird) cases:
    // 1. class and super class have config param (normal Ext JS) -> super(apply config onto defaults onto MXML configs)
    // 2. class has config param, super class not -> apply config onto this or (if inherits from Ext.Base) call initConfig()
    // 3. class has no config param, super class got one -> super(apply MXML configs onto defaults)
    // 4. class has no config param, super class too (normal MXML) -> super(); apply defaults to 'this'
    Ide config = constructorParam == null ? null : constructorParam.getIde();
    if (defaultsConfig.getFields() != null) {
      if (config == null) {
        config = new Ide(MxmlUtils.CONFIG);
        MxmlAstUtils.createVariableDeclaration(config, classDeclaration.getIde(), defaultsConfig);
      } else {
        ApplyExpr applyOntoDefaultsExpr = createExmlApply(defaultsConfig, new IdeExpr(config));
        AssignmentOpExpr assignmentOpExpr = new AssignmentOpExpr(new IdeExpr(config), MxmlAstUtils.SYM_EQ.withWhitespace(" "), applyOntoDefaultsExpr);
        constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(assignmentOpExpr));
      }
    }
    Expr configFromMxmlExpr = mxmlToModelParser.createExprFromElement(rootNode, true, true);
    // Only apply config onto config-from-MXML if the latter is not empty, otherwise use config in super() directly,
    // but not if super class needs an (empty) config object:
    Expr superConfigExpr = !(config == null && useSuperConfig) && isSuperConfigEmpty(configFromMxmlExpr)
            ? config == null ? null : new IdeExpr(config)
            : config == null ? configFromMxmlExpr : createExmlApply(configFromMxmlExpr, new IdeExpr(config));
    if (useSuperConfig) {
      constructorBodyDirectives.add(superConfigExpr == null
              ? MxmlAstUtils.createSuperConstructorCall()
              : MxmlAstUtils.createSuperConstructorCall(superConfigExpr));
    } else if (superConfigExpr != null) {
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(
              createExmlApply(MxmlAstUtils.createThisExpr(), superConfigExpr)));
    }

    classBodyDirectives.addAll(mxmlToModelParser.getClassBodyDirectives());

    postProcessClassBodyDirectives();

    super.scope(scope);
  }

  private static boolean isSuperConfigEmpty(Expr configFromMxmlExpr) {
    Expr superConfigObject = configFromMxmlExpr instanceof ApplyExpr
            ? ((ApplyExpr) configFromMxmlExpr).getArgs().getExpr().getHead()
            : configFromMxmlExpr;
    return superConfigObject instanceof ObjectLiteral && ((ObjectLiteral) superConfigObject).getFields() == null;
  }

  private ApplyExpr createExmlApply(Expr targetObject, Expr sourceObject) {
    return MxmlAstUtils.createApplyExpr(MxmlAstUtils.createDotExpr(addImport(NET_JANGAROO_EXT_EXML), APPLY), targetObject, sourceObject);
  }

  Ide createAuxVar(String name) {
    return constructorScope.createAuxVar(name);
  }

  void preProcessClassBodyDirectives() {
    boolean hasNativeConstructor = false;
    for (int i = 0; i < classBodyDirectives.size(); i++) {
      Directive directive = classBodyDirectives.get(i);
      if (isNativeConstructor.apply(directive)) {
        hasNativeConstructor = true;
        FunctionDeclaration constructor = MxmlAstUtils.createConstructor((FunctionDeclaration) directive, this.constructorBodyDirectives);
        Parameters params = constructor.getParams();
        if(null != params) {
          constructorParam = params.getHead();
        }
        classBodyDirectives.set(i, constructor);
      } else if (isInitMethod.apply(directive)) {
        initMethod = (FunctionDeclaration) directive;
      }
    }

    if(!hasNativeConstructor) {
      // inserting constructor
      classBodyDirectives.add(MxmlAstUtils.createConstructor(primaryDeclaration.getIde(), constructorBodyDirectives));
    } else if (null != constructorParam) {
      // remove "virtual" field declaration of constructor parameter:
      Iterator<Directive> iterator = classBodyDirectives.iterator();
      while (iterator.hasNext()) {
        Directive directive = iterator.next();
        if (directive instanceof IdeDeclaration) {
          IdeDeclaration declaration = (IdeDeclaration) directive;
          if (declaration.isPrivate() && constructorParam.getName().equals(declaration.getName())) {
            iterator.remove();
            break;
          }
        }
      }
    }

    if(null != initMethod) {
      CommaSeparatedList<Expr> args = null;
      if(null != constructorParam && initMethod.getParams() != null) {
        args = new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde()));
      }
      DotExpr initFunctionInvocation = new DotExpr(MxmlAstUtils.createThisExpr(), MxmlAstUtils.SYM_DOT, new Ide(initMethod.getIde().getSymbol().withoutWhitespace()));
      Directive directive = MxmlAstUtils.createSemicolonTerminatedStatement(new ApplyExpr(initFunctionInvocation, initMethod.getFun().getLParen(), args, initMethod.getFun().getRParen()));
      constructorBodyDirectives.add(directive);
    }
  }

  void postProcessClassBodyDirectives() {
    for(Directive directive : classBodyDirectives) {
      directive.setClassMember(true);
    }
  }

  ObjectLiteral createFields(boolean useSuperConfig) {
    List<ObjectField> defaults = new ArrayList<>();
    for (XmlElement declaration : rootElementProcessor.getDeclarations()) {
      XmlAttribute fieldNameSym = declaration.getAttribute(MxmlUtils.MXML_ID_ATTRIBUTE);
      if (fieldNameSym != null) {
        Expr valueExpr = mxmlToModelParser.createExprFromElement(declaration, null, constructorParam != null && useSuperConfig);
        if (valueExpr != null) {
          if (valueExpr instanceof AssignmentOpExpr) {
            constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(valueExpr));
          } else {
            defaults.add(MxmlAstUtils.createObjectField(fieldNameSym.getValue().getText(), valueExpr));
          }
        }
      }
    }
    return MxmlAstUtils.createObjectLiteral(defaults);
  }

  List<Directive> getClassBodyDirectives() {
    return classBodyDirectives;
  }

  @Override
  public String getQualifiedNameStr() {
    return classQName;
  }

  @Override
  public boolean isClass() {
    // MXML files do not have a primary declaration before scoping, but are known to denote classes.
    return true;
  }

  XmlTag getOptXmlHeader() {
    return optXmlHeader;
  }

  XmlElement getRootNode() {
    return rootNode;
  }

  RootElementProcessor getRootElementProcessor() {
    return rootElementProcessor;
  }

  @Override
  public InputSource getInputSource() {
    return source;
  }

  private void addImport(Ide classIde) {
    if(isNotYetImported(classIde)) {
      ImportDirective directive = MxmlAstUtils.createImport(classIde);
      getDirectives().add(directive);
    }
  }

  private boolean isNotYetImported(Ide classIde) {
    Ide qualifier = classIde.getQualifier();
    return qualifier != null && !importedSymbols.contains(qualifier.getQualifiedNameStr() + ".*") && importedSymbols.add(classIde.getQualifiedNameStr());
  }

  void addImport(@Nonnull JooSymbol symbol) {
    String jooValue = (String) symbol.getJooValue();
    if(!importedSymbols.contains(jooValue)) {
      ImportDirective directive = mxmlParserHelper.parseImport(symbol);
      if (null != directive && isNotYetImported(directive.getIde())) {
        getDirectives().add(directive);
      }
    }
  }

  @Nullable
  public Ide addImport(@Nonnull String classQName) {
    // do not import top-level classes
    if (!classQName.contains(".")) {
      // create ad-hoc Ide for MXML compiler:
      return new Ide(classQName);
    }
    ImportDirective importDirective = mxmlParserHelper.parseImport(classQName);
    if (importDirective == null) {
      // TODO: this should be an error!
      return null;
    }
    Ide ide = importDirective.getIde();
    if (isNotYetImported(ide)) {
      getDirectives().add(importDirective);
    }
    return ide;
  }

  @Nonnull
  public Map<String, VariableDeclaration> getVariables() {
    return classVariablesByName;
  }

  @Nullable
  public String getConstructorParamName() {
    return null != constructorParam ? constructorParam.getName() : null;
  }
}
