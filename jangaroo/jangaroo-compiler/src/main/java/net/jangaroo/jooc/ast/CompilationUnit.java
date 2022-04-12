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
import net.jangaroo.utils.CompilerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class CompilationUnit extends NodeImplBase {
  protected PackageDeclaration packageDeclaration;
  private JooSymbol lBrace;
  private List<Directive> directives;
  protected IdeDeclaration primaryDeclaration;
  private JooSymbol rBrace;

  private Set<String> compileDependencies = new HashSet<>();
  private List<String> resourceDependencies = new ArrayList<>();
  private Set<String> publicApiDependencies = new HashSet<>();
  private Set<String> usesDependencies = new HashSet<>();
  private Set<String> requiresDependencies = new HashSet<>();
  private Set<String> usedBuiltInIdentifiers = new TreeSet<>();
  private Scope scope;

  public CompilationUnit(PackageDeclaration packageDeclaration, JooSymbol lBrace, List<Directive> directives, IdeDeclaration primaryDeclaration, JooSymbol rBrace, List<IdeDeclaration> secondaryDeclarations) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.primaryDeclaration = primaryDeclaration;
    if (primaryDeclaration instanceof ClassDeclaration) {
      ((ClassDeclaration) primaryDeclaration).setSecondaryDeclarations(secondaryDeclarations);
      if (secondaryDeclarations != null) {
        for (IdeDeclaration secondaryDeclaration : secondaryDeclarations) {
          if (secondaryDeclaration instanceof ClassDeclaration) {
            this.directives.addAll(((ClassDeclaration) secondaryDeclaration).getDirectives());
          }
        }
      }
    }
    this.rBrace = rBrace;
  }

  public String getQualifiedNameStr() {
    return CompilerUtils.qName(getPackageDeclaration().getQualifiedNameStr(), getPrimaryDeclaration().getName());
  }

  public boolean isClass() {
    return getPrimaryDeclaration() instanceof ClassDeclaration;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    List<AstNode> result = new ArrayList<>(makeChildren(super.getChildren(), packageDeclaration, directives, primaryDeclaration));
    if (primaryDeclaration instanceof ClassDeclaration) {
      result.addAll(((ClassDeclaration) primaryDeclaration).getSecondaryDeclarations());
    }
    return result;
  }

  public List<Directive> getDirectives() {
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

  public Set<String> getRuntimeDependencies() {
    Set<String> runtimeDependencies = new HashSet<>(requiresDependencies);
    runtimeDependencies.addAll(usesDependencies);
    return runtimeDependencies;
  }

  public Set<String> getRuntimeDependencies(boolean required) {
    return required ? requiresDependencies : usesDependencies;
  }

  public Set<String> getCompileDependencies() {
    return compileDependencies;
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
    return getPrimaryDeclaration().getAnnotations();
  }

  public JooSymbol getSymbol() {
    return packageDeclaration.getSymbol();
  }

  public static CompilationUnit mapMixinInterface(CompilationUnit compilationUnit) {
    if (compilationUnit != null && compilationUnit.getPrimaryDeclaration() instanceof ClassDeclaration
            && ((ClassDeclaration)compilationUnit.getPrimaryDeclaration()).isInterface()) {
      CompilationUnit mixinCompilationUnit = getMixinCompilationUnit(compilationUnit.getPrimaryDeclaration());
      if (mixinCompilationUnit != null) {
        return mixinCompilationUnit;
      }
    }
    return compilationUnit;
  }

  public static CompilationUnit getMixinCompilationUnit(Declaration declaration) {
    Annotation mixinAnnotation = declaration.getAnnotation(Jooc.MIXIN_ANNOTATION_NAME);
    if (mixinAnnotation != null) {
      Iterator<String> mixinClassNames = getAnnotationDefaultParameterStringValues(mixinAnnotation).iterator();
      if (mixinClassNames.hasNext()) {
        String mixinClassName = mixinClassNames.next();
        CompilationUnit mixinCompilationUnit = mixinAnnotation.getIde().getScope().getCompiler().getCompilationUnit(mixinClassName);
        if (mixinCompilationUnit == null) {
          throw Jooc.error(declaration, "Mixin annotation refers to unresolvable class '" + mixinClassName + "'.");
        }
        return mixinCompilationUnit;
      }
    }
    return null;
  }

  public void addDependency(CompilationUnit otherUnit, Boolean required) {
    otherUnit = mapMixinInterface(otherUnit);
    // Predefined ides have a null unit.
    // Self dependencies are ignored.
    if (otherUnit != null && otherUnit != this) {
      String qName = otherUnit.getPrimaryDeclaration().getQualifiedNameStr();
      compileDependencies.add(qName);
      // not a type-only dependency and not already required:
      if (required != null && !requiresDependencies.contains(qName)) {
        // Dependencies on other modules may always be considered required,
        // because they cannot lead to cycles.
        if (required || !otherUnit.isInSourcePath()) {
          requiresDependencies.add(qName);
          usesDependencies.remove(qName); // required implied used
        } else {
          usesDependencies.add(qName);
        }
      }
    }
  }

  private static List<String> getAnnotationDefaultParameterStringValues(Annotation annotation) {
    List<String> values = new ArrayList<>();
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

  public void addBuiltInIdentifierUsage(String builtInIdentifier) {
    usedBuiltInIdentifiers.add(builtInIdentifier);
  }

  public Set<String> getUsedBuiltInIdentifiers() {
    return usedBuiltInIdentifiers;
  }

  public String cutOffExtNamespace(String targetName) {
    String extNamespace = getInputSource().getExtNamespace();
    if (extNamespace != null && !extNamespace.isEmpty()) {
      if (targetName.equals(extNamespace)) {
        // top-level namespace export like "Ext":
        targetName = "";
      } else {
        if (!targetName.startsWith(extNamespace + ".")) {
          throw JangarooParser.error("Source file fully-qualified name " + targetName + " does not start with configured extNamespace " + extNamespace);
        }
        // also cut off the ".":
        targetName = targetName.substring(extNamespace.length() + 1);
      }
    }
    return targetName;
  }

}
