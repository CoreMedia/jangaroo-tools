package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
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

  private static final String NET_JANGAROO_EXT_EXML = "net.jangaroo.ext.Exml";
  private static final JooSymbol EXML_SYMBOL = new JooSymbol("Exml");
  private static final String APPLY = "apply";
  private final RootElementProcessor rootElementProcessor = new RootElementProcessor();

  private final IsNativeConstructor isNativeConstructor = new IsNativeConstructor(this);
  private final IsInitMethod isInitMethod = new IsInitMethod();

  private final Collection<String> importedSymbols = new HashSet<>();

  private final XmlHeader optXmlHeader;
  private final XmlElement rootNode;
  private final MxmlParserHelper mxmlParserHelper;

  private final List<Directive> constructorBodyDirectives = new LinkedList<>();

  private FunctionDeclaration initMethod;
  private FunctionDeclaration nativeConstructor;
  private Parameter constructorParam;

  private final Map<String, VariableDeclaration> classVariablesByName = new LinkedHashMap<>();

  public MxmlCompilationUnit(@Nonnull InputSource source, @Nullable XmlHeader optXmlHeader, @Nonnull XmlElement rootNode, @Nonnull MxmlParserHelper mxmlParserHelper) {
    // no secondary declarations: https://issues.apache.org/jira/browse/FLEX-21373
    super(createPackageDeclaration(source), MxmlAstUtils.SYM_LBRACE, new LinkedList<>(), createClassDeclaration(source, rootNode), MxmlAstUtils.SYM_RBRACE, Collections.emptyList());
    this.optXmlHeader = optXmlHeader;
    this.rootNode = rootNode;
    this.mxmlParserHelper = mxmlParserHelper;
  }

  private static String getQName(@Nonnull InputSource source) {
    return CompilerUtils.qNameFromRelativePath(source.getRelativePath());
  }

  private static PackageDeclaration createPackageDeclaration(InputSource source) {
    return new PackageDeclaration(new JooSymbol(sym.PACKAGE, "package"), new Ide(CompilerUtils.packageName(getQName(source))));
  }

  private static IdeDeclaration createClassDeclaration(InputSource source, XmlElement rootNode) {
    JooSymbol rootNodeSymbol = rootNode.getSymbol();
    return MxmlAstUtils.createClassDeclaration(getQName(source), rootNodeSymbol);
  }

  private ClassDeclaration populateClassDeclaration(JangarooParser parser) {
    ClassDeclaration classDeclaration = (ClassDeclaration) getPrimaryDeclaration();

    // extends
    classDeclaration.setExtends(mxmlParserHelper.parseExtends(rootNode, getQualifiedNameStr()));

    // implements
    JooSymbol implSymbol = rootElementProcessor.getImpl();
    if (implSymbol != null) {
      classDeclaration.setImplements(mxmlParserHelper.parseImplements(implSymbol));
    }

    List<Directive> classBodyDirectives = getClassBodyDirectives();
    for (JooSymbol jooSymbol : rootElementProcessor.getScripts()) {
      ClassBody classBody = mxmlParserHelper.parseClassBody(jooSymbol);
      List<Directive> parsedClassBodyDirectives = classBody.getDirectives();
      if (null != parsedClassBodyDirectives) {
        classBodyDirectives.addAll(parsedClassBodyDirectives);
      } else {
        parser.getLog().warning(jooSymbol, "element is empty");
      }
    }

    return classDeclaration;
  }

  @Override
  public void scope(Scope scope) {
    JangarooParser parser = scope.getCompiler();
    rootElementProcessor.process(parser.getMxmlComponentRegistry(), rootNode);

    // handle imports
    for (JooSymbol jooSymbol : rootElementProcessor.getImports()) {
      addImport(jooSymbol);
    }

    // init class declaration
    ClassDeclaration classDeclaration = populateClassDeclaration(parser);
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

    List<Directive> classBodyDirectives = classDeclaration.getBody().getDirectives();
    // find member variables
    for (Directive directive : classBodyDirectives) {
      if (directive instanceof VariableDeclaration) {
        VariableDeclaration variableDeclaration = (VariableDeclaration) directive;
        classVariablesByName.put(variableDeclaration.getName(), variableDeclaration);
      }
    }

    preProcessClassBodyDirectives();

    createFields();

    postProcessClassBodyDirectives();

    super.scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    if(null != initMethod) {
      List<Expr> args = constructorParam != null
              ? Collections.singletonList(new IdeExpr(constructorParam.getIde())) : Collections.emptyList();
      ApplyExpr methodInvocation = MxmlAstUtils.createMethodInvocation(initMethod, MxmlAstUtils.SYM_THIS, args);
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(methodInvocation));
    }

    JangarooParser parser = getPackageDeclaration().getIde().getScope().getCompiler();
    MxmlToModelParser mxmlToModelParser = new MxmlToModelParser(parser);
    MxmlToModelParser.MxmlRootModel mxmlModel = mxmlToModelParser.parse(rootNode);
    MxmlModelToAstTransformer mxmlModelToAstTransformer = new MxmlModelToAstTransformer(this, mxmlParserHelper);

    ObjectLiteral objectLiteral = mxmlModelToAstTransformer.rootModelToObjectLiteral(mxmlModel);
    // If the super constructor also has a 'config' param, use the force.
    Ide superClassIde = ((ClassDeclaration) getPrimaryDeclaration()).getOptExtends().getSuperClass();
    if (constructorParam != null && CompilationUnitUtils.constructorSupportsConfigOptionsParameter(superClassIde.getQualifiedNameStr(), parser)) {
      applyConfigOnto(mxmlModelToAstTransformer.getDefaults(mxmlModel));
      applyConfigOnto(MxmlAstUtils.createApplyExpr(new IdeExpr(new Ide(getPrimaryDeclaration().getName())), objectLiteral));
      constructorBodyDirectives.add(MxmlAstUtils.createSuperConstructorCall(constructorParam.getIde()));
    } else {
      applyOntoThis(mxmlModelToAstTransformer.getDefaults(mxmlModel));
      applyOntoThis(objectLiteral);
    }
    FunctionDeclaration newConstructor = nativeConstructor == null
            ? MxmlAstUtils.createConstructor(primaryDeclaration.getIde(), constructorBodyDirectives)
            : MxmlAstUtils.createConstructor(nativeConstructor, constructorBodyDirectives);
    ((ClassDeclaration) getPrimaryDeclaration()).replaceConstructor(newConstructor);
    super.analyze(parentNode);
  }

  private void applyConfigOnto(Expr objectLiteral) {
    IdeExpr configExpr = new IdeExpr(constructorParam.getIde());
    exmlApply(configExpr, objectLiteral, configExpr);
  }

  private void applyOntoThis(ObjectLiteral objectLiteral) {
    exmlApply(null, new IdeExpr(MxmlAstUtils.SYM_THIS), objectLiteral);
  }

  private void exmlApply(Expr assignTo, Expr targetObject, Expr sourceObject) {
    if (!isEmptyObjectLiteral(sourceObject) && !isEmptyObjectLiteral(targetObject)) {
      addImport(NET_JANGAROO_EXT_EXML);
      Expr expr = MxmlAstUtils.createApplyExpr(MxmlAstUtils.createDotExpr(EXML_SYMBOL.withWhitespace(" "), APPLY), targetObject, sourceObject);
      if (assignTo != null) {
        expr = new AssignmentOpExpr(assignTo, MxmlAstUtils.SYM_EQ.withWhitespace(" "), expr);
      }
      constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(expr));
    }
  }

  private static boolean isEmptyObjectLiteral(Expr sourceObject) {
    return sourceObject instanceof ObjectLiteral && ((ObjectLiteral)sourceObject).getFields() == null;
  }

  private void preProcessClassBodyDirectives() {
    List<Directive> classBodyDirectives = getClassBodyDirectives();
    for (Directive directive : classBodyDirectives) {
      if (isNativeConstructor.apply(directive)) {
        nativeConstructor = (FunctionDeclaration) directive;
        Parameters params = nativeConstructor.getParams();
        if (null != params) {
          constructorParam = params.getHead();
        }
      } else if (isInitMethod.apply(directive)) {
        initMethod = (FunctionDeclaration) directive;
      }
    }

    if (constructorParam != null) {
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
  }

  private void postProcessClassBodyDirectives() {
    for(Directive directive : getClassBodyDirectives()) {
      directive.setClassMember(true);
    }
  }

  private void createFields() {
    List<Directive> classBodyDirectives = getClassBodyDirectives();
    for (XmlElement objectElement : rootElementProcessor.getDeclarations()) {
      VariableDeclaration variableDeclaration = getVariables().get(objectElement.getId());
      if (variableDeclaration == null) {
        String classQName = objectElement.getClassQName();
        addImport(classQName);
        String asDoc = MxmlUtils.toASDoc(objectElement.getSymbol().getWhitespace());
        int i = asDoc.lastIndexOf('\n');
        String additionalDeclaration = String.format("%s[%s]%spublic var %s:%s;",
                asDoc,
                Jooc.BINDABLE_ANNOTATION_NAME,
                i < 0 ? "\n" : asDoc.substring(i),
                objectElement.getId(), classQName);
        classBodyDirectives.add(mxmlParserHelper.parseClassBody(new JooSymbol(additionalDeclaration)).getDirectives().get(0));
      }
    }
  }

  private List<Directive> getClassBodyDirectives() {
    return ((ClassDeclaration) getPrimaryDeclaration()).getBody().getDirectives();
  }

  @SuppressWarnings("unused")
  XmlTag getOptXmlHeader() {
    return optXmlHeader;
  }

  XmlElement getRootNode() {
    return rootNode;
  }

  private void addDirective(Directive directive) {
    getDirectives().add(directive);
    Scope scope = getPackageDeclaration().getIde().getScope();
    if (scope != null) {
      directive.scope(scope);
    }
  }

  private void addImport(Ide classIde) {
    if(isNotYetImported(classIde)) {
      ImportDirective directive = MxmlAstUtils.createImport(classIde);
      addDirective(directive);
    }
  }

  private boolean isNotYetImported(Ide classIde) {
    Ide qualifier = classIde.getQualifier();
    return qualifier != null && !importedSymbols.contains(qualifier.getQualifiedNameStr() + ".*") && importedSymbols.add(classIde.getQualifiedNameStr());
  }

  private void addImport(@Nonnull JooSymbol symbol) {
    String jooValue = (String) symbol.getJooValue();
    if(!importedSymbols.contains(jooValue)) {
      ImportDirective directive = mxmlParserHelper.parseImport(symbol);
      if (null != directive && isNotYetImported(directive.getIde())) {
        addDirective(directive);
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
          addDirective(importDirective);
        }
      }
    }
    return ide;
  }

  @Nonnull
  public Map<String, VariableDeclaration> getVariables() {
    return classVariablesByName;
  }
}
