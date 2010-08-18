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
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.utils.BOMStripperInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc {

  final static String JOO_API_IN_JAR_DIRECTORY_PREFIX = "META-INF/joo-api/";

  public static final int RESULT_CODE_OK = 0;
  public static final int RESULT_CODE_COMPILATION_FAILED = 1;
  public static final int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  public static final int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  public static final int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;

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

  private InputSource sourcePathInputSource;
  private InputSource classPathInputSource;

  private final Scope globalScope = new DeclarationScope(null, null);
  private final IdeDeclaration voidDeclaration = declareType(globalScope, "void");
  private final IdeDeclaration anyDeclaration = declareType(globalScope, "*");

  public Jooc() {
    this(new StdOutCompileLog());
  }

  public Jooc(CompileLog log) {
    this.log = log;
  }

  public IdeDeclaration getAnyDeclaration() {
    return anyDeclaration;
  }

  public IdeDeclaration getVoidDeclaration() {
    return voidDeclaration;
  }

  public int run(JoocConfiguration config) {
    try {
      return run1(config);
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
  }

  private int run1(JoocConfiguration config) {
    logHolder.set(log);
    this.config = config;
    buildGlobalScope();
    for (File sourceDir : config.getSourcePath()) {
      try {
        canoncicalSourcePath.add(sourceDir.getCanonicalFile());
      } catch (IOException e) {
        throw new CompilerError("Cannot canonicalize source path dir: " + sourceDir.getAbsolutePath());
      }
    }

    try {
      sourcePathInputSource = PathInputSource.fromFiles(canoncicalSourcePath, new String[]{""});
      classPathInputSource = PathInputSource.fromFiles(config.getClassPath(), new String[]{"", JOO_API_IN_JAR_DIRECTORY_PREFIX});
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
        unit.writeOutput(codeSinkFactory, config.isVerbose());
        if (config.isGenerateApi()) {
          unit.writeOutput(apiSinkFactory, config.isVerbose());
        }
      }
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }
    int result = log.hasErrors() ? 1 : 0;
    logHolder.remove();
    return result;
  }

  private void buildGlobalScope() {
    //todo declare this depending on context
    declareValues(globalScope, new String[]{
      "this"});
  }

  private static IdeDeclaration declareType(Scope scope, String identifier) {
    IdeDeclaration decl = new PredefinedTypeDeclaration(identifier);
    decl.scope(scope);
    return decl;
  }

  private static void declareClasses(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      Ide ide = new Ide(new JooSymbol(identifier));
      IdeDeclaration decl = new ClassDeclaration(new JooSymbol[0], new JooSymbol("class"), ide, null, null,
        new ClassBody(null, Collections.<AstNode>emptyList(), null));
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

  public static void warning(String msg) {
    logHolder.get().warning(msg);
  }

  protected CompilationUnit importSource(InputSource source) {
    CompilationUnit unit = parse(source);
    if (unit != null) {
      unit.scope(globalScope);
      String prefix = unit.getPackageDeclaration().getQualifiedNameStr();
      if (!prefix.isEmpty()) {
        prefix += ".";
      }
      String qname = prefix + unit.getPrimaryDeclaration().getIde().getName();
      checkValidFileName(qname, unit, source);
      compilationUnitsByQName.put(qname, unit);
    }
    return unit;
  }

  private String buildSourceFileName(final InputSource inputSource, final String qname) {
    return qname.replace('.', inputSource.getFileSeparatorChar()) + AS_SUFFIX;
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
    if (file.isDirectory()) {
      throw error("Input file is a directory: " + file.getAbsolutePath());
    }
    CompilationUnit unit = importSource(new FileInputSource(findSourceDir(file), file));
    if (unit != null) {
      compileQueue.add(unit);
    }
  }

  public IdeDeclaration resolveImport(final ImportDirective importDirective) {
    String qname = importDirective.getQualifiedName();
    CompilationUnit compilationUnit = compilationUnitsByQName.get(qname);
    if (compilationUnit == null) {
      InputSource source = findSource(qname);
      if (source == null) {
        throw error(importDirective.getSymbol(), "cannot find source for " + qname);
      }
      compilationUnit = importSource(source);
    }
    if (compilationUnit == null) {
      throw error("unable to resolve import of " + qname);
    }
    return compilationUnit.getPrimaryDeclaration();
  }

  private InputSource findSource(final String qname) {
    // scan sourcepath
    InputSource result = sourcePathInputSource.getChild(getInputSourceFileName(qname, sourcePathInputSource, AS_SUFFIX));
    if (result == null) {
      // scan classpath
      result = classPathInputSource.getChild(getInputSourceFileName(qname, classPathInputSource, AS_SUFFIX));
    }
    return result;
  }

  private String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return qname.replace('.', is.getFileSeparatorChar()) + extension;
  }

  private void checkValidFileName(final String qname, final CompilationUnit unit, final InputSource source) {
    // check valid file name for qname
    String path = source.getRelativePath();
    if (path != null) {
      String expectedPath = buildSourceFileName(source, qname);
      if (!expectedPath.equals(path)) {
        warning(unit.getSymbol(),
          String.format("expected '%s' as the file name for %s, found: '%s'. -sourcepath not set (correctly)?",
            expectedPath,
            qname,
            path));
      }
    }
  }

  public List<String> getPackageIdes(String packageName) {
    List<String> result = new ArrayList<String>(10);
    addPackageFolderSymbols(result, packageName, sourcePathInputSource);
    addPackageFolderSymbols(result, packageName, classPathInputSource);
    return result;
  }

  private void addPackageFolderSymbols(final List<String> result, final String packageName, final InputSource path) {
    addPackageFolderSymbols(path.getChild(getInputSourceFileName(packageName, path, "")),
      result);
  }

  private void addPackageFolderSymbols(final InputSource folder, List<String> list) {
    if (folder != null) {
      final List<InputSource> childList = folder.list();
      for (InputSource child : childList) {
        if (!child.isDirectory() && child.getName().endsWith(AS_SUFFIX)) {
          list.add(nameWithoutExtension(child));
        }
      }
    }
  }

  private static String nameWithoutExtension(InputSource input) {
    String name = input.getName();
    int lastDot = name.lastIndexOf('.');
    return lastDot >= 0 ? name.substring(0, lastDot) : name;
  }


  public JoocConfiguration getConfig() {
    return config;
  }

  protected CompilationUnit parse(InputSource in) {

    if (!in.getName().endsWith(AS_SUFFIX)) {
      throw error("Input file must end with '" + AS_SUFFIX + "': " + in.getName());
    }
    Scanner s;
    if (config.isVerbose()) {
      System.out.println("Parsing " + in.getPath());
    }
    try {
      s = new Scanner(new InputStreamReader(new BOMStripperInputStream(in.getInputStream()), "UTF-8"));
    } catch (IOException e) {
      throw new CompilerError("Cannot read input file: " + in.getPath());
    }
    s.setInputSource(in);
    parser p = new parser(s);
    p.setCompileLog(log);
    p.setSemicolonInsertionMode(config.getSemicolonInsertionMode());
    try {
      Symbol tree = p.parse();
      CompilationUnit unit = (CompilationUnit) tree.value;
      unit.setCompiler(this);
      unit.setSource(in);
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
      JoocCommandLineParser commandLineParser = new JoocCommandLineParser();
      config = commandLineParser.parse(argv);
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
