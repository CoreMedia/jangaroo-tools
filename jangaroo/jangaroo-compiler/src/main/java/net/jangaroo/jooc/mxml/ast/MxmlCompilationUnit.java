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
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    Ide superConfigVar = null;
    // If the super constructor has a 'config' param, create a fresh var for that.
    if(CompilationUnitUtils.constructorSupportsConfigOptionsParameter(superClassIde.getQualifiedNameStr(), parser)) {
      superConfigVar = createAuxVar(MxmlUtils.CONFIG);
      Ide primaryDeclaration = classDeclaration.getOptExtends().getSuperClass();
      VariableDeclaration variableDeclaration = MxmlAstUtils.createVariableDeclaration(superConfigVar, primaryDeclaration);
      constructorBodyDirectives.add(variableDeclaration);
    }

    if (null == constructorParam || null == superConfigVar) {
      createFields(superConfigVar);
    } else {
      Ide defaultsConfigVar = createAuxVar(DEFAULTS);
      Ide primaryDeclaration = getPrimaryDeclaration().getIde();
      VariableDeclaration variableDeclaration = MxmlAstUtils.createVariableDeclaration(defaultsConfigVar, primaryDeclaration);
      constructorBodyDirectives.add(variableDeclaration);

      createFields(defaultsConfigVar);
      ImportDirective importDirective = mxmlParserHelper.parseImport(NET_JANGAROO_EXT_EXML);
      getDirectives().add(importDirective);
      Ide exml = mxmlParserHelper.parseIde(" " + NET_JANGAROO_EXT_EXML);

      CommaSeparatedList<Expr> exprCommaSeparatedList = new CommaSeparatedList<>(new IdeExpr(defaultsConfigVar), MxmlAstUtils.SYM_COMMA, new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde())));
      ApplyExpr applyExpr = new ApplyExpr(new DotExpr(new IdeExpr(exml), MxmlAstUtils.SYM_DOT, new Ide(new JooSymbol(APPLY))), MxmlAstUtils.SYM_LPAREN, exprCommaSeparatedList, MxmlAstUtils.SYM_RPAREN);
      IdeExpr config = new IdeExpr(constructorParam.getIde().getSymbol().withWhitespace("\n    "));
      AssignmentOpExpr assignmentOpExpr = new AssignmentOpExpr(config, MxmlAstUtils.SYM_EQ.withWhitespace(" "), applyExpr);
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(assignmentOpExpr));
    }

    mxmlToModelParser.processAttributesAndChildNodes(rootNode, superConfigVar, new Ide(Ide.THIS), superConfigVar != null);
    constructorBodyDirectives.addAll(mxmlToModelParser.getConstructorBodyDirectives());
    classBodyDirectives.addAll(mxmlToModelParser.getClassBodyDirectives());

    if (!(null == constructorParam || null == superConfigVar)) {
      CommaSeparatedList<Expr> exprCommaSeparatedList = new CommaSeparatedList<>(new IdeExpr(superConfigVar), MxmlAstUtils.SYM_COMMA, new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde())));
      Ide exml = mxmlParserHelper.parseIde(MxmlAstUtils.INDENT_4 + NET_JANGAROO_EXT_EXML);
      ApplyExpr applyExpr = new ApplyExpr(new DotExpr(new IdeExpr(exml), MxmlAstUtils.SYM_DOT, new Ide(new JooSymbol(APPLY))), MxmlAstUtils.SYM_LPAREN, exprCommaSeparatedList, MxmlAstUtils.SYM_RPAREN);
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(applyExpr));

      constructorBodyDirectives.add(MxmlAstUtils.createSuperConstructorCall(superConfigVar));
    }

    postProcessClassBodyDirectives();

    super.scope(scope);
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
      DotExpr initFunctionInvocation = new DotExpr(new IdeExpr(new Ide(MxmlAstUtils.SYM_THIS)), MxmlAstUtils.SYM_DOT, new Ide(initMethod.getIde().getSymbol().withoutWhitespace()));
      Directive directive = MxmlAstUtils.createSemicolonTerminatedStatement(new ApplyExpr(initFunctionInvocation, initMethod.getFun().getLParen(), args, initMethod.getFun().getRParen()));
      constructorBodyDirectives.add(directive);
    }
  }

  void postProcessClassBodyDirectives() {
    for(Directive directive : classBodyDirectives) {
      directive.setClassMember(true);
    }
  }

  void createFields(@Nullable Ide targetIde) {
    for (XmlElement declaration : rootElementProcessor.getDeclarations()) {
      mxmlToModelParser.createValueCodeFromElement(targetIde, declaration, null);
    }
    Collection<Directive> directives = mxmlToModelParser.getConstructorBodyDirectives();
    this.constructorBodyDirectives.addAll(directives);
    directives.clear();
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
    Ide ide = null;
    // do not import top-level classes
    if(classQName.contains(".")) {
      ImportDirective importDirective = mxmlParserHelper.parseImport(classQName);
      if (null != importDirective) {
        ide = importDirective.getIde();
        if (isNotYetImported(ide)) {
          getDirectives().add(importDirective);
        }
      }
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
