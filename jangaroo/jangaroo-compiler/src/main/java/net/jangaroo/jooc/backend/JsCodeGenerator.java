package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.Debug;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JoocProperties;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AsExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
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
import net.jangaroo.jooc.ast.NamespacedDeclaration;
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
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.UseNamespaceDirective;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.ast.VectorLiteral;
import net.jangaroo.jooc.ast.WhileStatement;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import net.jangaroo.jooc.util.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A visitor of the AST that generates executable JavaScript code on
 * a {@link net.jangaroo.jooc.JsWriter}.
 */
public class JsCodeGenerator extends CodeGeneratorBase {
  private static final JooSymbol SYM_VAR = new JooSymbol(sym.VAR, "var"); // NOSONAR introducing a constant for "var" would obscure the generated output
  private static final JooSymbol SYM_EQ = new JooSymbol(sym.EQ, "=");
  private static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");
  private static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  private static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  private static final JooSymbol SYM_LBRACK = new JooSymbol(sym.LBRACK, "[");
  private static final JooSymbol SYM_RBRACK = new JooSymbol(sym.RBRACK, "]");
  private static final Set<String> PRIMITIVES = new HashSet<String>(4);
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
  }

  private boolean expressionMode = false;

  public JsCodeGenerator(JsWriter out) {
    super(out);
  }

  private void writeThis(Ide ide) throws IOException {
    out.writeToken(ide.isRewriteThis() ? "this$" : "this");
  }

  private void generateIdeCodeAsExpr(Ide ide) throws IOException {
    if (out.isWritingComment()) {
      out.writeSymbol(ide.getIde());
      return;
    }
    out.writeSymbolWhitespace(ide.getIde());
    if (ide.isSuper()) {
      writeThis(ide);
      return;
    }
    if (!ide.isThis()) {
      IdeDeclaration decl = ide.getDeclaration(false);
      if (decl != null) {
        if (decl.isClassMember()) {
          if (!decl.isPrivateStatic()) {
            if (decl.isStatic()) {
              out.writeToken(decl.getClassDeclaration().getQualifiedNameStr());
            } else {
              if (ide.isBound()) {
                writeBoundMethodAccess(ide, null, null, decl);
                return;
              }
              writeThis(ide);
            }
          }
          writeMemberAccess(decl, null, ide, false);
          return;
        }
        if (ide.getPackagePrefix().length() > 0) {
          out.writeToken(ide.getPackagePrefix());
        }
      }
    }
    // take care of reserved words called as functions (Rhino does not like):
    if (SyntacticKeywords.RESERVED_WORDS.contains(ide.getIde().getText())) {
      out.writeToken("$$" + ide.getIde().getText());
    } else {
      out.writeSymbol(ide.getIde(), false);
    }
  }


  public void writeBoundMethodAccess(Ide ide, Ide optIde, JooSymbol optSymDot, IdeDeclaration decl) throws IOException {
    out.writeToken("$$bound(");
    if (optIde != null) {
      generateIdeCodeAsExpr(optIde);
    } else {
      writeThis(ide);
    }
    if (optSymDot != null) {
      out.writeSymbolWhitespace(optSymDot);
    }
    out.writeToken(",");
    out.beginString();
    if (ide.usePrivateMemberName(decl)) {
      out.writeToken(ide.getName() + "$" + ide.getScope().getClassDeclaration().getInheritanceLevel());
    } else {
      out.writeToken(ide.getName());
    }
    out.endString();
    out.writeToken(")");
  }

  @Override
  public void visitDotExpr(DotExpr dotExpr) throws IOException {
    dotExpr.getArg().visit(this);
    writeMemberAccess(Ide.resolveMember(dotExpr.getArg().getType(), dotExpr.getIde()), dotExpr.getOp(), dotExpr.getIde(), true);
  }

  private void writeMemberAccess(IdeDeclaration memberDeclaration, JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace) throws IOException {
    if (memberDeclaration != null) {
      if (memberIde.usePrivateMemberName(memberDeclaration)) {
        writePrivateMemberAccess(optSymDot, memberIde, writeMemberWhitespace, memberDeclaration.isStatic());
        return;
      }
    }
    if (optSymDot == null && memberDeclaration != null && !memberDeclaration.isConstructor()) {
      optSymDot = new JooSymbol(".");
    }
    boolean quote = false;
    if (optSymDot != null) {
      if (memberIde.getIde().getText().startsWith("@")) {
        quote = true;
        out.writeSymbolWhitespace(optSymDot);
        out.writeToken("['");
      } else {
        out.writeSymbol(optSymDot);
      }
    }
    out.writeSymbol(memberIde.getIde(), writeMemberWhitespace);
    if (quote) {
      out.writeToken("']");
    }
  }

  private void writePrivateMemberAccess(final JooSymbol optSymDot, Ide memberIde, boolean writeMemberWhitespace, boolean isStatic) throws IOException {
    if (writeMemberWhitespace) {
      out.writeSymbolWhitespace(memberIde.getIde());
    }
    if (isStatic) {
      out.writeToken("$$private");
      if (optSymDot != null) {
        out.writeSymbol(optSymDot);
      } else {
        out.writeToken(".");
      }
      out.writeSymbol(memberIde.getIde(), false);
    } else {
      if (optSymDot != null) {
        out.writeSymbol(optSymDot);
      } else {
        out.writeToken(".");
      }
      // awkward, but we have to be careful if we add characters to tokens:
      out.writeToken(memberIde.getName() + "$" + memberIde.getScope().getClassDeclaration().getInheritanceLevel());
    }
  }

  @Override
  public void visitTypeRelation(TypeRelation typeRelation) throws IOException {
    out.beginCommentWriteSymbol(typeRelation.getSymRelation());
    typeRelation.getType().getIde().visit(this);
    out.endComment();
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    visitIfNotNull(annotationParameter.getOptName(), "$value");
    writeSymbolReplacement(annotationParameter.getOptSymEq(), ":");
    visitIfNotNull(annotationParameter.getValue(), "null");
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    out.writeSymbol(anExtends.getSymExtends());
    writeQName(anExtends.getSuperClass());
  }

  private void writeQName(Ide classIde) throws IOException {
    out.writeSymbolWhitespace(classIde.getSymbol());
    String classQName = classIde.getDeclaration().getQualifiedNameStr();
    out.writeToken(classQName);
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
    out.write(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".prepare(");
    compilationUnit.getPackageDeclaration().visit(this);
    out.beginComment();
    out.writeSymbol(compilationUnit.getLBrace());
    out.endComment();
    compilationUnit.getPrimaryDeclaration().visit(this);
    out.write(",[");
    boolean first = true;
    for (String qname : compilationUnit.getDependencies()) {
      if (first) {
        first = false;
      } else {
        out.write(",");
      }
      out.write('"' + qname + '"');
    }
    out.write("]");
    out.write(", \"" + JoocProperties.getRuntimeVersion() + "\"");
    out.write(", \"" + JoocProperties.getVersion() + "\"");
    out.writeSymbolWhitespace(compilationUnit.getRBrace());
    out.write(");");
  }

  @Override
  public void visitIde(Ide ide) throws IOException {
    if (expressionMode) {
      generateIdeCodeAsExpr(ide);
      return;
    }
    out.writeSymbolWhitespace(ide.getIde());
    // take care of reserved words called as functions (Rhino does not like):
    if (!out.isWritingComment() && SyntacticKeywords.RESERVED_WORDS.contains(ide.getIde().getText())) {
      out.writeToken("$$" + ide.getIde().getText());
    } else {
      out.writeSymbol(ide.getIde(), false);
    }
  }

  private void generateQualifiedIdeCodeAsExpr(QualifiedIde qualifiedIde) throws IOException {
    boolean commentOutQualifierCode = false;
    IdeDeclaration memberDeclaration = null;
    IdeDeclaration qualifierDeclaration = qualifiedIde.getQualifier().getDeclaration(false);
    if (qualifierDeclaration != null && qualifierDeclaration.isConstructor()) {
      qualifierDeclaration = qualifierDeclaration.getClassDeclaration();
    }
    if (qualifierDeclaration != null && qualifierDeclaration.equals(qualifiedIde.getScope().getClassDeclaration())) {
      memberDeclaration = ((ClassDeclaration) qualifierDeclaration).getStaticMemberDeclaration(qualifiedIde.getName());
      commentOutQualifierCode = memberDeclaration != null && memberDeclaration.isPrivateStatic();
    }
    if (memberDeclaration == null) {
      final IdeDeclaration type = qualifiedIde.getQualifier().resolveDeclaration();
      memberDeclaration = Ide.resolveMember(type, qualifiedIde);
    }
    if (qualifiedIde.isBound()) {
      writeBoundMethodAccess(qualifiedIde, qualifiedIde.getQualifier(), qualifiedIde.getSymDot(), memberDeclaration);
      return;
    }
    if (commentOutQualifierCode) {
      // we will generate another qualifier in writeMemberAccess
      out.beginComment();
    }
    qualifiedIde.getQualifier().visit(this);
    if (commentOutQualifierCode) {
      out.endComment();
    }
    writeMemberAccess(memberDeclaration, qualifiedIde.getSymDot(), qualifiedIde, true);
  }

  @Override
  public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
    if (expressionMode) {
      generateQualifiedIdeCodeAsExpr(qualifiedIde);
      return;
    }
    qualifiedIde.getQualifier().visit(this);
    out.writeSymbol(qualifiedIde.getSymDot());
    visitIde(qualifiedIde);
  }

  private void generateIdeWithTypeParamCodeAsExpr(IdeWithTypeParam ideWithTypeParam) throws IOException {
    generateIdeCodeAsExpr(ideWithTypeParam);
    out.beginComment();
    out.writeSymbol(ideWithTypeParam.getSymDotLt());
    out.writeSymbol(ideWithTypeParam.getType().getIde().getIde());
    out.writeSymbol(ideWithTypeParam.getSymGt());
    out.endComment();
  }

  @Override
  public void visitIdeWithTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    if (expressionMode) {
      generateIdeWithTypeParamCodeAsExpr(ideWithTypeParam);
      return;
    }
    visitIde(ideWithTypeParam);
    writeTypeParamAsComment(ideWithTypeParam);
  }

  private void writeTypeParamAsComment(IdeWithTypeParam ideWithTypeParam) throws IOException {
    out.beginComment();
    writeTypeParam(ideWithTypeParam);
    out.endComment();
  }

  protected void writeTypeParam(IdeWithTypeParam ideWithTypeParam) throws IOException {
    out.writeSymbol(ideWithTypeParam.getSymDotLt());
    ideWithTypeParam.getType().visit(this);
    out.writeSymbol(ideWithTypeParam.getSymGt());
  }

  @Override
  public void visitNamespacedIde(NamespacedIde namespacedIde) throws IOException {
    // so far, namespaces are only comments:
    out.beginComment();
    out.writeSymbol(namespacedIde.getNamespace());
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
    writeQName(superTypes.getHead());
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
    expressionMode = true;
    try {
      ideExpr.getIde().visit(this);
    } finally {
      expressionMode = false;
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
    if (assignmentOpExpr.getOp().sym == sym.ANDANDEQ || assignmentOpExpr.getOp().sym == sym.OROREQ) {
      assignmentOpExpr.getArg1().visit(this);
      out.writeSymbolWhitespace(assignmentOpExpr.getOp());
      out.writeToken("=");
      // TODO: refactor for a simpler way to switch off white-space temporarily:
      JoocConfiguration options = (JoocConfiguration) out.getOptions();
      DebugMode mode = options.getDebugMode();
      options.setDebugMode(null);
      assignmentOpExpr.getArg1().visit(this);
      options.setDebugMode(mode);
      out.writeToken(assignmentOpExpr.getOp().sym == sym.ANDANDEQ ? "&&" : "||");
      out.writeToken("(");
      assignmentOpExpr.getArg2().visit(this);
      out.writeToken(")");
    } else {
      visitBinaryOpExpr(assignmentOpExpr);
    }
  }

  @Override
  public void visitInfixOpExpr(InfixOpExpr infixOpExpr) throws IOException {
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
      out.writeToken(functionExpr.getIde().getName());
    } else if (out.getKeepSource()) {
      out.writeToken(getFunctionNameAsIde(functionExpr));
    }
    generateFunTailCode(functionExpr);
  }

  public String getFunctionNameAsIde(FunctionExpr functionExpr) {
    IdeDeclaration classDeclaration = functionExpr.getClassDeclaration();
    String classNameAsIde = "";
    if (classDeclaration != null) {
      classNameAsIde = out.getQualifiedNameAsIde(classDeclaration);
    }
    JooSymbol sym = functionExpr.getSymbol();
    return classNameAsIde + "$" + sym.getLine() + "_" + sym.getColumn();
  }

  public void generateFunTailCode(FunctionExpr functionExpr) throws IOException {
    Parameters params = functionExpr.getParams();
    if (params != null && functionExpr.hasBody()) {
      // inject into body for generating initializers later:
      functionExpr.getBody().addBlockStartCodeGenerator(getParameterInitializerCodeGenerator(params));
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

  private static final MessageFormat IF_ARGUMENT_LENGTH_LTE_$N = new MessageFormat("if(arguments.length<={0})");
  private static final MessageFormat SWITCH_$INDEX = new MessageFormat("switch({0,choice,0#arguments.length|0<Math.max(arguments.length,{0})})");
  private static final MessageFormat CASE_$N = new MessageFormat("case {0}:");

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

  private static final MessageFormat VAR_$NAME_EQUALS_ARGUMENTS_SLICE_$INDEX = new MessageFormat("var {0}=Array.prototype.slice.call(arguments{1,choice,0#|0<,{1}});");

  public void generateRestParamCode(Parameter param, int paramIndex) throws IOException {
    String paramName = param.getName();
    if (paramName != null && !(paramName.equals("arguments") && paramIndex == 0)) {
      out.write(VAR_$NAME_EQUALS_ARGUMENTS_SLICE_$INDEX.format(paramName, paramIndex));
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
    generateFunJsCode(applyExpr);
    if (applyExpr.getArgs() != null) {
      boolean isAssert = applyExpr.getFun() instanceof IdeExpr && SyntacticKeywords.ASSERT.equals(applyExpr.getFun().getSymbol().getText());
      if (isAssert) {
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
        applyExpr.getArgs().visit(this);
      }
    }
  }

  private void generateFunJsCode(ApplyExpr applyExpr) throws IOException {
    // leave out constructor function if called as type cast function!
    // these old-style type casts are soo ugly....
    if (applyExpr.isTypeCast()) {
      out.beginComment();
      applyExpr.getFun().visit(this);
      out.endComment();
    } else {
      applyExpr.getFun().visit(this);
    }
  }

  @Override
  public void visitNewExpr(NewExpr newExpr) throws IOException {
    out.writeSymbol(newExpr.getSymNew());
    newExpr.getApplyConstructor().visit(this);
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    out.writeSymbolWhitespace(classBody.getLBrace());
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
    out.writeSymbolWhitespace(classBody.getRBrace());
  }

  private boolean beginStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (!inStaticInitializerBlock) {
      out.writeToken("function(){");
    }
    return true;
  }

  private boolean endStaticInitializer(JsWriter out, boolean inStaticInitializerBlock) throws IOException {
    if (inStaticInitializerBlock) {
      out.writeToken("},");
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
      out.writeToken("if(is");
      out.writeSymbol(aCatch.getLParen());
      out.writeSymbolWhitespace(localErrorVar);
      out.writeSymbolToken(errorVar);
      out.writeSymbolWhitespace(typeRelation.getSymRelation());
      out.writeToken(",");
      Ide typeIde = typeRelation.getType().getIde();
      out.writeSymbolWhitespace(typeIde.getIde());
      out.writeToken(typeIde.getDeclaration().getQualifiedNameStr());
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
  public void visitForInStatement(ForInStatement forInStatement) throws IOException {
    out.writeSymbol(forInStatement.getSymKeyword());
    if (forInStatement.getSymEach() != null) {
      out.beginComment();
      out.writeSymbol(forInStatement.getSymEach());
      out.endComment();
    }
    out.writeSymbol(forInStatement.getLParen());
    if (forInStatement.getSymEach() != null) {
      new VariableDeclaration(SYM_VAR, forInStatement.getAuxIde(), null, null).visit(this);
    } else {
      if (forInStatement.getDecl() != null) {
        forInStatement.getDecl().visit(this);
      } else {
        forInStatement.getLValue().visit(this);
      }
    }
    out.writeSymbol(forInStatement.getSymIn());
    forInStatement.getExpr().visit(this);
    out.writeSymbol(forInStatement.getRParen());
    if (forInStatement.getSymEach() != null) {
      // synthesize assigning the correct index to the variable given in the original for each statement:
      ArrayIndexExpr indexExpr = new ArrayIndexExpr(forInStatement.getExpr(), SYM_LBRACK,
              new CommaSeparatedList<IdeExpr>(new IdeExpr(forInStatement.getAuxIde())),
              SYM_RBRACK);
      Statement assignment = // NOSONAR no, this is not a JDBC statement that must be closed ...
              new SemicolonTerminatedStatement(forInStatement.getDecl() != null
                      ? new VariableDeclaration(SYM_VAR, forInStatement.getDecl().getIde(), forInStatement.getDecl().getOptTypeRelation(), new Initializer(SYM_EQ, indexExpr))
                      : new AssignmentOpExpr(forInStatement.getLValue(), SYM_EQ, indexExpr),
                      SYM_SEMICOLON);
      // inject synthesized statement into loop body:
      // todo: maybe we should correct the AST earlier, not during code generation?
      if (forInStatement.getBody() instanceof BlockStatement) {
        ((BlockStatement) forInStatement.getBody()).getDirectives().add(0, assignment);
      } else {
        forInStatement.setBody(new BlockStatement(SYM_LBRACE, Arrays.<Directive>asList(assignment, forInStatement.getBody()), SYM_RBRACE));
      }
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
      out.writeSymbol(variableDeclaration.getOptSymConstOrVar());
    } else {
      generateVariableDeclarationStartCode(variableDeclaration);
    }
    variableDeclaration.getIde().visit(this);
    visitIfNotNull(variableDeclaration.getOptTypeRelation());
    generateVariableDeclarationInitializerCode(variableDeclaration);
    visitIfNotNull(variableDeclaration.getOptNextVariableDeclaration());
    generateVariableDeclarationEndCode(variableDeclaration);
  }

  protected void generateVariableDeclarationStartCode(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.isClassMember()) {
      generateFieldStartCode(variableDeclaration);
    } else {
      generateVarStartCode(variableDeclaration);
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

  protected void generateFieldStartCode(VariableDeclaration variableDeclaration) throws IOException {
    out.beginString();
    writeModifiers(out, variableDeclaration);
    writeOptSymbol(variableDeclaration.getOptSymConstOrVar());
    out.endString();
    out.write(",{");
  }

  protected void generateVariableDeclarationInitializerCode(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.isClassMember()) {
      generateFieldInitializerCode(variableDeclaration);
    } else {
      visitIfNotNull(variableDeclaration.getOptInitializer());
    }
  }

  protected void generateFieldInitializerCode(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.getOptInitializer() != null) {
      out.writeSymbolWhitespace(variableDeclaration.getOptInitializer().getSymEq());
      out.write(':');
      boolean mustEvaluateAtRuntime = !variableDeclaration.getOptInitializer().getValue().isRuntimeConstant();
      if (mustEvaluateAtRuntime) {
        out.writeToken("function(){return(");
      }
      variableDeclaration.getOptInitializer().getValue().visit(this);
      if (mustEvaluateAtRuntime) {
        out.writeToken(");}");
      }
    } else {
      TypeRelation typeRelation = variableDeclaration.getOptTypeRelation();
      String emptyValue = VariableDeclaration.getDefaultValue(typeRelation);
      out.write(":" + emptyValue);
    }
  }

  protected void generateVariableDeclarationEndCode(VariableDeclaration variableDeclaration) throws IOException {
    if (variableDeclaration.isClassMember()) {
      generateFieldEndCode(variableDeclaration);
    } else {
      writeOptSymbol(variableDeclaration.getOptSymSemicolon());
    }
  }

  protected void generateFieldEndCode(VariableDeclaration variableDeclaration) throws IOException {
    if (!variableDeclaration.hasPreviousVariableDeclaration()) {
      out.write('}');
      Debug.assertTrue(variableDeclaration.getOptSymSemicolon() != null, "optSymSemicolon != null");
      out.writeSymbolWhitespace(variableDeclaration.getOptSymSemicolon());
      out.writeToken(",");
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
    assert functionDeclaration.isClassMember() || (!functionDeclaration.isNative() && !functionDeclaration.isAbstract());
    if (functionDeclaration.isThisAliased()) {
      functionDeclaration.getBody().addBlockStartCodeGenerator(ALIAS_THIS_CODE_GENERATOR);
    }
    if (functionDeclaration.isConstructor() && !functionDeclaration.containsSuperConstructorCall() && functionDeclaration.hasBody()) {
      functionDeclaration.getBody().addBlockStartCodeGenerator(new SuperCallCodeGenerator(functionDeclaration.getClassDeclaration()));
    }
    if (!functionDeclaration.isClassMember()) {
      functionDeclaration.getFun().visit(this);
    } else {
      if (functionDeclaration.isAbstract()) {
        out.beginComment();
        writeModifiers(out, functionDeclaration);
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
        functionDeclaration.getIde().visit(this);
      } else {
        out.beginString();
        writeModifiers(out, functionDeclaration);
        out.writeSymbol(functionDeclaration.getFun().getFunSymbol());
        if (functionDeclaration.isGetterOrSetter()) {
          out.writeSymbol(functionDeclaration.getSymGetOrSet());
        }
        functionDeclaration.getIde().visit(this);
        out.endString();
        if (functionDeclaration.isNative()) {
          out.beginComment();
        } else {
          out.writeToken(",");
          out.writeToken("function");
          if (out.getKeepSource()) {
            String functionName = functionDeclaration.getIde().getName();
            if (functionDeclaration.getSymGetOrSet() != null) {
              out.writeToken(functionName + "$" + functionDeclaration.getSymGetOrSet().getText());
            } else {
              out.writeToken(functionName);
            }
          }
        }
      }
      generateFunTailCode(functionDeclaration.getFun());
      if (functionDeclaration.isClassMember()) {
        if (functionDeclaration.isAbstract() || functionDeclaration.isNative()) {
          out.endComment();
        }
        out.write(',');
      }
    }
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    visitAll(classDeclaration.getDirectives());
    out.beginString();
    writeModifiers(out, classDeclaration);
    out.writeSymbol(classDeclaration.getSymClass());
    classDeclaration.getIde().visit(this);
    visitIfNotNull(classDeclaration.getOptExtends());
    visitIfNotNull(classDeclaration.getOptImplements());
    out.endString();
    out.write(",");
    out.write(classDeclaration.getInheritanceLevel() + ",");
    out.write("function($$private){");
    writeAliases(classDeclaration);
    out.write("return[");
    generateClassInits(classDeclaration);
    classDeclaration.getBody().visit(this);
    if (classDeclaration.getConstructor() == null && !classDeclaration.getFieldsWithInitializer().isEmpty()) {
      // generate default constructor that calls field initializers:
      out.write("\"public function " + classDeclaration.getName() + "\",function " + classDeclaration.getName() + "$(){");
      new SuperCallCodeGenerator(classDeclaration).generate(out, true);
      out.write("},");
    }

    for (IdeDeclaration secondaryDeclaration : classDeclaration.getSecondaryDeclarations()) {
      secondaryDeclaration.visit(this);
      out.writeToken(",");
    }

    out.write("undefined];},");
    generateStaticMethodList(classDeclaration);
  }

  private void generateFieldInitCode(ClassDeclaration classDeclaration, boolean startWithSemicolon, boolean preventFirefox9Bug) throws IOException {
    Iterator<VariableDeclaration> iterator = classDeclaration.getFieldsWithInitializer().iterator();
    if (iterator.hasNext()) {
      if (startWithSemicolon) {
        out.write(";");
      }
      if (preventFirefox9Bug) {
        out.writeToken("if(0===0){"); // THANK YOU, Firefox 9: https://bugzilla.mozilla.org/show_bug.cgi?id=706808#c27
      }
      do {
        VariableDeclaration field = iterator.next();
        generateInitCode(field, true);
      } while (iterator.hasNext());
      if (preventFirefox9Bug) {
        out.write("}");
      }
    }
  }

  public void generateInitCode(VariableDeclaration field, boolean endWithSemicolon) throws IOException {
    String accessCode = "this." + field.getName() + (field.isPrivate() ? "$" + field.getClassDeclaration().getInheritanceLevel() : "");
    out.write(accessCode + "=" + accessCode + "()");
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
        generateSuperConstructorCallCode(classDeclaration, null);
        out.writeToken(";");
        first = false;
      }
      generateFieldInitCode(classDeclaration, false, first);
    }
  }

  private void generateStaticMethodList(ClassDeclaration classDeclaration) throws IOException {
    out.write("[");
    boolean isFirst = true;
    for (TypedIdeDeclaration memberDeclaration : classDeclaration.getStaticMembers().values()) {
      if (memberDeclaration.isMethod() && !memberDeclaration.isPrivate() && !memberDeclaration.isProtected() && memberDeclaration.isStatic() && !memberDeclaration.isNative()) {
        if (isFirst) {
          isFirst = false;
        } else {
          out.write(",");
        }
        out.write('"');
        out.write(memberDeclaration.getName());
        out.write('"');
      }
    }
    out.write("]");
  }

  private void writeAliases(ClassDeclaration classDeclaration) throws IOException {
    boolean first = true;
    for (Map.Entry<String,String> entry : classDeclaration.getAuxVarDeclarations().entrySet()) {
      if (first) {
        out.writeToken("var");
        first = false;
      } else {
        out.writeToken(",");
      }
      out.writeToken(entry.getKey());
      out.writeToken("=");
      out.writeToken(entry.getValue());
    }
    if (!first) {
      out.writeToken(";");
    }
  }

  private void generateClassInits(ClassDeclaration classDeclaration) throws IOException {
    boolean first = true;
    Set<String> classInit = classDeclaration.getClassInit();
    for (String qualifiedNameStr : classInit) {
      if (!PRIMITIVES.contains(qualifiedNameStr)) {
        if (first) {
          first = false;
          out.write("function(){" + Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".init(");
        } else {
          out.write(",");
        }
        out.write(qualifiedNameStr);
      }
    }
    if (!first) {
      out.write(");},");
    }
  }

  @Override
  public void visitNamespacedDeclaration(NamespacedDeclaration namespacedDeclaration) throws IOException {
    out.beginString();
    writeModifiers(out, namespacedDeclaration);
    out.writeSymbol(namespacedDeclaration.getSymNamespace());
    namespacedDeclaration.getIde().visit(this);
    out.endString();
    out.writeSymbolWhitespace(namespacedDeclaration.getOptInitializer().getSymEq());
    out.writeToken(",");
    namespacedDeclaration.getOptInitializer().getValue().visit(this);
    writeSymbolReplacement(namespacedDeclaration.getOptSymSemicolon(), ",[]");
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    out.beginString();
    out.writeSymbol(packageDeclaration.getSymPackage());
    visitIfNotNull(packageDeclaration.getIde());
    out.endString();
    out.write(",");
  }

  @Override
  public void visitSuperConstructorCallStatement(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    if (superConstructorCallStatement.getClassDeclaration().getInheritanceLevel() > 1) {
      generateSuperConstructorCallCode(superConstructorCallStatement);
      generateFieldInitCode(superConstructorCallStatement.getClassDeclaration(), true, false);
    } else { // suppress for classes extending Object
      // Object super call does nothing anyway:
      out.beginComment();
      out.writeSymbol(superConstructorCallStatement.getSymbol());
      visitIfNotNull(superConstructorCallStatement.getArgs());
      out.endComment();
      generateFieldInitCode(superConstructorCallStatement.getClassDeclaration(), false, true);
    }
    out.writeSymbol(superConstructorCallStatement.getSymSemicolon());
  }

  private void generateSuperConstructorCallCode(SuperConstructorCallStatement superConstructorCallStatement) throws IOException {
    out.writeSymbolWhitespace(superConstructorCallStatement.getSymbol());
    generateSuperConstructorCallCode(superConstructorCallStatement.getClassDeclaration(), superConstructorCallStatement.getArgs());
  }

  private void generateSuperConstructorCallCode(ClassDeclaration classDeclaration, ParenthesizedExpr<CommaSeparatedList<Expr>> args) throws IOException {
    String superClassQName = classDeclaration.getSuperTypeDeclaration().getQualifiedNameStr();
    if ("Error".equals(superClassQName)) {
      // built-in Error constructor called as function unfortunately always creates a new Error object, so we have to use emulation provided by Jangaroo Runtime:
      out.write("joo.Error");
    } else {
      Ide superClassIde = classDeclaration.getSuperType().getIde();
      out.writeSymbolWhitespace(superClassIde.getSymbol());
      IdeDeclaration superClassDeclaration = superClassIde.getDeclaration();
      String packageName = superClassDeclaration.getPackageDeclaration().getQualifiedNameStr();
      String qName = superClassDeclaration.getName();
      if (packageName.length() > 0) {
        String packageAuxVar = classDeclaration.getAuxVarForPackage(packageName);
        qName = CompilerUtils.qName(packageAuxVar, qName);
      }
      out.write(qName);
    }
    out.writeToken(".call");
    if (args == null) {
      out.writeToken("(this)");
    } else {
      out.writeSymbol(args.getLParen());
      out.writeToken("this");
      CommaSeparatedList<Expr> arguments = args.getExpr();
      if (arguments != null) {
        if (arguments.getHead() != null) {
          out.writeToken(",");
        }
        arguments.visit(this);
      }
      out.writeSymbol(args.getRParen());
    }
  }

  @Override
  public void visitAnnotation(Annotation annotation) throws IOException {
    out.writeSymbolWhitespace(annotation.getLeftBracket());
    out.writeToken("{");
    annotation.getIde().visit(this);
    out.writeToken(":");
    writeSymbolReplacement(annotation.getOptLeftParen(), "{");
    visitIfNotNull(annotation.getOptAnnotationParameters());
    writeSymbolReplacement(annotation.getOptRightParen(), "}");
    out.writeSymbolWhitespace(annotation.getRightBracket());
    out.writeToken("},");
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
}
