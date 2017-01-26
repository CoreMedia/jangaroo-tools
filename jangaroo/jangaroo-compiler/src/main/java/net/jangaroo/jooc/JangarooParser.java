package net.jangaroo.jooc;

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.PredefinedTypeDeclaration;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.properties.Propc;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.BOMStripperInputStream;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JangarooParser implements CompilationUnitResolver, CompilationUnitRegistry {
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
  private MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();
  private List<String> compilableSuffixes = Arrays.asList(Jooc.PROPERTIES_SUFFIX, Jooc.AS_SUFFIX, Jooc.MXML_SUFFIX);

  private final Scope globalScope = new DeclarationScope(null, null, this);
  private final TypeDeclaration voidType = declareType(globalScope, AS3Type.VOID.toString(), false);
  private final TypeDeclaration anyType = declareType(globalScope, AS3Type.ANY.toString(), true);

  {
    declareValues(globalScope, new String[]{Ide.THIS});
  }

  public JangarooParser(@Nonnull ParserOptions config, @Nonnull CompileLog log) {
    this.config = config;
    this.log = log;
  }

  public TypeDeclaration getAnyType() {
    return anyType;
  }

  public TypeDeclaration getVoidType() {
    return anyType;
  }

  public static CompilerError error(String msg) {
    return new CompilerError(msg);
  }

  public static CompilerError error(String msg, final File file) {
    return new CompilerError(fileToSymbol(file), msg);
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
    return new CompilerError(fileToSymbol(file), msg, t);
  }

  protected static FilePosition fileToSymbol(File file) {
    return new FilePositionImpl(file);
  }

  public static CompilerError error(String msg, Throwable t) {
    return new CompilerError(msg, t);
  }

  public static void warning(FilePosition symbol, String msg) {
    defaultLog.get().warning(symbol, msg);
  }

  public static void warning(String msg, File file) {
    defaultLog.get().warning(fileToSymbol(file), msg);
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
    String inputSourceName = in.getName();
    boolean parseMxml = inputSourceName.endsWith(Jooc.MXML_SUFFIX);
    Reader reader;
    try {
      if (inputSourceName.endsWith(Jooc.PROPERTIES_SUFFIX)) {
        if (inputSourceName.contains("_")) {
          return null;
        }
        reader = createPropertiesClassReader(in);
      } else {
        reader = new InputStreamReader(new BOMStripperInputStream(in.getInputStream()), UTF_8);
      }
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

  public Reader createPropertiesClassReader(InputSource in) throws IOException {
    Propc propertyClassGenerator = new Propc();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    propertyClassGenerator.generateApi(
            CompilerUtils.qNameFromRelativePath(in.getRelativePath()),
            in.getInputStream(),
            new OutputStreamWriter(outputStream, UTF_8));
    // From the properties file, we generate AS code, then parse a CompilationUnit again.
    String output = outputStream.toString(UTF_8);
    return new StringReader(output);
  }

  private static TypeDeclaration declareType(Scope scope, String identifier, boolean dynamic) {
    TypeDeclaration decl = new PredefinedTypeDeclaration(identifier, dynamic);
    decl.scope(scope);
    return decl;
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
    String inputSourceFileName = getInputSourceFileName(qname, pathInputSource, suffix);
    return inputSourceFileName == null ? null : pathInputSource.getChild(inputSourceFileName);
  }

  public static String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return CompilerUtils.fileNameFromQName(qname, is.getFileSeparatorChar(), extension);
  }

  public CompilationUnit importSource(InputSource source) {
    CompilationUnit unit = compilationUnitsByInputSource.get(source);
    if (unit != null) {
      return unit;
    }

    String fileName = source.getName();
    if (!hasCompilableSuffix(fileName)) {
      throw error("Input file must end with one of '" + getCompilableSuffixes() + "': " + fileName);
    }
    unit = doParse(source, log, config.getSemicolonInsertionMode());
    if (unit == null) {
      return null;
    }

    String qname = unit.getQualifiedNameStr();
    compilationUnitsByQName.put(qname, unit);
    compilationUnitsByInputSource.put(source, unit);
    inputSourceByCompilationUnit.put(unit, source);
    unit.scope(globalScope);
    return unit;
  }

  private boolean hasCompilableSuffix(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return dotIndex != -1 && getCompilableSuffixes().contains(fileName.substring(dotIndex));
  }

  public IdeDeclaration resolveImport(final ImportDirective importDirective) {
    String qname = importDirective.getQualifiedName();
    CompilationUnit compilationUnit;
    try {
      compilationUnit = getCompilationUnit(qname);
    } catch (CompilerError e) {
      getLog().error(e.getSymbol(), e.getMessage());
      throw error(importDirective.getSymbol(), "Unable to import " + qname + ": error while parsing its source (see error above).");
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
      // The compilation unit has not yet been parsed.
      InputSource source = findSource(qname);
      if (source != null) {
        compilationUnit = importSource(source);
      }
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

    // fast path for MXML, so they don't get scoped too early:
    CompilationUnit compilationUnit = compilationUnitsByQName.get(name);
    if (compilationUnit == null) {
      // The compilation unit has not yet been parsed.
      InputSource source = findSource(name);
      if (source != null) {
        if (source.getName().endsWith(Jooc.MXML_SUFFIX)) {
          return true;
        }
      }
    }

    return resolveCompilationUnit(name).isClass();
  }


  @Override
  @Nonnull
  public CompilationUnit resolveCompilationUnit(@Nonnull String fullClassName) {
    CompilationUnit compilationUnit = getCompilationUnit(fullClassName);
    if (compilationUnit == null) {
      throw error("Undefined type: " + fullClassName);
    }
    return compilationUnit;
  }

  public MxmlComponentRegistry getMxmlComponentRegistry() {
    return mxmlComponentRegistry;
  }

  public List<String> getPackageIdes(String packageName) {
    List<String> result = new ArrayList<String>(10);
    addPackageFolderSymbols(result, packageName, sourcePathInputSource);
    addPackageFolderSymbols(result, packageName, classPathInputSource);
    return result;
  }

  private void addPackageFolderSymbols(final List<String> result, final String packageName, final InputSource path) {
    final InputSource folder = findInputSource(packageName, path, "");
    if (folder != null) {
      for (InputSource child : folder.list()) {
        if (!child.isDirectory() &&
                hasCompilableSuffix(child.getName())) {
          result.add(CompilerUtils.qNameFromRelativePath(child.getName()));
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

  @Override
  public boolean implementsInterface(CompilationUnit classCompilationUnit, String anInterface) {
    return classCompilationUnit != null && classCompilationUnit.isClass() &&
            implementsInterface((ClassDeclaration) classCompilationUnit.getPrimaryDeclaration(), anInterface);
  }

  public boolean implementsInterface(@Nonnull ClassDeclaration classDeclaration, String anInterface) {
    CompilationUnit interfaceCompilationUnit = getCompilationUnit(anInterface);
    return interfaceCompilationUnit != null && interfaceCompilationUnit.isClass() &&
            classDeclaration.isAssignableTo((ClassDeclaration) interfaceCompilationUnit.getPrimaryDeclaration());
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
