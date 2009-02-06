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
  TypeRelation optTypeRelation;

  public MemberDeclaration(JooSymbol[] modifiers, int allowedModifiers, Ide ide, TypeRelation optTypeRelation) {
    super(modifiers, allowedModifiers, ide);
    this.optTypeRelation = optTypeRelation;
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public ClassDeclaration getClassDeclaration() {
    return (ClassDeclaration) getParentDeclaration();
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

  public void analyze(Node parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);
    if (isField() || isMethod()) {
      getClassDeclaration().registerMember(this);
    }
  }
}
