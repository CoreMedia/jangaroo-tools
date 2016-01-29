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

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;
import java.util.List;

/**
 * @author Frank Wienberg
 */
public class UseNamespaceDirective extends Directive {

  private JooSymbol useKeyword;
  private JooSymbol namespaceKeyword;
  private Ide namespace;
  private JooSymbol symSemicolon;

  public UseNamespaceDirective(JooSymbol useKeyword, JooSymbol namespaceKeyword, Ide namespace, JooSymbol symSemicolon) {
    this.useKeyword = useKeyword;
    assert SyntacticKeywords.NAMESPACE.equals(namespaceKeyword.getText());
    this.namespaceKeyword = namespaceKeyword;
    this.namespace = namespace;
    this.symSemicolon = symSemicolon;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), namespace);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitUseNamespaceDirective(this);
  }

  @Override
  public void scope(final Scope scope) {
    getNamespace().scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    JangarooParser.warning(getNamespace().getSymbol(), "namespaces are not yet implemented, ignoring use namespace " + getNamespace().getName());
    getNamespace().analyze(this);
    super.analyze(parentNode);
  }

  public JooSymbol getSymbol() {
    return getUseKeyword();
  }

  public JooSymbol getUseKeyword() {
    return useKeyword;
  }

  public JooSymbol getNamespaceKeyword() {
    return namespaceKeyword;
  }

  public Ide getNamespace() {
    return namespace;
  }

  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

}