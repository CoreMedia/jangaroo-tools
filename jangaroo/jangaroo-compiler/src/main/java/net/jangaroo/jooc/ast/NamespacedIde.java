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
import net.jangaroo.jooc.sym;

import java.io.IOException;

/**
 * @author Frank Wienberg
 */
public class NamespacedIde extends Ide {

  private Ide namespace;
  private JooSymbol symNamespaceSep;
  private String qualifiedNameStr;

  public NamespacedIde(JooSymbol namespace, JooSymbol symNamespaceSep, JooSymbol symIde) {
    super(symIde);
    this.namespace = new Ide(namespace);
    this.symNamespaceSep = symNamespaceSep;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitNamespacedIde(this);
  }

  @Override
  public void analyze(AstNode parentNode) {
    if (namespace.getIde().sym == sym.IDE) { // all other symbols should be predefined namespaces like "public" etc.
      JangarooParser.warning(namespace.getSymbol(), "namespaces are not yet implemented, ignoring namespace " + namespace.getQualifiedNameStr());
    }
    super.analyze(parentNode);
  }

  public String[] getQualifiedName() {
    return new String[]{namespace.getQualifiedNameStr(), getIde().getText()};
  }

  @Override
  public String getQualifiedNameStr() {
    if (null == qualifiedNameStr) {
      qualifiedNameStr = QualifiedIde.constructQualifiedNameStr(getQualifiedName(), "::");
    }
    return qualifiedNameStr;
  }

  public JooSymbol getSymbol() {
    return namespace.getSymbol();
  }

  public Ide getNamespace() {
    return namespace;
  }

  public JooSymbol getSymNamespaceSep() {
    return symNamespaceSep;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    final NamespacedIde that = (NamespacedIde) o;
    return namespace.getQualifiedNameStr().equals(that.namespace.getQualifiedNameStr());

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + namespace.getQualifiedNameStr().hashCode();
    return result;
  }
}