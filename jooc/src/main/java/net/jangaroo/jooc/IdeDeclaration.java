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

import java.util.regex.Pattern;


/**
 * @author Andreas Gawecki
 */
public abstract class IdeDeclaration extends Declaration {

  private static Pattern PRIVATE_MEMBER_NAME = Pattern.compile("^[$](\\p{Alpha}|[_$])(\\p{Alnum}|[_$])*$");
  Ide ide;

  protected IdeDeclaration(JooSymbol[] modifiers, int allowedModifiers, Ide ide) {
    super(modifiers, allowedModifiers);
    this.ide = ide;
    if (PRIVATE_MEMBER_NAME.matcher(ide.getName()).matches()) {
      Jooc.error(ide, "Jangaroo identifier must not be an ActionScript identifier prefixed with a dollar sign ('$'): "+ide.getName());
    }
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymbol() {
    return ide.getSymbol();
  }

  public String getName() {
    return ide.getName();
  }

  public String[] getQualifiedName() {
    IdeDeclaration parentDeclaration = getParentDeclaration();
    if (parentDeclaration == null)
      return getIde().getQualifiedName();
    else {
      String[] prefixName = parentDeclaration.getQualifiedName();
      String[] result = new String[prefixName.length+1];
      System.arraycopy(prefixName, 0, result, 0, prefixName.length);
      result[prefixName.length] = ide.getName();
      return result;
    }
  }

  protected static String toPath(String[] qn) {
    StringBuffer result = new StringBuffer(20);
    for (int i = 0; i < qn.length; i++) {
      if (i > 0)
        result.append('.');
      result.append(qn[i]);
    }
    return result.toString();
  }

  public String getPath() {
    return toPath(getQualifiedName());
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
  }

}
