package net.jangaroo.jooc;

import java_cup.runtime.Symbol;
import net.jangaroo.jooc.backend.ActionScriptCodeGeneratingModelVisitor;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlToModelParser;
import net.jangaroo.utils.AS3Type;
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
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.utils.BOMStripperInputStream;
import net.jangaroo.utils.CompilerUtils;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JangarooParser {
  public static final String JOO_API_IN_JAR_DIRECTORY_PREFIX = "META-INF/joo-api/";

  protected CompileLog log;
  // a hack to always be able to access the current log:
  private static ThreadLocal<CompileLog> defaultLog = new ThreadLocal<CompileLog>();

  private InputSource sourcePathInputSource;
  private InputSource classPathInputSource;
  private ParserOptions config;
  private Map<String, CompilationUnit> compilationUnitsByQName = new LinkedHashMap<String, CompilationUnit>();
  private MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();

  protected final Scope globalScope = new DeclarationScope(null, null);

  {
    declareType(globalScope, AS3Type.VOID.toString());
    declareType(globalScope, AS3Type.ANY.toString());
  }

  public JangarooParser() {
  }

  public JangarooParser(ParserOptions config, CompileLog log) {
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

  public static CompilerError error(AstNode node, String msg) {
    return error(node.getSymbol(), msg);
  }

  public static CompilerError error(String msg, File file, Throwable t) {
    return new CompilerError(new FilePositionImpl(file), msg, t);
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

  public CompilationUnit doParse(InputSource in, CompileLog log, SemicolonInsertionMode semicolonInsertionMode) {
    Reader reader;
    try {
      if (in.getName().endsWith(Jooc.MXML_SUFFIX)) {
        CompilationUnitModel compilationUnitModel = new MxmlToModelParser(this).parse(in);
        // From the CompilationUnitModel, we generate AS code, then parse a CompilationUnit again.
        // TODO: This should be simplified to always using CompilationUnitModels for reference
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        compilationUnitModel.visit(new ActionScriptCodeGeneratingModelVisitor(writer));
        String output = outputStream.toString("UTF-8");
        reader = new StringReader(output);
      } else {
        reader = new InputStreamReader(new BOMStripperInputStream(in.getInputStream()), "UTF-8");
      }
    } catch (IOException e) {
      throw new CompilerError("Cannot read input file: " + in.getPath(), e);
    } catch (SAXException e) {
      e.printStackTrace();
      throw new CompilerError("Cannot parse MXML input file: " + in.getPath(), e);
    }
    Scanner s = new Scanner(reader);
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
    // scan sourcepath
    InputSource result = sourcePathInputSource.getChild(getInputSourceFileName(qname, sourcePathInputSource, Jooc.AS_SUFFIX));
      if (result == null) {
      result = sourcePathInputSource.getChild(getInputSourceFileName(qname, sourcePathInputSource, Jooc.MXML_SUFFIX));
      if (result == null) {
        // scan classpath
        result = classPathInputSource.getChild(getInputSourceFileName(qname, classPathInputSource, Jooc.AS_SUFFIX));
      }
    }
    return result;
  }

  public static String getInputSourceFileName(final String qname, InputSource is, String extension) {
    return CompilerUtils.fileNameFromQName(qname, is.getFileSeparatorChar(), extension);
  }

  public CompilationUnit importSource(InputSource source) {
    CompilationUnit unit = parse(source);
    if (unit != null) {
      unit.scope(globalScope);
      String prefix = unit.getPackageDeclaration().getQualifiedNameStr();
      String qname = CompilerUtils.qName(prefix, unit.getPrimaryDeclaration().getIde().getName());
      checkValidFileName(qname, unit, source);
      compilationUnitsByQName.put(qname, unit);
    }
    return unit;
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

  protected CompilationUnit parse(InputSource in) {
    if (!in.getName().endsWith(Jooc.AS_SUFFIX) && !in.getName().endsWith(Jooc.MXML_SUFFIX)) {
      throw error("Input file must end with '" + Jooc.AS_SUFFIX + "' or '" + Jooc.MXML_SUFFIX + "': " + in.getName());
    }
    if (config.isVerbose()) {
      System.out.println("Parsing " + in.getPath() + " (" + (in.isInSourcePath() ? "source" : "class") + "path)"); // NOSONAR this is a cmd line tool
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
        if (!child.isDirectory() &&
                (child.getName().endsWith(Jooc.AS_SUFFIX) || child.getName().endsWith(Jooc.MXML_SUFFIX))) {
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
    this.sourcePathInputSource = sourcePathInputSource;
    this.classPathInputSource = classPathInputSource;

    buildGlobalScope();
  }

  public void tearDown() {
    defaultLog.remove();
  }

  private static class FilePositionImpl implements FilePosition {
    private final File file;

    public FilePositionImpl(File file) {
      this.file = file;
    }

    @Override
    public String getFileName() {
      return file.getAbsolutePath();
    }

    @Override
    public int getLine() {
      return -1;
    }

    @Override
    public int getColumn() {
      return -1;
    }
  }
}
