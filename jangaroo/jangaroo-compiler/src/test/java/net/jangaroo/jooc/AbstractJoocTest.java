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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static net.jangaroo.jooc.FilePositionMatcher.matchesPosition;

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
    private LinkedHashMap<String, FilePosition> errors = new LinkedHashMap<>();

    @Override
    public void error(FilePosition position, String msg) {
      hasErrors = true;
      errors.put(msg, position);
      System.out.println(String.format("[ERROR] %s (%d:%d): %s", position.getFileName(), position.getLine(), position.getColumn(), msg));
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      System.out.println(String.format("[ERROR] %s", msg));
      errors.put(msg, null);
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
      return errors.keySet().contains(expected);
    }

    public FilePosition getPosition(String error) {
      return errors.get(error);
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

  void assertErrorAt(String expected, int line, int column) {
    Assert.assertThat(testLog.getPosition(expected), matchesPosition(line, column));
  }

  void assertApiCompilationResult(String relativeClassFileName, String expectedPath) throws URISyntaxException, IOException {
    apiOutputFolder.mkdirs(); // NOSONAR
    compile(".as", relativeClassFileName);
    verifyOutput(relativeClassFileName, apiOutputFolder, expectedPath, ".as");
  }

  void assertCompilationResult(String relativeClassFileName) throws URISyntaxException, IOException {
    assertCompilationResult(relativeClassFileName, ".as");
  }

  void assertCompilationResult(String relativeClassFileName, String extension) throws URISyntaxException, IOException {
    assertCompilationResult(relativeClassFileName, extension, "/expected");
  }

  void assertCompilationResult(String relativeClassFileName, String extension, String expectedPath) throws URISyntaxException, IOException {
    compile(extension, relativeClassFileName);
    verifyClassOutput(relativeClassFileName, expectedPath);
  }

  void verifyClassOutput(String relativeClassFileName, String expectedPath) throws URISyntaxException, IOException {
    verifyOutput(relativeClassFileName, outputFolder, expectedPath, ".js");
  }

  void verifyOutput(String relativeClassFileName, File targetDir, String expectedPath, String expectedExtension) throws URISyntaxException, IOException {
    File destFile = outputFile(targetDir, relativeClassFileName, expectedExtension);
    assertTrue("the output file " + destFile + " should exist, but doesn't", destFile.exists());

    String result = readFileToString(destFile);
    int sourceMappingUrlPos = result.lastIndexOf("//@ sourceMappingURL=");
    if (sourceMappingUrlPos != -1) {
      result = result.substring(0, sourceMappingUrlPos);
    }
    File expectedFile = getFile(expectedPath + '/' + relativeClassFileName + expectedExtension);
    String expected = readFileToString(expectedFile);
    expected = expected.replace("@runtimeVersion", JoocProperties.getRuntimeVersion());
    expected = expected.replace("@version", JoocProperties.getVersion());
    assertEquals("Result file " + destFile.getAbsolutePath() +
                    " not equal to expected file " + expectedFile.getAbsolutePath(),
            expected,
            result);
  }

  File outputFile(File targetDir, String relativeClassFileName, String expectedExtension) {
    return new File(targetDir, relativeClassFileName + expectedExtension);
  }

  void compile(String relativeClassFileName) throws URISyntaxException {
    compile(".as", relativeClassFileName);
  }

  void compile(String extension, String ...relativeClassFileNames) throws URISyntaxException {
    for (String relativeClassFileName : relativeClassFileNames) {
      File sourceFile = getFile("/" + relativeClassFileName + extension);
      config.addSourceFile(sourceFile);
    }
    jooc.run();
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
