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

import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.backend.CompilationUnitSink;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.MergedOutputCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.SingleFileCompilationUnitSinkFactory;
import net.jangaroo.jooc.config.JoocCommandLineParser;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc extends JangarooParser {

  public static final int RESULT_CODE_OK = 0;
  public static final int RESULT_CODE_COMPILATION_FAILED = 1;
  public static final int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  public static final int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  public static final int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;

  public static final String INPUT_FILE_SUFFIX = AS_SUFFIX;
  public static final String OUTPUT_FILE_SUFFIX = ".js";

  public static final String CLASS_LOADER_NAME = "classLoader";
  public static final String CLASS_LOADER_PACKAGE_NAME = "joo";
  public static final String CLASS_LOADER_FULLY_QUALIFIED_NAME = CLASS_LOADER_PACKAGE_NAME + "." + CLASS_LOADER_NAME;

  private List<CompilationUnit> compileQueue = new ArrayList<CompilationUnit>();

  public Jooc() {
    this(new StdOutCompileLog());
  }

  public Jooc(CompileLog log) {
    super(log);
  }

  public int run(JoocConfiguration config) {
    try {
      return run1(config);
    } catch (CompilerError e) {
      if (e.getSymbol() != null) {
        log.error(e.getSymbol(), e.getMessage());
      } else {
        log.error(e.getMessage());
      }
      return RESULT_CODE_COMPILATION_FAILED;
    } catch (Exception e) {
      e.printStackTrace();
      return RESULT_CODE_INTERNAL_COMPILER_ERROR;
    }
  }

  private List<File> canoncicalSourcePath = new ArrayList<File>();

  private boolean isParent(File dir, File file) throws IOException {
    File parent = file.getParentFile();
    while (parent != null) {
      if (parent.equals(dir)) {
        return true;
      }
      parent = parent.getParentFile();
    }
    return false;
  }

  protected File findSourceDir(final File file) throws IOException {
    File canonicalFile = file.getCanonicalFile();
    for (File sourceDir : canoncicalSourcePath) {
      if (isParent(sourceDir, canonicalFile)) {
        return sourceDir;
      }
    }
    return null;
  }

  private int run1(JoocConfiguration config) {
    for (File sourceDir : config.getSourcePath()) {
      try {
        canoncicalSourcePath.add(sourceDir.getCanonicalFile());
      } catch (IOException e) {
        throw new CompilerError("Cannot canonicalize source path dir: " + sourceDir.getAbsolutePath());
      }
    }
    InputSource sourcePathInputSource;
    InputSource classPathInputSource;
    try {
      sourcePathInputSource = PathInputSource.fromFiles(canoncicalSourcePath, new String[]{""});
      classPathInputSource = PathInputSource.fromFiles(config.getClassPath(), new String[]{"", JOO_API_IN_JAR_DIRECTORY_PREFIX});
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }

    setUp(config, sourcePathInputSource, classPathInputSource);

    try {
      for (File sourceFile : config.getSourceFiles()) {
        processSource(sourceFile);
      }

      CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(config, false);
      CompilationUnitSinkFactory apiSinkFactory = null;
      if (config.isGenerateApi()) {
        apiSinkFactory = createSinkFactory(config, true);
      }
      for (CompilationUnit unit : compileQueue) {
        unit.analyze(null, new AnalyzeContext(config));
        writeOutput(unit, codeSinkFactory, config.isVerbose());
        if (config.isGenerateApi()) {
          writeOutput(unit, apiSinkFactory, config.isVerbose());
        }
      }
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }
    int result = log.hasErrors() ? 1 : 0;
    tearDown();
    return result;
  }

  public void writeOutput(CompilationUnit compilationUnit,
                          CompilationUnitSinkFactory writerFactory,
                          boolean verbose) throws CompilerError {
    File sourceFile = ((FileInputSource) compilationUnit.getSource()).getFile();
    CompilationUnitSink sink = writerFactory.createSink(
      compilationUnit.getPackageDeclaration(), compilationUnit.getPrimaryDeclaration(),
      sourceFile, verbose);

    sink.writeOutput(compilationUnit);
  }

  private CompilationUnitSinkFactory createSinkFactory(JoocConfiguration config, final boolean generateActionScriptApi) {
    CompilationUnitSinkFactory codeSinkFactory;

    if (!generateActionScriptApi && config.isMergeOutput()) {
      codeSinkFactory = new MergedOutputCompilationUnitSinkFactory(
        config, config.getOutputFile()
      );
    } else {
      File outputDirectory = generateActionScriptApi ? config.getApiOutputDirectory() : config.getOutputDirectory();
      final String suffix = generateActionScriptApi ? AS_SUFFIX : OUTPUT_FILE_SUFFIX;
      codeSinkFactory = new SingleFileCompilationUnitSinkFactory(config, outputDirectory, generateActionScriptApi, suffix);
    }
    return codeSinkFactory;
  }

  public static String getResultCodeDescription(int resultCode) {
    switch (resultCode) {
      case RESULT_CODE_OK:
        return "ok";
      case RESULT_CODE_COMPILATION_FAILED:
        return "compilation failed";
      case RESULT_CODE_INTERNAL_COMPILER_ERROR:
        return "internal compiler error";
      case RESULT_CODE_UNRECOGNIZED_OPTION:
        return "unrecognized option";
      case RESULT_CODE_MISSING_OPTION_ARGUMENT:
        return "missing option argument";
      default:
        return "unknown result code";
    }
  }

  protected void processSource(File file) throws IOException {
    if (file.isDirectory()) {
      throw error("Input file is a directory: " + file.getAbsolutePath());
    }
    CompilationUnit unit = importSource(new FileInputSource(findSourceDir(file), file));
    if (unit != null) {
      compileQueue.add(unit);
    }
  }

  protected void printVersion() {
    String pkgName = "net.jangaroo.jooc";
    Package pkg = Package.getPackage(pkgName);
    String specTitle = pkg.getSpecificationTitle();
    if (specTitle == null) {
      System.out.println("cannot retrieve package version information for " + pkgName);
      return;
    }
    String specVendor = pkg.getSpecificationVendor();
    String specVersion = pkg.getSpecificationVersion();
    String implTitle = pkg.getImplementationTitle();
    String implVersion = pkg.getImplementationVersion();
    System.out.println(specTitle + " version " + specVersion);
    System.out.println(implTitle + " (build " + implVersion + ")");
    System.out.println(specVendor);
  }

  public int run(String[] argv) {
    try {
      JoocCommandLineParser commandLineParser = new JoocCommandLineParser();
      JoocConfiguration config = commandLineParser.parse(argv);
      if (config != null) {
        if (config.isVersion()) {
          printVersion();
        } else {
          return run(config);
        }
      }
    } catch (JoocCommandLineParser.CommandLineParseException e) {
      System.out.println(e.getMessage());
      return e.getExitCode();
    }
    return RESULT_CODE_OK;
  }

  public static void main(String[] argv) {
    Jooc compiler = new Jooc();
    int result = compiler.run(argv);
    if (result != 0) {
      System.exit(result);
    }
  }

}
