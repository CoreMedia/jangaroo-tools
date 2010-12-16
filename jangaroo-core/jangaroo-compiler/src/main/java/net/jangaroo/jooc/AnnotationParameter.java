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
 * @author Frank Wienberg
 */
class AnnotationParameter extends NodeImplBase {

  Ide optName;
  JooSymbol optSymEq;
  LiteralExpr value;
  private CompilationUnit compilationUnit;

  public AnnotationParameter(Ide optName, JooSymbol optSymEq, LiteralExpr value) {
    this.optName = optName;
    this.optSymEq = optSymEq;
    this.value = value;
  }

  @Override
  public void scope(final Scope scope) {
    if (value != null) {
      // TODO: is value really optional?
      value.scope(scope);
      compilationUnit = scope.getCompilationUnit();
    }
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (value != null) {
      value = (LiteralExpr)value.analyze(this, context);
      String metaName = ((CommaSeparatedList)parentNode).parentNode.getSymbol().getText();
      if ("Embed".equals(metaName) && optName != null && "source".equals(optName.getName())) {
        JooSymbol valueSymbol = value.getSymbol();
        String text = valueSymbol.getText();
        String quote = text.substring(0, 1);
        String source = (String)valueSymbol.getJooValue();
        String absoluteSource = compilationUnit.addResourceDependency(source);
        value.value = new JooSymbol(valueSymbol.sym, valueSymbol.getFileName(),
          valueSymbol.getLine(), valueSymbol.getColumn(), valueSymbol.getWhitespace(),
          quote + absoluteSource + quote,
          absoluteSource);
      }
    }
    return this;
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    if (optName != null && optSymEq != null) {
      optName.generateCode(out);
      out.writeSymbol(optSymEq);
    }
    value.generateCode(out);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    if (optName != null && optSymEq != null) {
      optName.generateCode(out);
      out.writeSymbolWhitespace(optSymEq);
    } else {
      out.writeToken("$value");
    }
    out.writeToken(":");
    value.generateCode(out);
  }

  public JooSymbol getSymbol() {
    return optName == null ? value.getSymbol() : optName.getSymbol();
  }


}
