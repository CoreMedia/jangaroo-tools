/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.util.Stack;

/**
 * @author Andreas Gawecki
 */
public class AnalyzeContext {

  protected PackageDeclaration packageDeclaration = null;
  protected Scope scope = null;

  public void enterScope(IdeDeclaration declaration) {
    scope = new Scope(declaration, scope);
  }

  public void leaveScope(Declaration declaration) {
    if (declaration != scope.getDeclaration())
      Jscc.error("internal error: wrong scope to leave");
    scope = scope.getParentScope();
  }

  public Scope getScope() {
    return scope;
  }

  public PackageDeclaration getCurrentPackage() {
    if (packageDeclaration == null) {
      packageDeclaration = scope.getPackageDeclaration();
    }
    return packageDeclaration;
  }

  public ClassDeclaration getCurrentClass() {
    return scope.getClassDeclaration();
  }

  public MethodDeclaration getCurrentMethod() {
    return scope.getMethodDeclaration();
  }

}
