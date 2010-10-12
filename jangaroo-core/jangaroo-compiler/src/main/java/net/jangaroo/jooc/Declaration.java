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
abstract class Declaration extends NodeImplBase {

  public JooSymbol[] getSymModifiers() {
    return symModifiers;
  }

  protected JooSymbol[] symModifiers;
  protected JooSymbol[] symInheritedModifiers = new JooSymbol[0];

  protected AstNode parentDeclaration = null;
  protected ClassDeclaration classDeclaration = null;

  private int modifiers = -1;
  protected int allowedModifiers = -1;

  protected static final int MODIFIER_PUBLIC = 1;
  protected static final int MODIFIER_PROTECTED = 2;
  protected static final int MODIFIER_PRIVATE = 2 * MODIFIER_PROTECTED;
  protected static final int MODIFIER_INTERNAL = 2 * MODIFIER_PRIVATE;
  protected static final int MODIFIER_STATIC = 2 * MODIFIER_INTERNAL;
  protected static final int MODIFIER_ABSTRACT = 2 * MODIFIER_STATIC;
  protected static final int MODIFIER_FINAL = 2 * MODIFIER_ABSTRACT;
  protected static final int MODIFIER_OVERRIDE = 2 * MODIFIER_FINAL;
  protected static final int MODIFIER_DYNAMIC = 2 * MODIFIER_OVERRIDE;
  protected static final int MODIFIER_NAMESPACE = 2 * MODIFIER_DYNAMIC;
  protected static final int MODIFIER_NATIVE = 2 * MODIFIER_NAMESPACE;
  protected static final int MODIFIER_VIRTUAL = 2 * MODIFIER_NATIVE;

  protected static final int MODIFIERS_SCOPE =
      MODIFIER_PRIVATE | MODIFIER_PROTECTED | MODIFIER_PUBLIC | MODIFIER_INTERNAL | MODIFIER_NAMESPACE;

  protected Declaration(JooSymbol[] modifiers, int allowedModifiers) {
    this.symModifiers = modifiers.clone();
    this.allowedModifiers = allowedModifiers;
    computeModifiers();
  }

  public AstNode getParentDeclaration() {
    return parentDeclaration;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  protected void setInheritedModifiers(final JooSymbol[] modifiers) {
    symInheritedModifiers = modifiers.clone();
    computeModifiers();
  }

  protected void computeModifiers() {
    modifiers = 0;
    for (JooSymbol modifier : symModifiers) {
      computeModifier(modifier);
    }
    for (JooSymbol modifier : symInheritedModifiers) {
      computeModifier(modifier);
    }
  }

  private void computeModifier(final JooSymbol modifier) {
    int flag = getModifierFlag(modifier);
    if ((allowedModifiers & flag) == 0) {
      throw Jooc.error(modifier, "modifier '" + modifier.getText() + "' not allowed here");
    }
    if ((flag & modifiers) != 0) {
      throw Jooc.error(modifier, "duplicate modifier '" + modifier.getText() + "'");
    }
    if ((flag & MODIFIERS_SCOPE) != 0 && (modifiers & MODIFIERS_SCOPE) != 0) {
      throw Jooc.error(modifier, "duplicate scope modifier '" + modifier.getText() + "'");
    }
    modifiers |= flag;
  }

  protected int getModifierFlag(JooSymbol modifier) {
    switch (modifier.sym) {
      case sym.PUBLIC:
        return MODIFIER_PUBLIC;
      case sym.PROTECTED:
        return MODIFIER_PROTECTED;
      case sym.PRIVATE:
        return MODIFIER_PRIVATE;
      case sym.INTERNAL:
        return MODIFIER_INTERNAL;
      case sym.IDE:
        return
            modifier.getText().equals(SyntacticKeywords.DYNAMIC) ? MODIFIER_DYNAMIC
                : modifier.getText().equals(SyntacticKeywords.STATIC) ? MODIFIER_STATIC
                : modifier.getText().equals(SyntacticKeywords.FINAL) ? MODIFIER_FINAL
                : modifier.getText().equals(SyntacticKeywords.NATIVE) ? MODIFIER_NATIVE
                : modifier.getText().equals(SyntacticKeywords.OVERRIDE) ? MODIFIER_OVERRIDE
                : modifier.getText().equals(SyntacticKeywords.VIRTUAL) ? MODIFIER_VIRTUAL
                : MODIFIER_NAMESPACE;

    }
    throw Jooc.error(modifier, "internal compiler error: invalid modifier '" + modifier.getText() + "'");
  }

  protected int getModifiers() {
    return modifiers;
  }

  public boolean isPublic() {
    return (getModifiers() & MODIFIER_PUBLIC) != 0;
  }

  public boolean isProtected() {
    return (getModifiers() & MODIFIER_PROTECTED) != 0;
  }

  public boolean isPrivate() {
    return (getModifiers() & MODIFIER_PRIVATE) != 0;
  }

  public boolean isOverride() {
    return (getModifiers() & MODIFIER_OVERRIDE) != 0;
  }

  public boolean isPrivateStatic() {
    return isPrivate() && isStatic();
  }

  public boolean isStatic() {
    return (getModifiers() & MODIFIER_STATIC) != 0;
  }

  public boolean isAbstract() {
    return (getModifiers() & MODIFIER_ABSTRACT) != 0;
  }

  public boolean isFinal() {
    return (getModifiers() & MODIFIER_FINAL) != 0;
  }

  public boolean isNative() {
    return (getModifiers() & MODIFIER_NATIVE) != 0;
  }

  protected void writeModifiers(JsWriter out) throws IOException {
    for (JooSymbol modifier : symModifiers) {
      out.writeSymbol(modifier);
    }
  }


  @Override
  public void scope(final Scope scope) {
    parentDeclaration = scope.getDefiningNode();
    classDeclaration = scope.getClassDeclaration();
  }

}
