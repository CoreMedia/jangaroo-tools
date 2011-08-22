package net.jangaroo.extxml;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class ExtAsToConfigClassConverterTest {
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  @Test
  public void testMain() throws Exception {
    File mappingProps = new File(getClass().getResource("/mapping.properties").toURI());
    File moduleRoot = new File(getClass().getResource("/testModule").toURI());

    String[] args = {"-m", moduleRoot.getAbsolutePath(), "-p", mappingProps.getAbsolutePath(), "-o" ,outputFolder.getRoot().getAbsolutePath()};
    ExtAsToConfigClassConverter.main(args);

    File result = new File(outputFolder.getRoot(), "com/coremedia/ui/config/image.as");
    assertTrue("config class not created",result.exists());
  }
}
