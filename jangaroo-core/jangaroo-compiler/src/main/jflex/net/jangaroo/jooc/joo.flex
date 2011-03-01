/*
 * Copyright 2008-2010 CoreMedia AG
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

%%

%class Scanner
%extends ScannerBase
%implements sym

%unicode

%line
%column

%implements java_cup.runtime.Scanner
%function scan
%type java_cup.runtime.Symbol
%eofval{
  return new java_cup.runtime.Symbol(<CUPSYM>.EOF);
%eofval}
%eofclose


%{

  protected JooSymbol symbol(int sym) {
    JooSymbol result = new JooSymbol(sym, fileName, yyline + 1, yycolumn + 1, whitespace, yytext());
    whitespace = "";
    return result;
  }

  protected JooSymbol symbol(int sym, Object value) {
    JooSymbol result = new JooSymbol(sym, fileName, yyline + 1, yycolumn + 1, whitespace, yytext(), value);
    whitespace = "";
    return result;
  }

  protected JooSymbol multiStateSymbol(int sym, Object value) {
    JooSymbol result = new JooSymbol(sym, fileName, yyline + 1, yycolumn + 1, whitespace, multiStateText, value);
    whitespace = "";
    return result;
  }

  protected void startRegexp(JooSymbol regexpStart) {
    multiStateText = "";
    string.setLength(0);
    whitespace = regexpStart.getWhitespace();
    assert(regexpStart.sym == sym.DIV || regexpStart.sym == sym.DIVEQ);
    pushback(regexpStart.sym == sym.DIVEQ ? 2 : 1);
    yybegin(REGEXP_START);
  }

  // workaround for bug in jflex column counting, works only if no newline is in these n characters 
  private void pushback(int n) {
    yypushback(n);
    yycolumn -= n;
  }

%}

LineTerminator = [\n\r\u2028\u2029]
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

IdentifierStart = [:letter:]|[$_@]
Identifier = {IdentifierStart}({IdentifierStart}|[:digit:])*

DecIntegerLiteral = 0 | [1-9][0-9]*
HexIntegerLiteral = 0x{HexDigit}+

DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

InvalidRegexpFlag = [$_]|[:letter:]

/*
 Regexp fragments translated from ECMA-262 grammar. These contain rules of the form "a but not b".
 From the jflex manual: By applying DeMorgan we get set difference:
 the expression that matches everything of a not matched by b is !(!a|b)
*/
RegularExpressionFirstChar = (!(!{RegularExpressionNonTerminator}|[*\\\/\[])) | {RegularExpressionBackslashSequence} | {RegularExpressionClass}
RegularExpressionChar = (!(!{RegularExpressionNonTerminator}|[\\\/\[])) | {RegularExpressionBackslashSequence} | {RegularExpressionClass}
RegularExpressionBackslashSequence = "\\" {RegularExpressionNonTerminator}

RegularExpressionNonTerminator = [^\n\r\u2028\u2029]
RegularExpressionClass = "[" {RegularExpressionClassChar}* "]"

/* Flex compc does not accept an unquoted / within a character class, contrary to ECMA.262: */
RegularExpressionClassChar = [^\n\r\u2028\u2029\]\\] | {RegularExpressionBackslashSequence}

RegularExpressionFlag = {IdentifierStart}

RegexpRest = {RegularExpressionChar}* "/" {RegularExpressionFlag}*

HexDigit          = [0-9abcdefABCDEF]

Include           = "include \"" ~"\""

%state STRING_SQ, STRING_DQ, REGEXP_START, REGEXP_FIRST, REGEXP_REST

%%

<YYINITIAL> {

  {Comment}                       { whitespace += yytext(); }
  {WhiteSpace}                    { whitespace += yytext(); }
  {Include}                       { yypushStream(createIncludeReader(yytext())); }

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
  ":*"                            { return symbol(ANYTYPE); }
  "*="                            { return symbol(MULTEQ); }
  "&="                            { return symbol(ANDEQ); }
  "&&="                           { return symbol(ANDANDEQ); }
  "|="                            { return symbol(OREQ); }
  "||="                           { return symbol(OROREQ); }
  "^="                            { return symbol(XOREQ); }
  "%="                            { return symbol(MODEQ); }
  "<<="                           { return symbol(LSHIFTEQ); }
  ">>="                           { return symbol(RSHIFTEQ); }
  ">>>="                          { return symbol(URSHIFTEQ); }
  "==="                           { return symbol(EQEQEQ); }
  "!=="                           { return symbol(NOTEQEQ); }
  "..."                           { return symbol(REST); }
  "::"                            { return symbol(NAMESPACESEP); }
  "/"                             { return symbol(DIV); }
  "/="                            { return symbol(DIVEQ); }
  ".<"                            { return symbol(DOTLT); }

  \"                              { multiStateText = yytext(); yybegin(STRING_DQ); string.setLength(0); }
  \'                              { multiStateText = yytext(); yybegin(STRING_SQ); string.setLength(0); }

  {DecIntegerLiteral}             { return symbol(INT_LITERAL, new Integer(yytext())); }
  {HexIntegerLiteral}             { return symbol(INT_LITERAL, Long.parseLong(yytext().substring(2),16)); }
  {DoubleLiteral}                 { return symbol(FLOAT_LITERAL, new Double(yytext())); }
}

<STRING_DQ> {
  \"                              { multiStateText += yytext(); yybegin(YYINITIAL);
                                    return multiStateSymbol(STRING_LITERAL, string.toString()); }
  [^\r\n\"\\]+                    { multiStateText += yytext(); string.append( yytext() ); }
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
  \\.                             { multiStateText += yytext(); string.append(yytext().substring(1)); }
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
  \\.                             { multiStateText += yytext(); string.append(yytext().substring(1)); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
}


<REGEXP_START> {
  "/"                             { multiStateText += yytext(); string.append(yytext()); yybegin(REGEXP_FIRST); }
}

<REGEXP_FIRST> {
  {RegularExpressionFirstChar}    { multiStateText += yytext(); string.append(yytext()); yybegin(REGEXP_REST); }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

<REGEXP_REST> {
  {RegexpRest}                    { multiStateText += yytext();
                                    string.append(yytext());
                                    yybegin(YYINITIAL);
                                    return multiStateSymbol(REGEXP_LITERAL, string.toString());
                                  }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

/* error catchall */
<YYINITIAL,STRING_DQ> .|\n                     { error("unrecognized input token"); }
<REGEXP_START,REGEXP_FIRST,REGEXP_REST> .|\n   { error("invalid regular expression literal"); } //todo be more precise

<<EOF>>                           { if (yymoreStreams()) yypopStream(); else return symbol(EOF); }
