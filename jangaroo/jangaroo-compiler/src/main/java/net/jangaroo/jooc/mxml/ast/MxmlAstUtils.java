package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.AnnotationsAndModifiers;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.BlockStatement;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.Initializer;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.ast.SuperConstructorCallStatement;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class MxmlAstUtils {

  static final String INDENT_4 = "\n    ";

  static final JooSymbol SYM_CLASS = new JooSymbol(sym.CLASS, "class");
  static final JooSymbol SYM_COLON = new JooSymbol(sym.COLON, ":");
  static final JooSymbol SYM_COMMA = new JooSymbol(sym.COMMA, ",");
  static final JooSymbol SYM_DOT = new JooSymbol(sym.DOT, ".");
  static final JooSymbol SYM_EQ = new JooSymbol(sym.EQ, "=");
  static final JooSymbol SYM_FUNCTION = new JooSymbol(sym.FUNCTION, "function");
  static final JooSymbol SYM_IMPORT = new JooSymbol(sym.IMPORT, "import").withWhitespace("\n");
  static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  static final JooSymbol SYM_LBRACK = new JooSymbol(sym.LBRACK, "[");
  static final JooSymbol SYM_LPAREN = new JooSymbol(sym.LPAREN, "(");
  static final JooSymbol SYM_NEW = new JooSymbol(sym.NEW, "new");
  static final JooSymbol SYM_NULL = new JooSymbol(sym.NULL_LITERAL, "null");
  static final JooSymbol SYM_PUBLIC = new JooSymbol(sym.PUBLIC, "public");
  static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  static final JooSymbol SYM_RBRACK = new JooSymbol(sym.RBRACK, "]");
  static final JooSymbol SYM_RPAREN = new JooSymbol(sym.RPAREN, ")");
  static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");
  static final JooSymbol SYM_SUPER = new JooSymbol(sym.SUPER, "super");
  static final JooSymbol SYM_THIS = new JooSymbol(sym.THIS, Ide.THIS);

  private MxmlAstUtils() {
    // hide constructor for utility class
  }

  @Nonnull
  static FunctionDeclaration createConstructor(@Nonnull FunctionDeclaration directive, @Nonnull List<Directive> constructorBodyDirectives) {
    BlockStatement constructorBody = new BlockStatement(SYM_LBRACE, constructorBodyDirectives, SYM_RBRACE.withWhitespace("\n  "));
    String whitespace = "";
    JooSymbol firstSymbol = Iterables.getFirst(Arrays.asList(directive.getSymModifiers()), null);
    if (null != firstSymbol) {
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
    return new ImportDirective(SYM_IMPORT, superClass, SYM_SEMICOLON);
  }

  @Nonnull
  static ClassDeclaration createClassDeclaration(@Nonnull String classQName, @Nonnull JooSymbol rootNodeSymbol) {
    ClassBody classBody = new ClassBody(SYM_LBRACE, new ArrayList<>(), SYM_RBRACE);
    String whitespace = MxmlUtils.toASDoc(rootNodeSymbol.getWhitespace());

    return new ClassDeclaration(new AnnotationsAndModifiers(new LinkedList<>(), Collections.singletonList(SYM_PUBLIC.withWhitespace(whitespace))),
            SYM_CLASS, new Ide(CompilerUtils.className(classQName)),
            null, null, classBody);
  }

  @Nonnull
  static SemicolonTerminatedStatement createSemicolonTerminatedStatement(@Nonnull AstNode astNode) {
    return new SemicolonTerminatedStatement(astNode, SYM_SEMICOLON);
  }

  @Nonnull
  static SuperConstructorCallStatement createSuperConstructorCall(Ide superConfigVar) {
    CommaSeparatedList<Expr> args = new CommaSeparatedList<>(new IdeExpr(superConfigVar));
    return new SuperConstructorCallStatement(SYM_SUPER.withWhitespace(INDENT_4), SYM_LPAREN, args, SYM_RPAREN, SYM_SEMICOLON);
  }

  @SafeVarargs
  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(T... elements) {
    return createCommaSeparatedList(Arrays.asList(elements));
  }

  static <T extends AstNode> CommaSeparatedList<T> createCommaSeparatedList(List<T> elements) {
    CommaSeparatedList<T> commaSeparatedList = null;
    for (T element : Lists.reverse(elements)) {
      commaSeparatedList = commaSeparatedList == null
              ? new CommaSeparatedList<>(element)
              : new CommaSeparatedList<>(element, SYM_COMMA, commaSeparatedList);
    }
    return commaSeparatedList;
  }

  static ApplyExpr createApplyExpr(Expr fun, Expr... parameters) {
    return new ApplyExpr(fun, SYM_LPAREN, createCommaSeparatedList(parameters), SYM_RPAREN);
  }

  static DotExpr createDotExpr(JooSymbol objectIde, String propertyIde) {
    return createDotExpr(objectIde, new JooSymbol(propertyIde));
  }

  static DotExpr createDotExpr(JooSymbol objectIde, JooSymbol propertyIde) {
    return new DotExpr(new IdeExpr(objectIde), SYM_DOT, new Ide(propertyIde));
  }

  static ApplyExpr createMethodInvocation(FunctionDeclaration method, JooSymbol objectIde, List<Expr> args) {
    DotExpr initFunctionInvocation = createDotExpr(objectIde, method.getIde().getSymbol().withoutWhitespace());
    return new ApplyExpr(initFunctionInvocation, method.getFun().getLParen(), createCommaSeparatedList(args), method.getFun().getRParen());
  }
}
