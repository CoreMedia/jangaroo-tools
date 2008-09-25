package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.*;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Compilation unit sink factory writing all generated classes to one file.
 */
public class MergedOutputCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private File outputFile;
  private CompilationUnitSink sink;

  public MergedOutputCompilationUnitSinkFactory(final File outputFile,
                                                boolean debugKeepSource,
                                                boolean debugKeepLines,
                                                boolean enableAssertions) {
    super(debugKeepLines, outputFile.getParentFile(), enableAssertions, debugKeepSource);
    this.outputFile = outputFile;

    createOutputDirs(outputFile);

    if (outputFile.exists())
      outputFile.delete();

    sink = new CompilationUnitSink() {

      public void writeOutput(CodeGenerator codeGenerator) {

        try {

          JsWriter out = new JsWriter(new FileWriter(outputFile, true));

          out.setEnableAssertions(isEnableAssertions());
          out.setKeepLines(isDebugKeepLines());
          out.setKeepSource(isDebugKeepSource());
          try {
            codeGenerator.generateCode(out);
            out.close();
          } catch (IOException e) {
            Jooc.error("error writing file: '" + outputFile.getAbsolutePath() + "'", e);
            outputFile.delete();
          }
        } catch (IOException e) {
          Jooc.error("cannot open output file for writing: '" + outputFile.getAbsolutePath() + "'", e);
        }

      }
    };
  }

  public CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                        ClassDeclaration classDeclaration, File sourceFile,
                                        final boolean verbose) {
    if (verbose)
      System.out.println("writing " + classDeclaration.getName() + " to file: '" + outputFile.getAbsolutePath() + "'");

    return sink;
  }
}