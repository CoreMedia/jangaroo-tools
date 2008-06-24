/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 *
 * JangarooScript lexical scanner definition for the JFlex scanner generator
 *
 * Author: Andreas Gawecki
 */

package com.coremedia.jscc;

import java.util.HashMap;
import java_cup.runtime.*;

%%

%class Scanner
%implements sym

%unicode

%line
%column

%cup

%{
  String whitespace = "";
  String multiStateText = "";
  StringBuffer string = new StringBuffer();
  String fileName = "";
  parser theParser;

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setParser(parser p) {
    theParser = p;
  }

  public String getFileName() {
    return fileName;
  }

  private JscSymbol symbol(int type) {
    JscSymbol result = new JscSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, yytext());
    whitespace = "";
    return result;
  }

  private JscSymbol symbol(int type, Object value) {
    JscSymbol result = new JscSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, yytext(), value);
    whitespace = "";
    return result;
  }

  private JscSymbol multiStateSymbol(int type, Object value) {
    JscSymbol result = new JscSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, multiStateText, value);
    whitespace = "";
    return result;
  }

  class ScanError extends RuntimeException {

    JscSymbol sym;

    public ScanError(String msg, JscSymbol sym) {
      super(msg);
      this.sym = sym;
    }
  }

  private void error(String msg) throws ScanError {
    throw new ScanError(msg, symbol(SCAN_ERROR));
  }

  // error reporting:
  static protected HashMap symbolMap = new java.util.HashMap(50);

  static protected void defsym(String abbrev, int sym) {
     symbolMap.put(new Integer(sym), abbrev);
  }

  public String getSymbolAbbreviation(int sym) {
    String value = (String) symbolMap.get(new Integer(sym));
    if (value != null)
      return "'" + value + "'";
    switch(sym) {
      case CHAR_LITERAL: return "character literal";
      case INT_LITERAL: return "integer literal";
      case FLOAT_LITERAL: return "float literal";
      case STRING_LITERAL: return "string literal";
      case BOOL_LITERAL: return "boolean literal";
      case REGEXP_LITERAL: return "regular expression literal";
      case IDE: return "identifier";
      case EOF: return "End of File";
    }
    return "?"+sym+"?";
  }

  static {
    defsym("abstract", ABSTRACT);
    defsym("assert", ASSERT);
    defsym("as", AS);
    defsym("break", BREAK);
    defsym("case", CASE);
    defsym("catch", CATCH);
    defsym("class", CLASS);
    defsym("const", CONST);
    defsym("continue", CONTINUE);
    defsym("debugger", DEBUGGER);
    defsym("default", DEFAULT);
    defsym("delete", DELETE);
    defsym("do", DO);
    defsym("else", ELSE);
    defsym("enum", ENUM);
    defsym("extends", EXTENDS);
    defsym("final", FINAL);
    defsym("finally", FINALLY);
    defsym("for", FOR);
    defsym("function", FUNCTION);
    defsym("goto", GOTO);
    defsym("if", IF);
    defsym("implements", IMPLEMENTS);
    defsym("import", IMPORT);
    defsym("in", IN);
    defsym("instanceof", INSTANCEOF);
    defsym("interface", INTERFACE);
    defsym("internal", INTERNAL);
    defsym("new", NEW);
    defsym("null", NULL_LITERAL);
    defsym("override", OVERRIDE);
    defsym("package", PACKAGE);
    defsym("private", PRIVATE);
    defsym("protected", PROTECTED);
    defsym("public", PUBLIC);
    defsym("return", RETURN);
    defsym("static", STATIC);
    defsym("super", SUPER);
    defsym("switch", SWITCH);
    defsym("synchronized", SYNCHRONIZED);
    defsym("this", THIS);
    defsym("throw", THROW);
    defsym("throws", THROWS);
    defsym("transient", TRANSIENT);
    defsym("try", TRY);
    defsym("typeof", TYPEOF);
    defsym("var", VAR);
    defsym("void", VOID);
    defsym("volatile", VOLATILE);
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
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

IdentifierStart = [:letter:]|_
Identifier = {IdentifierStart}({IdentifierStart}|[:digit:])*

DecIntegerLiteral = 0 | [1-9][0-9]*

DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

RegexpFirst = [^\n\\*/]
Regexp = [^\n\\/]
RegexpFlag = [gim]
InvalidRegexpFlag = [$_]|[:letter:]
NonTerminator = [^\n]

HexDigit          = [0-9abcdefABCDEF]

%state STRING, REGEXPFIRST, REGEXP, CHARLITERAL

%%

<YYINITIAL> {

  {Comment}                       { whitespace += yytext(); }
  {WhiteSpace}                    { whitespace += yytext(); }

  "abstract"                      { return symbol(ABSTRACT); }
  "assert"                        { return symbol(ASSERT); }
  "as"                            { return symbol(AS); }
  "break"                         { return symbol(BREAK); }
  "case"                          { return symbol(CASE); }
  "catch"                         { return symbol(CATCH); }
  "class"                         { return symbol(CLASS); }
  "const"                         { return symbol(CONST); }
  "continue"                      { return symbol(CONTINUE); }
  "debugger"                      { return symbol(DEBUGGER); }
  "default"                       { return symbol(DEFAULT); }
  "delete"                        { return symbol(DELETE); }
  "do"                            { return symbol(DO); }
  "else"                          { return symbol(ELSE); }
  "enum"                          { return symbol(ENUM); }
  "extends"                       { return symbol(EXTENDS); }
  "finally"                       { return symbol(FINALLY); }
  "for"                           { return symbol(FOR); }
  "function"                      { return symbol(FUNCTION); }
  "goto"                          { return symbol(GOTO); }
  "if"                            { return symbol(IF); }
  "implements"                    { return symbol(IMPLEMENTS); }
  "import"                        { return symbol(IMPORT); }
  "in"                            { return symbol(IN); }
  "instanceof"                    { return symbol(INSTANCEOF); }
  "interface"                     { return symbol(INTERFACE); }
  "internal"                      { return symbol(INTERNAL); }
  "new"                           { return symbol(NEW); }
  "null"                          { return symbol(NULL_LITERAL, null); }
  "override"                      { return symbol(OVERRIDE); }
  "package"                       { return symbol(PACKAGE); }
  "private"                       { return symbol(PRIVATE); }
  "protected"                     { return symbol(PROTECTED); }
  "public"                        { return symbol(PUBLIC); }
  "return"                        { return symbol(RETURN); }
  "static"                        { return symbol(STATIC); }
  "super"                         { return symbol(SUPER); }
  "switch"                        { return symbol(SWITCH); }
  "synchronized"                  { return symbol(SYNCHRONIZED); }
  "this"                          { return symbol(THIS); }
  "throw"                         { return symbol(THROW); }
  "throws"                        { return symbol(THROWS); }
  "transient"                     { return symbol(TRANSIENT); }
  "try"                           { return symbol(TRY); }
  "typeof"                        { return symbol(TYPEOF); }
  "var"                           { return symbol(VAR); }
  "void"                          { return symbol(VOID); }
  "volatile"                      { return symbol(VOLATILE); }
  "while"                         { return symbol(WHILE); }
  "with"                          { return symbol(WITH); }

  "true"                          { return symbol(BOOL_LITERAL, new Boolean(true)); }
  "false"                         { return symbol(BOOL_LITERAL, new Boolean(false)); }

  {Identifier}                    { return symbol(IDE, yytext()); }

  "("                             { return symbol(LPAREN); }
  ")"                             { return symbol(RPAREN); }
  "{"                             { return symbol(LBRACE); }
  "}"                             { return symbol(RBRACE); }
  "["                             { return symbol(LBRACK); }
  "]"                             { return symbol(RBRACK); }
  ";"                             { return symbol(SEMICOLON); }
  ","                             { return symbol(COMMA); }
  "."                             { return symbol(DOT); }
  "="                             { return symbol(EQ); }
  ">"                             { return symbol(GT); }
  "<"                             { return symbol(LT); }
  "!"                             { return symbol(NOT); }
  "?"                             { return symbol(QUESTION); }
  ":"                             { return symbol(COLON); }
  "=="                            { return symbol(EQEQ); }
  "<="                            { return symbol(LTEQ); }
  ">="                            { return symbol(GTEQ); }
  "!="                            { return symbol(NOTEQ); }
  "&&"                            { return symbol(ANDAND); }
  "||"                            { return symbol(OROR); }
  "++"                            { return symbol(PLUSPLUS); }
  "--"                            { return symbol(MINUSMINUS); }
  "+"                             { return symbol(PLUS); }
  "-"                             { return symbol(MINUS); }
  "*"                             { return symbol(MUL); }
  "&"                             { return symbol(AND); }
  "|"                             { return symbol(OR); }
  "^"                             { return symbol(XOR); }
  "%"                             { return symbol(MOD); }
  "<<"                            { return symbol(LSHIFT); }
  ">>"                            { return symbol(RSHIFT); }
  ">>>"                           { return symbol(URSHIFT); }
  "+="                            { return symbol(PLUSEQ); }
  "-="                            { return symbol(MINUSEQ); }
  "*="                            { return symbol(MULTEQ); }
  "&="                            { return symbol(ANDEQ); }
  "|="                            { return symbol(OREQ); }
  "^="                            { return symbol(XOREQ); }
  "%="                            { return symbol(MODEQ); }
  "<<="                           { return symbol(LSHIFTEQ); }
  ">>="                           { return symbol(RSHIFTEQ); }
  ">>>="                          { return symbol(URSHIFTEQ); }
  "==="                           { return symbol(EQEQEQ); }
  "!=="                           { return symbol(NOTEQEQ); }

  "/"                             { if (!theParser.isSymbolExpected(REGEXP_LITERAL))
                                      return symbol(DIV);
                                    multiStateText = yytext();
                                    yybegin(REGEXPFIRST);
                                    string.setLength(0); }
  "/="                            { if (!theParser.isSymbolExpected(REGEXP_LITERAL))
                                      return symbol(DIVEQ);
                                    multiStateText = yytext();
                                    yybegin(REGEXP);
                                    string.setLength(0);
                                    string.append('='); }

  \"                              { multiStateText = yytext(); yybegin(STRING); string.setLength(0); }
  \'                              { multiStateText = yytext(); yybegin(CHARLITERAL); }

  {DecIntegerLiteral}             { return symbol(INT_LITERAL, new Integer(yytext())); }
  {DoubleLiteral}                 { return symbol(FLOAT_LITERAL, new Double(yytext())); }
}

<STRING> {
  \"                              { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(STRING_LITERAL, string.toString()); }
  {StringCharacter}+              { multiStateText += yytext(); string.append( yytext() ); }
  "\\b"                           { multiStateText += yytext(); string.append( '\b' ); }
  "\\t"                           { multiStateText += yytext(); string.append( '\t' ); }
  "\\n"                           { multiStateText += yytext(); string.append( '\n' ); }
  "\\f"                           { multiStateText += yytext(); string.append( '\f' ); }
  "\\r"                           { multiStateText += yytext(); string.append( '\r' ); }
  "\\\""                          { multiStateText += yytext(); string.append( '\"' ); }
  "\\'"                           { multiStateText += yytext(); string.append( '\'' ); }
  "\\\\"                          { multiStateText += yytext(); string.append( '\\' ); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { multiStateText += yytext();
                                   char val = (char) Integer.parseInt(yytext().substring(2),16);
                        	   string.append(val); }
  \\.                             { error("Illegal escape sequence"); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
}

<CHARLITERAL> {
  {SingleCharacter}\'             { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character(yytext().charAt(0))); }
  "\\b"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\b'));}
  "\\t"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\t'));}
  "\\n"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\n'));}
  "\\f"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\f'));}
  "\\r"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\r'));}
  "\\\""\'                        { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\"'));}
  "\\'"\'                         { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\''));}
  "\\\\"\'                        { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(CHAR_LITERAL, new Character('\\')); }

  \\(u{HexDigit}{4}|x{HexDigit}{2})\'
                                  { multiStateText += yytext();
                                    yybegin(YYINITIAL);
                                    int val = Integer.parseInt(yytext().substring(2,yylength()-1),16);
	                            return multiStateSymbol(CHAR_LITERAL, new Character((char)val));
	                          }
  \\.                             { error("illegal escape sequence"); }
  {LineTerminator}                { error("unterminated character literal at end of line"); }
  .                               { error("expected \"'\" to terminate character literal"); }
}

