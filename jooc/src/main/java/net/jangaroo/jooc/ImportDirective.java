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
 */
public class ImportDirective extends NodeImplBase {

  JooSymbol importKeyword;
  Type type;
  JooSymbol semicolon;

  public ImportDirective(JooSymbol importKeyword, Type type, JooSymbol semicolon) {
    this.importKeyword = importKeyword;
    this.type = type;
    this.semicolon = semicolon;
  }

  @Override
  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (type instanceof IdeType) {
      Ide ide = ((IdeType)type).ide;
      String typeName = ide.getName();
      if ("*".equals(typeName)) {
        // found *-import, do not register, but add to package import list:
        String packageName = QualifiedIde.constructQualifiedNameStr(((QualifiedIde)ide).prefix.getQualifiedName());
        context.getCurrentPackage().addPackageImport(packageName);
      } else {
        context.getScope().declareIde(typeName, this);
        // also add the fully qualified name (might be the same string for top level imports):
        context.getScope().declareIde(QualifiedIde.constructQualifiedNameStr(ide.getQualifiedName()), this);
      }
    }
  }

  public void generateCode(JsWriter out) throws IOException {
    // just as comment yet:
    out.writeSymbolWhitespace(importKeyword);
    out.write("\"");
    out.writeSymbolToken(importKeyword);
    type.generateCode(out);
    out.write("\"");
    out.writeSymbolWhitespace(semicolon);
    out.write(",");
  }

  public JooSymbol getSymbol() {
      return importKeyword;
  }

}
