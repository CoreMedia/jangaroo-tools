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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class UseNamespaceDirective extends Directive {

  private JooSymbol useKeyword;
  private JooSymbol namespaceKeyword;
  private Ide namespace;
  private JooSymbol symSemicolon;

  public UseNamespaceDirective(JooSymbol useKeyword, JooSymbol namespaceKeyword, Ide namespace, JooSymbol symSemicolon) {
    this.setUseKeyword(useKeyword);
    assert SyntacticKeywords.NAMESPACE.equals(namespaceKeyword.getText());
    this.setNamespaceKeyword(namespaceKeyword);
    this.setNamespace(namespace);
    this.setSymSemicolon(symSemicolon);
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitUseNamespaceDirective(this);
  }

  @Override
  public void scope(final Scope scope) {
    getNamespace().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    Jooc.warning(getNamespace().getSymbol(), "namespaces are not yet implemented, ignoring use namespace " + getNamespace().getName());
    getNamespace().analyze(this, context);
    super.analyze(parentNode, context);
  }

  @Override
  public void generateAsApiCode(JsWriter out) throws IOException {
    // no API code generated!
  }

  public void generateJsCode(JsWriter out) throws IOException {
    out.beginComment();
    out.writeSymbol(getUseKeyword());
    out.writeSymbol(getNamespaceKeyword());
    getNamespace().generateJsCode(out);
    out.writeSymbol(getSymSemicolon());
    out.endComment();
  }

  public JooSymbol getSymbol() {
      return getUseKeyword();
  }

  public JooSymbol getUseKeyword() {
    return useKeyword;
  }

  public void setUseKeyword(JooSymbol useKeyword) {
    this.useKeyword = useKeyword;
  }

  public JooSymbol getNamespaceKeyword() {
    return namespaceKeyword;
  }

  public void setNamespaceKeyword(JooSymbol namespaceKeyword) {
    this.namespaceKeyword = namespaceKeyword;
  }

  public Ide getNamespace() {
    return namespace;
  }

  public void setNamespace(Ide namespace) {
    this.namespace = namespace;
  }

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

  public void setSymSemicolon(JooSymbol symSemicolon) {
    this.symSemicolon = symSemicolon;
  }
}