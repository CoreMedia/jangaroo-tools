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
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.utils.AS3Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
  protected PackageDeclaration packageDeclaration;
  private JooSymbol lBrace;
  private List<AstNode> directives;
  protected IdeDeclaration primaryDeclaration;
  private JooSymbol rBrace;

  private Map<String, Boolean> dependencies = new LinkedHashMap<>();
  private Set<String> dependenciesInModule = new LinkedHashSet<>();
  private List<String> resourceDependencies = new ArrayList<String>();
  private Set<String> publicApiDependencies = new LinkedHashSet<String>();
  private Scope scope;
  private Map<String, String> auxVarsByPackage = new LinkedHashMap<String, String>();

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

  public List<String> getResourceDependencies() {
    return resourceDependencies;
  }

  public Set<String> getPublicApiDependencies() {
    return publicApiDependencies;
  }

  public Set<String> getDependencies() {
    return dependencies.keySet();
  }

  public boolean isRequiredDependency(String qName) {
    return dependencies.get(qName);
  }

  public Set<String> getDependenciesInModule() {
    return dependenciesInModule;
  }

  public InputSource getInputSource() {
    return scope.getCompiler().getInputSource(this);
  }

  public void analyze(AstNode parentNode) {
    super.analyze(parentNode);
    packageDeclaration.analyze(this);
    analyze(packageDeclaration, directives);
    primaryDeclaration.analyze(this);
  }

  public List<Annotation> getAnnotations() {
    List<Annotation> annotations = new ArrayList<Annotation>();
    for (AstNode directive : getDirectives()) {
      if (directive instanceof Annotation) {
        annotations.add((Annotation)directive);
      }
    }
    return annotations;
  }

  public Annotation getAnnotation(String name) {
    for (Annotation annotation : getAnnotations()) {
      if (name.equals(annotation.getMetaName())) {
        return annotation;
      }
    }
    return null;
  }

  public List<Annotation> getAnnotations(String name) {
    List<Annotation> annotations = new ArrayList<Annotation>();
    for (Annotation annotation : getAnnotations()) {
      if (name.equals(annotation.getMetaName())) {
        annotations.add(annotation);
      }
    }
    return annotations;
  }

  public JooSymbol getSymbol() {
    return packageDeclaration.getSymbol();
  }

  public CompilationUnit mapMixinInterface(CompilationUnit compilationUnit) {
    if (compilationUnit != null && compilationUnit.getPrimaryDeclaration() instanceof ClassDeclaration
            && ((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).isInterface()) {
      Annotation mixinAnnotation = compilationUnit.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME);
      if (mixinAnnotation != null) {
        Iterator<String> mixinClassNames = getAnnotationDefaultParameterStringValues(mixinAnnotation).iterator();
        if (mixinClassNames.hasNext()) {
          String mixinClassName = mixinClassNames.next();
          CompilationUnit mixinCompilationUnit = scope.getCompiler().getCompilationUnit(mixinClassName);
          if (mixinCompilationUnit == null) {
            throw Jooc.error(compilationUnit, "Mixin annotation refers to unresolvable class '" + mixinClassName + "'.");
          }
          return mixinCompilationUnit;
        }
      }
    }
    return compilationUnit;
  }

  public void addDependency(CompilationUnit otherUnit, boolean required) {
    otherUnit = mapMixinInterface(otherUnit);
    // Predefined ides have a null unit.
    // Self dependencies are ignored.
    if (otherUnit != null && otherUnit != this) {
      String qName = otherUnit.getPrimaryDeclaration().getQualifiedNameStr();
      // Dependencies on other modules may always be considered required,
      // because they cannot lead to cycles.
      boolean alreadyRequired = Boolean.TRUE.equals(dependencies.get(qName));
      boolean inModule = otherUnit.isInSourcePath();
      dependencies.put(qName, required || alreadyRequired || !inModule);
      if (inModule) {
        dependenciesInModule.add(qName);
      } else {
        for (Annotation annotation : otherUnit.getAnnotations(Jooc.USES_ANNOTATION_NAME)) {
          for (String value : getAnnotationDefaultParameterStringValues(annotation)) {
            dependencies.put(scope.getCompiler().getCompilationUnit(value).getPrimaryDeclaration().getQualifiedNameStr(), true);
          }
        }
      }
    }
  }

  private List<String> getAnnotationDefaultParameterStringValues(Annotation annotation) {
    List<String> values = new ArrayList<String>();
    CommaSeparatedList<AnnotationParameter> current = annotation.getOptAnnotationParameters();
    while (current != null) {
      AnnotationParameter head = current.getHead();
      if (head.getOptName() == null) {
        AstNode value = head.getValue();
        if (value instanceof LiteralExpr) {
          LiteralExpr literal = (LiteralExpr) value;
          Object jooValue = literal.getSymbol().getJooValue();
          if (jooValue instanceof String) {
            values.add((String)jooValue);
          }
        }
      }
      current = current.getTail();
    }
    return values;
  }

  public void addRequiredDependency(CompilationUnit otherUnit) {
    addDependency(otherUnit, true);
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

  @Override
  public String toString() {
    return "CompilationUnit{" +
            "packageDeclaration=" + packageDeclaration +
            ", primaryDeclaration=" + primaryDeclaration +
            '}';
  }

  public boolean isInSourcePath() {
    return getInputSource().isInSourcePath();
  }
}
