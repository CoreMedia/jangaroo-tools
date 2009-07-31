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
public abstract class MemberDeclaration extends IdeDeclaration {
  private JooSymbol namespace;
  TypeRelation optTypeRelation;

  public MemberDeclaration(JooSymbol[] modifiers, int allowedModifiers, Ide ide, TypeRelation optTypeRelation) {
    super(modifiers, allowedModifiers, ide);
    this.namespace = findNamespace(modifiers);
    this.optTypeRelation = optTypeRelation;
  }

  private JooSymbol findNamespace(JooSymbol[] modifiers) {
    for (JooSymbol modifier : modifiers) {
      if (getModifierFlag(modifier)==MODIFIER_NAMESPACE) {
        return modifier;
      }
    }
    return null;
  }

  @Override
  public String getName() {
    return NamespacedIde.getNamespacePrefix(namespace)+super.getName();
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
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

  public Node analyze(Node parentNode, AnalyzeContext context) {
    ClassDeclaration classDeclaration = context.getCurrentClass();
    Scope scope = context.getScope();
    if (classDeclaration==scope.getDeclaration() && isStatic()) {
      // back out one scope in order to store static members in a different scope:
      context.leaveScope(classDeclaration);
    }
    super.analyze(parentNode, context);
    // restore old scope (may have been changed above):
    context.setScope(scope);
    if (isField() || isMethod()) {
      if (classDeclaration!=null) {
        classDeclaration.registerMember(this);
      }
    }
    if (namespace!=null) {
      NamespacedIde.warnUndefinedNamespace(context, namespace);
    }
    return this;
  }

  @Override
  boolean allowDuplicates(AnalyzeContext context) {
    // allow package members to clash with imports:
    return context.getCurrentClass()==null || super.allowDuplicates(context);
  }
}
