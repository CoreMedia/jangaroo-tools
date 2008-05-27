/*
 *   Copyright (c) 2004 CoreMedia AG, Hamburg. All rights reserved.
 */
package com.coremedia.jscc;

public abstract class MemberDeclaration extends IdeDeclaration {
  TypeRelation optTypeRelation;

  public MemberDeclaration(JscSymbol[] modifiers, int allowedModifiers, Ide ide, TypeRelation optTypeRelation) {
    super(modifiers, allowedModifiers, ide);
    this.optTypeRelation = optTypeRelation;
  }

  public TypeRelation getOptTypeRelation() {
    return optTypeRelation;
  }

  public ClassDeclaration getClassDeclaration() {
    return (ClassDeclaration) getParentDeclaration();
  }

  public void analyze(AnalyzeContext context) {
    super.analyze(context);
    if (isPrivate() && !isStatic()) {
      getClassDeclaration().registerPrivateMember(ide);
    }
  }
}
