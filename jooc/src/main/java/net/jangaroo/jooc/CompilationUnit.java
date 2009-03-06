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
import net.jangaroo.jooc.backend.CodeGenerator;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class CompilationUnit extends NodeImplBase implements CodeGenerator {

  public PackageDeclaration getPackageDeclaration() {
    return packageDeclaration;
  }

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
     out.write(Jooc.CLASS_FULLY_QUALIFIED_NAME + ".prepare(");
     packageDeclaration.generateCode(out);
     out.writeSymbolWhitespace(lBrace);
     if (directives!=null) {
       generateCode(directives, out);
     }
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
    if (directives!=null) {
      analyze(this, directives, context);
    }
    primaryDeclaration.analyze(this, context);
    context.leaveScope(packageDeclaration);
    context.leaveScope(globalObject);
    return this;
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
