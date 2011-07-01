package net.jangaroo.jooc;

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.JoocOptions;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.utils.BOMStripperInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbstractJooc {
  public static final String AS_SUFFIX_NO_DOT = "as";
  public static final String AS_SUFFIX = "." + AS_SUFFIX_NO_DOT;
  // a hack to always be able to access the current log:
  protected static ThreadLocal<CompileLog> logHolder = new ThreadLocal<CompileLog>();
  protected List<File> canoncicalSourcePath = new ArrayList<File>();
  protected InputSource sourcePathInputSource;
  protected InputSource classPathInputSource;
  protected JoocConfiguration config;
  protected CompileLog log;
  private Map<String, CompilationUnit> compilationUnitsByQName = new LinkedHashMap<String, CompilationUnit>();
  protected final Scope globalScope = new DeclarationScope(null, null);

  public AbstractJooc(CompileLog log) {
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
    logHolder.get().warning(symbol, msg);
  }

  public static void warning(String msg) {
    logHolder.get().warning(msg);
  }

  public static CompilationUnit doParse(InputSource in, CompileLog log, JoocOptions.SemicolonInsertionMode semicolonInsertionMode) {
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

  protected File findSourceDir(final File file) throws IOException {
    File canonicalFile = file.getCanonicalFile();
    for (File sourceDir : canoncicalSourcePath) {
      if (isParent(sourceDir, canonicalFile)) {
        return sourceDir;
      }
    }
    return null;
  }

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

  public JoocConfiguration getConfig() {
    return config;
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
}
