package net.jangaroo.jooc;

import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.util.IncludeEvaluator;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public abstract class ScannerBase implements sym {

  static int[] possibleTerminalsBeforeExpr = {
    CASE, DO, ELSE, IN, INSTANCEOF,
    RETURN, TYPEOF, WITH,
    PLUS, MINUS, NOT, DIV, MOD, MUL,
    LSHIFT, RSHIFT, URSHIFT,
    LT, GT, LTEQ, GTEQ,
    EQ, EQEQ, EQEQEQ, NOTEQ, NOTEQEQ,
    AND, XOR, OR, ANDAND, OROR,
    QUESTION, COLON, SEMICOLON, COMMA,
    MULTEQ, DIVEQ, MODEQ, PLUSEQ, MINUSEQ,
    LSHIFTEQ, RSHIFTEQ, URSHIFTEQ,
    ANDEQ, XOREQ, OREQ,
    LPAREN, LBRACE, LBRACK
  };

  protected String whitespace = "";
  protected String multiStateText = "";
  protected StringBuffer string = new StringBuffer();
  protected String fileName = "";
  protected int lastToken = -1;
  protected InputSource inputSource;

  private boolean lastTokenIn(int[] terminals) {
    for (int terminal : terminals) {
      if (lastToken == terminal)
        return true;
    }
    return false;
  }

  protected boolean maybeExpr() {
    return lastTokenIn(possibleTerminalsBeforeExpr);
  }

  public InputSource getInputSource() {
    return inputSource;
  }

  public void setInputSource(InputSource in) {
    this.inputSource = in;
    this.fileName = in.getPath();
  }

  public Reader createIncludeReader(String include) throws IOException {
    return IncludeEvaluator.createReader(include, getInputSource());
  }

  protected abstract JooSymbol symbol(int type);

  protected abstract JooSymbol symbol(int type, Object value);

  protected abstract JooSymbol multiStateSymbol(int type, Object value);

  /**
   * Pushback the current token so that it will be read again the next time next_token() is called
   */
  protected abstract void pushback(String whitespace);

  protected void error(String msg) throws ScanError {
    throw new ScanError(msg, symbol(SCAN_ERROR));
  }

  // error reporting:
  static protected HashMap<Integer,String> symbolMap = new java.util.HashMap<Integer, String>(50);

  static protected void defsym(String abbrev, int sym) {
    symbolMap.put(sym, abbrev);
  }

  public String getSymbolAbbreviation(int sym) {
    String value = (String) symbolMap.get(new Integer(sym));
    if (value != null)
      return "'" + value + "'";
    switch (sym) {
      case INT_LITERAL:
        return "integer literal";
      case FLOAT_LITERAL:
        return "float literal";
      case STRING_LITERAL:
        return "string literal";
      case BOOL_LITERAL:
        return "boolean literal";
      case REGEXP_LITERAL:
        return "regular expression literal";
      case IDE:
        return "identifier";
      case EOF:
        return "End of File";
    }
    return "?" + sym + "?";
  }

  static {
    defsym("as", AS);
    defsym("break", BREAK);
    defsym("case", CASE);
    defsym("catch", CATCH);
    defsym("class", CLASS);
    defsym("const", CONST);
    defsym("continue", CONTINUE);
    defsym("default", DEFAULT);
    defsym("delete", DELETE);
    defsym("do", DO);
    defsym("else", ELSE);
    defsym("extends", EXTENDS);
    defsym("finally", FINALLY);
    defsym("for", FOR);
    defsym("function", FUNCTION);
    defsym("if", IF);
    defsym("implements", IMPLEMENTS);
    defsym("import", IMPORT);
    defsym("in", IN);
    defsym("instanceof", INSTANCEOF);
    defsym("interface", INTERFACE);
    defsym("internal", INTERNAL);
    defsym("is", IS);
    defsym("new", NEW);
    defsym("null", NULL_LITERAL);
    defsym("package", PACKAGE);
    defsym("private", PRIVATE);
    defsym("protected", PROTECTED);
    defsym("public", PUBLIC);
    defsym("return", RETURN);
    defsym("super", SUPER);
    defsym("switch", SWITCH);
    defsym("this", THIS);
    defsym("throw", THROW);
    defsym("try", TRY);
    defsym("typeof", TYPEOF);
    defsym("use", USE);
    defsym("var", VAR);
    defsym("void", VOID);
    defsym("while", WHILE);
    defsym("with", WITH);
    defsym("(", LPAREN);
    defsym(")", RPAREN);
    defsym("{", LBRACE);
    defsym("}", RBRACE);
    defsym("[", LBRACK);
    defsym("]", RBRACK);
    defsym(";", SEMICOLON);
    defsym(",", COMMA);
    defsym(".", DOT);
    defsym("=", EQ);
    defsym(">", GT);
    defsym("<", LT);
    defsym("!", NOT);
    defsym("?", QUESTION);
    defsym(":", COLON);
    defsym("==", EQEQ);
    defsym("<=", LTEQ);
    defsym(">=", GTEQ);
    defsym("!=", NOTEQ);
    defsym("&&", ANDAND);
    defsym("||", OROR);
    defsym("++", PLUSPLUS);
    defsym("--", MINUSMINUS);
    defsym("+", PLUS);
    defsym("-", MINUS);
    defsym("*", MUL);
    defsym("/", DIV);
    defsym("&", AND);
    defsym("|", OR);
    defsym("^", XOR);
    defsym("%", MOD);
    defsym("~", BITNOT);
    defsym("<<", LSHIFT);
    defsym(">>", RSHIFT);
    defsym(">>>", URSHIFT);
    defsym("+=", PLUSEQ);
    defsym("-=", MINUSEQ);
    defsym("*=", MULTEQ);
    defsym("/=", DIVEQ);
    defsym("&=", ANDEQ);
    defsym("|=", OREQ);
    defsym("^=", XOREQ);
    defsym("%=", MODEQ);
    defsym("<<=", LSHIFTEQ);
    defsym(">>=", RSHIFTEQ);
    defsym(">>>=", URSHIFTEQ);
    defsym("===", EQEQEQ);
    defsym("!==", NOTEQEQ);
    defsym("...", REST);
    defsym("::", NAMESPACESEP);
  }

  class ScanError extends RuntimeException {

    JooSymbol sym;

    public ScanError(String msg, JooSymbol sym) {
      super(msg);
      this.sym = sym;
    }
  }
}
