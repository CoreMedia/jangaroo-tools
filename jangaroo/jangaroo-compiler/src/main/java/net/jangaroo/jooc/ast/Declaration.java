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

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.SyntacticKeywords;
import net.jangaroo.jooc.sym;

import java.util.List;

/**
 * Declarations are statements used to define entities such as variables, functions, classes,
 * and interfaces.
 * <p/>
 * todo rename to Definition (ECMAScript/Adobe speak)
 *
 * @author Andreas Gawecki
 */
public abstract class Declaration extends Statement {
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

  private List<Annotation> annotations;

  private JooSymbol[] symModifiers;
  private JooSymbol[] symInheritedModifiers = new JooSymbol[0];

  private AstNode parentDeclaration = null;
  private ClassDeclaration classDeclaration = null;
  private CompilationUnit compilationUnit;

  private int modifiers = -1;

  protected Declaration(List<Annotation> annotations, JooSymbol[] modifiers) {
    this.annotations = annotations;
    this.symModifiers = modifiers.clone();
    computeModifiers();
  }

  public AstNode getParentDeclaration() {
    return parentDeclaration;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  protected void setInheritedModifiers(final JooSymbol[] modifiers) {
    setSymInheritedModifiers(modifiers);
    computeModifiers();
  }

  protected void computeModifiers() {
    modifiers = 0;
    for (JooSymbol modifier : getSymModifiers()) {
      computeModifier(modifier);
    }
    for (JooSymbol modifier : getSymInheritedModifiers()) {
      computeModifier(modifier);
    }
  }

  private void computeModifier(final JooSymbol modifier) {
    int flag = getModifierFlag(modifier);
    if ((flag & modifiers) != 0) {
      throw JangarooParser.error(modifier, "duplicate modifier '" + modifier.getText() + "'");
    }
    if ((flag & MODIFIERS_SCOPE) != 0 && (modifiers & MODIFIERS_SCOPE) != 0) {
      throw JangarooParser.error(modifier, "duplicate scope modifier '" + modifier.getText() + "'");
    }
    modifiers |= flag;
  }

  private void checkAllowedModifiers() {
    int allowedModifiers = getAllowedModifiers();
    for (JooSymbol modifier : getSymModifiers()) {
      int flag = getModifierFlag(modifier);
      if ((allowedModifiers & flag) == 0) {
        throw JangarooParser.error(modifier, "modifier '" + modifier.getText() + "' not allowed here");
      }
    }
  }

  protected int getAllowedModifiers() {
    return 0;
  }

  @Override
  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    checkAllowedModifiers();
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
    throw JangarooParser.error(modifier, "internal compiler error: invalid modifier '" + modifier.getText() + "'");
  }

  public int getModifiers() {
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

  public boolean isPublicApi() {
    return isPublic() || isProtected();
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

  public boolean isDynamic() {
    return (getModifiers() & MODIFIER_DYNAMIC) != 0;
  }

  public boolean isNative() {
    return (getModifiers() & MODIFIER_NATIVE) != 0;
  }

  @Override
  public void scope(final Scope scope) {
    setParentDeclaration(scope.getDefiningNode());
    setClassDeclaration(scope.getClassDeclaration());
    compilationUnit = scope.getCompilationUnit();
  }

  public JooSymbol[] getSymInheritedModifiers() {
    return symInheritedModifiers.clone();
  }

  public void setSymInheritedModifiers(JooSymbol[] symInheritedModifiers) {
    this.symInheritedModifiers = symInheritedModifiers.clone();
  }

  public void setParentDeclaration(AstNode parentDeclaration) {
    this.parentDeclaration = parentDeclaration;
  }

  public void setClassDeclaration(ClassDeclaration classDeclaration) {
    this.classDeclaration = classDeclaration;
  }

  public JooSymbol[] getSymModifiers() {
    return symModifiers.clone();
  }

  public CompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

}
