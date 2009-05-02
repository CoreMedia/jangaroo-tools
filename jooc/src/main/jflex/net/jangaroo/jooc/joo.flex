/*
 * Copyright 2008 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/*
 * JangarooScript lexical scanner definition for the JFlex scanner generator
 *
 * Author: Andreas Gawecki
 */

/*
From http://livedocs.adobe.com/specs/actionscript/3/as3_specification118.html:

13.1 Lexical

Lexical keywords are removed from the available program namespace during scanning. It is a syntax error to use any of
these names except as indicated by the grammar. Syntactic keywords appear to the lexical scanner as identifier tokens,
but are given special meaning in certain contexts by the parser.

The following list contains all keywords:

as break case catch class const continue default delete do else extends false finally for function if implements import
in instanceof interface internal is new null package private protected public return super switch this throw
true try typeof use var void while with

The following list contains all identifiers that are syntactic keywords:

each get set to namespace include dynamic final native override static

Former Jangaroo keywords:

abstract assert enum final goto namespace override static synchronized throws transient volatile


13.2 Syntactic

Identifiers with special meaning become keywords in certain syntactic contexts:

    * In a for-each-in statement between the 'for' token and the '(' token:
      each
    * In a function definition between the 'function' token and an identifier token:
      get set
    * As the first word of a directive:
      namespace include
    * In an attribute list or wherever an attribute list can be used:
      dynamic final native override static

It is a syntax error to use a syntactic keyword in a context where it is treated as a keyword:

namespace = "hello"
namespace()

In these cases, the grammar requires an identifier after the namespace keyword.
*/

package net.jangaroo.jooc;

import java.util.HashMap;
import java_cup.runtime.*;
import net.jangaroo.jooc.util.IncludeEvaluator;

%%

%class Scanner
%implements sym

%unicode

%line
%column

%cup

