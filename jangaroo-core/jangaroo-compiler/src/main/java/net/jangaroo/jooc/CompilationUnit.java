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

import java.io.File;
import java.io.FilenameFilter;
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
  private List<AstNode> implicitDirectives = new ArrayList<AstNode>(10);
  private IdeDeclaration primaryDeclaration;
  private JooSymbol rBrace;
  private Collection<File> sourcePath = new LinkedHashSet<File>();

  protected File sourceFile;

  protected JsWriter out;

  public CompilationUnit(PackageDeclaration packageDeclaration, JooSymbol lBrace, List<AstNode> directives, IdeDeclaration primaryDeclaration, JooSymbol rBrace) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.primaryDeclaration = primaryDeclaration;
    this.rBrace = rBrace;
  }

  public Collection<File> getSourcePath() {
    return sourcePath;
  }

  /**
   * Set the source path in order to add additional source root directories. These are needed to
   * detect implicit imports to classes within the same package.
   *
   * @param sourcePath a collection of root directories containing Jangaroo sources
   */
  public void setSourcePath(final Collection<File> sourcePath) {
    this.sourcePath = sourcePath;
  }

  /**
   * @param sourceFile the source file of this compilation unit.
   */
  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  private static String withoutAS(String name) {
    return name.substring(0, name.length() - Jooc.INPUT_FILE_SUFFIX.length());
  }

  public File getSourceFile() {
    return sourceFile;
  }

  public void writeOutput(CompilationUnitSinkFactory writerFactory,
                          boolean verbose) throws Jooc.CompilerError {
    CompilationUnitSink sink = writerFactory.createSink(
      packageDeclaration, primaryDeclaration,
      sourceFile, verbose);

    sink.writeOutput(this);
  }

  public void generateCode(JsWriter out) throws IOException {
    out.write(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".prepare(");
    packageDeclaration.generateCode(out);
    out.writeSymbolWhitespace(lBrace);
    out.write("[");
    Collection<AstNode> allDirectives = implicitDirectives;
    if (directives != null) {
      allDirectives = new ArrayList<AstNode>(implicitDirectives.size()+directives.size());
      allDirectives.addAll(implicitDirectives);
      allDirectives.addAll(directives);
    }
    generateCode(allDirectives, out);
    out.write("\"\"],");
    primaryDeclaration.generateCode(out);
    out.write(",[");
    boolean first = true;
    for (AstNode node : allDirectives) {
      if (node instanceof ImportDirective) {
        ImportDirective importDirective = (ImportDirective) node;
        if (importDirective.isUsed()) {
          String externalUsage = importDirective.getQualifiedName();
          if (first) {
            first = false;
          } else {
            out.write(",");
          }
          out.write('"' + externalUsage + '"');
        }
      }
    }
    out.write("]");
    out.writeSymbolWhitespace(rBrace);
    out.write(");");
  }

  public AstNode analyze(AstNode parentNode, AnalyzeContext context) {
    // establish global scope for built-in identifiers:
    IdeType globalObject = new IdeType("globalObject");
    context.enterScope(globalObject);
    declareIdes(context.getScope(), new String[]{
      "undefined",
      "window",  // TODO: or rather have to import?
      "int",
      "uint",
      "Object",
      "Function",
      "Class",
      "Array",
      "Boolean",
      "String",
      "Number",
      "RegExp",
      "Date",
      "Math",
      "parseInt",
      "parseFloat",
      "isNaN",
      "NaN",
      "isFinite",
      "Infinity",
      "decodeURI",
      "decodeURIComponent",
      "encodeURI",
      "encodeURIComponent",
      "trace"});

    // add implicit same package import
    Ide packageIde = packageDeclaration.getIde();
    if (packageIde != null)
      addStarImport(packageIde);
    // add implicit toplevel package import
    addStarImport(null);

    super.analyze(parentNode, context);

    context.enterScope(packageDeclaration);
    packageDeclaration.analyze(this, context);
    analyzeDirectives(context);
    primaryDeclaration.analyze(this, context);
    context.leaveScope(packageDeclaration);
    context.leaveScope(globalObject);
    return this;
  }

  private void addStarImport(final Ide packageIde) {
    ImportDirective importDirective = new ImportDirective(packageIde, "*");
    directives.add(0, importDirective);
  }

  private String getRelativePackagePath(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }

  private void addPackageFolderSymbols(final File folder, List<String> list) {
    String[] symbols = folder.list(new SourceFilenameFilter());
    if (symbols != null) {
      for (String symbol : symbols) {
        list.add(withoutAS(symbol));
      }
    }
  }

  public List<String> getPackageIdes(String packageName) {
    List<String> result = new ArrayList<String>(10);
    final String relativePackagePath = getRelativePackagePath(packageName);
    for (File sourceDir : getSourcePath()) {
      final File packageFolder = relativePackagePath.isEmpty() ? sourceDir : new File(sourceDir, relativePackagePath);
      addPackageFolderSymbols(packageFolder, result);
    }
    return result;
  }

  private void analyzeDirectives(AnalyzeContext context) {
     this.directives = analyze(this, this.directives, context);
  }

  private void declareIdes(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      scope.declareIde(identifier, new IdeType(identifier));
    }
  }

  public JooSymbol getSymbol() {
    return packageDeclaration.getSymbol();
  }

  /**
   * Callback to be used in ImportDirective#analyze()
   * @param directive must already be analyzed
   */
  public void addImplicitDirective(final ImportDirective directive) {
    implicitDirectives.add(directive);
  }

  private static class SourceFilenameFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
      return name.endsWith(Jooc.INPUT_FILE_SUFFIX);
    }
  }
}
