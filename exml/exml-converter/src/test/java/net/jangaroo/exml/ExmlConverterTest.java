package net.jangaroo.exml;

import net.jangaroo.exml.exmlconverter.ExmlConverterTool;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class ExmlConverterTest {
  private class ExitError extends Error {}
  
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  private class TestTool extends ExmlConverter {

    @Override
    void exit(int code) {
      throw new ExitError();
    }
  }

  @Test(expected = ExitError.class)
  public void testUsageNoArgs() throws Exception {
    new TestTool().main(new String[0]);
  }

  @Test
  public void testMain() throws Exception {
    File mappingProps = new File(getClass().getResource("/mapping.properties").toURI());
    File moduleRoot = new File(getClass().getResource("/testModule").toURI());

    String[] args = {"-m", moduleRoot.getAbsolutePath(), "-p", mappingProps.getAbsolutePath(), "-o" ,outputFolder.getRoot().getAbsolutePath()};
    ExmlConverter.main(args);

    File result = new File(outputFolder.getRoot(), "com/coremedia/ui/config/image.as");
    assertTrue("config class not created",result.exists());
  }
}
