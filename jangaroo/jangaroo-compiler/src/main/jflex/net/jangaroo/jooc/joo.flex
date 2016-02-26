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
  void prepareEmbedded(java.io.Reader reader, int line, int column) {
    yyreset(reader);
    yyline = line;
    yycolumn = column;
  }

  protected int getColumn() {
    return yycolumn + 1;
  }

  protected int getLine() {
    return yyline + 1;
  }

  protected void yybeginRegExpStart() {
    yybegin(REGEXP_START);
  }

%}

LineTerminator = [\n\r\u2028\u2029]
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ �\t\f]

Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

IdentifierStart = [:letter:]|[$_@]
Identifier = {IdentifierStart}({IdentifierStart}|[:digit:])*
XMLIdentifier = {IdentifierStart}({IdentifierStart}|[:digit:]|-)*

DecIntegerLiteral = 0 | [1-9][0-9]*
HexIntegerLiteral = 0[xX]{HexDigit}+

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

XmlComment = "<!--" ~"-->"

%state STRING_SQ, STRING_DQ, REGEXP_START, REGEXP_FIRST, REGEXP_REST, VECTOR_TYPE, MXML, XML_ATTRIBUTE_VALUE_DQ, XML_ATTRIBUTE_VALUE_SQ, XML_TEXT_CONTENT, CDATA_SECTION

%%

<YYINITIAL> {

  {Comment}                       { pushWhitespace(yytext()); }
  {WhiteSpace}                    { pushWhitespace(yytext()); }
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

  "true"                          { return symbol(BOOL_LITERAL, Boolean.TRUE); }
  "false"                         { return symbol(BOOL_LITERAL, Boolean.FALSE); }

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
  ".<"                            { increaseVectorNestingLevel(); yybegin(VECTOR_TYPE); return symbol(DOTLT); }

  \"                              { setMultiStateText(yytext()); yybegin(STRING_DQ); clearString(); }
  \'                              { setMultiStateText(yytext()); yybegin(STRING_SQ); clearString(); }

  {DecIntegerLiteral}             { return symbol(INT_LITERAL, new Long(yytext())); }
  {HexIntegerLiteral}             { return symbol(INT_LITERAL, Long.parseLong(yytext().substring(2),16)); }
  {DoubleLiteral}                 { return symbol(FLOAT_LITERAL, new Double(yytext())); }
}

<VECTOR_TYPE> {
  {Comment}                       { pushWhitespace(yytext()); }
  {WhiteSpace}                    { pushWhitespace(yytext()); }
  {Identifier}                    { return symbol(IDE, yytext()); }
  "*"                             { return symbol(MUL); }
  "."                             { return symbol(DOT); }
  ".<"                            { increaseVectorNestingLevel(); return symbol(DOTLT); }
  ">"                             { if (decreaseVectorNestingLevel()) { yybegin(YYINITIAL); } return symbol(GT); }
}

