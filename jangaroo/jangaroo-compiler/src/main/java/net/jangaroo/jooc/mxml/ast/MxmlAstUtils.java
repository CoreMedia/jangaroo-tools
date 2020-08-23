package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationsAndModifiers;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class MxmlAstUtils {

  public static final String INDENT_4 = "\n    ";
  static final JooSymbol[] SYM_EMPTY_MODIFIERS = new JooSymbol[]{};

  static final JooSymbol SYM_CLASS = new JooSymbol(sym.CLASS, "class");
  static final JooSymbol SYM_COLON = new JooSymbol(sym.COLON, ":");
  static final JooSymbol SYM_COMMA = new JooSymbol(sym.COMMA, ",");
  static final JooSymbol SYM_DOT = new JooSymbol(sym.DOT, ".");
  static final JooSymbol SYM_EQ = new JooSymbol(sym.EQ, "=");
  static final JooSymbol SYM_FUNCTION = new JooSymbol(sym.FUNCTION, "function");
  static final JooSymbol SYM_IMPORT = new JooSymbol(sym.IMPORT, "import");
  static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  static final JooSymbol SYM_LBRACK = new JooSymbol(sym.LBRACK, "[");
  static final JooSymbol SYM_LPAREN = new JooSymbol(sym.LPAREN, "(");
  static final JooSymbol SYM_NULL = new JooSymbol(sym.NULL_LITERAL, "null");
  static final JooSymbol SYM_PUBLIC = new JooSymbol(sym.PUBLIC, "public");
  static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  static final JooSymbol SYM_RBRACK = new JooSymbol(sym.RBRACK, "]");
  static final JooSymbol SYM_RPAREN = new JooSymbol(sym.RPAREN, ")");
  static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");
  static final JooSymbol SYM_SUPER = new JooSymbol(sym.SUPER, "super");
  static final JooSymbol SYM_THIS = new JooSymbol(sym.THIS, Ide.THIS);
  static final JooSymbol SYM_VAR = new JooSymbol(sym.VAR, "var");
  static final JooSymbol SYM_NEW = new JooSymbol(sym.NEW, "new");
  private static final List<Annotation> EMPTY_ANNOTATIONS = Collections.<Annotation>emptyList();

  private MxmlAstUtils() {
    // hide constructor for utility class
  }

  @Nonnull
  static FunctionDeclaration createConstructor(@Nonnull FunctionDeclaration directive, @Nonnull List<Directive> constructorBodyDirectives) {
    BlockStatement constructorBody = new BlockStatement(SYM_LBRACE, constructorBodyDirectives, SYM_RBRACE.withWhitespace("\n  "));
    String whitespace = "";
    JooSymbol firstSymbol = Iterables.getFirst(Arrays.asList(directive.getSymModifiers()), null);
    if(null != firstSymbol) {
      whitespace = firstSymbol.getWhitespace();
    }
    return new FunctionDeclaration(new AnnotationsAndModifiers(null, Collections.singletonList(SYM_PUBLIC.withWhitespace(whitespace))),
            SYM_FUNCTION,
            directive.getSymGetOrSet(),
            directive.getIde(),
            directive.getFun().getLParen(),
            directive.getParams(),
            directive.getFun().getRParen(),
            null,
            constructorBody,
            null
            );
  }

  @Nonnull
  static FunctionDeclaration createConstructor(@Nonnull Ide ide, @Nonnull List<Directive> constructorBodyDirectives) {
    BlockStatement constructorBody = new BlockStatement(SYM_LBRACE, constructorBodyDirectives, SYM_RBRACE.withWhitespace("\n"));
    TypeRelation typeRelation = new TypeRelation(SYM_COLON, new Type(ide));
    Parameters params = new Parameters(new Parameter(null, new Ide(MxmlUtils.CONFIG), typeRelation, new Initializer(SYM_EQ, new LiteralExpr(SYM_NULL))));
    return new FunctionDeclaration(new AnnotationsAndModifiers(null, Collections.singletonList(SYM_PUBLIC)),
            SYM_FUNCTION, null, ide, SYM_LPAREN, params, SYM_RPAREN, null, constructorBody, null);
  }

  @Nonnull
  static ImportDirective createImport(@Nonnull Ide superClass) {
    return new ImportDirective(SYM_IMPORT.withWhitespace("\n"), superClass, SYM_SEMICOLON);
  }

  @Nonnull
  static ClassDeclaration createClassDeclaration(@Nonnull String classQName, @Nonnull JooSymbol rootNodeSymbol, @Nonnull Extends ext, @Nullable Implements impl, @Nonnull List<Directive> classBodyDirectives, @Nonnull InputSource source) {
    ClassBody classBody = new ClassBody(SYM_LBRACE, classBodyDirectives, SYM_RBRACE);
    String whitespace = MxmlUtils.toASDoc(rootNodeSymbol.getWhitespace());

    return new ClassDeclaration(new AnnotationsAndModifiers(new LinkedList<>(), Collections.singletonList(SYM_PUBLIC.withWhitespace(whitespace))),
            SYM_CLASS, new Ide(CompilerUtils.className(classQName)),
            ext, impl, classBody);
  }

  @Nonnull
  static VariableDeclaration createVariableDeclaration(@Nonnull Ide name, @Nonnull Ide type) {
    Expr emptyObject = new ObjectLiteral(SYM_LBRACE, null, null, SYM_RBRACE);
    Expr initializerExpr = createCastExpr(type, emptyObject);
    return createVariableDeclaration(name, type, initializerExpr);
  }

  static ApplyExpr createCastExpr(@Nonnull Ide type, Expr expr) {
    IdeExpr fun = new IdeExpr(type);
    return createApplyExpr(fun, expr);
  }

  static ApplyExpr createApplyExpr(Expr fun, Expr ...args) {
    return new ApplyExpr(fun, SYM_LPAREN, createCommaSeparatedList(args), SYM_RPAREN);
  }

  static DotExpr createDotExpr(Ide object, String property) {
    return createDotExpr(new IdeExpr(object), new Ide(property));
  }

  static DotExpr createDotExpr(Expr object, Ide property) {
    return new DotExpr(object, SYM_DOT, property);
  }

  static ArrayIndexExpr createArrayIndexExpr(Expr expr, String index) {
    return new ArrayIndexExpr(expr, SYM_LBRACK, createStringLiteral(index), SYM_RBRACK);
  }

  static LiteralExpr createStringLiteral(String value) {
    return createStringLiteral(value, "");
  }

  static LiteralExpr createStringLiteral(String value, String whiteSpace) {
    return new LiteralExpr(new JooSymbol(sym.STRING_LITERAL, null, -1, -1, whiteSpace, CompilerUtils.quote(value), value));
  }

  @Nonnull
  static VariableDeclaration createVariableDeclaration(@Nonnull Ide name, @Nonnull Ide type, @Nullable Expr initializerExpr) {
    Initializer initializer = initializerExpr == null ? null : new Initializer(SYM_EQ, initializerExpr);
    return new VariableDeclaration(new AnnotationsAndModifiers(null,null),
            SYM_VAR.withWhitespace(INDENT_4), name, new TypeRelation(SYM_COLON, new Type(type)), initializer,
            null, SYM_SEMICOLON);
  }

  @Nonnull
  static SemicolonTerminatedStatement createSemicolonTerminatedStatement(@Nonnull AstNode astNode) {
    return new SemicolonTerminatedStatement(astNode, SYM_SEMICOLON);
  }

  @Nonnull
  static SuperConstructorCallStatement createSuperConstructorCall(Ide superConfigVar) {
    return createSuperConstructorCall(new IdeExpr(superConfigVar));
  }

  static SuperConstructorCallStatement createSuperConstructorCall(Expr ...argExprs) {
    CommaSeparatedList<Expr> args = createCommaSeparatedList(argExprs);
    return new SuperConstructorCallStatement(SYM_SUPER.withWhitespace(INDENT_4), SYM_LPAREN, args, SYM_RPAREN, SYM_SEMICOLON);
  }

  @Nonnull
  static Directive createPropertyAssignment(@Nonnull Ide variable, @Nonnull Expr rightHandSide, @Nonnull String propertyName, boolean untypedAccess) {
    Expr leftHandSide;
    Ide varWithWhitespace = new Ide(variable.getIde().withWhitespace(INDENT_4));
    if (untypedAccess) {
      leftHandSide = new ArrayIndexExpr(new IdeExpr(varWithWhitespace), SYM_LBRACK, new LiteralExpr(new JooSymbol('"' + propertyName + '"')), SYM_RBRACK);
    } else {
      Ide propertyNameIde = new Ide(propertyName);
      if(Ide.THIS.equals(varWithWhitespace.getName())) {
        leftHandSide = new IdeExpr(propertyNameIde);
      } else {
        leftHandSide = new DotExpr(new IdeExpr(varWithWhitespace), SYM_DOT, propertyNameIde);
      }
    }

    AssignmentOpExpr assignmentOpExpr = createAssignmentOpExpr(leftHandSide, rightHandSide);
    return createSemicolonTerminatedStatement(assignmentOpExpr);
  }

  @Nonnull
  static AssignmentOpExpr createAssignmentOpExpr(Expr leftHandSide, @Nonnull Expr rightHandSide) {
    return new AssignmentOpExpr(leftHandSide, SYM_EQ.withWhitespace(" "), rightHandSide);
  }

  @Nonnull
  static ObjectField createObjectField( @Nonnull String propertyName, @Nonnull Expr rightHandSide) {
    return new ObjectField(createObjectFieldLabel(propertyName), SYM_COLON, rightHandSide);
  }

  private static AstNode createObjectFieldLabel(@Nonnull String propertyName) {
    return Ide.isValidIdentifier(propertyName)
            ? new Ide(new JooSymbol(propertyName).withWhitespace(INDENT_4))
            : createStringLiteral(propertyName, INDENT_4);
  }

  @Nonnull
  static ObjectLiteral createObjectLiteral( @Nonnull List<ObjectField> objectFields) {
    return new ObjectLiteral(
            SYM_LBRACE,
            createCommaSeparatedList(objectFields),
            null,
            SYM_RBRACE.withWhitespace(INDENT_4));
  }

  @Nonnull
  static ArrayLiteral createArrayLiteral(@Nonnull List<Expr> arrayElements) {
    return new ArrayLiteral(
            SYM_LBRACK,
            createCommaSeparatedList(arrayElements),
            SYM_RBRACK);
  }

  @SafeVarargs
  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(T ...elements) {
    return createCommaSeparatedList(Arrays.asList(elements));
  }

  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(List<T> elements) {
    CommaSeparatedList<T> result = null;
    JooSymbol comma = null;
    for (int i = elements.size() - 1; i >= 0; i--) {
      T t = elements.get(i);
      result = new CommaSeparatedList<>(t, comma, result);
      comma = SYM_COMMA;
    }
    return result;
  }

  static NewExpr createNewExpr(Ide typeIde, Expr ...args) {
    return new NewExpr(SYM_NEW.withWhitespace(" "), createApplyExpr(new IdeExpr(typeIde), args));
  }

  static LiteralExpr createNullLiteral() {
    return new LiteralExpr(SYM_NULL);
  }

  static IdeExpr createThisExpr() {
    return new IdeExpr(new Ide(SYM_THIS));
  }
}
