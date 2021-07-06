package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JooSymbol;
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
import net.jangaroo.jooc.ast.ObjectFieldOrSpread;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.Spread;
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

  static final String INDENT_4 = "\n    ";

  static JooSymbol sym_class() { return new JooSymbol(sym.CLASS, "class"); }
  static JooSymbol sym_colon() { return new JooSymbol(sym.COLON, ":"); }
  static JooSymbol sym_comma() { return new JooSymbol(sym.COMMA, ","); }
  static JooSymbol sym_dot() { return new JooSymbol(sym.DOT, "."); }
  static JooSymbol sym_eq() { return new JooSymbol(sym.EQ, "="); }
  static JooSymbol sym_function() { return new JooSymbol(sym.FUNCTION, "function"); }
  static JooSymbol sym_import() { return new JooSymbol(sym.IMPORT, "import"); }
  static JooSymbol sym_lbrace() { return new JooSymbol(sym.LBRACE, "{"); }
  static JooSymbol sym_lbrack() { return new JooSymbol(sym.LBRACK, "["); }
  static JooSymbol sym_lparen() { return new JooSymbol(sym.LPAREN, "("); }
  static JooSymbol sym_null() { return new JooSymbol(sym.NULL_LITERAL, "null"); }
  static JooSymbol sym_public() { return new JooSymbol(sym.PUBLIC, "public"); }
  static JooSymbol sym_rbrace() { return new JooSymbol(sym.RBRACE, "}"); }
  static JooSymbol sym_rbrack() { return new JooSymbol(sym.RBRACK, "]"); }
  static JooSymbol sym_rparen() { return new JooSymbol(sym.RPAREN, ")"); }
  static JooSymbol sym_semicolon() { return new JooSymbol(sym.SEMICOLON, ";"); }
  static JooSymbol sym_super() { return new JooSymbol(sym.SUPER, "super"); }
  static JooSymbol sym_this() { return new JooSymbol(sym.THIS, Ide.THIS); }
  static JooSymbol sym_var() { return new JooSymbol(sym.VAR, "var"); }
  static JooSymbol sym_new() { return new JooSymbol(sym.NEW, "new"); }

  private MxmlAstUtils() {
    // hide constructor for utility class
  }

  @Nonnull
  static FunctionDeclaration createConstructor(@Nonnull FunctionDeclaration directive, @Nonnull List<Directive> constructorBodyDirectives) {
    BlockStatement constructorBody = new BlockStatement(sym_lbrace(), constructorBodyDirectives, sym_rbrace().withWhitespace("\n  "));
    String whitespace = "";
    JooSymbol firstSymbol = Iterables.getFirst(Arrays.asList(directive.getSymModifiers()), null);
    if(null != firstSymbol) {
      whitespace = firstSymbol.getWhitespace();
    }
    return new FunctionDeclaration(new AnnotationsAndModifiers(null, Collections.singletonList(sym_public().withWhitespace(whitespace))),
            sym_function(),
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
    BlockStatement constructorBody = new BlockStatement(sym_lbrace(), constructorBodyDirectives, sym_rbrace().withWhitespace("\n"));
    TypeRelation typeRelation = new TypeRelation(sym_colon(), new Type(ide));
    Parameters params = new Parameters(new Parameter(null, new Ide(MxmlUtils.CONFIG), typeRelation, new Initializer(sym_eq(), new LiteralExpr(sym_null()))));
    return new FunctionDeclaration(new AnnotationsAndModifiers(null, Collections.singletonList(sym_public())),
            sym_function(), null, ide, sym_lparen(), params, sym_rparen(), null, constructorBody, null);
  }

  @Nonnull
  static ImportDirective createImport(@Nonnull Ide superClass) {
    return new ImportDirective(sym_import().withWhitespace("\n"), superClass, sym_semicolon());
  }

  @Nonnull
  static ClassDeclaration createClassDeclaration(@Nonnull String classQName, @Nonnull JooSymbol rootNodeSymbol, @Nonnull Extends ext, @Nullable Implements impl, @Nonnull List<Directive> classBodyDirectives, @Nonnull InputSource source) {
    ClassBody classBody = new ClassBody(sym_lbrace(), classBodyDirectives, sym_rbrace());
    String whitespace = MxmlUtils.toASDoc(rootNodeSymbol.getWhitespace());

    return new ClassDeclaration(new AnnotationsAndModifiers(new LinkedList<>(), Collections.singletonList(sym_public().withWhitespace(whitespace))),
            sym_class(), new Ide(CompilerUtils.className(classQName)),
            ext, impl, classBody);
  }

  @Nonnull
  static ApplyExpr createCastExpr(@Nonnull Ide type, Expr expr) {
    IdeExpr fun = new IdeExpr(type);
    return createApplyExpr(fun, expr);
  }

  @Nonnull
  static ApplyExpr createApplyExpr(Expr fun, Expr ...args) {
    return new ApplyExpr(fun, sym_lparen(), createCommaSeparatedList(args), sym_rparen());
  }

  @Nonnull
  static DotExpr createDotExpr(Ide object, String property) {
    return createDotExpr(new IdeExpr(object), new Ide(property));
  }

  @Nonnull
  static DotExpr createDotExpr(Expr object, Ide property) {
    return new DotExpr(object, sym_dot(), property);
  }

  @Nonnull
  static ArrayIndexExpr createArrayIndexExpr(Expr expr, String index) {
    return new ArrayIndexExpr(expr, sym_lbrack(), createStringLiteral(index), sym_rbrack());
  }

  @Nonnull
  static LiteralExpr createStringLiteral(String value) {
    return createStringLiteral(value, "");
  }

  @Nonnull
  static LiteralExpr createStringLiteral(String value, String whiteSpace) {
    return new LiteralExpr(new JooSymbol(sym.STRING_LITERAL, null, -1, -1, whiteSpace, CompilerUtils.quote(value), value));
  }

  @Nonnull
  static VariableDeclaration createVariableDeclaration(@Nonnull Ide name, @Nonnull Ide type, @Nullable Expr initializerExpr) {
    Initializer initializer = initializerExpr == null ? null : new Initializer(sym_eq(), initializerExpr);
    return new VariableDeclaration(new AnnotationsAndModifiers(null,null),
            sym_var().withWhitespace(INDENT_4), name, new TypeRelation(sym_colon(), new Type(type)), initializer,
            null, sym_semicolon());
  }

  @Nonnull
  static SemicolonTerminatedStatement createSemicolonTerminatedStatement(@Nonnull AstNode astNode) {
    return new SemicolonTerminatedStatement(astNode, sym_semicolon());
  }

  @Nonnull
  static SuperConstructorCallStatement createSuperConstructorCall(Ide superConfigVar) {
    return createSuperConstructorCall(new IdeExpr(superConfigVar));
  }

  @Nonnull
  static SuperConstructorCallStatement createSuperConstructorCall(Expr ...argExprs) {
    CommaSeparatedList<Expr> args = createCommaSeparatedList(argExprs);
    return new SuperConstructorCallStatement(sym_super().withWhitespace(INDENT_4), sym_lparen(), args, sym_rparen(), sym_semicolon());
  }

  @Nonnull
  static Directive createPropertyAssignment(@Nonnull Ide variable, @Nonnull Expr rightHandSide, @Nonnull String propertyName, boolean untypedAccess) {
    Expr leftHandSide;
    Ide varWithWhitespace = new Ide(variable.getIde());
    if (untypedAccess) {
      leftHandSide = new ArrayIndexExpr(new IdeExpr(varWithWhitespace), sym_lbrack(), new LiteralExpr(new JooSymbol('"' + propertyName + '"')), sym_rbrack());
    } else {
      Ide propertyNameIde = new Ide(propertyName);
      if(Ide.THIS.equals(varWithWhitespace.getName())) {
        leftHandSide = new IdeExpr(propertyNameIde);
      } else {
        leftHandSide = new DotExpr(new IdeExpr(varWithWhitespace), sym_dot(), propertyNameIde);
      }
    }

    AssignmentOpExpr assignmentOpExpr = createAssignmentOpExpr(leftHandSide, rightHandSide);
    return createSemicolonTerminatedStatement(assignmentOpExpr);
  }

  @Nonnull
  static AssignmentOpExpr createAssignmentOpExpr(Expr leftHandSide, @Nonnull Expr rightHandSide) {
    return new AssignmentOpExpr(leftHandSide, sym_eq().withWhitespace(" "), rightHandSide);
  }

  @Nonnull
  static ObjectField createObjectField( @Nonnull String propertyName, @Nonnull Expr rightHandSide) {
    return new ObjectField(createObjectFieldLabel(propertyName), sym_colon(), rightHandSide);
  }

  @Nonnull
  private static AstNode createObjectFieldLabel(@Nonnull String propertyName) {
    return Ide.isValidIdentifier(propertyName)
            ? new Ide(new JooSymbol(propertyName))
            : createStringLiteral(propertyName);
  }

  @Nonnull
  static Spread createSpread(@Nonnull Expr expr) {
    return new Spread(new JooSymbol(sym.REST, "..."), expr);
  }

  @Nonnull
  static ObjectLiteral createObjectLiteral( @Nonnull List<ObjectFieldOrSpread> objectFields) {
    return new ObjectLiteral(
            sym_lbrace(),
            createCommaSeparatedList(objectFields),
            null,
            sym_rbrace());
  }

  @Nonnull
  static ArrayLiteral createArrayLiteral(@Nonnull List<Expr> arrayElements) {
    return new ArrayLiteral(
            sym_lbrack(),
            createCommaSeparatedList(arrayElements),
            sym_rbrack());
  }

  @SafeVarargs
  @Nonnull
  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(T ...elements) {
    return createCommaSeparatedList(Arrays.asList(elements));
  }

  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(List<T> elements) {
    CommaSeparatedList<T> result = null;
    JooSymbol comma = null;
    for (int i = elements.size() - 1; i >= 0; i--) {
      T t = elements.get(i);
      result = new CommaSeparatedList<>(t, comma, result);
      comma = sym_comma();
    }
    return result;
  }

  @Nonnull
  static Expr createNewExpr(Ide typeIde, Expr ...args) {
    return createApplyExpr(new NewExpr(sym_new().withWhitespace(" "), new IdeExpr(typeIde)), args);
  }

  @Nonnull
  static LiteralExpr createNullLiteral() {
    return new LiteralExpr(sym_null());
  }

  @Nonnull
  static IdeExpr createThisExpr() {
    return new IdeExpr(new Ide(sym_this()));
  }
}
