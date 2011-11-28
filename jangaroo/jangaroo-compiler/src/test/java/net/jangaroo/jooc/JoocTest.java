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
    assertTrue("Expected error not occured", testLog.hasError("Type was not found or was not a compile-time constant: SomeClass"));
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
  public void testParameterInitializers() throws Exception {
    File sourcefile = getFile("/package1/ParameterInitializers.as");
    config.addSourceFile(sourcefile);
    //noinspection ResultOfMethodCallIgnored
    apiOutputFolder.mkdirs(); // NOSONAR
    jooc.run();

    File destFile = new File(apiOutputFolder,"package1/ParameterInitializers.as");
    assertTrue(destFile.exists());

    String result = FileUtils.readFileToString(destFile);
    String expected = FileUtils.readFileToString(getFile("/expectedApi/package1/ParameterInitializers.as"));
    assertEquals("Result file not equal", expected, result);
  }


  private void assertCompilationResult(String relativeClassFileName) throws URISyntaxException, IOException {
    File sourcefile = getFile("/" + relativeClassFileName + ".as");
    config.addSourceFile(sourcefile);
    jooc.run();

    File destFile = new File(outputFolder, relativeClassFileName + ".js");
    assertTrue(destFile.exists());

    String result = FileUtils.readFileToString(destFile);
    String expected = FileUtils.readFileToString(getFile("/expected/" + relativeClassFileName + ".js"));
    expected = expected.replace("@runtimeVersion", JoocProperties.getRuntimeVersion());
    expected = expected.replace("@version", JoocProperties.getVersion());
    assertEquals("Result file not equal", expected, result);
  }

  private File getFile(String absolutePath) throws URISyntaxException {
    return new File(getClass().getResource(absolutePath).toURI());
  }
}
