package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitRegistry;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.TransitiveAstVisitor;
import net.jangaroo.jooc.config.JoocOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Compilation unit sink factory writing all generated classes to one file.
 */
public class MergedOutputCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private File outputFile;
  private CompilationUnitSink sink;

  public MergedOutputCompilationUnitSinkFactory(JoocOptions options, final File outputFile, final CompilationUnitResolver compilationUnitModelResolver, final CompilationUnitRegistry compilationUnitRegistry) {
    super(options, outputFile.getAbsoluteFile().getParentFile());
    this.outputFile = outputFile;

    createOutputDirs(outputFile);

    if (outputFile.exists()) {
      if(!outputFile.delete()) {
        throw JangarooParser.error("error deleting file: '" + outputFile.getAbsolutePath() + "'", outputFile);
      }
    }

    sink = new CompilationUnitSink() {

      public File writeOutput(CompilationUnit compilationUnit) {

        try {
          JsWriter out = new JsWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true), "UTF-8"));
          try {
            try {
              out.setOptions(getOptions());

              compilationUnit.visit(new TransitiveAstVisitor(new EmbeddedAssetResolver(compilationUnit, compilationUnitRegistry)));
              compilationUnit.visit(new JsCodeGenerator(out, compilationUnitModelResolver));
            } finally {
              out.close();
            }
          } catch (IOException e) {
            //noinspection ResultOfMethodCallIgnored
            outputFile.delete(); // NOSONAR
            throw JangarooParser.error("error writing file: '" + outputFile.getAbsolutePath() + "'", outputFile, e);
          }
        } catch (IOException e) {
          throw JangarooParser.error("cannot open output file for writing: '" + outputFile.getAbsolutePath() + "'", outputFile, e);
        }

        return outputFile;
      }
    };
  }

  public CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                        IdeDeclaration primaryDeclaration, File sourceFile,
                                        final boolean verbose) {
    if (verbose) {
      System.out.println("writing " + primaryDeclaration.getName() + " to file: '" + outputFile.getAbsolutePath() + "'"); // NOSONAR this is a cmd line tool
    }

    return sink;
  }
}