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
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Andreas Gawecki
 */
public class PackageDeclaration extends IdeDeclaration  {

  JooSymbol symPackage;
  private List<String> packageImports = new ArrayList<String>();

  public void addPackageImport(String packageName) {
    packageImports.add(packageName);
  }

  public List<String> getPackageImports() {
    return Collections.unmodifiableList(packageImports);
  }

  public PackageDeclaration(JooSymbol symPackage, Ide ide) {
    super(new JooSymbol[0], 0, ide);
    this.symPackage = symPackage;
  }

  public void generateCode(JsWriter out) throws IOException {
    out.beginString();
    out.writeSymbol(symPackage);
    if (ide!=null) {
      ide.generateCode(out);
    }
    out.endString();
    out.write(",");
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    // do *not* call super!
    this.parentNode = parentNode;
    addPackageImport(ide.getQualifiedNameStr());
    return this;
  }

  public JooSymbol getSymbol() {
    return symPackage;
  }

  public boolean isFullyQualifiedIde(AnalyzeContext context, String dotExpr) {
    return packageImports.contains(dotExpr.substring(0, dotExpr.lastIndexOf('.')))
      || context.getScope().findScopeThatDeclares(dotExpr) != null;
  }
}
