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

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.sym;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ImportDirective extends Directive {

  private static final JooSymbol IMPORT_SYMBOL = new JooSymbol(sym.IMPORT, "import");
  private static final JooSymbol DOT_SYMBOL = new JooSymbol(sym.DOT, ".");

  private JooSymbol importKeyword;
  private Ide ide;

  private JooSymbol symSemicolon;

  private final boolean explicit;

  public ImportDirective(Ide packageIde, String typeName) {
    this(IMPORT_SYMBOL, createIde(packageIde, new JooSymbol(typeName)), false);
  }

  public ImportDirective(JooSymbol importKeyword, Ide ide, JooSymbol symSemicolon) {
    this(importKeyword, ide, true);
    this.symSemicolon = symSemicolon;
  }

  private ImportDirective(JooSymbol importKeyword, Ide ide, boolean explicit) {
    this.importKeyword = importKeyword;
    this.ide = ide;
    this.explicit = explicit;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), ide);
  }

  private static Ide createIde(Ide prefix, JooSymbol symIde) {
    return prefix == null ? new Ide(symIde) : new QualifiedIde(prefix, DOT_SYMBOL, symIde);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitImportDirective(this);
  }

  public void scope(final Scope scope) {
    getIde().scope(scope);
    scope.addImport(this);
  }

  public String getQualifiedName() {
    return getIde().getQualifiedNameStr();
  }

  public JooSymbol getSymbol() {
    return getImportKeyword();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ImportDirective that = (ImportDirective) o;

    if (isExplicit() != that.isExplicit()) {
      return false;
    }
    if (getIde() != null ? !getIde().equals(that.getIde()) : that.getIde() != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = getIde() != null ? getIde().hashCode() : 0;
    result = 31 * result + (isExplicit() ? 1 : 0);
    return result;
  }

  public JooSymbol getImportKeyword() {
    return importKeyword;
  }

  public Ide getIde() {
    return ide;
  }

  /**
   * null if not explicit
   * @return the semicolon symbol
   */
  public JooSymbol getSymSemicolon() {
    return symSemicolon;
  }

  public boolean isExplicit() {
    return explicit;
  }
}
