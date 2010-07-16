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
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class ImportDirective extends NodeImplBase {

  private static final JooSymbol IMPORT_SYMBOL = new JooSymbol(sym.IMPORT, "import");
  private static final JooSymbol DOT_SYMBOL = new JooSymbol(sym.DOT, ".");

  JooSymbol importKeyword;
  Ide ide;
  private final boolean explicit;

  private static Ide createIde(Ide prefix, JooSymbol symIde) {
    return prefix == null ? new Ide(symIde) : new QualifiedIde(prefix, DOT_SYMBOL, symIde);
  }

  public ImportDirective(Ide packageIde, String typeName) {
    this(IMPORT_SYMBOL, createIde(packageIde, new JooSymbol(typeName)), false);
  }

  public ImportDirective(JooSymbol importKeyword, Ide ide) {
    this(importKeyword, ide, true);
  }

  private ImportDirective(JooSymbol importKeyword, Ide ide, boolean explicit) {
    this.importKeyword = importKeyword;
    this.ide = ide;
    this.explicit = explicit;
  }

  public void scope(final Scope scope) {
    ide.scope(scope);
    scope.addImport(this);
  }

  public String getQualifiedName() {
    return ide.getQualifiedNameStr();
  }

  @Override
  protected void generateAsApiCode(final JsWriter out) throws IOException {
    if (explicit) {
      out.writeSymbol(importKeyword);
      ide.generateCode(out);
    }
    // else skip it
  }


  protected void generateJsCode(JsWriter out) throws IOException {
    if (explicit) {
      out.beginComment();
      out.writeSymbol(importKeyword);
      ide.generateCode(out);
      out.endComment();
    }
  }

  public JooSymbol getSymbol() {
    return importKeyword;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final ImportDirective that = (ImportDirective) o;

    if (explicit != that.explicit) return false;
    if (ide != null ? !ide.equals(that.ide) : that.ide != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = ide != null ? ide.hashCode() : 0;
    result = 31 * result + (explicit ? 1 : 0);
    return result;
  }
}
