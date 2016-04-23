package net.jangaroo.jooc;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java_cup.runtime.Symbol;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.backend.ApiModelGenerator;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.CompilationUnitModelResolver;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.BOMStripperInputStream;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JangarooParser extends CompilationUnitModelResolver implements CompilationUnitRegistry {
  public static final String JOO_API_IN_JAR_DIRECTORY_PREFIX = "META-INF/joo-api/";
  static final String UTF_8 = "UTF-8";

  protected CompileLog log;
  // a hack to always be able to access the current log:
  private static ThreadLocal<CompileLog> defaultLog = new ThreadLocal<CompileLog>();

  private InputSource sourcePathInputSource;
  private InputSource classPathInputSource;
  private ParserOptions config;
  private Map<InputSource, CompilationUnit> compilationUnitsByInputSource = new HashMap<>();
  private Map<CompilationUnit, InputSource> inputSourceByCompilationUnit = new HashMap<>();
  private Map<String, CompilationUnit> compilationUnitsByQName = new LinkedHashMap<String, CompilationUnit>();
  private Map<String, Boolean> isClassByQName = new LinkedHashMap<String, Boolean>();
  private Map<String, CompilationUnitModel> compilationUnitModelsByQName = new LinkedHashMap<String, CompilationUnitModel>();
  private MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();
  private List<String> compilableSuffixes = Arrays.asList(Jooc.AS_SUFFIX, Jooc.MXML_SUFFIX);

  private final Scope globalScope = new DeclarationScope(null, null, this);

  {
    declareType(globalScope, AS3Type.VOID.toString());
    declareType(globalScope, AS3Type.ANY.toString());
    declareValues(globalScope, new String[]{Ide.THIS});
  }

  public JangarooParser(@Nonnull ParserOptions config, @Nonnull CompileLog log) {
    this.config = config;
    this.log = log;
  }

  public static CompilerError error(String msg) {
    return new CompilerError(msg);
  }

  public static CompilerError error(String msg, final File file) {
    return new CompilerError(new FilePositionImpl(file), msg);
  }

  public static CompilerError error(FilePosition symbol, String msg) {
    return new CompilerError(symbol, msg);
  }

  public static CompilerError error(FilePosition symbol, String msg, Throwable t) {
    return new CompilerError(symbol, msg, t);
  }

  public static CompilerError error(AstNode node, String msg) {
    return error(node.getSymbol(), msg);
  }

  public static CompilerError error(String msg, File file, Throwable t) {
    return new CompilerError(new FilePositionImpl(file), msg, t);
  }

  public static CompilerError error(String msg, Throwable t) {
    return new CompilerError(msg, t);
  }

  public static void warning(FilePosition symbol, String msg) {
    defaultLog.get().warning(symbol, msg);
  }

  public static void warning(String msg, File file) {
    defaultLog.get().warning(new FilePositionImpl(file), msg);
  }

  public static void warning(String msg) {
    defaultLog.get().warning(msg);
  }

  public ParserOptions getConfig() {
    return config;
  }

  public void setConfig(ParserOptions config) {
    this.config = config;
  }

  public CompileLog getLog() {
    return log;
  }

  public void setLog(CompileLog log) {
    this.log = log;
  }

  public List<String> getCompilableSuffixes() {
    return compilableSuffixes;
  }

  public void setCompilableSuffixes(List<String> compilableSuffixes) {
    this.compilableSuffixes = compilableSuffixes;
  }

  public CompilationUnit doParse(InputSource in, CompileLog log, SemicolonInsertionMode semicolonInsertionMode) {
    if (config.isVerbose()) {
      System.out.println("Parsing " + in.getPath() + " (" + (in.isInSourcePath() ? "source" : "class") + "path)"); // NOSONAR this is a cmd line tool
    }
    boolean parseMxml = in.getName().endsWith(Jooc.MXML_SUFFIX);
    Reader reader;
    try {
      reader = new InputStreamReader(new BOMStripperInputStream(in.getInputStream()), UTF_8);
    } catch (IOException e) {
      throw new CompilerError("Cannot read input file: " + in.getPath(), e);
    }
    Scanner s = new Scanner(reader);
    s.yybegin(parseMxml ? Scanner.MXML : Scanner.YYINITIAL);
    s.setInputSource(in);
    JooParser p = new JooParser(s);
    p.setCompileLog(log);
    p.setSemicolonInsertionMode(semicolonInsertionMode);
    try {
      Symbol tree = p.parse();
      return (CompilationUnit) tree.value;
    } catch (Scanner.ScanError se) {
      log.error(se.getSym(), se.getMessage());
      return null;
    } catch (JooParser.FatalSyntaxError e) {
      // message already logged in parser
      return null;
    } catch (Exception e) {
      throw new IllegalArgumentException("could not parse Jangaroo source", e);
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
    InputSource result;
    // scan sourcepath
    for (String suffix : compilableSuffixes) {
      result = findInputSource(qname, sourcePathInputSource, suffix);
      if (result != null) {
        return result;
      }
    }
    // scan classpath
    for (String suffix : compilableSuffixes) {
      result = findInputSource(qname, classPathInputSource, suffix);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static InputSource findInputSource(String qname, InputSource pathInputSource, String suffix) {
    return pathInputSource.getChild(getInputSourceFileName(qname, pathInputSource, suffix));
  }

  public static String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return CompilerUtils.fileNameFromQName(qname, is.getFileSeparatorChar(), extension);
  }

  public CompilationUnit importSource(InputSource source, boolean forceParse) {
    if (!forceParse) {
      CompilationUnit unit = compilationUnitsByInputSource.get(source);
      if (unit != null) {
        return unit;
      }
    }

    String fileName = source.getName();
    int dotIndex = fileName.lastIndexOf('.');
    String fileExtension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    List<String> suffixes = getCompilableSuffixes();
    if(!suffixes.contains(fileExtension)) {
      throw error("Input file must end with one of '" + suffixes + "': " + fileName);
    }
    CompilationUnit unit = doParse(source, log, config.getSemicolonInsertionMode());
    if (null != unit) {
      unit.scope(globalScope);
    }
    if (null == unit) {
      return null;
    }

    String prefix = unit.getPackageDeclaration().getQualifiedNameStr();
    String qname = CompilerUtils.qName(prefix, unit.getPrimaryDeclaration().getIde().getName());
    checkValidFileName(qname, unit, source);

    compilationUnitsByQName.put(qname, unit);
    compilationUnitsByInputSource.put(source, unit);
    inputSourceByCompilationUnit.put(unit, source);
    return unit;
  }

  public IdeDeclaration resolveImport(final ImportDirective importDirective) {
    String qname = importDirective.getQualifiedName();
    CompilationUnit compilationUnit;
    try {
      compilationUnit = getCompilationUnit(qname);
    } catch (CompilerError e) {
      throw error(importDirective.getSymbol(), "Unable to import " + qname + ": error while parsing its source (see below).", e);
    }
    if (compilationUnit == null) {
      throw error(importDirective.getSymbol(), "unable to resolve import of " + qname);
    }
    return compilationUnit.getPrimaryDeclaration();
  }

  public Collection<CompilationUnit> getCompilationUnits() {
    return compilationUnitsByQName.values();
  }

  public CompilationUnit getCompilationUnit(String qname) {
    CompilationUnit compilationUnit = compilationUnitsByQName.get(qname);
    if (compilationUnit == null) {
      InputSource source = findSource(qname);
      if (source == null) {
        return null;
      }
      compilationUnit = importSource(source, false);
    }
    return compilationUnit;
  }

  /**
   * Return true if the argument name identifies a class.
   *
   * @param name the name to check
   * @return whether the argument name identifies a class
   */
  public boolean isClass(String name) {
    if (name == null) {
      return false;
    }
    Boolean result = isClassByQName.get(name);
    if (result != null) {
      return result;
    }
    resolveCompilationUnit(name);
    return isClassByQName.get(name);
  }


  @Override
  @Nonnull
  public CompilationUnitModel resolveCompilationUnit(@Nonnull String fullClassName) {
    CompilationUnitModel compilationUnitModel = compilationUnitModelsByQName.get(fullClassName);
    if (compilationUnitModel == null) {
      // Use a marker in the lookup table to identify infinite loops.
      if (compilationUnitModelsByQName.containsKey(fullClassName)) {
        throw error("Cyclic dependency involving " + fullClassName + ", " +
                "possibly a cyclic inheritance relation");
      }
      compilationUnitModelsByQName.put(fullClassName, null);

      CompilationUnit compilationUnit = compilationUnitsByQName.get(fullClassName);
      boolean isMxmlClass = false;
      if (compilationUnit == null) {
        // The compilation unit has not yet been parsed.
        InputSource source = findSource(fullClassName);
        if (source != null) {
          if (source.getName().endsWith(Jooc.MXML_SUFFIX)) {
            isMxmlClass = true;
            // MXML files denote classes.
            isClassByQName.put(fullClassName, true);
            compilationUnit = doParse(source, log, config.getSemicolonInsertionMode());
          } else {
            compilationUnit = getCompilationUnit(fullClassName);
          }
        }
      }
      if (compilationUnit == null) {
        throw error("Undefined type: " + fullClassName);
      }

      compilationUnitModel = new CompilationUnitModel(CompilerUtils.packageName(fullClassName), new ClassModel(CompilerUtils.className(fullClassName)));
      compilationUnitModelsByQName.put(fullClassName, compilationUnitModel);
      isClassByQName.put(fullClassName, isMxmlClass || compilationUnitModel.getPrimaryDeclaration() instanceof ClassModel);

      if(isMxmlClass) {
        compilationUnit.scope(globalScope);
      }
      try {
        new ApiModelGenerator(false).generateModel(compilationUnit, compilationUnitModel);
      } catch (IOException e) {
        throw error("Unexpected I/O error while building compilation unit model", e);
      }
    }
    return compilationUnitModel;
  }

  public MxmlComponentRegistry getMxmlComponentRegistry() {
    return mxmlComponentRegistry;
  }

  private void checkValidFileName(final String qname, final CompilationUnit unit, final InputSource source) {
    // check valid file name for qname
    String path = CompilerUtils.removeExtension(source.getRelativePath());
    if (path != null) {
      String expectedPath = getInputSourceFileName(qname, source, "");
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
    addPackageFolderSymbols(findInputSource(packageName, path, ""), result);
  }

  private void addPackageFolderSymbols(final InputSource folder, List<String> list) {
    if (folder != null) {
      for (InputSource child : folder.list()) {
        if (!child.isDirectory() &&
                (child.getName().endsWith(Jooc.AS_SUFFIX) || child.getName().endsWith(Jooc.MXML_SUFFIX))) {
          list.add(nameWithoutExtension(child));
        }
      }
    }
  }

  public void setUp(InputSource sourcePathInputSource, InputSource classPathInputSource) {
    defaultLog.set(log);
    this.sourcePathInputSource = sourcePathInputSource;
    this.classPathInputSource = classPathInputSource;
  }

  public void tearDown() {
    defaultLog.remove();
  }

  public InputSource getInputSource(final CompilationUnit compilationUnit) {
    return inputSourceByCompilationUnit.get(compilationUnit);
  }

  private static class FilePositionImpl implements FilePosition {
    private final String fileName;
    private final int line;
    private final int column;

    public FilePositionImpl(File file) {
      this(file.getAbsolutePath(), -1, -1);
    }

    public FilePositionImpl(String fileName, int line, int column) {
      this.fileName = fileName;
      this.line = line;
      this.column = column;
    }

    @Override
    public String getFileName() {
      return fileName;
    }

    @Override
    public int getLine() {
      return line;
    }

    @Override
    public int getColumn() {
      return column;
    }
  }
}
