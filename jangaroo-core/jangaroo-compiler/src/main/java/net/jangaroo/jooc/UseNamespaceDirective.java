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
public class UseNamespaceDirective extends Directive {

  JooSymbol useKeyword;
  JooSymbol namespaceKeyword;
  Ide namespace;
  JooSymbol symSemicolon;

  public UseNamespaceDirective(JooSymbol useKeyword, JooSymbol namespaceKeyword, Ide namespace, JooSymbol symSemicolon) {
    this.useKeyword = useKeyword;
    assert SyntacticKeywords.NAMESPACE.equals(namespaceKeyword.getText());
    this.namespaceKeyword = namespaceKeyword;
    this.namespace = namespace;
    this.symSemicolon = symSemicolon;
  }

  @Override
  public void scope(final Scope scope) {
    namespace.scope(scope);
  }

  @Override
  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    Jooc.warning(namespace.getSymbol(), "namespaces are not yet implemented, ignoring use namespace " + namespace.getName());
    namespace.analyze(this, context);
    return super.analyze(parentNode, context);
  }

  @Override
  protected void generateAsApiCode(JsWriter out) throws IOException {
    // no API code generated!
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.beginComment();
    out.writeSymbol(useKeyword);
    out.writeSymbol(namespaceKeyword);
    namespace.generateCode(out);
    out.writeSymbol(symSemicolon);
    out.endComment();
  }

  public JooSymbol getSymbol() {
      return useKeyword;
  }

}