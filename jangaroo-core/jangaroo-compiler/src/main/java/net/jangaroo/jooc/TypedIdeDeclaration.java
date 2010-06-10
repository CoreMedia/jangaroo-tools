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

/**
 * @author Andreas Gawecki
 */
public abstract class TypedIdeDeclaration extends IdeDeclaration {

  private JooSymbol namespace;
  TypeRelation optTypeRelation;

  public TypedIdeDeclaration(JooSymbol[] modifiers, int allowedModifiers, Ide ide, TypeRelation optTypeRelation) {
    super(modifiers, allowedModifiers, ide);
    this.namespace = findNamespace(modifiers);
    this.optTypeRelation = optTypeRelation;
  }

  private JooSymbol findNamespace(JooSymbol[] modifiers) {
    for (JooSymbol modifier : modifiers) {
      if (getModifierFlag(modifier) == MODIFIER_NAMESPACE) {
        return modifier;
      }
    }
    return null;
  }

  @Override
  public String getName() {
    return NamespacedIde.getNamespacePrefix(namespace) + super.getName();
  }

  @Override
  public void scope(Scope scope) {
    if (optTypeRelation != null) {
      optTypeRelation.scope(scope);
    }
    ClassDeclaration classDeclaration = scope.getClassDeclaration();
    if (classDeclaration == scope.getDefiningNode() && isStatic()) {
      // back out one scope in order to store static members in a different scope:
      scope = scope.getParentScope();
    }
    super.scope(scope);
    if (isClassMember()) {
      if (classDeclaration != null) {
        classDeclaration.registerMember(this);
      }
    }
  }

  @Override
  boolean allowDuplicates(Scope scope) {
    // allow package members to clash with imports:
    return scope.getClassDeclaration() == null || super.allowDuplicates(scope);
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return optTypeRelation == null ? null : optTypeRelation.getType().resolveDeclaration();
  }
}
