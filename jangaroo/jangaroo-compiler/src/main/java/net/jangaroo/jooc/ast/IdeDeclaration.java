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
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;

import java.util.regex.Pattern;


/**
 * @author Andreas Gawecki
 */
public abstract class IdeDeclaration extends Declaration {

  private static Pattern PRIVATE_MEMBER_NAME = Pattern.compile("[^$]\\$[0-9]+$");

  private Ide ide;

  protected IdeDeclaration(JooSymbol[] modifiers, Ide ide) {
    super(modifiers);
    this.setIde(ide);
    if (ide != null && PRIVATE_MEMBER_NAME.matcher(ide.getName()).matches()) {
      Jooc.warning(ide.getSymbol(), "Jangaroo identifier must not be an ActionScript identifier postfixed with a dollar sign ('$') followed by a number.");
    }
  }

  protected IdeDeclaration(Ide ide) {
    this(new JooSymbol[0], ide);
  }

  public Ide getIde() {
    return ide;
  }

  public JooSymbol getSymbol() {
    return getIde().getSymbol();
  }

  public String getName() {
    return getIde() == null ? "" : getIde().getName();
  }

  public String[] getQualifiedName() {
    AstNode parentDeclaration = getParentDeclaration();
    if (!(parentDeclaration instanceof IdeDeclaration)) {
      return getIde() == null ? new String[0] : getIde().getQualifiedName();
    } else {
      String[] prefixName = ((IdeDeclaration) parentDeclaration).getQualifiedName();
      String[] result = new String[prefixName.length + 1];
      System.arraycopy(prefixName, 0, result, 0, prefixName.length);
      result[prefixName.length] = getIde().getName();
      return result;
    }
  }

  public String getQualifiedNameStr() {
    return QualifiedIde.constructQualifiedNameStr(getQualifiedName(), ".");
  }

  @Override
  public void scope(final Scope scope) {
    super.scope(scope);
    if (getIde() != null) {
      getIde().scope(scope);
      AstNode oldNode = scope.declareIde(this);
      if (oldNode != null) {
        handleDuplicateDeclaration(scope, oldNode);
      }
    }
  }

  public void handleDuplicateDeclaration(Scope scope, AstNode oldNode) {
    String msg = "Duplicate declaration of identifier '" + getName() + "'";
    if (allowDuplicates(scope)) {
      Jooc.warning(getSymbol(), msg);
    } else {
      throw Jooc.error(getSymbol(), msg);
    }
  }

  boolean allowDuplicates(Scope scope) {
    return false;
  }

  public boolean isField() {
    return false;
  }

  public boolean isMethod() {
    return false;
  }

  public boolean isConstructor() {
    return false;
  }

  public boolean isPrivateStaticMethod() {
    return isPrivate() && isStatic() && isMethod();
  }

  /**
   * Resolve this declaration to the underlying Class or PredefinedType declaration
   */
  public IdeDeclaration resolveDeclaration() {
    return null;
  }

  public IdeDeclaration resolvePropertyDeclaration(String ide) {
    return null;
  }

  @Override
  public String toString() {
    return getQualifiedNameStr();
  }

  public boolean isPrimaryDeclaration() {
    return getIde() != null &&
            getIde().getScope() != null &&
            getIde().getScope().getCompilationUnit() != null &&
            this == getIde().getScope().getCompilationUnit().getPrimaryDeclaration();
  }

  public void setIde(Ide ide) {
    this.ide = ide;
  }
}
