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

import java.util.List;

/**
 * @author Andreas Gawecki
 */
public abstract class TypedIdeDeclaration extends IdeDeclaration {

  private JooSymbol namespace;
  private TypeRelation optTypeRelation;

  public TypedIdeDeclaration(JooSymbol[] modifiers, Ide ide, TypeRelation optTypeRelation) {
    super(modifiers, ide);
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

  public JooSymbol getNamespace() {
    return namespace;
  }

  @Override
  public boolean isPublicApi() {
    return super.isPublicApi() || getNamespace() != null;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), optTypeRelation);
  }

  @Override
  public String getName() {
    return NamespacedIde.getNamespacePrefix(namespace) + super.getName();
  }

  @Override
  public void scope(Scope scope) {
    if (getOptTypeRelation() != null) {
      getOptTypeRelation().scope(scope);
    }
    ClassDeclaration classDeclaration = scope.getClassDeclaration();
    super.scope(scope);
    if (isClassMember()) {
      if (classDeclaration != null) {
        classDeclaration.registerMember(this);
      }
    }
  }

  protected void addPublicApiDependencyOn(TypeRelation optTypeRelation) {
    if (optTypeRelation != null) {
      optTypeRelation.getType().getIde().addPublicApiDependency();
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode); // computes modifiers
    if (isPublicApi()) {
      addPublicApiDependencyOn(optTypeRelation);
    }
  }

  @Override
  boolean allowDuplicates(Scope scope) {
    // allow package members to clash with imports:
    return scope.getClassDeclaration() == null || super.allowDuplicates(scope);
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    if (getOptTypeRelation() == null) {
      return null;
    } else {
      if (getOptTypeRelation().getType().getIde().equals(getIde())) {
        throw Jooc.error(getSymbol(), "Type was not found or was not a compile-time constant: " + getIde().getSymbol().getText());
      } else {
        return getOptTypeRelation().getType().resolveDeclaration();
      }
    }
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

}
