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
import net.jangaroo.jooc.Scope;

import java.util.List;

/**
 * @author Andreas Gawecki
 */
public abstract class TypedIdeDeclaration extends IdeDeclaration implements Typed {

  private Ide namespace;
  private TypeRelation optTypeRelation;

  TypedIdeDeclaration(AnnotationsAndModifiers am, Ide ide, TypeRelation optTypeRelation) {
    super(am, ide);
    this.namespace = findNamespace(am.getModifiers());
    this.optTypeRelation = optTypeRelation;
  }

  private Ide findNamespace(List<JooSymbol> modifiers) {
    for (JooSymbol modifier : modifiers) {
      if (getModifierFlag(modifier) == MODIFIER_NAMESPACE) {
        return new Ide(modifier);
      }
    }
    return null;
  }

  public Ide getNamespace() {
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
  public void scope(Scope scope) {
    if (namespace != null) {
      namespace.scope(scope);
    }
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

  void addPublicApiDependencyOn(TypeRelation optTypeRelation) {
    if (optTypeRelation != null) {
      optTypeRelation.getType().getIde().addPublicApiDependency();
    }
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode); // computes modifiers
    if (namespace != null) {
      namespace.analyze(parentNode);
    }
    if (isPublicApi()) {
      if (namespace != null) {
        namespace.addPublicApiDependency();
      }
      addPublicApiDependencyOn(optTypeRelation);
    }
    if (optTypeRelation != null) {
      optTypeRelation.analyze(parentNode);
    }
  }

  @Override
  boolean allowDuplicates(Scope scope) {
    // allow package members to clash with imports:
    return scope.getClassDeclaration() == null || super.allowDuplicates(scope);
  }

  @Override
  public IdeDeclaration resolveDeclaration() {
    return getOptTypeRelation() == null ? null : getOptTypeRelation().getType().resolveDeclaration();
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

}
