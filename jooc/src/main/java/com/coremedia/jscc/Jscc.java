/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc;

import java_cup.runtime.Symbol;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Andreas Gawecki
 */
public class Jscc {

  public static final int RESULT_CODE_OK = 0;
  public static final int RESULT_CODE_COMPILATION_FAILED = 1;
  public static final int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  public static final int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  public static final int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;

  public static final String JS2_SUFFIX = ".js2";
  public static final String JS_SUFFIX = ".js";
  public static final String CLASS_CLASS_NAME = "Class";
  public static final String CLASS_PACKAGE_NAME = "joo";
  public static final String CLASS_FULLY_QUALIFIED_NAME = CLASS_PACKAGE_NAME + "." + CLASS_CLASS_NAME;

  private String destionationDirName;
  private File destinationDir;
  private boolean debugLines;
  private boolean debugSource;
  private boolean debugVars;
  private boolean debugMembers;
  private boolean enableAssertions;
  private boolean verbose;

  private CompileLog log = new CompileLog();

  private ArrayList sourceFileNames = new ArrayList();

  public ArrayList/*CompilationUnit*/ getCompilationUnits() {
    return compilationUnits;
  }

  private ArrayList compilationUnits = new ArrayList();


  static class CompilerError extends RuntimeException {
    JscSymbol symbol = null;

    CompilerError(String msg) {
      super(msg);
    }

    CompilerError(String msg, Throwable rootCause) {
      super(msg, rootCause);
    }

    CompilerError(JscSymbol symbol, String msg) {
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

  public void addSourceFile(String fileName) {
    sourceFileNames.add(fileName);
  }

  static void error(String msg) {
    throw new CompilerError(msg);
  }

  static void error(JscSymbol symbol, String msg) {
    throw new CompilerError(symbol, msg);
  }

  static void error(Node node, String msg) {
    error(node.getSymbol(), msg);
  }

  static void error(String msg, Throwable t) {
    throw new CompilerError(msg, t);
  }

  protected void processSource(String fileName) {
    CompilationUnit unit = parse(fileName);
    if (unit != null)
      compilationUnits.add(unit);
  }

  protected CompilationUnit parse(String fileName) {
    File in = new File(fileName);
    if (in.isDirectory())
      error("Input file is a directory: " + in.getAbsolutePath());
    if (!in.getName().endsWith(JS2_SUFFIX))
      error("Input file must end with '" + JS2_SUFFIX + "': " + in.getAbsolutePath());
    Scanner s;
    if (verbose)
      System.out.println("Parsing " + fileName);
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
      if (destinationDir != null)
        unit.setDestionationDir(destinationDir);
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

  protected void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("jooc [options] <file> ...", options);
  }

  protected void printVersion() {
    String pkgName = "com.coremedia.jscc";
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

  /** @noinspection AccessStaticViaInstance*/
  public int run(String[] argv) {
    try {
      Option help = new Option("help", "print this message");
      Option version = OptionBuilder
              .withDescription("print version information and exit")
              .create("version");
      Option verboseOption = OptionBuilder.withLongOpt("verbose")
              .withDescription("be extra verbose")
              .create("v");
      Option debugOption = OptionBuilder.withDescription( "generate debugging information" )
              .hasOptionalArgs()
              .create("g");
      Option destinationDir = OptionBuilder.withArgName("dir")
              .hasArg()
              .withDescription("destination directory for generated JavaScript files")
              .create("d");
      Option enableAssertionsOption = OptionBuilder.withLongOpt("enableassertions")
              .withDescription("enable assertions")
              .create("ea");
      Options options = new Options();
      options.addOption(help);
      options.addOption(version);
      options.addOption(verboseOption);
      options.addOption(debugOption);
      options.addOption(destinationDir);
      options.addOption(enableAssertionsOption);
      CommandLineParser parser = new GnuParser();
      CommandLine line;
      try {
        line = parser.parse(options, argv);
      } catch (UnrecognizedOptionException e) {
        System.out.println(e.getMessage());
        return RESULT_CODE_UNRECOGNIZED_OPTION;
      } catch (MissingArgumentException e) {
        System.out.println(e.getMessage());
        return RESULT_CODE_MISSING_OPTION_ARGUMENT;
      }
      if (line.hasOption("help")) {
        printHelp(options);
        return RESULT_CODE_OK;
      }
      if (line.hasOption("version")) {
        printVersion();
        return RESULT_CODE_OK;
      }
      verbose = line.hasOption(verboseOption.getOpt());
      if (line.hasOption(destinationDir.getOpt())) {
        destionationDirName = line.getOptionValue(destinationDir.getOpt());
        this.destinationDir = new File(destionationDirName);
        if (!this.destinationDir.exists())
          error("destination directory does not exist: " + this.destinationDir.getAbsolutePath());
      }
      if (line.hasOption(enableAssertionsOption.getOpt()))
        enableAssertions = true;
      if (line.hasOption(debugOption.getOpt())) {
        String[] values = line.getOptionValues(debugOption.getOpt());
        if (values == null || values.length == 0) {
          System.out.println("-g option present.");
          debugLines = debugSource = debugMembers = debugVars = true;
        } else {
          System.out.println("-g option value: " + Arrays.asList(values));
          for (int i = 0; i < values.length; i++) {
            String value = (String)values[i];
            if (value.equals("source"))
              debugSource = true;
            else if (value.equals("source"))
              debugSource = true;
            else if (value.equals("lines"))
              debugLines = true;
            else if (value.equals("vars"))
              debugVars = true;
            else if (value.equals("members"))
              debugMembers = true;
            else if (value.equals("none"))
              debugLines = debugSource = debugMembers = debugVars = false;
            else
              error("unknown -g argument: " + value);
          }
        }
      } else {
        debugLines = debugSource = false;
        debugMembers = debugVars = false;
        debugLines = true;//TODO:option g:none etc
      }
      if (verbose) {
        System.out.println("enableassertions=" +  enableAssertions);
        System.out.println("-g option values:");
        System.out.println("source=" + debugSource);
        System.out.println("lines=" + debugLines);
        System.out.println("vars=" + debugVars);
        System.out.println("members=" + debugMembers);
      }
      String[] fileNames = line.getArgs();
      if (fileNames.length == 0) {
        printHelp(options);
        return RESULT_CODE_OK;
      }
      for (int i = 0; i < fileNames.length; i++)
        addSourceFile(fileNames[i]);
      for (int i = 0; i < sourceFileNames.size(); i++)
        processSource((String) sourceFileNames.get(i));
      for (int i = 0; i < compilationUnits.size(); i++) {
        CompilationUnit unit = (CompilationUnit) compilationUnits.get(i);
        unit.analyze(new AnalyzeContext());
        unit.writeOutput(verbose, debugSource, debugLines, enableAssertions);
      }
      return log.hasErrors() ? 1 : 0;
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
  }

  public static void main(String[] argv) {
    Jscc compiler = new Jscc();
    int result = compiler.run(argv);
    if (result != 0)
      System.exit(result);
  }

}
