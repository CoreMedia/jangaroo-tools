package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.CatalogGenerator;
import net.jangaroo.jooc.mxml.ComponentPackageManifestParser;
import net.jangaroo.jooc.mxml.ComponentPackageModel;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AbstractJoocTest {

  @Rule
  public final TemporaryFolder tmpFolder = new TemporaryFolder();
  protected final TestLog testLog = new TestLog();

  protected File outputFolder;
  protected File apiOutputFolder;
  protected Jooc jooc;

  protected JoocConfiguration config;

  static class TestLog implements CompileLog {

    private boolean hasErrors = false;
    private List<String> errors = new ArrayList<String>();

    @Override
    public void error(FilePosition position, String msg) {
      hasErrors = true;
      errors.add(msg);
      System.out.println(String.format("[ERROR] %s (%d:%d): %s", position.getFileName(), position.getLine(), position.getColumn(), msg));
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      System.out.println(String.format("[ERROR] %s", msg));
      errors.add(msg);
    }

    @Override
    public void warning(FilePosition position, String msg) {
      System.out.println(String.format("[WARN ] %s (%d:%d): %s", position.getFileName(), position.getLine(), position.getColumn(), msg));
    }

    @Override
    public void warning(String msg) {
      System.out.println(String.format("[WARN ] %s", msg));
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
    config.setVerbose(true);
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
    jooc.getMxmlComponentRegistry().registerElement("library://test.namespace", "someOtherClass",
            "package1.someOtherPackage.SomeOtherClass");
  }

  void generateCatalogFromManifest(File manifestFile, File catalogFile) throws IOException {
    InputSource inputSource = new FileInputSource(manifestFile, true);
    ComponentPackageModel componentPackageModel = new ComponentPackageManifestParser("library://test.namespace").parse(inputSource.getInputStream());
    MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();
    mxmlComponentRegistry.add(componentPackageModel);
    new CatalogGenerator(mxmlComponentRegistry).generateCatalog(catalogFile);
  }

  void assertApiCompilationResult(String path, String expectPath) throws URISyntaxException, IOException {
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


  void assertCompilationResult(String relativeClassFileName) throws URISyntaxException, IOException {
    assertCompilationResult(relativeClassFileName, ".as");
  }

  void assertCompilationResult(String relativeClassFileName, String extension) throws URISyntaxException, IOException {
    File destFile = compile(relativeClassFileName, extension);
    assertTrue("the output file " + destFile + " should exist, but doesn't", destFile.exists());

    String result = readFileToString(destFile);
    int sourceMappingUrlPos = result.lastIndexOf("//@ sourceMappingURL=");
    if (sourceMappingUrlPos != -1) {
      result = result.substring(0, sourceMappingUrlPos);
    }
    File expectedFile = getFile("/expected/" + relativeClassFileName + ".js");
    String expected = readFileToString(expectedFile);
    expected = expected.replace("@runtimeVersion", JoocProperties.getRuntimeVersion());
    expected = expected.replace("@version", JoocProperties.getVersion());
    assertEquals("Result file " + destFile.getAbsolutePath() +
            " not equal to expected file " + expectedFile.getAbsolutePath(),
            expected,
            result);
  }

  File compile(String relativeClassFileName) throws URISyntaxException {
    return compile(relativeClassFileName, ".as");
  }

  private File compile(String relativeClassFileName, String extension) throws URISyntaxException {
    File sourceFile = getFile("/" + relativeClassFileName + extension);
    config.addSourceFile(sourceFile);
    jooc.run();
    return new File(outputFolder, relativeClassFileName + ".js");
  }

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  static String readFileToString(File file) throws IOException {
    String result = FileUtils.readFileToString(file);
    if (!"\n".equals(LINE_SEPARATOR)) { // Windows...
      // normalize line separators:
      return result.replace(LINE_SEPARATOR, "\n");
    }
    return result;
  }

  File getFile(String absolutePath) throws URISyntaxException {
    return new File(getClass().getResource(absolutePath).toURI());
  }
}
