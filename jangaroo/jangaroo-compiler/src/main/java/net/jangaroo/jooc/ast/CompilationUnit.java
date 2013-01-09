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

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.utils.AS3Type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class CompilationUnit extends NodeImplBase {
  private PackageDeclaration packageDeclaration;
  private JooSymbol lBrace;
  private List<AstNode> directives;
  private IdeDeclaration primaryDeclaration;
  private JooSymbol rBrace;

  private Map<String, CompilationUnit> dependencies = new LinkedHashMap<String, CompilationUnit>();
  private Set<String> publicApiDependencies = new HashSet<String>();
  private Set<String> usedBuiltIns = new LinkedHashSet<String>();
  private Scope scope;
  private Map<String, String> auxVarsByPackage = new LinkedHashMap<String, String>();
  private boolean auxVarsRendered;

  private InputSource source;
  private JangarooParser compiler;

  public CompilationUnit(PackageDeclaration packageDeclaration, JooSymbol lBrace, List<AstNode> directives, IdeDeclaration primaryDeclaration, JooSymbol rBrace, List<IdeDeclaration> secondaryDeclarations) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.primaryDeclaration = primaryDeclaration;
    if (primaryDeclaration instanceof ClassDeclaration) {
      ((ClassDeclaration) primaryDeclaration).setSecondaryDeclarations(secondaryDeclarations);
    }
    this.rBrace = rBrace;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    List<AstNode> result = new ArrayList<AstNode>(makeChildren(super.getChildren(), packageDeclaration, directives, primaryDeclaration));
    if (primaryDeclaration instanceof ClassDeclaration) {
      result.addAll(((ClassDeclaration) primaryDeclaration).getSecondaryDeclarations());
    }
    return result;
  }

  public List<AstNode> getDirectives() {
    return directives;
  }

  public void addBuiltInUsage(String builtIn) {
    usedBuiltIns.add(builtIn);
  }

  public String getAuxVarForPackage(String packageQName) {
    return auxVarsByPackage.get(packageQName);
  }

  public String getAuxVarForPackage(Scope lookupScope, String packageQName) {
    if (auxVarsRendered) {
      throw new IllegalStateException("aux vars already rendered!");
    }
    String auxVar = getAuxVarForPackage(packageQName);
    if (auxVar == null) {
      auxVar = scope.createAuxVar(lookupScope).getName();
      auxVarsByPackage.put(packageQName, auxVar);
    }
    return auxVar;
  }

  public Map<String, String> getAuxVarDeclarations() {
    auxVarsRendered = true;
    LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
    for (String builtIn : usedBuiltIns) {
      String value = "joo." + ("$$bound".equals(builtIn) ? "boundMethod" : builtIn);
      result.put(builtIn, value);
    }
    for (Map.Entry<String,String> entry : auxVarsByPackage.entrySet()) {
      result.put(entry.getValue(), entry.getKey());
    }
    return result;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitCompilationUnit(this);
  }

  private void addStarImport(final Ide packageIde) {
    ImportDirective importDirective = new ImportDirective(packageIde, AS3Type.ANY.toString());
    directives.add(0, importDirective);
  }

  @Override
  public void scope(final Scope scope) {
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        Ide packageIde = packageDeclaration.getIde();
        packageDeclaration.scope(scope);
        if (packageIde != null) {
          // add implicit same package import
          addStarImport(packageIde);
        }
        // add implicit toplevel package import
        addStarImport(null);
        for (AstNode node : getDirectives()) {
          node.scope(scope);
        }
        withNewDeclarationScope(packageDeclaration, scope, new Scoped() {
          @Override
          public void run(final Scope scope) {
            CompilationUnit.this.scope = scope;
            primaryDeclaration.scope(scope);
          }
        });
      }
    });
  }

  public PackageDeclaration getPackageDeclaration() {
    return packageDeclaration;
  }

  public IdeDeclaration getPrimaryDeclaration() {
    return primaryDeclaration;
  }

  public JooSymbol getLBrace() {
    return lBrace;
  }

  public JooSymbol getRBrace() {
    return rBrace;
  }

  public Set<String> getDependencies() {
    return dependencies.keySet();
  }

  public Set<String> getPublicApiDependencies() {
    return publicApiDependencies;
  }

  public Collection<CompilationUnit> getDependenciesAsCompilationUnits() {
    return dependencies.values();
  }

  public JangarooParser getCompiler() {
    return compiler;
  }

  public void setCompiler(final JangarooParser compiler) {
    this.compiler = compiler;
  }

  /**
   * @param source the source of this compilation unit.
   */
  public void setSource(InputSource source) {
    this.source = source;
  }

  public InputSource getSource() {
    return source;
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    packageDeclaration.analyze(this);
    primaryDeclaration.analyze(this);
  }

  public Annotation getAnnotation(String name) {
    List<AstNode> directives = getDirectives();
    for (AstNode directive : directives) {
      if (directive instanceof Annotation) {
        Annotation annotation = (Annotation)directive;
        if (name.equals(annotation.getMetaName())) {
          return annotation;
        }
      }
    }
    return null;
  }
  
  public JooSymbol getSymbol() {
    return packageDeclaration.getSymbol();
  }

  public void addDependency(CompilationUnit otherUnit) {
    // predefined ides have a null unit
    if (otherUnit != null && otherUnit != this && otherUnit.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME) == null) {
      String qname = otherUnit.getPrimaryDeclaration().getQualifiedNameStr();
      dependencies.put(qname, otherUnit);
    }
  }

  public void addPublicApiDependency(CompilationUnit otherUnit) {
    // predefined ides have a null unit
    if (otherUnit != null && otherUnit != this) {
      //todo extend runtime to load units with primary decls other than classes
      final IdeDeclaration otherUnitPrimaryDeclaration = otherUnit.getPrimaryDeclaration();
      if (otherUnitPrimaryDeclaration instanceof ClassDeclaration
        || otherUnitPrimaryDeclaration instanceof NamespaceDeclaration) {
        // It is a class ...
        String qname = otherUnitPrimaryDeclaration.getQualifiedNameStr();
        if (qname.indexOf('.') != -1) {
          // ... outside of the root package.
          publicApiDependencies.add(qname);
        }
      }
    }
  }

  /**
   * Add a dependency to a resource at the given path, which is relative to this compilation unit's file.
   *
   * @param relativePath relative path of the dependency
   * @return the path relative to the source directory
   */
  public String addResourceDependency(String relativePath) {
    String path = relativePath.startsWith("/") || relativePath.startsWith("\\")
            ? relativePath
            : new File(source.getParent().getRelativePath(), relativePath).getPath().replace('\\', '/');
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    // dependencies.put("resource:" + path, null); TODO: replace by RequireJS plugin "text!path"
    return path;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + getPrimaryDeclaration().getQualifiedNameStr() + ")";
  }
}
