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
import net.jangaroo.jooc.SyntacticKeywords;

import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class FunctionDeclaration extends TypedIdeDeclaration {

  private FunctionExpr fun;
  private JooSymbol symGetOrSet;
  private JooSymbol optSymSemicolon;

  private boolean isConstructor = false;
  private boolean containsSuperConstructorCall = false;

  private static final int DEFAULT_ALLOWED_METHOD_MODIFIERS =
          MODIFIER_OVERRIDE | MODIFIER_ABSTRACT | MODIFIER_VIRTUAL | MODIFIER_FINAL | MODIFIERS_SCOPE | MODIFIER_STATIC | MODIFIER_NATIVE;

  public FunctionDeclaration(List<JooSymbol> modifiers, JooSymbol symFunction, JooSymbol symGetOrSet, Ide ide, JooSymbol lParen,
                             Parameters params, JooSymbol rParen, TypeRelation optTypeRelation,
                             BlockStatement optBody,
                             JooSymbol optSymSemicolon) {
    super(modifiers.toArray(new JooSymbol[modifiers.size()]), ide, null); //todo pass Function type as typeRelation
    this.fun = new FunctionExpr(this, symFunction, ide, lParen, params, rParen, optTypeRelation, optBody);
    this.symGetOrSet = symGetOrSet;
    this.optSymSemicolon = optSymSemicolon;
    if (isGetterOrSetter() && !(isGetter() || isSetter())) {
      throw Jooc.error(symGetOrSet, "Expected 'get' or 'set'.");
    }
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitFunctionDeclaration(this);
  }

  public boolean overrides() {
    return (getModifiers() & MODIFIER_OVERRIDE) != 0;
  }

  @Override
  public boolean isMethod() {
    return isClassMember();
  }

  public boolean isGetterOrSetter() {
    return symGetOrSet != null;
  }

  public boolean isGetter() {
    return isGetterOrSetter() && SyntacticKeywords.GET.equals(symGetOrSet.getText());
  }

  public boolean isSetter() {
    return isGetterOrSetter() && SyntacticKeywords.SET.equals(symGetOrSet.getText());
  }

  public final boolean isConstructor() {
    return isConstructor;
  }

  public FunctionExpr getFun() {
    return fun;
  }

  public JooSymbol getSymGetOrSet() {
    return symGetOrSet;
  }

  public JooSymbol getOptSymSemicolon() {
    return optSymSemicolon;
  }

  public boolean containsSuperConstructorCall() {
    return isContainsSuperConstructorCall();
  }

  public void setContainsSuperConstructorCall(boolean containsSuperConstructorCallStatement) {
    this.containsSuperConstructorCall = containsSuperConstructorCallStatement;
  }

  public boolean isAbstract() {
    return getClassDeclaration() != null && getClassDeclaration().isInterface() || super.isAbstract();
  }

  public Parameters getParams() {
    return fun.getParams();
  }

  public boolean hasBody() {
    return fun.hasBody();
  }

  public BlockStatement getBody() {
    return fun.getBody();
  }

  @Override
  public void scope(Scope scope) {
    final ClassDeclaration classDeclaration = scope.getClassDeclaration();
    // todo: temporarily resetting the ide field looks weird
    Ide oldIde = getIde();
    if (classDeclaration != null && getIde().getName().equals(classDeclaration.getName())) {
      setConstructor(true);
      classDeclaration.setConstructor(this);
      setIde(null); // do NOT declare constructor ide in scope, as it would override the class, is not inherited, etc.!
    }
    super.scope(scope);
    setIde(oldIde);
    //todo check correct override usage
    if (overrides() && isAbstract()) {
      throw Jooc.error(this, "overriding methods are not allowed to be declared abstract");
    }
    if (isAbstract()) {
      if (classDeclaration == null) {
        throw Jooc.error(this, "package-scoped function " + getName() + " must not be abstract.");
      }
      if (!classDeclaration.isAbstract()) {
        throw Jooc.error(this, classDeclaration.getName() + "is not declared abstract");
      }
      if (hasBody()) {
        throw Jooc.error(this, "abstract method must not be implemented");
      }
    }
    if (isNative() && hasBody()) {
      throw Jooc.error(this, "native method must not be implemented");
    }
    if (!isAbstract() && !isNative() && !hasBody()) {
      throw Jooc.error(this, "method must either be implemented or declared abstract or native");
    }
    //TODO:check whether abstract method does not actually override
    if (!isStatic()) {
      ClassDeclaration currentClass = scope.getClassDeclaration();
      if (classDeclaration != null) { // otherwise we are in a global function - todo parse them as function declaration
        // declare this and super
        final Type thisType = currentClass.getThisType();
        final Parameter thisParam = new Parameter(null, new Ide("this"), new TypeRelation(null, thisType), null);
        fun.addImplicitParam(thisParam);
        final Type superType = currentClass.getSuperType();
        if (superType != null) {
          fun.addImplicitParam(new Parameter(null, new Ide("super"), new TypeRelation(null, superType), null));
        }
      }
    }
    fun.scope(scope);
    if (containsSuperConstructorCall()) {
      // must be contained at top level
      BlockStatement block = getBody();
      block.checkSuperConstructorCall();
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode); // computes modifiers
    fun.analyze(this);
  }

  @Override
  protected int getAllowedModifiers() {
    return isConstructor() ? MODIFIERS_SCOPE | MODIFIER_NATIVE : DEFAULT_ALLOWED_METHOD_MODIFIERS;
  }

  @Override
  public void handleDuplicateDeclaration(Scope scope, AstNode oldNode) {
    if (isGetterOrSetter() && oldNode instanceof FunctionDeclaration) {
      FunctionDeclaration other = (FunctionDeclaration) oldNode;
      if (other.isGetterOrSetter() && isGetter() != other.isGetter()) {
        // found counterpart for this getter or setter:
        // do not trigger warning or error!
        return;
      }
    }
    super.handleDuplicateDeclaration(scope, oldNode);
  }

  @Override
  public JooSymbol getSymbol() {
    return fun.getSymbol();
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    // todo this looks quirky, try not to define constructor within scope?
    return isConstructor() ? getClassDeclaration() : super.resolveDeclaration();
  }

  public void setConstructor(boolean constructor) {
    isConstructor = constructor;
  }

  public boolean isContainsSuperConstructorCall() {
    return containsSuperConstructorCall;
  }
}
