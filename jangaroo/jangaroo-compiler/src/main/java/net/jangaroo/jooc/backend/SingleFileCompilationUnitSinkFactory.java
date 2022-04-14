package net.jangaroo.jooc.backend;

import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import net.jangaroo.jooc.CompilationUnitRegistry;
import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.TransitiveAstVisitor;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.JoocOptions;
import net.jangaroo.jooc.util.FilePosition;
import net.jangaroo.properties.PropcHelper;
import net.jangaroo.utils.CompilerUtils;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static net.jangaroo.jooc.Jooc.NATIVE_ANNOTATION_NAME;

/**
 * Compilation unit sink factory for one compilation unit per output file.
 */
public class SingleFileCompilationUnitSinkFactory extends AbstractCompilationUnitSinkFactory {

  private final CompilationUnitRegistry compilationUnitRegistry;
  private final String suffix;
  private final String nativeSuffix;
  private final boolean generateApi;
  private final CompilationUnitResolver compilationUnitModelResolver;

  public SingleFileCompilationUnitSinkFactory(JoocOptions options, File destinationDir, boolean generateApi, String suffix, String nativeSuffix, CompilationUnitResolver compilationUnitModelResolver, CompilationUnitRegistry compilationUnitRegistry) {
    super(options, destinationDir);
    this.suffix = suffix;
    this.nativeSuffix = nativeSuffix;
    this.generateApi = generateApi;
    this.compilationUnitModelResolver = compilationUnitModelResolver;
    this.compilationUnitRegistry = compilationUnitRegistry;
  }

  private File getOutputFile(File sourceFile, IdeDeclaration primaryDeclaration) {
    File outputDirectory = getOutputDir();
    if (outputDirectory == null) {
      outputDirectory = sourceFile.getAbsoluteFile().getParentFile();
      return new File(outputDirectory, CompilerUtils.qNameFromFile(outputDirectory, sourceFile) + suffix);
    }
    String qName = generateApi
            ? primaryDeclaration.getQualifiedNameStr() : primaryDeclaration.getExtNamespaceRelativeTargetQualifiedNameStr();
    if (!generateApi && !options.isMigrateToTypeScript() && qName.endsWith(CompilerUtils.PROPERTIES_CLASS_SUFFIX)) {
      Locale locale = PropcHelper.computeLocale(qName);
      File propertiesOutputDirectory = ((JoocConfiguration) options).getLocalizedOutputDirectory();
      if (propertiesOutputDirectory == null) {
        propertiesOutputDirectory = new File(((JoocConfiguration) options).getOutputDirectory().getParentFile(), "locale");
      }
      outputDirectory = new File(propertiesOutputDirectory, PropcHelper.localeOrDefaultLocaleString(locale));
      if (locale != null) {
        qName = PropcHelper.computeBaseClassName(qName);
      }
    }
    String suffix = nativeSuffix != null &&
            (primaryDeclaration.isNative() || primaryDeclaration.getAnnotation(NATIVE_ANNOTATION_NAME) != null)
            ? nativeSuffix : this.suffix;
    return CompilerUtils.fileFromQName(qName, outputDirectory, suffix);
  }

  public CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                                        IdeDeclaration primaryDeclaration, final File sourceFile,
                                        final boolean verbose) {
    final File outFile = getOutputFile(sourceFile, primaryDeclaration);
    createOutputDirs(outFile);

    return compilationUnit -> {
      if (verbose) {
        System.out.println("writing file: '" + outFile.getAbsolutePath() + "'"); // NOSONAR this is a cmd line tool
      }

      try {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8);
        try {
          if (generateApi) {
            ApiModelGenerator apiModelGenerator = new ApiModelGenerator(isExcludeClassByDefault(getOptions()));
            apiModelGenerator.generateModel(compilationUnit).visit(new ActionScriptCodeGeneratingModelVisitor(writer));
          } else {
            JsWriter out = new JsWriter(writer);
            String codeSuffix = "";
            //noinspection TryFinallyCanBeTryWithResources
            try {
              out.setOptions(getOptions());
              compilationUnit.visit(Jooc.OUTPUT_FILE_SUFFIX.equals(suffix) ? new JsCodeGenerator(out, compilationUnitModelResolver, new JsModuleResolver(compilationUnitModelResolver)) : new TypeScriptCodeGenerator(new TypeScriptModuleResolver(compilationUnitModelResolver), out, compilationUnitModelResolver));
              if (options.isGenerateSourceMaps()) {
                codeSuffix = generateSourceMap(out, outFile);
              }
            } finally {
              out.close(codeSuffix);
            }
            if (options.isGenerateSourceMaps()) {
              FileUtils.getFileUtils().copyFile(sourceFile, new File(outFile.getParentFile(), sourceFile.getName()));
            }
          }
        } catch (IOException e) {
          //noinspection ResultOfMethodCallIgnored
          outFile.delete(); // NOSONAR
          throw JangarooParser.error("error writing file: '" + outFile.getAbsolutePath() + "'", outFile, e);
        }
      } catch (IOException e) {
        throw JangarooParser.error("cannot open output file for writing: '" + outFile.getAbsolutePath() + "'", outFile, e);
      }

      return outFile;
    };
  }

  private String generateSourceMap(JsWriter out, File outFile) throws IOException {
    SourceMapGenerator sourceMapGenerator = SourceMapGeneratorFactory.getInstance(SourceMapFormat.V3);
    sourceMapGenerator.validate(true);
    for (JsWriter.SymbolToOutputFilePosition entry : out.getSourceMappings()) {
      String sourceFilename = entry.getSymbol().getFileName();
      sourceFilename = sourceFilename.substring(sourceFilename.lastIndexOf(File.separatorChar) + 1);
      // for debugging:
//                  System.err.println("*** Created mapping " +
//                    entry.getSymbol().getFileName() + ":" + entry.getSourceFilePosition() +
//                    " ->\n   " + outFile.getAbsolutePath() + ":" + entry.getOutputFileStartPosition());
      sourceMapGenerator.addMapping(
              sourceFilename,
              entry.getSymbol().getText(),
              googleFilePosition(entry.getSourceFilePosition()),
              googleFilePosition(entry.getOutputFileStartPosition()),
              googleFilePosition(entry.getOutputFileEndPosition()));
    }
    String sourceMapFilename = outFile.getAbsolutePath() + ".map";
//                System.out.println("*** Creating source map file " + sourceMapFilename);
    try (FileWriter sourceMapWriter = new FileWriter(sourceMapFilename)) {
      sourceMapGenerator.appendTo(sourceMapWriter, outFile.getName());
    }
    return "//# sourceMappingURL=" + outFile.getName() + ".map";
  }

  private com.google.debugging.sourcemap.FilePosition googleFilePosition(FilePosition position) {
    return new com.google.debugging.sourcemap.FilePosition(position.getLine(), position.getColumn());
  }

  private static boolean isExcludeClassByDefault(JoocOptions options) {
    try {
      return options.isExcludeClassByDefault();
    } catch (IncompatibleClassChangeError e) {
      // ignore, old front ends did not know that you can exclude classes by default
      return false;
    }

  }
}
