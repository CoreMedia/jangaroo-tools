package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CodeGenerator;
import net.jangaroo.jooc.IdeDeclaration;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.PackageDeclaration;
import net.jangaroo.jooc.config.JoocOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Compilation unit sink factory for one compilation unit per output file.
 */
public class SingleFileCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private String suffix;

  public SingleFileCompilationUnitSinkFactory(JoocOptions options, File destinationDir, String suffix) {
    super(options, destinationDir);
    this.suffix = suffix;
  }

  protected File getOutputFile(File sourceFile, String[] packageName) {
    return new File(getOutputFileName(sourceFile, packageName));
  }

  protected String getOutputFileName(File sourceFile, String[] packageName) {
    String result;
    if (getOutputDir() == null) {
      result = sourceFile.getAbsoluteFile().getParentFile().getAbsolutePath();
    } else {
      result = getOutputDir().getAbsolutePath();
      StringBuffer buffy = new StringBuffer(result);
      for (String aPackageName : packageName) {
        buffy.append(File.separator);
        buffy.append(aPackageName);
      }
      result = buffy.toString();
    }
    result += File.separator;
    result += sourceFile.getName();
    int dotpos = result.lastIndexOf('.');
    if (dotpos >= 0) {
      result = result.substring(0, dotpos);
    }
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
    if (!classPart.equals(className)) {
      Jooc.warning(primaryDeclaration.getSymbol(),
          "class name should be equal to file name: expected " + classPart + ", found " + className);
    }
    createOutputDirs(outFile);

    return new CompilationUnitSink() {
      public void writeOutput(CodeGenerator codeGenerator) {
        if (verbose)
          System.out.println("writing file: '" + outFile.getAbsolutePath() + "'");

        try {
          JsWriter out = new JsWriter(new FileWriter(outFile));

          out.setOptions(getOptions());
          try {
            codeGenerator.generateCode(out);
            out.close();
          } catch (IOException e) {
            outFile.delete();
            throw Jooc.error("error writing file: '" + outFile.getAbsolutePath() + "'", e);
          }
        } catch (IOException e) {
          throw Jooc.error("cannot open output file for writing: '" + outFile.getAbsolutePath() + "'", e);
        }

      }
    };
  }
}