<REGEXPFIRST> {
  {RegexpFirst}                   { multiStateText += yytext(); string.append(yytext()); yybegin(REGEXP); }
  "\\"{NonTerminator}             { multiStateText += yytext(); string.append(yytext()); yybegin(REGEXP); }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

<REGEXP> {
  "/"                             { multiStateText += yytext();
                                    yybegin(YYINITIAL);
                                    return multiStateSymbol(REGEXP_LITERAL, string.toString());
                                  }
  "/"{RegexpFlag}+                { multiStateText += yytext();
                                    yybegin(YYINITIAL);
                                    return multiStateSymbol(REGEXP_LITERAL, string.toString());
                                  }
  "/"{RegexpFlag}+{InvalidRegexpFlag}
                                  { error("invalid regular expression flag: '" +
                                      yytext().charAt(yytext().length()-1) + "'");
                                  }
  {Regexp}+                       { multiStateText += yytext(); string.append(yytext()); }
  "\\"{NonTerminator}             { multiStateText += yytext(); string.append(yytext()); }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

/* error catchall */
<YYINITIAL,STRING,REGEXP,REGEXPFIRST> .|\n
                                  { char ch = yytext().charAt(0);
                                    String hex = Integer.toHexString((int)ch);
                                    while (hex.length() < 4)
                                      hex = "0"+hex;
                                    error("illegal character: \\u" + hex);
                                  }
<<EOF>>                           { return symbol(EOF); }
