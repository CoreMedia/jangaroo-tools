package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.*;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Compilation unit sink factory for one compilation unit per output file.
 */
public class SingleFileCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private String suffix;

  public SingleFileCompilationUnitSinkFactory(File destinationDir,
                                              String suffix,
                                              boolean debugKeepSource,
                                              boolean debugKeepLines,
                                              boolean enableAssertions) {
    super(debugKeepLines, destinationDir, enableAssertions, debugKeepSource);
    this.suffix = suffix;
  }

  protected File getOutputFile(File sourceFile, String[] packageName) {
    return new File(getOutputFileName(sourceFile, packageName));
  }

  protected String getOutputFileName(File sourceFile, String[] packageName) {
    String result;
    if (getOutputDir() == null) {
      result = sourceFile.getParentFile().getAbsolutePath();
    } else {
      result = getOutputDir().getAbsolutePath();
      for (String aPackageName : packageName) {
        result += File.separator;
        result += aPackageName;
      }
    }
    result += File.separator;
    result += sourceFile.getName();
    int dotpos = result.lastIndexOf('.');
    if (dotpos >= 0)
      result = result.substring(0, dotpos);
    result += suffix;
    return result;
  }

  public CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                               IdeDeclaration primaryDeclaration, File sourceFile,
                               final boolean verbose) {
    final File outFile = getOutputFile(sourceFile, packageDeclaration.getQualifiedName());
    String fileName = outFile.getName();
    String classPart = fileName.substring(0, fileName.lastIndexOf('.'));

    String className = primaryDeclaration.getName();
    if (!classPart.equals(className))
      Jooc.error(primaryDeclaration,
       "class name must be equal to file name: expected " + classPart + ", found " + className);

    createOutputDirs(outFile);

    return new CompilationUnitSink() {
      public void writeOutput(CodeGenerator codeGenerator) {
        if (verbose)
          System.out.println("writing file: '" + outFile.getAbsolutePath() + "'");

        try {
          JsWriter out = new JsWriter(new FileWriter(outFile));

          out.setEnableAssertions(isEnableAssertions());
          out.setKeepLines(isDebugKeepLines());
          out.setKeepSource(isDebugKeepSource());
          try {
            codeGenerator.generateCode(out);
            out.close();
          } catch (IOException e) {
            Jooc.error("error writing file: '" + outFile.getAbsolutePath() + "'", e);
            outFile.delete();
          }
        } catch (IOException e) {
          Jooc.error("cannot open output file for writing: '" + outFile.getAbsolutePath() + "'", e);
        }

      }
    };
  }
}
