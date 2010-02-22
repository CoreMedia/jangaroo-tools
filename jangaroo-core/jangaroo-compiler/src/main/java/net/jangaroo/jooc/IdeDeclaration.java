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
    if (ide!=null && PRIVATE_MEMBER_NAME.matcher(ide.getName()).matches()) {
      Jooc.warning(ide.getSymbol(), "Jangaroo identifier must not be an ActionScript identifier prefixed with a dollar sign ('$').");
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
    Node parentDeclaration = getParentDeclaration();
    if (!(parentDeclaration instanceof IdeDeclaration)) {
      return getIde() == null ? new String[0] : getIde().getQualifiedName();
    } else {
      String[] prefixName = ((IdeDeclaration)parentDeclaration).getQualifiedName();
      String[] result = new String[prefixName.length+1];
      System.arraycopy(prefixName, 0, result, 0, prefixName.length);
      result[prefixName.length] = ide.getName();
      return result;
    }
  }

  public String getQualifiedNameStr() {
    return QualifiedIde.constructQualifiedNameStr(getQualifiedName(), ".");
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    Node oldNode = context.getScope().declareIde(getName(), this);
    if (oldNode!=null) {
      handleDuplicateDeclaration(context, oldNode);
    }
    return this;
  }

  void handleDuplicateDeclaration(AnalyzeContext context, Node oldNode) {
    String msg = "Duplicate declaration of identifier '" + getName() + "'";
    if (allowDuplicates(context)) {
      Jooc.warning(getSymbol(), msg);
    } else {
      throw Jooc.error(getSymbol(), msg);
    }
  }

  boolean allowDuplicates(AnalyzeContext context) {
    return false;
  }

}