<STRING_DQ> {
  \"                              { pushMultiStateText(yytext()); yybegin(YYINITIAL);
                                    return multiStateSymbol(STRING_LITERAL, getString()); }
  [^\r\n\"\\]+                    { pushMultiStateText(yytext()); pushString( yytext() ); }
  "\\b"                           { pushMultiStateText(yytext()); pushString( '\b' ); }
  "\\t"                           { pushMultiStateText(yytext()); pushString( '\t' ); }
  "\\n"                           { pushMultiStateText(yytext()); pushString( '\n' ); }
  "\\f"                           { pushMultiStateText(yytext()); pushString( '\f' ); }
  "\\r"                           { pushMultiStateText(yytext()); pushString( '\r' ); }
  "\\\""                          { pushMultiStateText(yytext()); pushString( '\"' ); }
  "\\\'"                          { pushMultiStateText(yytext()); pushString( '\'' ); }
  "\\\\"                          { pushMultiStateText(yytext()); pushString( '\\' ); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { pushMultiStateText(yytext());
                                   char val = (char) Integer.parseInt(yytext().substring(2),16);
                        	   pushString(val); }
  \\.                             { pushMultiStateText(yytext()); pushString(yytext().substring(1)); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
}

<STRING_SQ> {
  \'                              { pushMultiStateText(yytext()); yybegin(YYINITIAL); return multiStateSymbol(STRING_LITERAL, getString()); }
  [^\r\n'\\]+                     { pushMultiStateText(yytext()); pushString( yytext() ); }
  "\\b"                           { pushMultiStateText(yytext()); pushString( '\b' ); }
  "\\t"                           { pushMultiStateText(yytext()); pushString( '\t' ); }
  "\\n"                           { pushMultiStateText(yytext()); pushString( '\n' ); }
  "\\f"                           { pushMultiStateText(yytext()); pushString( '\f' ); }
  "\\r"                           { pushMultiStateText(yytext()); pushString( '\r' ); }
  "\\\""                          { pushMultiStateText(yytext()); pushString( '\"' ); }
  "\\\'"                          { pushMultiStateText(yytext()); pushString( '\'' ); }
  "\\\\"                          { pushMultiStateText(yytext()); pushString( '\\' ); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { pushMultiStateText(yytext());
                                   char val = (char) Integer.parseInt(yytext().substring(2),16);
                        	   pushString(val); }
  \\.                             { pushMultiStateText(yytext()); pushString(yytext().substring(1)); }
  {LineTerminator}                { error("Unterminated string at end of line"); }
}


<REGEXP_START> {
  "/"                             { pushMultiStateText(yytext()); pushString(yytext()); yybegin(REGEXP_FIRST); }
}

<REGEXP_FIRST> {
  {RegularExpressionFirstChar}    { pushMultiStateText(yytext()); pushString(yytext()); yybegin(REGEXP_REST); }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

<REGEXP_REST> {
  {RegexpRest}                    { pushMultiStateText(yytext());
                                    pushString(yytext());
                                    yybegin(YYINITIAL);
                                    return multiStateSymbol(REGEXP_LITERAL, getString());
                                  }
  {LineTerminator}                { error("unterminated regular expression at end of line"); }
}

<MXML> {
  "<?"                            { return symbol(LT_QUESTION); }
  "?>"                            { return symbol(QUESTION_GT); }
  {XMLIdentifier}                    { return symbol(IDE, yytext()); }
  {WhiteSpace}                    { pushWhitespace(yytext()); }
  {XmlComment}                    { pushWhitespace(yytext()); }
  \"                              { setMultiStateText(""); yybegin(XML_ATTRIBUTE_VALUE_DQ); clearString(); }
  \'                              { setMultiStateText(""); yybegin(XML_ATTRIBUTE_VALUE_SQ); clearString(); }
  "<"                             { return symbol(LT); }
  "</"                            { return symbol(LT_SLASH); }
  "/>"                            { return symbol(SLASH_GT); }
  ">"                             { setMultiStateText(""); yybegin(XML_TEXT_CONTENT); clearString(); return symbol(GT); }
  ":"                             { return symbol(COLON); }
  "="                             { return symbol(EQ); }
}

<XML_ATTRIBUTE_VALUE_DQ> {
  \"                              { yybegin(MXML);
                                    return multiStateSymbol(STRING_LITERAL, null); }
  [^\r\n\"\\]+                    { pushMultiStateText(unescapeXml(yytext())); }
  "\\b"                           { pushMultiStateText(yytext()); }
  "\\t"                           { pushMultiStateText(yytext()); }
  "\\n"                           { pushMultiStateText(yytext()); }
  "\\f"                           { pushMultiStateText(yytext()); }
  "\\r"                           { pushMultiStateText(yytext()); }
  "\\\""                          { pushMultiStateText(yytext()); }
  "\\\'"                          { pushMultiStateText(yytext()); }
  "\\\\"                          { pushMultiStateText(yytext()); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { pushMultiStateText(yytext()); }
  \\.                             { pushMultiStateText(yytext()); }
  {WhiteSpace}                    { pushWhitespace(yytext()); }
}

<XML_ATTRIBUTE_VALUE_SQ> {
  \'                              { yybegin(MXML);
                                    return multiStateSymbol(STRING_LITERAL, null); }
  [^\r\n'\\]+                     { pushMultiStateText(unescapeXml(yytext())); }
  "\\b"                           { pushMultiStateText(yytext()); }
  "\\t"                           { pushMultiStateText(yytext()); }
  "\\n"                           { pushMultiStateText(yytext()); }
  "\\f"                           { pushMultiStateText(yytext()); }
  "\\r"                           { pushMultiStateText(yytext()); }
  "\\\""                          { pushMultiStateText(yytext()); }
  "\\\'"                          { pushMultiStateText(yytext()); }
  "\\\\"                          { pushMultiStateText(yytext()); }
\\(u{HexDigit}{4}|x{HexDigit}{2}) { pushMultiStateText(yytext()); }
  \\.                             { pushMultiStateText(yytext()); }
  {WhiteSpace}                    { pushWhitespace(yytext()); }
}

<XML_TEXT_CONTENT> {
  "<![CDATA["                     { setMultiStateText(""); yybegin(CDATA_SECTION); clearString(); }
  .|{LineTerminator} / "<"        { pushString(yytext()); yybegin(MXML);
                                    return xmlUnescaped(STRING_LITERAL, getString()); }
  .|{LineTerminator}              { pushString(yytext()); }
}

<CDATA_SECTION> {
  "]]>" / "<"                     { yybegin(MXML);
                                    return multiStateSymbol(STRING_LITERAL, getString()); }
  "]]>"                           { yybegin(XML_TEXT_CONTENT); }
  .|{LineTerminator}              { pushMultiStateText(yytext()); pushString(yytext()); }
}

/* error catchall */
<YYINITIAL,STRING_DQ> .|\n                     { error("unrecognized input token"); }
<REGEXP_START,REGEXP_FIRST,REGEXP_REST> .|\n   { error("invalid regular expression literal"); } //todo be more precise
<VECTOR_TYPE> .|\n                             { error("invalid Vector type"); }
<<EOF>>                           { if (yymoreStreams()) yypopStream(); else return symbol(EOF); }
