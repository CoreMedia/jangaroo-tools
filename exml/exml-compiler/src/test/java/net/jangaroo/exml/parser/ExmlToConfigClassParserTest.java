package net.jangaroo.exml.parser;

import net.jangaroo.exml.model.ConfigClass;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ExmlToConfigClassParserTest {

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  @Test
  public void testGenerateConfig() throws Exception {
    File result = new File(outputFolder.getRoot(), "testNamespace/config/TestComponent.as");
    File source = getFile("/testPackage/TestComponent.exml");
    File sourceRoot = getFile("/");

    ConfigClass configClass = ExmlToConfigClassParser.generateConfigClass(source, sourceRoot, outputFolder.getRoot(), "testNamespace.config");

    assertNotNull(configClass);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateWithExisitingNewOutputFile() throws Exception {
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "TestComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");
    File sourceRoot = getFile("/");

    ExmlToConfigClassParser.generateConfigClass(source, sourceRoot, outputFolder.getRoot(), "testNamespace.config");

    assertFalse("The files should differ because it was not written!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")).equals(FileUtils.readFileToString(result)));
  }

  @Test
  public void testGenerateWithExisitingOldOutputFile() throws Exception {
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "TestComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");
    File sourceRoot = getFile("/");

    //change modification date to 'old'
    result.setLastModified(source.lastModified()-1000);

    ExmlToConfigClassParser.generateConfigClass(source, sourceRoot, outputFolder.getRoot(), "testNamespace.config");

    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")), FileUtils.readFileToString(result));
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlToConfigClassParserTest.class.getResource(path).toURI());
  }
}
