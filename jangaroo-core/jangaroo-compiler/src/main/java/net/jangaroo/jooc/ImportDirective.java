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
  Type type;
  private final boolean explicit;
  private boolean used = false;

  private static Ide createIde(Ide prefix, JooSymbol symIde) {
    return prefix==null ? new Ide(symIde) : new QualifiedIde(prefix, DOT_SYMBOL, symIde);
  }

  public ImportDirective(Ide packageIde, String typeName) {
    this(IMPORT_SYMBOL, new IdeType(createIde(packageIde, new JooSymbol(typeName))), false);
  }

  public ImportDirective(JooSymbol importKeyword, Type type) {
    this(importKeyword, type, true);
  }

  private ImportDirective(JooSymbol importKeyword, Type type, boolean explicit) {
    this.importKeyword = importKeyword;
    this.type = type;
    this.explicit = explicit;
  }

  public boolean isUsed() {
    return used;
  }

  public void wasUsed(ClassDeclaration classDeclaration) {
    if (!used) {
      if (classDeclaration==null || !classDeclaration.getIde().getQualifiedNameStr().equals(((IdeType)type).getIde().getQualifiedNameStr())) {
        used = true;
      }
    }
  }

  @Override
  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (type instanceof IdeType) {
      Ide ide = ((IdeType)type).ide;
      if (ide instanceof QualifiedIde) {
        context.getCurrentPackage().addImport((QualifiedIde)ide);
      }
      String typeName = ide.getName();
      if (!"*".equals(typeName)) {
        context.getScope().declareIde(typeName, this);
        // also add the fully qualified name (might be the same string for top level imports):
        context.getScope().declareIde(ide.getQualifiedNameStr(), this);
      }
    }
    return this;
  }

  public String getQualifiedName() {
    return ((IdeType)type).ide.getQualifiedNameStr();
  }

  public void generateCode(JsWriter out) throws IOException {
    if (used || explicit) {
      out.beginString();
      out.writeSymbol(importKeyword);
      type.generateCode(out);
      out.endString();
      out.writeToken(",");
    }
  }

  public JooSymbol getSymbol() {
      return importKeyword;
  }

}
