package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.*;
import net.jangaroo.jooc.config.JoocOptions;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Compilation unit sink factory writing all generated classes to one file.
 */
public class MergedOutputCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private File outputFile;
  private CompilationUnitSink sink;

  public MergedOutputCompilationUnitSinkFactory(JoocOptions options, final File outputFile) {
    super(options, outputFile.getAbsoluteFile().getParentFile());
    this.outputFile = outputFile;

    createOutputDirs(outputFile);

    if (outputFile.exists())
      outputFile.delete();

    sink = new CompilationUnitSink() {

      public void writeOutput(CodeGenerator codeGenerator) {

        try {

          JsWriter out = new JsWriter(new FileWriter(outputFile, true));

          out.setOptions(getOptions());
          try {
            codeGenerator.generateCode(out);
            out.close();
          } catch (IOException e) {
            outputFile.delete();
            throw Jooc.error("error writing file: '" + outputFile.getAbsolutePath() + "'", e);
          }
        } catch (IOException e) {
          throw Jooc.error("cannot open output file for writing: '" + outputFile.getAbsolutePath() + "'", e);
        }

      }
    };
  }

  public CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                        IdeDeclaration primaryDeclaration, File sourceFile,
                                        final boolean verbose) {
    if (verbose)
      System.out.println("writing " + primaryDeclaration.getName() + " to file: '" + outputFile.getAbsolutePath() + "'");

    return sink;
  }
}