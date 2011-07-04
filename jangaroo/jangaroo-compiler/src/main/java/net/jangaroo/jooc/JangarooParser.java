package net.jangaroo.jooc;

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.utils.BOMStripperInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JangarooParser {
  public static final String JOO_API_IN_JAR_DIRECTORY_PREFIX = "META-INF/joo-api/";

  public static final String AS_SUFFIX_NO_DOT = "as";
  public static final String AS_SUFFIX = "." + AS_SUFFIX_NO_DOT;

  protected CompileLog log;
  // a hack to always be able to access the current log:
  private static ThreadLocal<CompileLog> defaultLog = new ThreadLocal<CompileLog>();

  private InputSource sourcePathInputSource;
  private InputSource classPathInputSource;
  private ParserOptions config;
  private Map<String, CompilationUnit> compilationUnitsByQName = new LinkedHashMap<String, CompilationUnit>();

  protected final Scope globalScope = new DeclarationScope(null, null);
  {
    declareType(globalScope, "void");
    declareType(globalScope, "*");
  }

  public JangarooParser(ParserOptions config, CompileLog log) {
    this.config = config;
    this.log = log;
  }

  public static CompilerError error(String msg) {
    return new CompilerError(msg);
  }

  public static CompilerError error(JooSymbol symbol, String msg) {
    return new CompilerError(symbol, msg);
  }

  public static CompilerError error(AstNode node, String msg) {
    return error(node.getSymbol(), msg);
  }

  public static CompilerError error(String msg, Throwable t) {
    return new CompilerError(msg, t);
  }

  public static void warning(JooSymbol symbol, String msg) {
    defaultLog.get().warning(symbol, msg);
  }

  public static void warning(String msg) {
    defaultLog.get().warning(msg);
  }

  public ParserOptions getConfig() {
    return config;
  }

  public static CompilationUnit doParse(InputSource in, CompileLog log, SemicolonInsertionMode semicolonInsertionMode) {
    Scanner s;
    try {
      s = new Scanner(new InputStreamReader(new BOMStripperInputStream(in.getInputStream()), "UTF-8"));
    } catch (IOException e) {
      throw new CompilerError("Cannot read input file: " + in.getPath());
    }
    s.setInputSource(in);
    parser p = new parser(s);
    p.setCompileLog(log);
    p.setSemicolonInsertionMode(semicolonInsertionMode);
    try {
      Symbol tree = p.parse();
      return (CompilationUnit) tree.value;
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

  private static String nameWithoutExtension(InputSource input) {
    String name = input.getName();
    int lastDot = name.lastIndexOf('.');
    return lastDot >= 0 ? name.substring(0, lastDot) : name;
  }

  private static void declareType(Scope scope, String identifier) {
    IdeDeclaration decl = new PredefinedTypeDeclaration(identifier);
    decl.scope(scope);
  }

  protected static void declareValues(Scope scope, String[] identifiers) {
    for (String identifier : identifiers) {
      Ide ide = new Ide(new JooSymbol(identifier));
      IdeDeclaration decl = new VariableDeclaration(new JooSymbol("var"), ide, null, null);
      decl.scope(scope);
    }
  }

  protected InputSource findSource(final String qname) {
    // scan sourcepath
    InputSource result = sourcePathInputSource.getChild(getInputSourceFileName(qname, sourcePathInputSource, AS_SUFFIX));
    if (result == null) {
      // scan classpath
      result = classPathInputSource.getChild(getInputSourceFileName(qname, classPathInputSource, AS_SUFFIX));
    }
    return result;
  }

  protected String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return qname.replace('.', is.getFileSeparatorChar()) + extension;
  }

  public CompilationUnit importSource(InputSource source) {
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

  public IdeDeclaration resolveImport(final ImportDirective importDirective) {
    String qname = importDirective.getQualifiedName();
    CompilationUnit compilationUnit = getCompilationsUnit(qname);
    if (compilationUnit == null) {
      throw error(importDirective.getSymbol(), "unable to resolve import of " + qname);
    }
    return compilationUnit.getPrimaryDeclaration();
  }

  public CompilationUnit getCompilationsUnit(String qname) {
    CompilationUnit compilationUnit = compilationUnitsByQName.get(qname);
    if (compilationUnit == null) {
      InputSource source = findSource(qname);
      if (source == null) {
        return null;
      }
      compilationUnit = importSource(source);
    }
    return compilationUnit;
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

  protected CompilationUnit parse(InputSource in) {
    if (!in.getName().endsWith(AS_SUFFIX)) {
      throw error("Input file must end with '" + AS_SUFFIX + "': " + in.getName());
    }
    if (config.isVerbose()) {
      System.out.println("Parsing " + in.getPath());
    }
    CompilationUnit unit = doParse(in, log, config.getSemicolonInsertionMode());
    if (unit != null) {
      unit.setCompiler(this);
      unit.setSource(in);
    }
    return unit;
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
      for (InputSource child : folder.list()) {
        if (!child.isDirectory() && child.getName().endsWith(AS_SUFFIX)) {
          list.add(nameWithoutExtension(child));
        }
      }
    }
  }

  private void buildGlobalScope() {
    //todo declare this depending on context
    declareValues(globalScope, new String[]{
            "this"});
  }

  public void setUp(InputSource sourcePathInputSource, InputSource classPathInputSource) {
    defaultLog.set(log);
    this.config = config;
    this.sourcePathInputSource = sourcePathInputSource;
    this.classPathInputSource = classPathInputSource;

    buildGlobalScope();
  }

  public void tearDown() {
    defaultLog.remove();
  }
}
