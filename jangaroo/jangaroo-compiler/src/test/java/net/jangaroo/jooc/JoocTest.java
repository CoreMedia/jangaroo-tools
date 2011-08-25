package net.jangaroo.jooc;

import net.jangaroo.jooc.config.JoocConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

  private class TestLog implements CompileLog {

    private boolean hasErrors = false;
    @Override
    public void error(JooSymbol sym, String msg) {
      hasErrors = true;
      fail(msg);
    }

    @Override
    public void error(String msg) {
      hasErrors = true;
      fail(msg);
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
  }

  @Before
  public void setup() throws Exception{
    config = new JoocConfiguration();
    File sourceDir = getFile("/");
    List<File> sourcepath = new ArrayList<File>();
    sourcepath.add(sourceDir);
    config.setSourcePath(sourcepath);

    config.setOutputDirectory(outputFolder.getRoot());
    jooc = new Jooc(config, new TestLog());
  }

  @Test
  public void testEqualDeclarationVariableType() throws Exception {
    File sourcefile = getFile("/package1/SomeClass.as");
    config.addSourceFile(sourcefile);
    jooc.run();

    File destFile = new File(outputFolder.getRoot(),"package1/SomeClass.js");
    assertTrue(destFile.exists());
  }

  private File getFile(String absolutePath) throws URISyntaxException {
    return new File(getClass().getResource(absolutePath).toURI());
  }
}
