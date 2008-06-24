/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class CompilationUnit extends NodeImplBase {

  public PackageDeclaration getPackageDeclaration() {
    return packageDeclaration;
  }

  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  PackageDeclaration packageDeclaration;
  JscSymbol lBrace;
  Directives directives;
  ClassDeclaration classDeclaration;
  JscSymbol rBrace;


  protected File sourceFile;

  protected File destionationDir = null;
  protected File outputFile = null;
  protected JsWriter out;

  public CompilationUnit(PackageDeclaration packageDeclaration, JscSymbol lBrace, Directives directives, ClassDeclaration classDeclaration, JscSymbol rBrace) {
    this.packageDeclaration = packageDeclaration;
    this.lBrace = lBrace;
    this.directives = directives;
    this.classDeclaration = classDeclaration;
    this.rBrace = rBrace;
  }

  public void setDestionationDir(File destionationDir) {
    this.destionationDir = destionationDir;
  }

  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }

  public File getSourceFile() {
    return sourceFile;
  }

  protected File getOutputFile() {
    if (outputFile == null) {
      outputFile = new File(getOutputFileName());
    }
    return outputFile;
  }

  protected void createOutputDirs(File outputFile) {
    File parentDir = outputFile.getParentFile();
    if (!parentDir.exists() && !parentDir.mkdirs()) {
      Jscc.error("cannot create directories '" + parentDir.getAbsolutePath() + "'");
    }
  }

  public void writeOutput(boolean verbose, boolean debugKeepSource, boolean debugKeepLines, boolean enableAssertions) throws Jscc.CompilerError {
    File outFile = getOutputFile();
    String fileName = outFile.getName();
    String classPart = fileName.substring(0, fileName.lastIndexOf('.'));
    String className = classDeclaration.getName();
    if (!classPart.equals(className))
      Jscc.error(classDeclaration,
       "class name must be equal to file name: expected " + classPart + ", found " + className);
    createOutputDirs(outFile);
    try {
      if (verbose)
        System.out.println("writing file: '" + outFile.getAbsolutePath() + "'");
      out = new JsWriter(new FileWriter(outFile));
    } catch (IOException e) {
      Jscc.error("cannot open output file for writing: '" + outFile.getAbsolutePath() + "'", e);
    }
    out.setEnableAssertions(enableAssertions);
    out.setKeepLines(debugKeepLines);
    out.setKeepSource(debugKeepSource);
    try {
      generateCode(out);
      out.close();
    } catch (IOException e) {
      Jscc.error("error writing file: '" + outFile.getAbsolutePath() + "'", e);
      outFile.delete();
    }
  }

  public void generateCode(JsWriter out) throws IOException {
     out.write("joo.Class.prepare(");
     packageDeclaration.generateCode(out);
     out.writeSymbolWhitespace(lBrace);
     if (directives!=null) {
       directives.generateCode(out);
     }
     classDeclaration.generateCode(out);
     out.writeSymbolWhitespace(rBrace);
     out.write(");");
  }

  public void analyze(AnalyzeContext context) {
    packageDeclaration.analyze(context);
    classDeclaration.analyze(context);
  }

  protected String[] getPackageName() {
    return packageDeclaration.getQualifiedName();
  }

  protected String getOutputFileName() {
    String result = "";
    if (destionationDir == null) {
      result = sourceFile.getParentFile().getAbsolutePath();
    } else {
      result = destionationDir.getAbsolutePath();
      String[] packageName = getPackageName();
      for (int i = 0; i < packageName.length; i++) {
        result += File.separator;
        result += new String(packageName[i]);
      }
    }
    result += File.separator;
    result += sourceFile.getName();
    int dotpos = result.lastIndexOf('.');
    if (dotpos >= 0)
      result = result.substring(0, dotpos);
    result += ".js";
    return result;
  }

  public JscSymbol getSymbol() {
     return packageDeclaration.getSymbol();
  }

}
