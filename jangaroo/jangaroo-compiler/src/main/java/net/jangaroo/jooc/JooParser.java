package net.jangaroo.jooc;

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.config.SemicolonInsertionMode;

import java.util.regex.Pattern;

public class JooParser extends parser {

  class FatalSyntaxError extends RuntimeException {
    FatalSyntaxError(String msg) {
      super(msg);
    }
  }

  private SemicolonInsertionMode semicolonInsertionMode;
  private CompileLog log;
  private boolean eofSeen = false;

  // pattern for line terminator characters according to ECMA-262:
  private final static Pattern LINE_TERMINATORS_PATTERN = Pattern.compile("[\n\r\u2028\u2029]");

  private Scanner scanner;

  public JooParser(Scanner scanner) {
    super(scanner);
    this.scanner = scanner;
  }

  public void setCompileLog(CompileLog log) {
    this.log = log;
  }

  public void setSemicolonInsertionMode(SemicolonInsertionMode semicolonInsertionMode) {
    this.semicolonInsertionMode = semicolonInsertionMode;
  }

  private boolean containsLineTerminator(String text) {
    return LINE_TERMINATORS_PATTERN.matcher(text).find();
  }

  private boolean insertVirtualToken(int token, String text) {
    if (eofSeen) {
      return false;
    }
    JooSymbol currentToken = (JooSymbol)cur_token;
    eofSeen = currentToken.sym == sym.EOF;
    if (token == sym.SEMICOLON) {
      if (currentToken.isSemicolonInsertedBefore()) {
        // we already tried semicolon insertion here, avoid infinite loop
        unrecovered_syntax_error(cur_token);
      }
      boolean isBraceOrEof = cur_token.sym == sym.RBRACE || cur_token.sym == sym.EOF;
      if (!isBraceOrEof) {
        checkSemicolonInsertionMode();
      }
    }
    if (currentToken.isVirtual() && currentToken.sym == token) {
      // avoid infinite loop, but isn't it a parser bug?
      report_error("Internal parser error: infinite loop during virtual token insertion: " + scanner.getSymbolAbbreviation(cur_token.sym), cur_token);
      return false;
    }
    JooSymbol virtualToken = new JooSymbol(token, currentToken.getFileName(), currentToken.getLine(), currentToken.getColumn(), "", text);
    virtualToken.setVirtual(true);
    currentToken.setSemicolonInsertedBefore(token == sym.SEMICOLON);
    cur_token = virtualToken;
    scanner.pushback(currentToken);
    return true;
  }

  private boolean replaceToken(int token) {
    JooSymbol currentToken = (JooSymbol)cur_token;
    cur_token = new JooSymbol(token, currentToken.getFileName(), currentToken.getLine(),
      currentToken.getColumn(), currentToken.getWhitespace(), currentToken.getText());
    return true;
  }

  private int getActionFromTable(int sym) {
    return get_action(((Symbol)stack.peek()).parse_state, sym);
  }

  private boolean isActionDefinedFor(int sym) {
    return getActionFromTable(sym) != 0;
  }

  private boolean isShiftActionDefinedFor(int sym) {
    return getActionFromTable(sym) > 0;
  }

  protected boolean error_recovery(boolean debug) {
    String whitespace = ((JooSymbol)cur_token).getWhitespace();
    boolean isPostfixOp = cur_token.sym == sym.PLUSPLUS || cur_token.sym == sym.MINUSMINUS;

    // check for regular expression start
    if ((cur_token.sym == sym.DIV || cur_token.sym == sym.DIVEQ) && isActionDefinedFor(sym.REGEXP_START)) {
      // this is only the case if we are not just before parsing an expression statement
      scanner.startRegexp((JooSymbol)cur_token);
      return replaceToken(sym.REGEXP_START);
    }

    // check for Type start
    if (cur_token.sym == sym.MULTEQ && isActionDefinedFor(sym.TYPE_START)) {
      scanner.startType((JooSymbol)cur_token);
      return replaceToken(sym.TYPE_START);
    }

    // mimic ECMA-262 grammar precondition: token notin { '{', 'function' } for expression statements
    if (cur_token.sym == sym.LBRACE && isActionDefinedFor(sym.LBRACE_EXPR)) {
      // this is only the case if we are not just before parsing an expression statement
      return replaceToken(sym.LBRACE_EXPR);
    }
    if (cur_token.sym == sym.FUNCTION && isActionDefinedFor(sym.FUNCTION_EXPR)) {
      // this is only the case if we are not just before parsing an expression statement
      return replaceToken(sym.FUNCTION_EXPR);
    }

    // disambiguate metadata annotations and array literals (both start with '[')
    if (cur_token.sym == sym.LBRACK && isActionDefinedFor(sym.LBRACK_EXPR)) {
      return replaceToken(sym.LBRACK_EXPR);
    }

    // try semicolon insertion
    boolean isShiftActionDefinedForNoLineTerminatorHere = isShiftActionDefinedFor(sym.NO_LINE_TERMINATOR_HERE);
    if (isShiftActionDefinedForNoLineTerminatorHere || (isPostfixOp && isActionDefinedFor(sym.NO_LINE_TERMINATOR_HERE_POSTFIX_OP))) {
      // ECMA-262 restricted production
      if (containsLineTerminator(whitespace)) {
        // avoid syntax error later on if there is no action for semicolon
        if (isActionDefinedFor(sym.SEMICOLON)) {
          return insertVirtualToken(sym.SEMICOLON, ";");
        } else if (isPostfixOp) {
          report_fatal_error("postfix operator should be on same line (semicolon insertion would produce syntax error)", cur_token);
        }
      }
      // no semicolon to insert, proceed as if a NO_LINE_TERMINATOR_HERE(_POSTFIX_OP) token has been seen
      return insertVirtualToken(isShiftActionDefinedForNoLineTerminatorHere
        ? sym.NO_LINE_TERMINATOR_HERE
        : sym.NO_LINE_TERMINATOR_HERE_POSTFIX_OP,
        "[no line terminator here]");
    }
    if (isActionDefinedFor(sym.SEMICOLON)) {
      boolean isBraceOrEof = cur_token.sym == sym.RBRACE || cur_token.sym == sym.EOF;
      if (isBraceOrEof || containsLineTerminator(whitespace)) {
        return insertVirtualToken(sym.SEMICOLON, ";");
      }
    }
    return false;
  }

  private void checkSemicolonInsertionMode() {
    switch (semicolonInsertionMode) {
      case ERROR:
        log.error((JooSymbol)cur_token, "automatic semicolon insertion required by language spec, but forbidden by jooc semicolonInsertionMode");
        break;
      case WARN:
        log.warning((JooSymbol)cur_token, "automatic semicolon insertion");
        break;
    }
  }

  public void report_error(String message, Object info) {
    if (info instanceof JooSymbol) {
      log.error((JooSymbol)info, message);
    } else {
      log.error("Error: " + message);
    }
  }

  public void unrecovered_syntax_error(Symbol cur_token) {
    report_fatal_error("Syntax error: " + scanner.getSymbolAbbreviation(cur_token.sym), cur_token);
  }

  public void report_fatal_error(String message, Object info) {
    report_error(message, info);
    done_parsing();
    throw new FatalSyntaxError("Fatal Syntax Error");
  }

  public void syntax_error(Symbol cur_token) {
    //ignore, we try to recover with SEMICOLON insertion
    //if that fails, unrecovered_syntax_error() will be called and report the error
  }

}
