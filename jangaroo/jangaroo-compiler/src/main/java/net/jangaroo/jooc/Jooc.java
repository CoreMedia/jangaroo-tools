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

import net.jangaroo.jooc.api.CompilationResult;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.backend.CompilationUnitSink;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.MergedOutputCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.SingleFileCompilationUnitSinkFactory;
import net.jangaroo.jooc.cli.CommandLineParseException;
import net.jangaroo.jooc.cli.JoocCommandLineParser;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.PublicApiViolationsMode;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc extends JangarooParser implements net.jangaroo.jooc.api.Jooc {

  public static final String CLASS_LOADER_NAME = "classLoader";
  public static final String CLASS_LOADER_PACKAGE_NAME = "joo";
  public static final String CLASS_LOADER_FULLY_QUALIFIED_NAME = CLASS_LOADER_PACKAGE_NAME + "." + CLASS_LOADER_NAME;
  public static final String PUBLIC_API_EXCLUSION_ANNOTATION_NAME = "ExcludeClass";

  private List<CompilationUnit> compileQueue = new ArrayList<CompilationUnit>();

  public Jooc() {
  }

  public Jooc(JoocConfiguration config) {
    this(config, new StdOutCompileLog());
  }

  public Jooc(JoocConfiguration config, CompileLog log) {
    super(config, log);
  }

  public JoocConfiguration getConfig() {
    return (JoocConfiguration) super.getConfig();
  }

  @Override
  public void setConfig(JoocConfiguration config) {
    super.setConfig(config);
  }

  @Override
  public CompilationResult run() {
    try {
      return run1();
    } catch (CompilerError e) {
      if (e.getSymbol() != null) {
        log.error(e.getSymbol(), e.getMessage());
      } else {
        log.error(e.getMessage());
      }
      return new CompilationResultImpl(CompilationResult.RESULT_CODE_COMPILATION_FAILED);
    } catch (Exception e) {
      e.printStackTrace(); // NOSONAR something serious happened and we cannot get it into the ordinary log
      log.error(e.getMessage());
      return new CompilationResultImpl(CompilationResult.RESULT_CODE_INTERNAL_COMPILER_ERROR);
    }
  }

  private CompilationResult run1() {
    InputSource sourcePathInputSource;
    InputSource classPathInputSource;
    try {
      sourcePathInputSource = PathInputSource.fromFiles(getConfig().getSourcePath(), new String[]{""});
      classPathInputSource = PathInputSource.fromFiles(getConfig().getClassPath(), new String[]{"", JOO_API_IN_JAR_DIRECTORY_PREFIX});
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }

    setUp(sourcePathInputSource, classPathInputSource);

    HashMap<File, File> outputFileMap = new HashMap<File, File>();
    try {
      for (File sourceFile : getConfig().getSourceFiles()) {
        processSource(sourceFile);
      }

      CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(getConfig(), false);
      CompilationUnitSinkFactory apiSinkFactory = null;
      if (getConfig().isGenerateApi()) {
        apiSinkFactory = createSinkFactory(getConfig(), true);
      }
      for (CompilationUnit unit : compileQueue) {
        unit.analyze(null);
        if (getConfig().getPublicApiViolationsMode() != PublicApiViolationsMode.ALLOW) {
          reportPublicApiViolations(unit);
        }
        File sourceFile = ((FileInputSource)unit.getSource()).getFile();
        File outputFile = writeOutput(sourceFile, unit, codeSinkFactory, getConfig().isVerbose());
        outputFileMap.put(sourceFile, outputFile);
        if (getConfig().isGenerateApi()) {
          writeOutput(sourceFile, unit, apiSinkFactory, getConfig().isVerbose());
        }
      }
      int result = log.hasErrors() ? CompilationResult.RESULT_CODE_COMPILATION_FAILED : CompilationResult.RESULT_CODE_OK;
      return new CompilationResultImpl(result, outputFileMap);
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    } finally {
      tearDown();
    }
  }

  private void reportPublicApiViolations(CompilationUnit unit) {
    for (CompilationUnit compilationUnit : unit.getDependenciesAsCompilationUnits()) {
      if (compilationUnit.getSource() instanceof ZipEntryInputSource
        && compilationUnit.getAnnotation(PUBLIC_API_EXCLUSION_ANNOTATION_NAME) != null) {
        String msg = "PUBLIC API VIOLATION: " + compilationUnit.getPrimaryDeclaration().getQualifiedNameStr();
        File sourceFile = new File(unit.getSymbol().getFileName());
        if (getConfig().getPublicApiViolationsMode() == PublicApiViolationsMode.WARN) {
          JangarooParser.warning(msg, sourceFile);
        } else {
          throw JangarooParser.error(msg, sourceFile);
        }
      }
    }
  }

  public File writeOutput(File sourceFile,
                          CompilationUnit compilationUnit,
                          CompilationUnitSinkFactory writerFactory,
                          boolean verbose) throws CompilerError {
    CompilationUnitSink sink = writerFactory.createSink(
            compilationUnit.getPackageDeclaration(), compilationUnit.getPrimaryDeclaration(),
            sourceFile, verbose);

    return sink.writeOutput(compilationUnit);
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
      case CompilationResult.RESULT_CODE_OK:
        return "ok";
      case CompilationResult.RESULT_CODE_COMPILATION_FAILED:
        return "compilation failed";
      case CompilationResult.RESULT_CODE_INTERNAL_COMPILER_ERROR:
        return "internal compiler error";
      case CompilationResult.RESULT_CODE_UNRECOGNIZED_OPTION:
        return "unrecognized option";
      case CompilationResult.RESULT_CODE_MISSING_OPTION_ARGUMENT:
        return "missing option argument";
      case CompilationResult.RESULT_CODE_ILLEGAL_OPTION_VALUE:
        return "illegal option value";
      default:
        return "unknown result code";
    }
  }

  protected void processSource(File file) throws IOException {
    if (file.isDirectory()) {
      throw error("Input file is a directory.", file);
    }
    CompilationUnit unit = importSource(new FileInputSource(getConfig().findSourceDir(file), file));
    if (unit != null) {
      compileQueue.add(unit);
    }
  }



  public static int run(String[] argv, CompileLog log) {
    try {
      JoocCommandLineParser commandLineParser = new JoocCommandLineParser();
      JoocConfiguration config = commandLineParser.parse(argv);
      if (config != null) {
        return new Jooc(config, log).run().getResultCode();
      }
    } catch (CommandLineParseException e) {
      System.out.println(e.getMessage()); // NOSONAR this is a commandline tool
      return e.getExitCode();
    }
    return CompilationResult.RESULT_CODE_OK;
  }

  public static void main(String[] argv) {
    int result = run(argv, new StdOutCompileLog());
    if (result != 0) {
      System.exit(result);
    }
  }
}
