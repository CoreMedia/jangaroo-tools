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
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class CompilationUnit extends NodeImplBase implements CodeGenerator {

  public PackageDeclaration getPackageDeclaration() {
    return packageDeclaration;
  }

  private Set<String> samePackageSymbols;
  PackageDeclaration packageDeclaration;
  JooSymbol lBrace;
  List<Node> directives;
  IdeDeclaration primaryDeclaration;
  JooSymbol rBrace;


  protected File sourceFile;

  protected JsWriter out;

  public CompilationUnit(PackageDeclaration packageDeclaration, JooSymbol lBrace, List<Node> directives, IdeDeclaration primaryDeclaration, JooSymbol rBrace) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.primaryDeclaration = primaryDeclaration;
    this.rBrace = rBrace;
  }

  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
    File folder = sourceFile.getParentFile();
    String[] symbols = folder.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(Jooc.INPUT_FILE_SUFFIX);
      }
    });
    samePackageSymbols = new HashSet<String>(symbols.length);
    for (String symbol : symbols) {
      samePackageSymbols.add(withoutAS(symbol));
    }
  }

  private static String withoutAS(String name) {
    return name.substring(0,name.length()- Jooc.INPUT_FILE_SUFFIX.length());
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
     if (directives!=null) {
       generateCode(directives, out);
     }
     out.write("\"\"],");
     primaryDeclaration.generateCode(out);
     out.writeSymbolWhitespace(rBrace);
     out.write(");");
  }

  public Node analyze(Node parentNode, AnalyzeContext context) {
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
      "encodeURIComponent",
      "decodeURIComponent",
      "trace",
      "joo"});
    super.analyze(parentNode, context);
    context.enterScope(packageDeclaration);
    packageDeclaration.analyze(this, context);
    analyzeDirectives(context);
    primaryDeclaration.analyze(this, context);
    context.leaveScope(packageDeclaration);
    context.leaveScope(globalObject);
    return this;
  }

  private void analyzeDirectives(AnalyzeContext context) {
    Ide packageIde = context.getCurrentPackage().getIde();
    Scope packageScope = context.getScope();
    List<Node> directives = new ArrayList<Node>();
    for (String samePackageSymbol : samePackageSymbols) {
      ImportDirective importDirective = new ImportDirective(packageIde, samePackageSymbol);
      packageScope.declareIde(samePackageSymbol, importDirective);
      directives.add(0,importDirective);
    }
    if (this.directives==null) {
      this.directives = directives;
    } else {
      this.directives.addAll(0, directives);
    }
    this.directives = analyze(this, this.directives, context);
  }

  private void declareIdes(Scope scope, String[] identifiers) {
    for (String identifier: identifiers) {
      scope.declareIde(identifier, new IdeType(identifier));
    }
  }

  public JooSymbol getSymbol() {
     return packageDeclaration.getSymbol();
  }

}
