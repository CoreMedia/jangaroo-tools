package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassBody;
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

  private final Collection<String> importedSymbols = new HashSet<String>();

  private final InputSource source;
  private final XmlElement rootNode;
  private final MxmlParserHelper mxmlParserHelper;

  private final List<Directive> constructorBodyDirectives = new LinkedList<Directive>();
  private final List<Directive> classBodyDirectives = new LinkedList<Directive>();
  private final String classQName;

  private FunctionDeclaration initMethod;
  private Parameter constructorParam;

  private MxmlToModelParser mxmlToModelParser;
  private final Map<String, VariableDeclaration> classVariablesByName = new LinkedHashMap<String, VariableDeclaration>();

  public MxmlCompilationUnit(@Nonnull InputSource source, @Nonnull XmlElement rootNode, @Nonnull MxmlParserHelper mxmlParserHelper) {
    // no secondary declarations: https://issues.apache.org/jira/browse/FLEX-21373
    super(null, MxmlAstUtils.SYM_LBRACE, new LinkedList<AstNode>(), null, MxmlAstUtils.SYM_RBRACE, Collections.<IdeDeclaration>emptyList());
    this.source = source;
    this.rootNode = rootNode;
    this.mxmlParserHelper = mxmlParserHelper;
    classQName = CompilerUtils.qNameFromRelativPath(source.getRelativePath());
  }

  @Override
  public void scope(Scope scope) {
    packageDeclaration = mxmlParserHelper.parsePackageDeclaration(classQName);

    JangarooParser parser = scope.getCompiler();
    mxmlToModelParser = new MxmlToModelParser(parser, mxmlParserHelper, this, scope);

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
      ClassBody classBody = mxmlParserHelper.parseClassBody(jooSymbol);
      List<Directive> directives = classBody.getDirectives();
      if(null != directives) {
        getDirectives().addAll(directives);
      }
    }

    // find member variables
    for (Directive directive : classBodyDirectives) {
      if (directive instanceof VariableDeclaration) {
        VariableDeclaration variableDeclaration = (VariableDeclaration) directive;
        classVariablesByName.put(variableDeclaration.getName(), variableDeclaration);
      }
    }

    preProcessClassBodyDirectives();

    Ide superConfigVar = null;
    // If the super constructor has a 'config' param, create a fresh var for that.
    if(CompilationUnitModelUtils.constructorSupportsConfigOptionsParameter(superClassIde.getQualifiedNameStr(), parser)) {
      superConfigVar = scope.createAuxVar(null, MxmlUtils.CONFIG);
      Ide primaryDeclaration = getPrimaryDeclaration().getIde();
      VariableDeclaration variableDeclaration = MxmlAstUtils.createVariableDeclaration(superConfigVar, primaryDeclaration, true);
      constructorBodyDirectives.add(variableDeclaration);
    }

    Ide exml = null;
    if (null == constructorParam || null == superConfigVar) {
      createFields(superConfigVar);
    } else {
      Ide defaultsConfigVar = scope.createAuxVar(null, DEFAULTS);
      Ide primaryDeclaration = getPrimaryDeclaration().getIde();
      VariableDeclaration variableDeclaration = MxmlAstUtils.createVariableDeclaration(defaultsConfigVar, primaryDeclaration, false);
      constructorBodyDirectives.add(variableDeclaration);

      createFields(defaultsConfigVar);
      ImportDirective importDirective = mxmlParserHelper.parseImport(NET_JANGAROO_EXT_EXML);
      exml = importDirective.getIde();
      getDirectives().add(importDirective);

      CommaSeparatedList<Expr> exprCommaSeparatedList = new CommaSeparatedList<Expr>(new IdeExpr(defaultsConfigVar), MxmlAstUtils.SYM_COMMA, new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde())));
      ApplyExpr applyExpr = new ApplyExpr(new DotExpr(new IdeExpr(exml), MxmlAstUtils.SYM_DOT, new Ide(new JooSymbol(APPLY))), MxmlAstUtils.SYM_LPAREN, exprCommaSeparatedList, MxmlAstUtils.SYM_RPAREN);
      IdeExpr config = new IdeExpr(constructorParam.getIde().getSymbol().withWhitespace("\n"));
      AssignmentOpExpr assignmentOpExpr = new AssignmentOpExpr(config, MxmlAstUtils.SYM_EQ, applyExpr);
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(assignmentOpExpr));
    }

    String varName = null;
    if(null != superConfigVar) {
      varName = superConfigVar.getName();
    }
    mxmlToModelParser.processAttributesAndChildNodes(rootNode, varName, Ide.THIS, superConfigVar != null);
    constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(mxmlToModelParser.getConstructorCode()));
    classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(mxmlToModelParser.getClassBodyCode()).getDirectives());

    if (null != exml) {
      CommaSeparatedList<Expr> exprCommaSeparatedList = new CommaSeparatedList<Expr>(new IdeExpr(superConfigVar), MxmlAstUtils.SYM_COMMA, new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde())));
      ApplyExpr applyExpr = new ApplyExpr(new DotExpr(new IdeExpr(exml), MxmlAstUtils.SYM_DOT, new Ide(new JooSymbol(APPLY))), MxmlAstUtils.SYM_LPAREN, exprCommaSeparatedList, MxmlAstUtils.SYM_RPAREN);
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(applyExpr));

      constructorBodyDirectives.add(MxmlAstUtils.createSuperConstructorCall(exprCommaSeparatedList));
    }

    postProcessClassBodyDirectives();

    super.scope(scope);
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
    }

    if(null != initMethod) {
      CommaSeparatedList<Expr> args = null;
      if(null != constructorParam) {
        args = new CommaSeparatedList<Expr>(new IdeExpr(constructorParam.getIde()));
      }
      DotExpr intFunctionInvocation = new DotExpr(new IdeExpr(new Ide(MxmlAstUtils.SYM_THIS)), MxmlAstUtils.SYM_DOT, initMethod.getIde());
      Directive directive = MxmlAstUtils.createSemicolonTerminatedStatement(new ApplyExpr(intFunctionInvocation, initMethod.getFun().getLParen(), args, initMethod.getFun().getRParen()));
      constructorBodyDirectives.add(directive);
    }
  }

  void postProcessClassBodyDirectives() {
    for(Directive directive : classBodyDirectives) {
      directive.setClassMember(true);
    }
  }

  void createFields(@Nullable Ide targetIde) {
    String name = null != targetIde ? targetIde.getName() : "";
    for (XmlElement declaration : rootElementProcessor.getDeclarations()) {
      mxmlToModelParser.createValueCodeFromElement(name, declaration, null);
    }
  }

  List<Directive> getClassBodyDirectives() {
    return classBodyDirectives;
  }

  String getClassQName() {
    return classQName;
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

  public void addImport(@Nonnull String classQName) {
    // do not import top-level classes
    if(classQName.contains(".")) {
      ImportDirective importDirective = mxmlParserHelper.parseImport(classQName);
      if (null != importDirective && isNotYetImported(importDirective.getIde())) {
        classBodyDirectives.add(importDirective);
      }
    }
  }

  public Map<String, VariableDeclaration> getVariables() {
    return classVariablesByName;
  }
}
