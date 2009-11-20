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

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.MergedOutputCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.SingleFileCompilationUnitSinkFactory;
import net.jangaroo.jooc.config.JoocCommandLineParser;
import net.jangaroo.jooc.config.JoocConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc {

  public static final int RESULT_CODE_OK = 0;
  public static final int RESULT_CODE_COMPILATION_FAILED = 1;
  public static final int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  public static final int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  public static final int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;

  public static final String JS2_SUFFIX_NO_DOT = "js2";
  public static final String JS2_SUFFIX = "." + JS2_SUFFIX_NO_DOT;
  public static final String AS_SUFFIX_NO_DOT = "as";
  public static final String AS_SUFFIX = "." + AS_SUFFIX_NO_DOT;
  public static final String INPUT_FILE_SUFFIX_NO_DOT = AS_SUFFIX_NO_DOT;
  public static final String INPUT_FILE_SUFFIX = AS_SUFFIX;
  public static final String OUTPUT_FILE_SUFFIX = ".js";
  
  public static final String CLASS_LOADER_NAME = "classLoader";
  public static final String CLASS_LOADER_PACKAGE_NAME = "joo";
  public static final String CLASS_LOADER_FULLY_QUALIFIED_NAME = CLASS_LOADER_PACKAGE_NAME + "." + CLASS_LOADER_NAME;

  private JoocConfiguration config;
  // a hack to always be able to access the current log:
  private static ThreadLocal<CompileLog> logHolder = new ThreadLocal<CompileLog>();
  private CompileLog log;

  private ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

  public Jooc() {
    this(new StdOutCompileLog());
  }

  public Jooc(CompileLog log) {
    this.log = log;
  }

  public int run(JoocConfiguration config) {
    logHolder.set(log);
    this.config = config;
    for (File sourceFile : config.getSourceFiles()) {
      processSource(sourceFile);
    }

    CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(config);

    for (CompilationUnit unit : compilationUnits) {
      unit.analyze(null, new AnalyzeContext(config));
      unit.writeOutput(codeSinkFactory, config.isVerbose());
    }
    int result = log.hasErrors() ? 1 : 0;
    logHolder.remove();
    return result;
  }

  private CompilationUnitSinkFactory createSinkFactory(JoocConfiguration config) {
    CompilationUnitSinkFactory codeSinkFactory;

    if (config.isMergeOutput()) {
      codeSinkFactory = new MergedOutputCompilationUnitSinkFactory(
        config, config.getOutputFile()
      );
    } else {
      codeSinkFactory = new SingleFileCompilationUnitSinkFactory(
        config, config.getOutputDirectory(), OUTPUT_FILE_SUFFIX
      );
    }
    return codeSinkFactory;
  }

  static class CompilerError extends RuntimeException {
    JooSymbol symbol = null;

    CompilerError(String msg) {
      super(msg);
    }

    CompilerError(String msg, Throwable rootCause) {
      super(msg, rootCause);
    }

    CompilerError(JooSymbol symbol, String msg) {
      super(msg);
      this.symbol = symbol;
    }
  }

  public static String getResultCodeDescription(int resultCode) {
    switch (resultCode) {
      case RESULT_CODE_OK: return "ok";
      case RESULT_CODE_COMPILATION_FAILED: return "compilation failed";
      case RESULT_CODE_INTERNAL_COMPILER_ERROR: return "internal compiler error";
      case RESULT_CODE_UNRECOGNIZED_OPTION: return "unrecognized option";
      case RESULT_CODE_MISSING_OPTION_ARGUMENT: return "missing option argument";
      default: return "unknown result code";
    }
  }

  public static void error(String msg) {
    throw new CompilerError(msg);
  }

  static void error(JooSymbol symbol, String msg) {
    throw new CompilerError(symbol, msg);
  }

  public static void error(Node node, String msg) {
    error(node.getSymbol(), msg);
  }

  public static void error(String msg, Throwable t) {
    throw new CompilerError(msg, t);
  }

  public static void warning(JooSymbol symbol, String msg) {
    logHolder.get().warning(symbol, msg);
  }

  protected void processSource(String fileName) {
    processSource(new File(fileName));
  }

  protected void processSource(File file) {
    CompilationUnit unit = parse(file);
    if (unit != null)
      compilationUnits.add(unit);
  }

  protected CompilationUnit parse(File in) {
    if (in.isDirectory())
      error("Input file is a directory: " + in.getAbsolutePath());
    if (!in.getName().endsWith(JS2_SUFFIX) && !in.getName().endsWith(AS_SUFFIX))
      error("Input file must end with '" + JS2_SUFFIX + " or " + AS_SUFFIX + "': " + in.getAbsolutePath());
    Scanner s;
    if (config.isVerbose())
      System.out.println("Parsing " + in.getAbsolutePath());
    try {
      s = new Scanner(new FileReader(in));
    } catch (FileNotFoundException e) {
      throw new CompilerError("Input file not found: " + in.getAbsolutePath());
    }
    s.setFileName(in.getAbsolutePath());
    parser p = new parser(s);
    p.setCompileLog(log);
    try {
      Symbol tree = p.parse();
      CompilationUnit unit = (CompilationUnit) tree.value;
      unit.setSourceFile(in);
      return unit;
    } catch (Scanner.ScanError se) {
      log.error(se.sym, se.getMessage());
      return null;
    } catch (parser.FatalSyntaxError e) {
      // message already logged in parser
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
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
      JoocCommandLineParser parser = new JoocCommandLineParser();
      config = parser.parse(argv);
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
    } catch (CompilerError e) {
      if (e.symbol != null)
        log.error(e.symbol, e.getMessage());
      else
        log.error(e.getMessage());
      return RESULT_CODE_COMPILATION_FAILED;
    } catch (Exception e) {
      e.printStackTrace();
      return RESULT_CODE_INTERNAL_COMPILER_ERROR;
    }
    return RESULT_CODE_OK;
  }

  public static void main(String[] argv) {
    Jooc compiler = new Jooc();
    int result = compiler.run(argv);
    if (result != 0)
      System.exit(result);
  }

}
