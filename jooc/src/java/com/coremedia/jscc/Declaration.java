/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.IOException;

abstract class Declaration extends NodeImplBase {

  public JscSymbol[] getSymModifiers() {
    return symModifiers;
  }

  JscSymbol[] symModifiers;

  protected IdeDeclaration parentDeclaration = null;
  protected ClassDeclaration classDeclaration = null;

  private int modifiers = -1;
  protected int allowedModifiers = -1;

  protected static final int MODIFIER_PUBLIC = 1;
  protected static final int MODIFIER_PROTECTED = 2*MODIFIER_PUBLIC;
  protected static final int MODIFIER_PRIVATE = 2*MODIFIER_PROTECTED;
  protected static final int MODIFIER_STATIC = 2*MODIFIER_PRIVATE;
  protected static final int MODIFIER_ABSTRACT = 2*MODIFIER_STATIC;
  protected static final int MODIFIER_FINAL = 2*MODIFIER_ABSTRACT;
  protected static final int MODIFIER_OVERRIDE = 2*MODIFIER_FINAL;

  protected static final int MODIFIERS_SCOPE = MODIFIER_PRIVATE|MODIFIER_PROTECTED|MODIFIER_PUBLIC;

  protected Declaration(JscSymbol[] modifiers, int allowedModifiers) {
    int test = 0 | MODIFIER_ABSTRACT | MODIFIER_STATIC;
    this.symModifiers = modifiers;
    this.allowedModifiers = allowedModifiers;
  }

    public IdeDeclaration getParentDeclaration() {
    return parentDeclaration;
  }

  protected void computeModifiers() {
    modifiers = 0;
    for (int i = 0; i < symModifiers.length; i++) {
      JscSymbol modifier = symModifiers[i];
      int flag = 0;
      switch (modifier.sym) {
        case sym.PUBLIC: flag = MODIFIER_PUBLIC; break;
        case sym.PROTECTED: flag = MODIFIER_PROTECTED; break;
        case sym.PRIVATE: flag = MODIFIER_PRIVATE; break;
        case sym.STATIC: flag = MODIFIER_STATIC; break;
        case sym.ABSTRACT: flag = MODIFIER_ABSTRACT; break;
        case sym.FINAL: flag = MODIFIER_FINAL; break;
        case sym.OVERRIDE: flag = MODIFIER_OVERRIDE; break;
        default: Jscc.error(modifier, "internal compiler error: invalid modifier '" +  modifier.getText() + "'");
      }
      if ((allowedModifiers & flag) == 0)
        Jscc.error(modifier, "modifier '" +  modifier.getText() + "' not allowed here");
      if ((flag & modifiers) != 0)
        Jscc.error(modifier, "duplicate modifier '" +  modifier.getText() + "'");
      if ((flag & MODIFIERS_SCOPE) != 0 && (modifiers & MODIFIERS_SCOPE) != 0)
        Jscc.error(modifier, "duplicate scope modifier '" +  modifier.getText() + "'");
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
      JscSymbol modifier = symModifiers[i];
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
        JscSymbol modifier = symModifiers[i];
        if (i==0)
          out.writeSymbolToken(modifier);
        else
          out.writeSymbol(modifier);
      }
      return true;
    }
    return false;
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    parentDeclaration = context.getScope().getDeclaration();
    classDeclaration = context.getCurrentClass();
    computeModifiers();
  }

}