%{

static int[] terminalsAllowedBeforeRegexpLiteral = {
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

  String whitespace = "";
  String multiStateText = "";
  StringBuffer string = new StringBuffer();
  String fileName = "";
  int lastToken = -1;

  private boolean maybeRegexpLiteral() {
    for (int i = 0; i < terminalsAllowedBeforeRegexpLiteral.length; i++) {
      if (lastToken == terminalsAllowedBeforeRegexpLiteral[i])
        return true;
    }
    return false;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }

  private JooSymbol symbol(int type) {
    lastToken = type;
    JooSymbol result = new JooSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, yytext());
    whitespace = "";
    return result;
  }

  private JooSymbol symbol(int type, Object value) {
    lastToken = type;
    JooSymbol result = new JooSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, yytext(), value);
    whitespace = "";
    return result;
  }

  private JooSymbol multiStateSymbol(int type, Object value) {
    lastToken = type;
    JooSymbol result = new JooSymbol(type, fileName, yyline+1, yycolumn+1, whitespace, multiStateText, value);
    whitespace = "";
    return result;
  }

  class ScanError extends RuntimeException {

    JooSymbol sym;

    public ScanError(String msg, JooSymbol sym) {
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
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

IdentifierStart = [:letter:]|[$_]
Identifier = {IdentifierStart}({IdentifierStart}|[:digit:])*

DecIntegerLiteral = 0 | [1-9][0-9]*
HexIntegerLiteral = 0x{HexDigit}+

DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

RegexpFirst = [^\n\\*/]
Regexp = [^\n\\/]
RegexpFlag = [gim]
InvalidRegexpFlag = [$_]|[:letter:]
NonTerminator = [^\n]

HexDigit          = [0-9abcdefABCDEF]

Include           = "include \"" ~"\""

%state STRING_SQ, STRING_DQ, REGEXPFIRST, REGEXP

%%

<YYINITIAL> {

  {Comment}                       { whitespace += yytext(); }
  {WhiteSpace}                    { whitespace += yytext(); }
  {Include}                       { yypushStream(IncludeEvaluator.createReader(yytext(),getFileName())); }

  "as"                            { return symbol(AS); }
  "break"                         { return symbol(BREAK); }
  "case"                          { return symbol(CASE); }
  "catch"                         { return symbol(CATCH); }
  "class"                         { return symbol(CLASS); }
  "const"                         { return symbol(CONST); }
  "continue"                      { return symbol(CONTINUE); }
  "default"                       { return symbol(DEFAULT); }
  "delete"                        { return symbol(DELETE); }
  "do"                            { return symbol(DO); }
  "else"                          { return symbol(ELSE); }
  "extends"                       { return symbol(EXTENDS); }
  "finally"                       { return symbol(FINALLY); }
  "for"                           { return symbol(FOR); }
  "function"                      { return symbol(FUNCTION); }
  "if"                            { return symbol(IF); }
  "implements"                    { return symbol(IMPLEMENTS); }
  "import"                        { return symbol(IMPORT); }
  "in"                            { return symbol(IN); }
  "instanceof"                    { return symbol(INSTANCEOF); }
  "interface"                     { return symbol(INTERFACE); }
  "internal"                      { return symbol(INTERNAL); }
  "is"                            { return symbol(IS); }
  "new"                           { return symbol(NEW); }
  "null"                          { return symbol(NULL_LITERAL, null); }
  "package"                       { return symbol(PACKAGE); }
  "private"                       { return symbol(PRIVATE); }
  "protected"                     { return symbol(PROTECTED); }
  "public"                        { return symbol(PUBLIC); }
  "return"                        { return symbol(RETURN); }
  "super"                         { return symbol(SUPER); }
  "switch"                        { return symbol(SWITCH); }
  "this"                          { return symbol(THIS); }
  "throw"                         { return symbol(THROW); }
  "try"                           { return symbol(TRY); }
  "typeof"                        { return symbol(TYPEOF); }
  "use"                           { return symbol(USE); }
  "var"                           { return symbol(VAR); }
  "void"                          { return symbol(VOID); }
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
  "~"                             { return symbol(BITNOT); }
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
  "..."                           { return symbol(REST); }
  "::"                            { return symbol(NAMESPACESEP); }

  "/"                             { if (!maybeRegexpLiteral())
                                      return symbol(DIV);
                                    multiStateText = yytext();
                                    yybegin(REGEXPFIRST);
                                    string.setLength(0); }
  "/="                            { if (!maybeRegexpLiteral())
                                      return symbol(DIVEQ);
                                    multiStateText = yytext();
                                    yybegin(REGEXP);
                                    string.setLength(0);
                                    string.append('='); }

  \"                              { multiStateText = yytext(); yybegin(STRING_DQ); string.setLength(0); }
  \'                              { multiStateText = yytext(); yybegin(STRING_SQ); string.setLength(0); }

  {DecIntegerLiteral}             { return symbol(INT_LITERAL, new Integer(yytext())); }
  {HexIntegerLiteral}             { return symbol(INT_LITERAL, Long.parseLong(yytext().substring(2),16)); }
  {DoubleLiteral}                 { return symbol(FLOAT_LITERAL, new Double(yytext())); }
}

<STRING_DQ> {
  \"                              { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(STRING_LITERAL, string.toString()); }
  [^\r\n\"\\]+                     { multiStateText += yytext(); string.append( yytext() ); }
  "\\b"                           { multiStateText += yytext(); string.append( '\b' ); }
  "\\t"                           { multiStateText += yytext(); string.append( '\t' ); }
  "\\n"                           { multiStateText += yytext(); string.append( '\n' ); }
  "\\f"                           { multiStateText += yytext(); string.append( '\f' ); }
  "\\r"                           { multiStateText += yytext(); string.append( '\r' ); }
  "\\\""                          { multiStateText += yytext(); string.append( '\"' ); }
  "\\\'"                          { multiStateText += yytext(); string.append( '\'' ); }
  "\\\\"                          { multiStateText += yytext(); string.append( '\\' ); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { multiStateText += yytext();
                                   char val = (char) Integer.parseInt(yytext().substring(2),16);
                        	   string.append(val); }
  \\.                             { error("Illegal escape sequence"); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
}

<STRING_SQ> {
  \'                              { multiStateText += yytext(); yybegin(YYINITIAL); return multiStateSymbol(STRING_LITERAL, string.toString()); }
  [^\r\n'\\]+                     { multiStateText += yytext(); string.append( yytext() ); }
  "\\b"                           { multiStateText += yytext(); string.append( '\b' ); }
  "\\t"                           { multiStateText += yytext(); string.append( '\t' ); }
  "\\n"                           { multiStateText += yytext(); string.append( '\n' ); }
  "\\f"                           { multiStateText += yytext(); string.append( '\f' ); }
  "\\r"                           { multiStateText += yytext(); string.append( '\r' ); }
  "\\\""                          { multiStateText += yytext(); string.append( '\"' ); }
  "\\\'"                          { multiStateText += yytext(); string.append( '\'' ); }
  "\\\\"                          { multiStateText += yytext(); string.append( '\\' ); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { multiStateText += yytext();
                                   char val = (char) Integer.parseInt(yytext().substring(2),16);
                        	   string.append(val); }
  \\.                             { error("Illegal escape sequence"); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
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
<YYINITIAL,STRING_DQ,STRING_SQ,REGEXP,REGEXPFIRST> .|\n
                                  { char ch = yytext().charAt(0);
                                    String hex = Integer.toHexString((int)ch);
                                    while (hex.length() < 4)
                                      hex = "0"+hex;
                                    error("illegal character: \\u" + hex);
                                  }
<<EOF>>                           { if (yymoreStreams()) yypopStream(); else return symbol(EOF); }
