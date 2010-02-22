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

import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.JoocOptions;

/**
 * @author Andreas Gawecki
 */
public class AnalyzeContext {

  protected PackageDeclaration packageDeclaration = null;
  protected Scope scope = null;
  private JoocOptions config;

  public AnalyzeContext(JoocOptions config) {
    this.config = config;
  }

  public void enterScope(Node declaration) {
    scope = new Scope(declaration, scope);
  }

  public void leaveScope(Node declaration) {
    if (declaration != scope.getDeclaration())
      throw Jooc.error("internal error: wrong scope to leave");
    scope = scope.getParentScope();
  }

  public Scope getScope() {
    return scope;
  }

  void setScope(Scope scope) {
    this.scope = scope;
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

  public JoocOptions getConfig() {
    return config;
  }
}
