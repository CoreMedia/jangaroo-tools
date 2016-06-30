package net.jangaroo.jooc.mvnplugin.util;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A facade to call the Google Closure compiler
 */
public class ClosureExecutor {

  private static final String OUTPUT_CHARSET = "UTF-8";
  private static final String SOURCE_MAP_EXTENSION = ".map";

  /**
   * Calls the Closure compiler with
   *   --js {sources}
   *   --create_source_map {output.name}.map
   *   --source_map_format=V3
   *   --compilation_level WHITESPACE_ONLY
   *   --js_output_file {output.path}
   */
  static public void compile(Set<File> sources, File output, Log log) throws MojoExecutionException {

    try {
      CompilerOptions options = new CompilerOptions();
      CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);
      options.setOutputCharset(OUTPUT_CHARSET);

      File sourceMap = new File(output.getPath() + SOURCE_MAP_EXTENSION);
      options.setSourceMapFormat(SourceMap.Format.V3);
      options.setSourceMapOutputPath(sourceMap.getPath());
      String outputFilePath = output.getPath();
      String prefix = outputFilePath.substring(0, outputFilePath.lastIndexOf(File.separator) + 1);
      if (File.separatorChar == '\\') {
        prefix = prefix.replace('\\', '/');
      }
      options.setSourceMapLocationMappings(Collections.singletonList(new SourceMap.LocationMapping(prefix, "")));

      List<SourceFile> sourceFiles = new ArrayList<>();
      for (File source : sources) {
        SourceFile sourceFile = SourceFile.fromFile(source);
        sourceFiles.add(sourceFile);
      }
      Compiler compiler = new Compiler();
      log.info(String.format("Calling Closure compiler w/ %d source files", sourceFiles.size()));
      final Result result = compiler.compile(Collections.<SourceFile>emptyList(), sourceFiles, options);

      if (compiler.hasErrors()) {
        throw new MojoExecutionException(compiler.getErrors()[0].description);
      }

      try (PrintWriter writer = new PrintWriter(output)) {
        writer.append(compiler.toSource());
        writer.printf("\n//# sourceMappingURL=%s\n", sourceMap.getName());
      }
      StringBuffer sourceMapContent = new StringBuffer();
      result.sourceMap.appendTo(sourceMapContent, output.getName());
      try (Writer writer = new FileWriter(sourceMap)) {
        writer.append(sourceMapContent.toString());
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Error calling Closure compiler", e);
    }
  }

}
