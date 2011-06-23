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

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.AnalyzeContext;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class AnnotationParameter extends NodeImplBase {

  private Ide optName;
  private JooSymbol optSymEq;
  private LiteralExpr value;
  private CompilationUnit compilationUnit;
  private Annotation parentAnnotation;

  public AnnotationParameter(Ide optName, JooSymbol optSymEq, LiteralExpr value) {
    this.setOptName(optName);
    this.setOptSymEq(optSymEq);
    this.setValue(value);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitAnnotationParameter(this);
  }

  public Annotation getParentAnnotation() {
    return parentAnnotation;
  }

  public void setParentAnnotation(Annotation parentAnnotation) {
    this.parentAnnotation = parentAnnotation;
  }

  @Override
  public void scope(final Scope scope) {
    if (getValue() != null) {
      // TODO: is value really optional?
      getValue().scope(scope);
      compilationUnit = scope.getCompilationUnit();
    }
  }

  public void analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (getValue() != null) {
      getValue().analyze(this, context);
      String metaName = parentAnnotation.getMetaName();
      if ("Embed".equals(metaName) && getOptName() != null && "source".equals(getOptName().getName())) {
        JooSymbol valueSymbol = getValue().getSymbol();
        String text = valueSymbol.getText();
        String quote = text.substring(0, 1);
        String source = (String)valueSymbol.getJooValue();
        String absoluteSource = compilationUnit.addResourceDependency(source);
        getValue().setValue(new JooSymbol(valueSymbol.sym, valueSymbol.getFileName(),
          valueSymbol.getLine(), valueSymbol.getColumn(), valueSymbol.getWhitespace(),
          quote + absoluteSource + quote,
          absoluteSource));
      }
    }
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    if (getOptName() != null && getOptSymEq() != null) {
      getOptName().generateAsApiCode(out);
      out.writeSymbol(getOptSymEq());
    }
    getValue().generateAsApiCode(out);
  }

  public void generateJsCode(JsWriter out) throws IOException {
    if (getOptName() != null && getOptSymEq() != null) {
      getOptName().generateJsCode(out);
      out.writeSymbolWhitespace(getOptSymEq());
    } else {
      out.writeToken("$value");
    }
    out.writeToken(":");
    getValue().generateJsCode(out);
  }

  public JooSymbol getSymbol() {
    return getOptName() == null ? getValue().getSymbol() : getOptName().getSymbol();
  }


  public Ide getOptName() {
    return optName;
  }

  public void setOptName(Ide optName) {
    this.optName = optName;
  }

  public JooSymbol getOptSymEq() {
    return optSymEq;
  }

  public void setOptSymEq(JooSymbol optSymEq) {
    this.optSymEq = optSymEq;
  }

  public LiteralExpr getValue() {
    return value;
  }

  public void setValue(LiteralExpr value) {
    this.value = value;
  }
}
