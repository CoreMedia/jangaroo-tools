package net.jangaroo.jooc;

import net.jangaroo.jooc.config.DebugMode;
import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 */
public class JoocTest {

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();
  private Jooc jooc;
  private JoocConfiguration config;

  private TestLog testLog = new TestLog();

  private class TestLog implements CompileLog {

    private boolean hasErrors = false;
    private List<String> errors = new ArrayList<String>();

    @Override
    public void error(JooSymbol sym, String msg) {
      hasErrors = true;
      errors.add(msg);
      System.out.println(sym.getLine() + ";" + sym.getColumn() + ": '" + msg);
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      errors.add(msg);
    }

    @Override
    public void warning(JooSymbol sym, String msg) {
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
    config = new JoocConfiguration();
    File sourceDir = getFile("/");
    List<File> sourcepath = new ArrayList<File>();
    sourcepath.add(sourceDir);
    config.setSourcePath(sourcepath);
    config.setDebugMode(DebugMode.SOURCE);
    config.setOutputDirectory(outputFolder.getRoot());
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
    File sourcefile = getFile("/package1/WithStaticReference.as");
    config.addSourceFile(sourcefile);
    jooc.run();

    File destFile = new File(outputFolder.getRoot(),"package1/WithStaticReference.js");
    assertTrue(destFile.exists());

    String result = FileUtils.readFileToString(destFile);
    String expected = FileUtils.readFileToString(getFile("/expected/package1/WithStaticReference.js"));
    assertEquals("Result file not equal", expected, result);
  }

  private File getFile(String absolutePath) throws URISyntaxException {
    return new File(getClass().getResource(absolutePath).toURI());
  }
}
