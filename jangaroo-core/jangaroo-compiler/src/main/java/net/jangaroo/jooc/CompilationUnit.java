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

import net.jangaroo.jooc.backend.CompilationUnitSink;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class CompilationUnit extends NodeImplBase implements CodeGenerator {

  public PackageDeclaration getPackageDeclaration() {
    return packageDeclaration;
  }

  private PackageDeclaration packageDeclaration;
  private JooSymbol lBrace;
  private List<AstNode> directives;
  private IdeDeclaration primaryDeclaration;
  private JooSymbol rBrace;
  private Set<String> dependencies = new LinkedHashSet<String>();
  private InputSource source;
  private Jooc compiler;

  public CompilationUnit(PackageDeclaration packageDeclaration, JooSymbol lBrace, List<AstNode> directives, IdeDeclaration primaryDeclaration, JooSymbol rBrace) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.primaryDeclaration = primaryDeclaration;
    this.rBrace = rBrace;
  }

  @Override
  public void scope(final Scope scope) {
    withNewDeclarationScope(this, scope, new Scoped() {
      @Override
      public void run(final Scope scope) {
        // add implicit same package import
        Ide packageIde = packageDeclaration.getIde();
        if (packageIde != null)
          addStarImport(packageIde);
        // add implicit toplevel package import
        addStarImport(null);
        scope(directives, scope);
        packageDeclaration.scope(scope);
        withNewDeclarationScope(packageDeclaration, scope, new Scoped() {
          @Override
          public void run(final Scope scope) {
            primaryDeclaration.scope(scope);
          }
        });
      }
    });
  }

  public IdeDeclaration getPrimaryDeclaration() {
    return primaryDeclaration;
  }

  public Jooc getCompiler() {
    return compiler;
  }

  public void setCompiler(final Jooc compiler) {
    this.compiler = compiler;
  }

  public Collection<File> getSourcePath() {
    return getCompiler().getConfig().getSourcePath();
  }

  /**
   * @param source the source of this compilation unit.
   */
  public void setSource(InputSource source) {
    this.source = source;
  }

  public void writeOutput(CompilationUnitSinkFactory writerFactory,
                          boolean verbose) throws Jooc.CompilerError {
    File sourceFile = ((FileInputSource) this.source).getFile();
    CompilationUnitSink sink = writerFactory.createSink(
      packageDeclaration, primaryDeclaration,
      sourceFile, verbose);

    sink.writeOutput(this);
  }

  @Override
  protected void generateAsApiCode(final JsWriter out) throws IOException {
    packageDeclaration.generateCode(out);
    out.writeSymbol(lBrace);
    generateCode(directives, out);
    primaryDeclaration.generateCode(out);
    out.writeSymbol(rBrace);
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    out.write(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".prepare(");
    packageDeclaration.generateCode(out);
    out.beginComment();
    out.writeSymbol(lBrace);
    generateCode(directives, out);
    out.endComment();
    primaryDeclaration.generateCode(out);
    out.write(",[");
    boolean first = true;
    for (String qname : dependencies) {
      if (first) {
        first = false;
      } else {
        out.write(",");
      }
      out.write('"' + qname + '"');
    }
    out.write("]");
    out.writeSymbolWhitespace(rBrace);
    out.write(");");
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    super.analyze(parentNode, context);

    packageDeclaration.analyze(this, context);
    analyzeDirectives(context);
    primaryDeclaration.analyze(this, context);
    return this;
  }

  private void addStarImport(final Ide packageIde) {
    ImportDirective importDirective = new ImportDirective(packageIde, "*");
    directives.add(0, importDirective);
  }

  private void analyzeDirectives(AnalyzeContext context) {
    this.directives = analyze(this, this.directives, context);
  }

  public JooSymbol getSymbol() {
    return packageDeclaration.getSymbol();
  }

  public void addDependency(CompilationUnit otherUnit) {
    // predefined ides have a null unit
    if (otherUnit != null && otherUnit != this) {
      //todo extend runtime to load units with primary decls other than classes
      final IdeDeclaration primaryDeclaration = otherUnit.getPrimaryDeclaration();
      if (primaryDeclaration instanceof ClassDeclaration && !primaryDeclaration.isNative()) {
        String qname = primaryDeclaration.getQualifiedNameStr();
        dependencies.add(qname);
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + getPrimaryDeclaration().getQualifiedNameStr() + ")";
  }
}
