package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class JoocTest {

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();
  public File outputFolder;
  public File apiOutputFolder;
  private Jooc jooc;
  private JoocConfiguration config;

  private TestLog testLog = new TestLog();

  private class TestLog implements CompileLog {

    private boolean hasErrors = false;
    private List<String> errors = new ArrayList<String>();

    @Override
    public void error(FilePosition position, String msg) {
      hasErrors = true;
      errors.add(msg);
      System.out.println(position.getLine() + ";" + position.getColumn() + ": '" + msg);
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      errors.add(msg);
    }

    @Override
    public void warning(FilePosition position, String msg) {
      System.out.println(msg);
    }

    @Override
    public void warning(String msg) {
      System.out.println(msg);
    }

    @Override
    public boolean hasErrors() {
      return hasErrors;
    }

    public boolean hasError(String expected) {
      return errors.contains(expected);
    }

    public void reset() {
      errors.clear();
    }
  }

  @Before
  public void setup() throws Exception{
    outputFolder = tmpFolder.newFolder("jangaroo-output");
    apiOutputFolder = tmpFolder.newFolder("joo-api");
    config = new JoocConfiguration();
    File sourceDir = getFile("/");
    List<File> sourcepath = new ArrayList<File>();
    sourcepath.add(sourceDir);
    config.setSourcePath(sourcepath);
    config.setDebugMode(DebugMode.SOURCE);
    config.setOutputDirectory(outputFolder);
    //noinspection ResultOfMethodCallIgnored
    config.setApiOutputDirectory(apiOutputFolder);
    testLog.reset();
    jooc = new Jooc(config, testLog);
  }

  @Test
  public void testEqualDeclarationVariableType() throws Exception {
    File sourcefile = getFile("/package1/SomeClass.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    assertTrue("Expected error (type not a compile-time constant) did not occur",
            testLog.hasError("Type was not found or was not a compile-time constant: SomeClass"));
  }

  @Test
  public void testNonConstantInitializer() throws Exception {
    File sourcefile = getFile("/package1/NonConstantInitializer.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    assertTrue("Expected error (initializer not a compile-time constant) did not occur",
            testLog.hasError("Parameter initializer must be compile-time constant."));
  }

  @Test
  public void testStaticReference() throws Exception {
    assertCompilationResult("package1/WithStaticReference");
  }

  @Test
  public void testNoMultipleThisAliases() throws Exception {
    assertCompilationResult("package1/NoMultipleThisAliases");
  }

  @Test
  public void testNoPrimitiveInit() throws Exception {
    assertCompilationResult("package1/NoPrimitiveInit");
  }

  @Test
  public void testChainedConstants() throws Exception {
    assertCompilationResult("package1/ChainedConstants");
  }

  @Test
  public void testChainedConstantsApi() throws Exception {
    assertApiCompilationResult("package1/ChainedConstants");
  }

  @Test
  public void testStaticAndNonStatic() throws Exception {
    assertCompilationResult("package1/StaticAndNonStatic");
  }

  @Test
  public void testPublicApiApi() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/IncludedClass");
  }

  @Test
  public void testInterfaceApi() throws Exception {
    assertApiCompilationResult("package1/Interface");
  }

  @Test
  public void testImplementInterface() throws Exception {
    assertApiCompilationResult("package1/ImplementsInterface");
  }

  @Test
  public void testParameterInitializers() throws Exception {
    assertApiCompilationResult("package1/ParameterInitializers");
  }

  @Test
  public void testSuperCallParameters() throws Exception {
    assertApiCompilationResult("package1/SuperCallParameters");
  }

  @Test
  public void testImportReduction() throws Exception {
    assertApiCompilationResult("package1/someOtherPackage/ImportReduction");
  }

  @Test
  public void testImportReductionExcludeClass() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/someOtherPackage/ImportReduction", "withExclude/");
  }

  @Test
  public void testAuxVarConfusion() throws Exception {
    assertCompilationResult("package1/AuxVarConfusion");
  }

  @Test
  public void testNamespaceDeclarationApi() throws Exception {
    assertApiCompilationResult("package1/testNamespace");
  }

  @Test
  public void testPackageGlobalVar() throws Exception {
    assertCompilationResult("package1/somePackageGlobal");
  }
  
  @Test
  public void testUninitializedPackageGlobalVar() throws Exception {
    assertCompilationResult("package1/uninitializedPackageGlobal");
  }
  
  @Test
  public void testPackageGlobalFun() throws Exception {
    assertCompilationResult("package1/somePackageGlobalFun");
  }
  
  @Test
  public void testPackageGlobalVarApi() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/somePackageGlobal");
  }
  
  @Test
  public void testPackageGlobalFunApi() throws Exception {
    assertApiCompilationResult("package1/somePackageGlobalFun");
  }
  
  @Test
  public void testNativeApi() throws Exception {
    File compileResult = compile("package1/SomeNativeClass");
    assertFalse("[Native] classes must not have compile output.", compileResult.exists());
    assertApiCompilationResult("package1/SomeNativeClass");
  }

  @Test
  public void testClassWithNamespacedMembers() throws Exception {
    assertApiCompilationResult("package1/someOtherPackage/NamespacedMembers");
  }

  private void assertApiCompilationResult(String path) throws URISyntaxException, IOException {
    assertApiCompilationResult(path, "");
  }

  private void assertApiCompilationResult(String path, String expectPath) throws URISyntaxException, IOException {
    File sourcefile = getFile("/" + path + ".as");
    config.addSourceFile(sourcefile);
    //noinspection ResultOfMethodCallIgnored
    apiOutputFolder.mkdirs(); // NOSONAR
    jooc.run();

    File destFile = new File(apiOutputFolder, path + ".as");
    assertTrue(destFile.exists());

    String result = readFileToString(destFile);
    String expected = readFileToString(getFile("/expectedApi/" + expectPath + path + ".as"));
    assertEquals("Result file not equal", expected, result);
  }


  private void assertCompilationResult(String relativeClassFileName) throws URISyntaxException, IOException {
    File destFile = compile(relativeClassFileName);
    assertTrue("the output file " + destFile + " should exist, but doesn't", destFile.exists());

    String result = readFileToString(destFile);
    String expected = readFileToString(getFile("/expected/" + relativeClassFileName + ".js"));
    expected = expected.replace("@runtimeVersion", JoocProperties.getRuntimeVersion());
    expected = expected.replace("@version", JoocProperties.getVersion());
    assertEquals("Result file not equal", expected, result);
  }

  private File compile(String relativeClassFileName) throws URISyntaxException {
    File sourceFile = getFile("/" + relativeClassFileName + ".as");
    config.addSourceFile(sourceFile);
    jooc.run();
    return new File(outputFolder, relativeClassFileName + ".js");
  }

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private static String readFileToString(File file) throws IOException {
    String result = FileUtils.readFileToString(file);
    if (!"\n".equals(LINE_SEPARATOR)) { // Windows...
      // normalize line separators:
      return result.replace(LINE_SEPARATOR, "\n");
    }
    return result;
  }

  private File getFile(String absolutePath) throws URISyntaxException {
    return new File(getClass().getResource(absolutePath).toURI());
  }
}
