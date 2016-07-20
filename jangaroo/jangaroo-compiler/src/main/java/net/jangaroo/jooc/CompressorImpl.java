package net.jangaroo.jooc;

import com.google.common.io.LineReader;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.WarningLevel;
import net.jangaroo.jooc.api.Compressor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A facade to call the Google Closure compiler
 */
public class CompressorImpl implements Compressor {

  private static final String OUTPUT_CHARSET = "UTF-8";
  private static final String SOURCE_MAP_EXTENSION = ".map";

  /**
   * Calls the Google Closure compiler with
   * --js {sources}
   * --create_source_map {output.name}.map
   * --source_map_format=V3
   * --compilation_level WHITESPACE_ONLY
   * --js_output_file {output.path}
   */
  @Override
  public void compress(Collection<File> sources, File output) throws IOException {

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

    WarningLevel level = WarningLevel.QUIET;
    level.setOptionsForWarningLevel(options);

    List<SourceFile> sourceFiles = new ArrayList<>();
    for (File source : sources) {
      SourceFile sourceFile = SourceFile.fromFile(source);
      sourceFiles.add(sourceFile);
    }
    Compiler compiler = new Compiler();
    final Result result = compiler.compile(Collections.<SourceFile>emptyList(), sourceFiles, options);

    if (compiler.hasErrors()) {
      throw new IllegalArgumentException(compiler.getErrors()[0].description);
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
  }

  @Override
  public void compressFileList(File fileList, File output) throws IOException {

    try (Reader reader = new FileReader(fileList)) {
      LineReader in = new LineReader(reader);
      List<File> files = new ArrayList<>();
      String line;
      while ((line = in.readLine()) != null) {
        String filename = line.trim();
        if (!filename.isEmpty()) {
          files.add(new File(filename));
        }
      }
      compress(files, output);
    }
  }
  }
