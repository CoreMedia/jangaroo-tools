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
package net.jangaroo.jooc;

import java.io.IOException;

/**
 * An annotation (square bracket meta data).
 *
 * @author Frank Wienberg
 */
public class Annotation extends Directive {

  JooSymbol leftBracket;
  Ide ide;
  JooSymbol optLeftParen;
  CommaSeparatedList<AnnotationParameter> optAnnotationParameters;
  JooSymbol optRightParen;
  JooSymbol rightBracket;

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol rightBracket) {
    this(leftBracket, ide, null, null, null, rightBracket);
  }

  public Annotation(JooSymbol leftBracket, Ide ide, JooSymbol optLeftParen, CommaSeparatedList<AnnotationParameter> optAnnotationParameters, JooSymbol optRightParen, JooSymbol optRightBracket) {
    this.leftBracket = leftBracket;
    this.ide = ide;
    this.optLeftParen = optLeftParen;
    this.optRightParen = optRightParen;
    this.optAnnotationParameters = optAnnotationParameters;
    this.rightBracket = optRightBracket;
    for (CommaSeparatedList<AnnotationParameter> params = optAnnotationParameters; params != null; params = params.tail) {
      params.head.setParentAnnotation(this);

    }
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitAnnotation(this);
  }

  @Override
  public void scope(final Scope scope) {
    ide.scope(scope);
    if (optAnnotationParameters != null) {
      optAnnotationParameters.scope(scope);
    }
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (optAnnotationParameters != null) {
      optAnnotationParameters.analyze(this, context);
    }
  }

  public JooSymbol getSymbol() {
    return ide.getSymbol();
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.writeSymbolWhitespace(leftBracket);
    out.writeToken("{");
    ide.generateCode(out);
    out.writeToken(":");
    if (optLeftParen != null) {
      out.writeSymbolWhitespace(optLeftParen);
    }
    out.writeToken("{");
    if (optAnnotationParameters != null) {
      optAnnotationParameters.generateCode(out);
    }
    if (optRightParen != null) {
      out.writeSymbolWhitespace(optRightParen);
    }
    out.writeToken("}");
    out.writeSymbolWhitespace(rightBracket);
    out.writeToken("},");
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    out.writeSymbol(leftBracket);
    ide.generateCode(out);
    if (optLeftParen != null) {
      out.writeSymbol(optLeftParen);
    }
    if (optAnnotationParameters != null) {
      optAnnotationParameters.generateCode(out);
    }
    if (optRightParen != null) {
      out.writeSymbol(optRightParen);
    }
    out.writeSymbol(rightBracket);
  }

  public String getMetaName() {
    return getSymbol().getText();
  }
}
