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
import net.jangaroo.jooc.sym;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class NamespacedIde extends Ide {

  private JooSymbol namespace;
  private JooSymbol symNamespaceSep;

  public NamespacedIde(JooSymbol namespace, JooSymbol symNamespaceSep, JooSymbol symIde) {
    super(symIde);
    this.namespace = namespace;
    this.symNamespaceSep = symNamespaceSep;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitNamespacedIde(this);
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
  }

  @Override
  public void analyze(AstNode parentNode, AnalyzeContext context) {
    if (namespace.sym == sym.IDE) { // all other symbols should be predefined namespaces like "public" etc.
      Jooc.warning(namespace, "namespaces are not yet implemented, ignoring namespace " + namespace.getText());
    }
    super.analyze(parentNode, context);
  }

  static String getNamespacePrefix(JooSymbol namespace) {
    return ""; // TODO: namespace==null || namespace.sym!=sym.IDE ? "" : namespace.getText()+"::";
  }

  @Override
  public String getName() {
    return getNamespacePrefix(namespace)+super.getName();
  }

  public String[] getQualifiedName() {
    return new String[]{namespace.getText(), getIde().getText()};
  }

  @Override
  public String getQualifiedNameStr() {
    return QualifiedIde.constructQualifiedNameStr(getQualifiedName(), "::");
  }

  public JooSymbol getSymbol() {
    return namespace;
  }

  public JooSymbol getNamespace() {
    return namespace;
  }

  public JooSymbol getSymNamespaceSep() {
    return symNamespaceSep;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    final NamespacedIde that = (NamespacedIde) o;
    return namespace.getText().equals(that.namespace.getText());

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + namespace.getText().hashCode();
    return result;
  }
}