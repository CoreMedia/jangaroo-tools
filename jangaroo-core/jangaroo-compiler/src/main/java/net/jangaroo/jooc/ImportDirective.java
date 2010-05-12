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
import java.util.List;

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
  private boolean used = false;

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

  public boolean isUsed() {
    return used;
  }

  public void wasUsed() {
    used = true;
  }

  @Override
  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    String typeName = ide.getName();
    Scope packageScope = context.getScope();
    Ide packageIde = null;
    String packageName = "";
    if (ide instanceof QualifiedIde) {
      packageIde = ((QualifiedIde) ide).prefix;
      packageName = packageIde.getQualifiedNameStr();
      context.getCurrentPackage().addImport((QualifiedIde) ide);
    }
    if ("*".equals(typeName)) {
      final CompilationUnit compilationUnit = context.getScope().getCompilationUnit();
      final List<String> ides = compilationUnit.getPackageIdes(packageName);
      for (String typeToImport : ides) {
        ImportDirective importDirective = new ImportDirective(packageIde, typeToImport);
        importDirective.analyze(parentNode, context);
        compilationUnit.addImplicitDirective(importDirective);
        //System.out.println("*** adding implicit import " + importDirective.getQualifiedName());
      }
    } else {
      packageScope.declareIde(typeName, this);
      // also add the fully qualified name (might be the same string for top level imports):
      packageScope.declareIde(ide.getQualifiedNameStr(), this);
    }
    return this;
  }

  public String getQualifiedName() {
    return ide.getQualifiedNameStr();
  }

  public void generateCode(JsWriter out) throws IOException {
    if (used || explicit) {
      out.beginString();
      out.writeSymbol(importKeyword);
      ide.generateCode(out);
      out.endString();
      out.writeToken(",");
    }
  }

  public JooSymbol getSymbol() {
    return importKeyword;
  }

}
