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

import java.io.*;
import java.util.*;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
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
  public static final String INPUT_FILE_SUFFIX = AS_SUFFIX;
  public static final String OUTPUT_FILE_SUFFIX = ".js";

  public static final String CLASS_LOADER_NAME = "classLoader";
  public static final String CLASS_LOADER_PACKAGE_NAME = "joo";
  public static final String CLASS_LOADER_FULLY_QUALIFIED_NAME = CLASS_LOADER_PACKAGE_NAME + "." + CLASS_LOADER_NAME;

  private JoocConfiguration config;
  // a hack to always be able to access the current log:
  private static ThreadLocal<CompileLog> logHolder = new ThreadLocal<CompileLog>();
  private CompileLog log;

  private List<File> canoncicalSourcePath = new ArrayList<File>();
  private List<CompilationUnit> compileQueue = new ArrayList<CompilationUnit>();

  private Map<String, CompilationUnit> compilationUnitsByQName = new LinkedHashMap<String, CompilationUnit>();
  private Map<File, CompilationUnit> compilationUnitsByFile = new HashMap<File, CompilationUnit>();

  private Scope globalScope;

  public Jooc() {
    this(new StdOutCompileLog());
  }

  public Jooc(CompileLog log) {
    this.log = log;
  }

  public int run(JoocConfiguration config) {
    globalScope = buildGlobalScope();
    logHolder.set(log);
    this.config = config;
    for (File sourceDir : config.getSourcePath()) {
      try {
        canoncicalSourcePath.add(sourceDir.getCanonicalFile());
      } catch (IOException e) {
        throw new CompilerError("Cannot canonicalize source path dir: " + sourceDir.getAbsolutePath());
      }
    }

    for (File sourceFile : config.getSourceFiles()) {
      processSource(sourceFile);
    }

    CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(config);

    for (CompilationUnit unit : compileQueue) {
      unit.analyze(null, new AnalyzeContext(config));
      unit.writeOutput(codeSinkFactory, config.isVerbose());
    }
    int result = log.hasErrors() ? 1 : 0;
    logHolder.remove();
    return result;
  }

  private static void declareTypes(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      IdeDeclaration decl = new PredefinedTypeDeclaration(identifier);
      decl.scope(scope);
    }
  }

  private static void declareClasses(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      Ide ide = new Ide(new JooSymbol(identifier));
      IdeDeclaration decl = new ClassDeclaration(new JooSymbol[0], new JooSymbol("class"), ide, null, null,
        new ClassBody(null, Collections.EMPTY_LIST, null));
      decl.scope(scope);
    }
  }

  private static void declareValues(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      Ide ide = new Ide(new JooSymbol(identifier));
      IdeDeclaration decl = new VariableDeclaration(new JooSymbol("var"), ide, null, null);
      decl.scope(scope);
    }
  }

  private static Scope buildGlobalScope() {
    // establish global scope for built-in identifiers:
    Scope scope = new DeclarationScope(null, null);

    //todo move these into runtime api *as files where possible, for functions/objects extend import mechanism
    declareTypes(scope, new String[]{
      "int",
      "uint"});

    declareClasses(scope, new String[]{
      "Object",
      "Function",
      "Class",
      "Boolean",
      "String",
      "Number",
      "RegExp",
      "Date",
      "Math"});

    declareValues(scope, new String[]{
      "undefined",
      "this",
      "window",
      "parseInt",
      "parseFloat",
      "isNaN",
      "NaN",
      "isFinite",
      "Infinity",
      "decodeURI",
      "decodeURIComponent",
      "encodeURI",
      "encodeURIComponent",
      "trace"});
    return scope;
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
    private JooSymbol symbol = null;

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

  public static CompilerError error(String msg) {
    return new CompilerError(msg);
  }

  static CompilerError error(JooSymbol symbol, String msg) {
    return new CompilerError(symbol, msg);
  }

  public static CompilerError error(AstNode node, String msg) {
    return error(node.getSymbol(), msg);
  }

  public static CompilerError error(String msg, Throwable t) {
    return new CompilerError(msg, t);
  }

  public static void warning(JooSymbol symbol, String msg) {
    logHolder.get().warning(symbol, msg);
  }

  protected CompilationUnit importSource(File file) {
    File canonicalFile;
    try {
      canonicalFile = file.getCanonicalFile();
    } catch (IOException e) {
      throw new CompilerError("I/O error while canonicalizing source file: " + file.getAbsolutePath());
    }
    CompilationUnit unit = compilationUnitsByFile.get(canonicalFile);
    if (unit == null) {
      unit = parse(file);
      if (unit != null) {
        unit.scope(globalScope);
        String qname = unit.getPackageDeclaration().getQualifiedNameStr() + "." + unit.getPrimaryDeclaration().getIde().getName();

        // check valid file name for qname
        File sourceDir = findSourceDir(canonicalFile);
        File expectedSourceFile = buildSourceFileName(sourceDir, qname);
        if (!expectedSourceFile.equals(canonicalFile)) {
          warning(unit.getSymbol(),
            String.format("expected '%s' as the file name for this compilation unit, found: '%s'. -sourcepath not set (correctly)?",
              expectedSourceFile.getAbsolutePath(),
              canonicalFile.getAbsolutePath()));
        }
        compilationUnitsByQName.put(qname, unit);
        compilationUnitsByFile.put(canonicalFile, unit);
      }
    }
    return unit;
  }

  private File buildSourceFileName(final File sourceDir, final String qname) {
    return new File(sourceDir, qname.replace('.', File.separatorChar) + AS_SUFFIX);
  }

  private File findSourceDir(final File file) {
    for (File sourceDir : canoncicalSourcePath) {
      if (isParent(sourceDir, file)) {
        return sourceDir;
      }
    }
    return null;
  }

  private boolean isParent(File dir, File file) {
    File parent = file.getParentFile();
    while (parent != null) {
      if (parent.equals(dir)) {
        return true;
      }
      parent = parent.getParentFile();
    }
    return false;
  }

  protected void processSource(File file) {
    CompilationUnit unit = importSource(file);
    if (unit != null)
      compileQueue.add(unit);
  }

  public IdeDeclaration resolveImport(final ImportDirective importDirective) {
    String qname = importDirective.getQualifiedName();
    CompilationUnit compilationUnit = compilationUnitsByQName.get(qname);
    if (compilationUnit == null) {
      File source = findSourceFile(qname);
      compilationUnit = importSource(source);
    }
    if (compilationUnit == null) {
      throw error("unable to resolve import of " + qname);
    }
    return compilationUnit.getPrimaryDeclaration();
  }

  private File findSourceFile(final String qname) {
    String relativeFileName = qname.replace('.', File.separatorChar) + AS_SUFFIX;
    for (File sourceDir : getConfig().getSourcePath()) {
      File sourceFile = new File(sourceDir, relativeFileName);
      if (sourceFile.exists()) {
        return sourceFile;
      }
    }
    throw error("cannot find source file for " + qname);
  }

  public List<String> getPackageIdes(String packageName) {
    List<String> result = new ArrayList<String>(10);
    final String relativePackagePath = getRelativeFileName(packageName);
    for (File sourceDir : getConfig().getSourcePath()) {
      final File packageFolder = relativePackagePath.isEmpty() ? sourceDir : new File(sourceDir, relativePackagePath);
      addPackageFolderSymbols(packageFolder, result);
    }
    return result;
  }

  private String getRelativeFileName(String qualifiedName) {
    return qualifiedName.replace('.', File.separatorChar);
  }

  private void addPackageFolderSymbols(final File folder, List<String> list) {
    String[] symbols = folder.list(new SourceFilenameFilter());
    if (symbols != null) {
      for (String symbol : symbols) {
        list.add(withoutAS(symbol));
      }
    }
  }

  private static class SourceFilenameFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
      return name.endsWith(Jooc.INPUT_FILE_SUFFIX);
    }
  }

  private static String withoutAS(String name) {
    return name.substring(0, name.length() - Jooc.INPUT_FILE_SUFFIX.length());
  }


  public JoocConfiguration getConfig() {
    return config;
  }

  protected CompilationUnit parse(File in) {
    if (in.isDirectory()) {
      throw error("Input file is a directory: " + in.getAbsolutePath());
    }
    if (!in.getName().endsWith(JS2_SUFFIX) && !in.getName().endsWith(AS_SUFFIX)) {
      throw error("Input file must end with '" + JS2_SUFFIX + " or " + AS_SUFFIX + "': " + in.getAbsolutePath());
    }
    Scanner s;
    if (config.isVerbose()) {
      System.out.println("Parsing " + in.getAbsolutePath());
    }
    try {
      s = new Scanner(new InputStreamReader(new FileInputStream(in), "UTF-8"));
    } catch (IOException e) {
      throw new CompilerError("Input file not found: " + in.getAbsolutePath());
    }
    s.setFileName(in.getAbsolutePath());
    parser p = new parser(s);
    p.setCompileLog(log);
    try {
      Symbol tree = p.parse();
      CompilationUnit unit = (CompilationUnit) tree.value;
      unit.setCompiler(this);
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
      if (e.symbol != null) {
        log.error(e.symbol, e.getMessage());
      } else {
        log.error(e.getMessage());
      }
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
    if (result != 0) {
      System.exit(result);
    }
  }

}
