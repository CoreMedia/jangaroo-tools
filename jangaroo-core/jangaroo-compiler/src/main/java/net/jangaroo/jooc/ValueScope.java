/*
 * Copyright 2010 CoreMedia AG
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Gawecki
 */
class ValueScope extends ScopeImplBase implements Scope {

  protected AstNode definingNode;

  public ValueScope(AstNode definingNode, Scope parent) {
    super(parent);
    this.definingNode = definingNode;
  }

  @Override
  public AstNode getDefiningNode() {
    return definingNode;
  }

  protected Map<String, AstNode> ides = new HashMap<String, AstNode>();

  @Override
  public AstNode declareIde(String name, AstNode decl) {
    return ides.put(name, decl);
  }

  @Override
  public AstNode declareIde(String name, AstNode node, boolean allowDuplicates, JooSymbol ideSymbol) {
    AstNode oldNode = declareIde(name, node);
    if (oldNode != null) {
      String msg = "Duplicate declaration of identifier '" + name + "'";
      if (allowDuplicates) {
        Jooc.warning(ideSymbol, msg);
      } else {
        throw Jooc.error(ideSymbol, msg);
      }
    }
    return oldNode;
  }

  @Override
  public AstNode getIdeDeclaration(Ide ide) {
    return getIdeDeclaration(ide.getName());
  }

  @Override
  public AstNode getIdeDeclaration(String name) {
    return ides.get(name);
  }

  @Override
  public Scope findScopeThatDeclares(Ide ide) {
    return findScopeThatDeclares(ide.getName());
  }

  @Override
  public Scope findScopeThatDeclares(String name) {
    return getIdeDeclaration(name) != null ? this
      : getParentScope() == null ? null
      : getParentScope().findScopeThatDeclares(name);
  }

  @Override
  public Ide createAuxVar() {
    int i = 1;
    while (true) {
      String auxVarName = "$" + i;
      Ide auxVar = new Ide(new JooSymbol(auxVarName));
      if (findScopeThatDeclares(auxVar) == null) {
        declareIde(auxVarName, auxVar);
        return auxVar;
      }
      ++i;
    }
  }

  @Override
  public void addExternalUsage(Ide ide) {
    String fqn = ide.getQualifiedNameStr();
    Scope packageScope = findScopeThatDeclares(fqn);
    if (packageScope != null) {
      AstNode classImport = packageScope.getIdeDeclaration(fqn);
      if (classImport instanceof ImportDirective) {
        ((ImportDirective) classImport).wasUsed();
      }
    }
  }

  @Override
  public CompilationUnit getCompilationUnit() {
    return (CompilationUnit) getPackageDeclaration().parentNode;
  }

  @Override
  public PackageDeclaration getPackageDeclaration() {
    if (definingNode instanceof PackageDeclaration) {
      return (PackageDeclaration) definingNode;
    }
    return super.getPackageDeclaration();
  }

  @Override
  public ClassDeclaration getClassDeclaration() {
    if (definingNode instanceof ClassDeclaration) {
      return (ClassDeclaration) definingNode;
    }
    return super.getClassDeclaration();
  }

  @Override
  public MethodDeclaration getMethodDeclaration() {
    if (definingNode instanceof MethodDeclaration) {
      return (MethodDeclaration) definingNode;
    }
    return super.getMethodDeclaration();
  }

}
