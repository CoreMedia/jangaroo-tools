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

  JooSymbol[] symModifiers;

  protected Node parentDeclaration = null;
  protected ClassDeclaration classDeclaration = null;

  private int modifiers = -1;
  protected int allowedModifiers = -1;

  protected static final int MODIFIER_PUBLIC = 1;
  protected static final int MODIFIER_PROTECTED = 2*MODIFIER_PUBLIC;
  protected static final int MODIFIER_PRIVATE = 2*MODIFIER_PROTECTED;
  protected static final int MODIFIER_INTERNAL = 2*MODIFIER_PRIVATE;
  protected static final int MODIFIER_STATIC = 2*MODIFIER_INTERNAL;
  protected static final int MODIFIER_ABSTRACT = 2*MODIFIER_STATIC;
  protected static final int MODIFIER_FINAL = 2*MODIFIER_ABSTRACT;
  protected static final int MODIFIER_OVERRIDE = 2*MODIFIER_FINAL;

  protected static final int MODIFIERS_SCOPE =
    MODIFIER_PRIVATE|MODIFIER_PROTECTED|MODIFIER_PUBLIC|MODIFIER_INTERNAL;

  protected Declaration(JooSymbol[] modifiers, int allowedModifiers) {
    this.symModifiers = modifiers;
    this.allowedModifiers = allowedModifiers;
  }

  public Node getParentDeclaration() {
    return parentDeclaration;
  }

  protected void computeModifiers() {
    modifiers = 0;
    for (int i = 0; i < symModifiers.length; i++) {
      JooSymbol modifier = symModifiers[i];
      int flag = 0;
      switch (modifier.sym) {
        case sym.PUBLIC: flag = MODIFIER_PUBLIC; break;
        case sym.PROTECTED: flag = MODIFIER_PROTECTED; break;
        case sym.PRIVATE: flag = MODIFIER_PRIVATE; break;
        case sym.INTERNAL: flag = MODIFIER_INTERNAL; break;
        case sym.STATIC: flag = MODIFIER_STATIC; break;
        case sym.ABSTRACT: flag = MODIFIER_ABSTRACT; break;
        case sym.FINAL: flag = MODIFIER_FINAL; break;
        case sym.OVERRIDE: flag = MODIFIER_OVERRIDE; break;
        default: Jooc.error(modifier, "internal compiler error: invalid modifier '" +  modifier.getText() + "'");
      }
      if ((allowedModifiers & flag) == 0)
        Jooc.error(modifier, "modifier '" +  modifier.getText() + "' not allowed here");
      if ((flag & modifiers) != 0)
        Jooc.error(modifier, "duplicate modifier '" +  modifier.getText() + "'");
      if ((flag & MODIFIERS_SCOPE) != 0 && (modifiers & MODIFIERS_SCOPE) != 0)
        Jooc.error(modifier, "duplicate scope modifier '" +  modifier.getText() + "'");
      modifiers |= flag;
    }
  }

  protected int getModifiers() {
    return modifiers;
  }

  public boolean isPublic() {
    return (modifiers & MODIFIER_PUBLIC) != 0;
  }

  public boolean isProtected() {
    return (modifiers & MODIFIER_PROTECTED) != 0;
  }

  public boolean isPrivate() {
    return (modifiers & MODIFIER_PRIVATE) != 0;
  }

  public boolean isStatic() {
    return (modifiers & MODIFIER_STATIC) != 0;
  }

  public boolean isAbstract() {
    return (modifiers & MODIFIER_ABSTRACT) != 0;
  }

  public boolean isFinal() {
    return (modifiers & MODIFIER_FINAL) != 0;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  protected void writeModifiers(JsWriter out) throws IOException {
    for (int i = 0; i < symModifiers.length; i++) {
      JooSymbol modifier = symModifiers[i];
      out.writeSymbol(modifier);
    }
  }

  protected void writeRuntimeModifiers(JsWriter out) throws IOException {
    if (writeRuntimeModifiersUnclosed(out)) {
      out.write("\",");
    }
  }

  protected boolean writeRuntimeModifiersUnclosed(JsWriter out) throws IOException {
    if (symModifiers.length>0) {
      out.writeSymbolWhitespace(symModifiers[0]);
      out.write('"');
      for (int i = 0; i < symModifiers.length; i++) {
        JooSymbol modifier = symModifiers[i];
        if (i==0)
          out.writeSymbolToken(modifier);
        else
          out.writeSymbol(modifier);
      }
      return true;
    }
    return false;
  }

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    parentDeclaration = context.getScope().getDeclaration();
    classDeclaration = context.getCurrentClass();
    computeModifiers();
  }

}
