package net.jangaroo.jooc.backend;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AsExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.BinaryOpExpr;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.BreakStatement;
import net.jangaroo.jooc.ast.CaseStatement;
import net.jangaroo.jooc.ast.Catch;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.ConditionalExpr;
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
import net.jangaroo.jooc.ast.IsExpr;
import net.jangaroo.jooc.ast.LabeledStatement;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NamespaceDeclaration;
import net.jangaroo.jooc.ast.NamespacedIde;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.OpExpr;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.ParenthesizedExpr;
import net.jangaroo.jooc.ast.PostfixOpExpr;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.PrefixOpExpr;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.ReturnStatement;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.Statement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.SwitchStatement;
import net.jangaroo.jooc.ast.ThrowStatement;
import net.jangaroo.jooc.ast.TryStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.UseNamespaceDirective;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.ast.WhileStatement;
import net.jangaroo.jooc.model.MethodType;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit;
import net.jangaroo.jooc.types.ExpressionType;
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CodeGeneratorBase implements AstVisitor {
  protected static final String PROPERTY_CLASS_INSTANCE = "INSTANCE";
  protected final CompilationUnitResolver compilationUnitModelResolver;
  protected JsWriter out;
  private ListMultimap<BlockStatement, CodeGenerator> blockStartCodeGenerators =
          ArrayListMultimap.create();


  public CodeGeneratorBase(JsWriter out, CompilationUnitResolver compilationUnitModelResolver) {
    this.out = out;
    this.compilationUnitModelResolver = compilationUnitModelResolver;
  }

  protected void addBlockStartCodeGenerator(BlockStatement block, CodeGenerator codeGenerator) {
    blockStartCodeGenerators.put(block, codeGenerator);
  }

  @Override
  public void visitBlockStatement(BlockStatement blockStatement) throws IOException {
    out.writeSymbol(blockStatement.getLBrace());
    boolean first = true;
    List<CodeGenerator> codeGenerators = blockStartCodeGenerators.get(blockStatement);
    if (codeGenerators != null) {
      for (CodeGenerator codeGenerator : codeGenerators) {
        codeGenerator.generate(out, first);
        first = false;
      }
    }
    visitBlockStatementDirectives(blockStatement);
    out.writeSymbol(blockStatement.getRBrace());
  }

  protected void visitBlockStatementDirectives(BlockStatement blockStatement) throws IOException {
    visitAll(blockStatement.getDirectives());
  }

  protected void writeModifiers(JsWriter out, IdeDeclaration declaration) throws IOException {
    for (JooSymbol modifier : declaration.getSymModifiers()) {
      out.writeSymbol(modifier);
    }
  }

  @Override
  public final void visitLiteralExpr(LiteralExpr literalExpr) throws IOException {
    out.writeSymbol(literalExpr.getValue());
  }

  @Override
  public final void visitPostfixOpExpr(PostfixOpExpr postfixOpExpr) throws IOException {
    postfixOpExpr.getArg().visit(this);
    out.writeSymbol(postfixOpExpr.getOp());
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    visitPostfixOpExpr(dotExpr);
    dotExpr.getIde().visit(this);
  }

  @Override
  public final void visitPrefixOpExpr(PrefixOpExpr prefixOpExpr) throws IOException {
    out.writeSymbol(prefixOpExpr.getOp());
    prefixOpExpr.getArg().visit(this);
  }

  @Override
  public final void visitBinaryOpExpr(BinaryOpExpr binaryOpExpr) throws IOException {
    binaryOpExpr.getArg1().visit(this);
    out.writeSymbol(binaryOpExpr.getOp());
    binaryOpExpr.getArg2().visit(this);
  }

  @Override
  public final void visitIsExpr(IsExpr isExpr) throws IOException {
    visitInfixOpExpr(isExpr);
  }

  @Override
  public final void visitConditionalExpr(ConditionalExpr conditionalExpr) throws IOException {
    conditionalExpr.getCond().visit(this);
    out.writeSymbol(conditionalExpr.getSymQuestion());
    conditionalExpr.getIfTrue().visit(this);
    out.writeSymbol(conditionalExpr.getSymColon());
    conditionalExpr.getIfFalse().visit(this);
  }

  @Override
  public final <T extends AstNode> void visitCommaSeparatedList(CommaSeparatedList<T> commaSeparatedList) throws IOException {
    visitIfNotNull(commaSeparatedList.getHead());
    if (commaSeparatedList.getSymComma() != null) {
      out.writeSymbol(commaSeparatedList.getSymComma());
      visitIfNotNull(commaSeparatedList.getTail());
    }
  }

  protected TypedIdeDeclaration findMemberWithBindableAnnotation(Ide qIde, MethodType methodType, TypeDeclaration typeDeclaration) {
    String memberName = qIde.getIde().getText();
    TypeDeclaration currentTypeDeclaration = typeDeclaration;
    List<TypedIdeDeclaration> incorrectOverrides = new ArrayList<>();
    do {
      TypedIdeDeclaration member = lookupPropertyDeclaration(currentTypeDeclaration, memberName, methodType);
      if (member == null || !member.isPublic() || member.getClassDeclaration() == null) {
        break;
      }
      if (isBindableWithoutAccessor(member)) {
        for (TypedIdeDeclaration incorrectOverride : incorrectOverrides) {
          qIde.getScope().getCompiler().getLog().warning(incorrectOverride.getSymbol(),
                  String.format("Ext config '%s' with annotation [Bindable(style=\"methods\")] must be redeclared/overwritten with the same annotation.", memberName));
        }
        return member;
      }
      // Maybe it is an Ext [Bindable(style="methods")], incorrectly redeclared as simple property or [Bindable]?
      // So even after finding a member, continue searching where we left off:
      currentTypeDeclaration = member.getClassDeclaration().getSuperTypeDeclaration();
      incorrectOverrides.add(member);
    } while (currentTypeDeclaration != null);
    return null;
  }

  boolean isBindableWithoutAccessor(TypedIdeDeclaration member) {
    Annotation bindableAnnotation = member.getAnnotation(Jooc.BINDABLE_ANNOTATION_NAME);
    // Since it seems we cannot "patch" Ext to support accessors for configs, treat *all* Ext [Bindables]
    // and those explicitly annotated as [Bindable(style="methods")] as configs without accessors:
    return bindableAnnotation != null &&
            (bindableAnnotation.getPropertiesByName().get("style") != null ||
                    "ext".equals(member.getClassDeclaration().getQualifiedName()[0]));
  }

  private TypedIdeDeclaration lookupPropertyDeclaration(TypeDeclaration classDeclaration, String memberName,
                                                        MethodType methodType) {
    if (classDeclaration == null) {
      return null;
    }
    TypedIdeDeclaration member = classDeclaration.getMemberDeclaration(memberName);
    if (member instanceof PropertyDeclaration) {
      member = ((PropertyDeclaration) member).getAccessor(methodType == MethodType.SET);
    }
    if (member instanceof VariableDeclaration) {
      if (((VariableDeclaration) member).isConst() && methodType == MethodType.SET) {
        // cannot write a const:
        member = null;
      }
    } else if (member instanceof FunctionDeclaration) {
      FunctionDeclaration method = (FunctionDeclaration) member;
      MethodType foundMethodType = method.isGetter() ? MethodType.GET : method.isSetter() ? MethodType.SET : null;
      if (methodType != foundMethodType) {
        member = null;
      }
    }
    if (member == null) {
      member = lookupPropertyDeclaration(classDeclaration.getSuperTypeDeclaration(), memberName, methodType);
    }
    return member;
  }

  protected static Map<String, Object> getBindablePropertiesByName(TypedIdeDeclaration member) {
    return member.getAnnotations(Jooc.BINDABLE_ANNOTATION_NAME).get(0).getPropertiesByName();
  }

  protected static String getBindablePropertyName(MethodType methodType, TypedIdeDeclaration member) {
    Object bindableAnnotationValue = CodeGeneratorBase.getBindablePropertiesByName(member).get(null);
    if (bindableAnnotationValue == null) {
      return methodType + MxmlUtils.capitalize(member.getName());
    } else {
      return (String) bindableAnnotationValue;
    }
  }

  @Override
  public void visitParameters(Parameters parameters) throws IOException {
    visitCommaSeparatedList(parameters);
  }

  @Override
  public final void visitPredefinedTypeDeclaration(PredefinedTypeDeclaration predefinedTypeDeclaration) {
    throw new IllegalStateException("there should be no code generation for predefined types");
  }

  protected void writeOptSymbol(JooSymbol symbol) throws IOException {
    if (symbol != null) {
      out.writeSymbol(symbol);
    }
  }

  protected void writeOptSymbolWhitespace(JooSymbol symbol) throws IOException {
    if (symbol != null) {
      out.writeSymbolWhitespace(symbol);
    }
  }

  protected void writeOptSymbol(JooSymbol optSymbol, String defaultToken) throws IOException {
    if (optSymbol != null) {
      out.writeSymbol(optSymbol);
    } else {
      out.writeToken(defaultToken);
    }
  }

  protected void writeSymbolReplacement(JooSymbol symbol, String replacementToken) throws IOException {
    if (symbol != null) {
      out.writeSymbolWhitespace(symbol);
    }
    out.writeTokenForSymbol(replacementToken, symbol);
  }

  protected void visitIfNotNull(AstNode args) throws IOException {
    if (args != null) {
      args.visit(this);
    }
  }

  protected void visitIfNotNull(AstNode args, String replacementToken) throws IOException {
    if (args != null) {
      args.visit(this);
    } else {
      out.writeToken(replacementToken);
    }
  }

  protected void visitAll(Iterable<? extends AstNode> nodes) throws IOException {
    for (AstNode node : nodes) {
      node.visit(this);
    }
  }

  protected List<AssignmentOpExpr> getPropertiesClassAssignments(FunctionDeclaration constructorDeclaration,
                                                                 boolean includeLiterals,
                                                                 boolean includeReferences) {
    if (constructorDeclaration == null) {
      return Collections.emptyList();
    }
    List<AssignmentOpExpr> result = new ArrayList<>();
    for (Directive constructorDirective : constructorDeclaration.getBody().getDirectives()) {
      if (constructorDirective instanceof SemicolonTerminatedStatement) {
        AstNode optStatement = ((SemicolonTerminatedStatement) constructorDirective).getOptStatement();
        if (optStatement instanceof AssignmentOpExpr) {
          AssignmentOpExpr assignmentOpExpr = (AssignmentOpExpr) optStatement;
          Map.Entry<Expr, AstNode> objectAndProperty = getObjectAndProperty(assignmentOpExpr);
          Expr arg = objectAndProperty.getKey();
          if (arg instanceof IdeExpr && ((IdeExpr) arg).getIde().isThis()) {
            boolean isLiteral = assignmentOpExpr.getArg2() instanceof LiteralExpr;
            if (isLiteral == includeLiterals ||
                    !isLiteral == includeReferences) {
              result.add(assignmentOpExpr);
            }
          }
        }
      }
    }
    return result;
  }

  protected void renderPropertiesClassValues(List<AssignmentOpExpr> propertyAssignments,
                                             boolean useComments,
                                             boolean useDeclarationComments,
                                             boolean startWithComma) throws IOException {
    for (AssignmentOpExpr propertyAssignment : propertyAssignments) {
      if (startWithComma) {
        out.writeToken(",");
      } else {
        startWithComma = true;
      }
      AstNode index = getObjectAndProperty(propertyAssignment).getValue();
      if (useComments) {
        if (useDeclarationComments && index instanceof Ide && ((Ide) index).getDeclaration(false) != null) {
          IdeDeclaration declaration = ((Ide) index).getDeclaration();
          out.writeSymbolWhitespace(declaration.getSymbol());
        } else {
          out.writeSymbolWhitespace(propertyAssignment.getSymbol());
        }
      } else {
        out.write("\n  ");
      }
      index.visit(this);
      out.writeToken(":");
      propertyAssignment.getArg2().visit(this);
    }
  }

  protected Map.Entry<Expr, AstNode> getObjectAndProperty(AssignmentOpExpr assignmentOpExpr) {
    Expr lhs = assignmentOpExpr.getArg1();
    if (lhs instanceof IdeExpr) {
      lhs = ((IdeExpr) lhs).getNormalizedExpr();
    }
    if (lhs instanceof DotExpr) {
      DotExpr dotExpr = (DotExpr) lhs;
      return new AbstractMap.SimpleEntry<>(dotExpr.getArg(), dotExpr.getIde());
    } else if (lhs instanceof ArrayIndexExpr) {
      ArrayIndexExpr arrayIndexExpr = (ArrayIndexExpr) lhs;
      return new AbstractMap.SimpleEntry<>(arrayIndexExpr.getArray(), arrayIndexExpr.getIndexExpr().getExpr());
    }
    throw new CompilerError("Properties class constructor code does not match standard format.");
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    out.writeSymbol(typeRelation.getSymbol());
    typeRelation.getType().visit(this);
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitIfNotNull(annotationParameter.getOptName());
    writeOptSymbol(annotationParameter.getOptSymEq());
    visitIfNotNull(annotationParameter.getValue());
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    anExtends.getSuperClass().visit(this);
  }

  protected static boolean isPropertiesClass(IdeDeclaration primaryDeclaration) {
    return primaryDeclaration instanceof ClassDeclaration
            && primaryDeclaration.getName().endsWith(CompilerUtils.PROPERTIES_CLASS_SUFFIX);
  }

  protected static boolean isPropertiesSubclass(IdeDeclaration primaryDeclaration) {
    String classname = primaryDeclaration.getName();
    return CodeGeneratorBase.isPropertiesClass(primaryDeclaration) &&
            classname.substring(0, classname.length() - CompilerUtils.PROPERTIES_CLASS_SUFFIX.length()).contains("_") ;
  }

  @Override
  public void visitInitializer(Initializer initializer) throws IOException {
    out.writeSymbol(initializer.getSymEq());
    initializer.getValue().visit(this);
  }

  @Override
  public void visitObjectField(ObjectField objectField) throws IOException {
    DotExpr exmlAppendOrPrepend = getExmlAppendOrPrepend(objectField.getValue());
    if (exmlAppendOrPrepend != null) {
      handleExmlAppendPrepend(objectField, exmlAppendOrPrepend);
    } else {
      objectField.getLabel().visit(this);
      out.writeSymbol(objectField.getSymColon());
      objectField.getValue().visit(this);
    }
  }

  protected abstract void handleExmlAppendPrepend(ObjectField objectField, DotExpr exmlAppendOrPrepend) throws IOException;

  private DotExpr getExmlAppendOrPrepend(Expr value) {
    if (value instanceof ApplyExpr) {
      ApplyExpr applyExpr = (ApplyExpr) value;
      if (applyExpr.getFun() instanceof DotExpr) {
        DotExpr dotExpr = (DotExpr) applyExpr.getFun();
        String methodName = dotExpr.getIde().getName();
        if ((methodName.equals("append") || methodName.equals("prepend"))
                && MxmlCompilationUnit.NET_JANGAROO_EXT_EXML.equals(getArgFQN(dotExpr))) {
          return dotExpr;
        }
      }
    }
    return null;
  }

  static String getArgFQN(DotExpr dotExpr) {
    if (dotExpr.getArg() instanceof IdeExpr) {
      IdeExpr ideExpr = (IdeExpr) dotExpr.getArg();
      IdeDeclaration declaration = ideExpr.getIde().getDeclaration(false);
      if (declaration != null) {
        return declaration.getQualifiedNameStr();
      }
    }
    return null;
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
    compilationUnit.getPackageDeclaration().visit(this);
    out.writeSymbol(compilationUnit.getLBrace());
    visitAll(compilationUnit.getDirectives());
    IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
    primaryDeclaration.visit(this);
    out.writeSymbol(compilationUnit.getRBrace());
    if (primaryDeclaration instanceof ClassDeclaration) {
      visitAll(((ClassDeclaration) primaryDeclaration).getSecondaryDeclarations());
    }
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    out.writeSymbol(ide.getIde());
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    qualifiedIde.getQualifier().visit(this);
    out.writeSymbol(qualifiedIde.getSymDot());
    visitIde(qualifiedIde);
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    out.writeSymbol(ideWithTypeParam.getIde());
    out.writeSymbol(ideWithTypeParam.getSymDotLt());
    ideWithTypeParam.getType().visit(this);
    out.writeSymbol(ideWithTypeParam.getSymGt());
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    namespacedIde.getNamespace().visit(this);
    out.writeSymbol(namespacedIde.getSymNamespaceSep());
    out.writeSymbol(namespacedIde.getIde());
  }

  @Override
  public void visitImplements(Implements anImplements) throws IOException {
    out.writeSymbol(anImplements.getSymImplements());
    anImplements.getSuperTypes().visit(this);
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
    ideExpr.getIde().visit(this);
  }

  @Override
  public <T extends Expr> void visitParenthesizedExpr(ParenthesizedExpr<T> parenthesizedExpr) throws IOException {
    if (parenthesizedExpr.getParentNode() instanceof OpExpr
            && parenthesizedExpr.getExpr() instanceof InfixOpExpr) {
      // suppress redundant parenthesis like !(AS3.is(foo, Foo)), because Babel does so, too:
      generateInfixOpExpr((InfixOpExpr) parenthesizedExpr.getExpr(),
              parenthesizedExpr.getLParen(), parenthesizedExpr.getRParen());
    } else {
      out.writeSymbol(parenthesizedExpr.getLParen());
      visitIfNotNull(parenthesizedExpr.getExpr());
      out.writeSymbol(parenthesizedExpr.getRParen());
    }
  }

  @Override
  public void visitArrayLiteral(ArrayLiteral arrayLiteral) throws IOException {
    visitParenthesizedExpr(arrayLiteral);
  }

  @Override
  public void visitAssignmentOpExpr(AssignmentOpExpr assignmentOpExpr) throws IOException {
    visitBinaryOpExpr(assignmentOpExpr);
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
    generateInfixOpExpr(infixOpExpr, new JooSymbol("("), new JooSymbol(")"));
  }

  private void generateInfixOpExpr(InfixOpExpr infixOpExpr, JooSymbol lParenSym, JooSymbol rParenSym) throws IOException {
    out.writeSymbolWhitespace(lParenSym);
    out.writeToken(builtInIdentifierCode(infixOpExpr.getOp().getText()));
    out.writeSymbol(lParenSym);
    infixOpExpr.getArg1().visit(this);
    out.write(',');
    out.writeSymbolWhitespace(infixOpExpr.getOp());
    infixOpExpr.getArg2().visit(this);
    out.writeSymbol(rParenSym);
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

  protected static FunctionDeclaration findFunctionDeclaration(AstNode node) {
    AstNode parent = node;
    do {
      parent = parent.getParentNode();
      if (parent instanceof FunctionDeclaration) {
        return (FunctionDeclaration) parent;
      }
    } while (parent != null);
    return null;
  }

  @Override
  public void visitFunctionExpr(FunctionExpr functionExpr) throws IOException {
    out.writeSymbol(functionExpr.getSymFunction());
    visitIfNotNull(functionExpr.getIde());
    generateFunctionExprSignature(functionExpr);
    visitIfNotNull(functionExpr.getBody());
  }

  @Override
  public void visitVectorLiteral(VectorLiteral vectorLiteral) throws IOException {
    out.writeSymbol(vectorLiteral.getSymNew());
    out.writeSymbol(vectorLiteral.getSymLt());
    vectorLiteral.getVectorType().visit(this);
    out.writeSymbol(vectorLiteral.getSymGt());
    vectorLiteral.getArrayLiteral().visit(this);
  }

  @Override
  public void visitApplyExpr(ApplyExpr applyExpr) throws IOException {
    applyExpr.getFun().visit(this);
    visitApplyExprArguments(applyExpr);
  }

  public void visitApplyExprArguments(ApplyExpr applyExpr) throws IOException {
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    if (args != null) {
      FunctionDeclaration functionDeclaration = applyExpr.resolveFunction();
      if (functionDeclaration != null) {
        writeArgumentsWithOptCoercesAndDefaultValues(applyExpr, functionDeclaration);
      } else {
        args.visit(this);
      }
    }
  }

  private void writeArgumentsWithOptCoercesAndDefaultValues(ApplyExpr applyExpr,
                                                            FunctionDeclaration methodDeclaration) throws IOException {
    ParenthesizedExpr<CommaSeparatedList<Expr>> args = applyExpr.getArgs();
    out.writeSymbol(args.getLParen());
    Collection<Annotation> parameterAnnotations = methodDeclaration.getAnnotations(Jooc.PARAMETER_ANNOTATION_NAME);
    CommaSeparatedList<Expr> arguments = args.getExpr();
    Parameters params = methodDeclaration.getParams();
    boolean first = true;
    Map<String, String> propertiesClassByParamName = new HashMap<>();
    while (params != null || arguments != null) {
      Parameter parameter = params == null ? null : params.getHead();
      Annotation parameterAnnotation = parameter == null || parameterAnnotations == null ? null
              : parameterAnnotations.stream()
              .filter(someParameterAnnotation -> parameter.getName().equals(someParameterAnnotation.getPropertiesByName().get(null)))
              .findAny().orElse(null);
      if (arguments != null) {
        Object coerceToObj = parameterAnnotation == null ? null
                : parameterAnnotation.getPropertiesByName().get(Jooc.PARAMETER_ANNOTATION_COERCE_TO_PROPERTY);
        boolean coerced = false;
        Expr argument = arguments.getHead();
        if (coerceToObj instanceof String) {
          String coerceTo = (String) coerceToObj;
          if (Jooc.COERCE_TO_VALUE_PROPERTIES_CLASS.equals(coerceTo)) {
            ClassDeclaration propertiesClass = applyExpr.getPropertiesClass(argument);
            String propertiesClassAccessCode = null;
            if (propertiesClass != null) {
              coerced = true;
              propertiesClassAccessCode = compilationUnitAccessCode(propertiesClass);
              out.write(propertiesClassAccessCode);
            }
            propertiesClassByParamName.put(parameter.getName(), propertiesClassAccessCode);
          } else if (coerceTo.startsWith(Jooc.COERCE_TO_VALUE_KEYOF_PREFIX)) {
            if (!(argument instanceof LiteralExpr)) {
              // a computed key needs a type assertion to be a accepted as a key of the properties class:
              String referencedParam = coerceTo.substring(Jooc.COERCE_TO_VALUE_KEYOF_PREFIX.length());
              if (!propertiesClassByParamName.containsKey(referencedParam)) {
                throw JangarooParser.error(argument.getSymbol(),
                        String.format("Referenced properties class parameter name '%s' not found.", referencedParam));
              }
              String referencedPropertiesClass = propertiesClassByParamName.get(referencedParam);
              if (referencedPropertiesClass != null) {
                coerced = true;
                generateTypeAssertion(argument, "keyof " + referencedPropertiesClass);
              }
            }
          } else {
            ExpressionType type = argument.getType();
            if (type == null || !type.getAS3Type().toString().equals(coerceTo)) {
              coerced = true;
              out.write(coerceTo + "(");
              argument.visit(this);
              out.write(")");
            }
          }
        }
        if (!coerced) {
          argument.visit(this);
        }
        writeOptSymbol(arguments.getSymComma());
        arguments = arguments.getTail();
      } else {
        if (parameterAnnotation != null) {
          // too few arguments? check whether next parameter is optional in AS3, but required in TS:
          Initializer parameterInitializer = parameter.getOptInitializer();
          if (parameterInitializer != null && parameterAnnotation.getPropertiesByName().containsKey(Jooc.PARAMETER_ANNOTATION_REQUIRED_PROPERTY)) {
            // parameter is optional in ActionScript, but required in TypeScript: add default value
            if (!first) {
              out.write(",");
            }
            parameterInitializer.getValue().visit(this);
          }
        }
      }
      if (params != null) {
        params = params.getTail();
      }
      first = false;
    }
    out.writeSymbol(args.getRParen());
  }

  void generateTypeAssertion(Expr argument, String type) throws IOException {
    // default: suppress type assertion, only generate argument:
    argument.visit(this);
  }

  @Override
  public void visitNewExpr(NewExpr newExpr) throws IOException {
    out.writeSymbol(newExpr.getSymNew());
    newExpr.getApplyConstructor().visit(this);
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    out.writeSymbol(classBody.getLBrace());
    visitClassBodyDirectives(classBody.getDirectives());
    out.writeSymbol(classBody.getRBrace());
  }

  public void visitClassBodyDirectives(List<Directive> classBodyDirectives) throws IOException {
    List<Directive> staticInitializerDirectives = null;
    for (Directive directive : classBodyDirectives) {
      final boolean isStaticInitializer = directive instanceof EmptyStatement
          // if EmptyStatement, stay in current mode:
          ? staticInitializerDirectives != null
          // else, determine mode depending on whether directive is statement, but not declaration:
          : directive instanceof Statement && !(directive instanceof Declaration);
      if (isStaticInitializer) {
        if (staticInitializerDirectives == null) {
          // first static initializer statement, start with empty list:
          staticInitializerDirectives = new ArrayList<>();
        }
        // collect static initializer statement, will be visited when block is complete:
        staticInitializerDirectives.add(directive);
      } else {
        if (staticInitializerDirectives != null) {
          // back in non-static-initializer mode, generate collected statements:
          generateStaticInitializer(staticInitializerDirectives);
          staticInitializerDirectives = null;
        }
        directive.visit(this);
      }
    }
    if (staticInitializerDirectives != null) {
      // generate remaining collected statements:
      generateStaticInitializer(staticInitializerDirectives);
    }
  }

  abstract void generateStaticInitializer(List<Directive> directives) throws IOException;

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
  }

  protected abstract String builtInIdentifierCode(String builtInIdentifier);

  @Override
  public void visitCatch(Catch aCatch) throws IOException {
    List<Catch> catches = aCatch.getParentTryStatement().getCatches();
    Catch firstCatch = catches.get(0);
    boolean isFirst = aCatch.equals(firstCatch);
    boolean isLast = aCatch.equals(catches.get(catches.size() - 1));
    TypeRelation typeRelation = aCatch.getParam().getOptTypeRelation();
    boolean hasCondition = aCatch.hasCondition();
    if (!hasCondition && !isLast) {
      throw JangarooParser.error(aCatch.getRParen(), "Only last catch clause may be untyped.");
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
        out.beginComment();
        typeRelation.visit(this);
        out.endComment();
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
      out.writeToken("if(" + builtInIdentifierCode("is"));
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
      addBlockStartCodeGenerator(aCatch.getBlock(), new VarCodeGenerator(localErrorVar, errorVar));
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

  abstract String compilationUnitAccessCode(IdeDeclaration declaration);

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
  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
    out.writeSymbol(forInStatement.getSymKeyword());
    writeOptSymbol(forInStatement.getSymEach());
    out.writeSymbol(forInStatement.getLParen());
    visitIfNotNull(forInStatement.getDecl());
    visitIfNotNull(forInStatement.getLValue());
    out.writeSymbol(forInStatement.getSymIn());
    forInStatement.getExpr().visit(this);
    out.writeSymbol(forInStatement.getRParen());
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
  public void visitBreakStatement(BreakStatement breakStatement) throws IOException {
    out.writeSymbol(breakStatement.getSymKeyword());
    visitIfNotNull(breakStatement.getOptStatement());
    visitIfNotNull(breakStatement.getOptLabel());
    writeOptSymbol(breakStatement.getOptSymSemicolon());
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
  public void visitThrowStatement(ThrowStatement throwStatement) throws IOException {
    out.writeSymbol(throwStatement.getSymKeyword());
    visitIfNotNull(throwStatement.getOptStatement());
    writeOptSymbol(throwStatement.getOptSymSemicolon());
  }

  @Override
  public void visitReturnStatement(ReturnStatement returnStatement) throws IOException {
    out.writeSymbol(returnStatement.getSymKeyword());
    if (returnStatement.getOptStatement() != null) {
      returnStatement.getOptStatement().visit(this);
    } else {
      // check if we need to complement "return this":
      FunctionDeclaration functionDeclaration = findFunctionDeclaration(returnStatement);
      if (functionDeclaration != null && Ide.THIS.equals(functionDeclaration.getFun().getReturnTypeFromAnnotation())) {
        out.writeToken(Ide.THIS);
      }
    }
    writeOptSymbol(returnStatement.getOptSymSemicolon());
  }

  @Override
  public void visitParameter(Parameter parameter) throws IOException {
    writeOptSymbol(parameter.getOptSymRest());
    parameter.getIde().visit(this);
    visitParameterTypeRelation(parameter);
    visitIfNotNull(parameter.getOptInitializer());
  }

  void visitParameterTypeRelation(Parameter parameter) throws IOException {
    TypeRelation typeRelation = parameter.getOptTypeRelation();
    // only visit non-synthesized rest parameter's type relation:
    if (typeRelation != null && !typeRelation.getSymRelation().isVirtual()) {
      typeRelation.visit(this);
    }
  }

  void visitDeclarationAnnotationsAndModifiers(IdeDeclaration declaration) throws IOException {
    visitAll(declaration.getAnnotations());
    writeModifiers(out, declaration);
  }

  @Override
  public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(variableDeclaration);
    writeOptSymbol(variableDeclaration.getOptSymConstOrVar());
    visitVariableDeclarationBase(variableDeclaration);
    visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
    writeOptSymbol(variableDeclaration.getOptSymSemicolon());
  }

  void visitVariableDeclarationBase(VariableDeclaration variableDeclaration) throws IOException {
    variableDeclaration.getIde().visit(this);
    visitIfNotNull(variableDeclaration.getOptTypeRelation());
    visitIfNotNull(variableDeclaration.getOptInitializer());
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(functionDeclaration);
    out.writeSymbol(functionDeclaration.getSymbol());
    writeOptSymbol(functionDeclaration.getSymGetOrSet());
    functionDeclaration.getIde().visit(this);
    generateFunctionExprSignature(functionDeclaration.getFun());
    visitIfNotNull(functionDeclaration.getFun().getBody());
    writeOptSymbol(functionDeclaration.getOptSymSemicolon());
  }

  void generateFunctionExprSignature(FunctionExpr functionExpr) throws IOException {
    out.writeSymbol(functionExpr.getLParen());
    visitIfNotNull(functionExpr.getParams());
    out.writeSymbol(functionExpr.getRParen());
    generateFunctionExprReturnTypeRelation(functionExpr);
  }

  void generateFunctionExprReturnTypeRelation(FunctionExpr functionExpr) throws IOException {
    visitIfNotNull(functionExpr.getOptTypeRelation());
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    classDeclaration.getBody().visit(this);
  }

  @Override
  public void visitNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) throws IOException {
    visitDeclarationAnnotationsAndModifiers(namespaceDeclaration);
    out.writeSymbol(namespaceDeclaration.getSymNamespace());
    visitIfNotNull(namespaceDeclaration.getOptInitializer());
    writeOptSymbol(namespaceDeclaration.getOptSymSemicolon());
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    out.writeSymbol(packageDeclaration.getSymPackage());
    visitIfNotNull(packageDeclaration.getIde());
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    out.writeSymbol(superConstructorCallStatement.getSymbol());
    superConstructorCallStatement.getArgs().visit(this);
    writeOptSymbol(superConstructorCallStatement.getSymSemicolon());

  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    out.writeSymbol(annotation.getLeftBracket());
    annotation.getIde().visit(this);
    writeOptSymbol(annotation.getOptLeftParen());
    visitIfNotNull(annotation.getOptAnnotationParameters());
    writeOptSymbol(annotation.getOptRightParen());
    out.writeSymbol(annotation.getRightBracket());
  }

  @Override
  public void visitUseNamespaceDirective(UseNamespaceDirective useNamespaceDirective) throws IOException {
    out.writeSymbol(useNamespaceDirective.getUseKeyword());
    out.writeSymbol(useNamespaceDirective.getNamespaceKeyword());
    useNamespaceDirective.getNamespace().visit(this);
    out.writeSymbol(useNamespaceDirective.getSymSemicolon());
  }

  @Override
  public void visitImportDirective(ImportDirective importDirective) throws IOException {
    if (importDirective.isExplicit()) {
      out.writeSymbol(importDirective.getImportKeyword());
      importDirective.getIde().visit(this);
      out.writeSymbol(importDirective.getSymSemicolon());
    }
  }

}
