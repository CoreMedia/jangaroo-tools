package net.jangaroo.exml.test;

import net.jangaroo.exml.generator.ExmlConfigClassGenerator;
import net.jangaroo.exml.model.ConfigClass;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ExmlToConfigClassParserTest extends AbstractExmlTest {

  @Test
  public void testGenerateConfig() throws Exception {
    setUp("testNamespace.config");
    File result = new File(outputFolder.getRoot(), "testNamespace/config/TestComponent.as");
    File source = getFile("/testPackage/TestComponent.exml");

    ExmlConfigClassGenerator exmlConfigClassGenerator = new ExmlConfigClassGenerator(registry.getConfig());
    ConfigClass configClass = exmlConfigClassGenerator.generateConfigClass(source);

    assertNotNull(configClass);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateWithExisitingNewOutputFile() throws Exception {
    setUp("testNamespace.config");
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "TestComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");

    ExmlConfigClassGenerator exmlConfigClassGenerator = new ExmlConfigClassGenerator(registry.getConfig());
    exmlConfigClassGenerator.generateConfigClass(source);

    assertFalse("The files should differ because it was not written!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")).equals(FileUtils.readFileToString(result)));
  }

  @Test
  public void testGenerateWithExisitingOldOutputFile() throws Exception {
    setUp("testNamespace.config");
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "TestComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");

    //change modification date to 'old'
    result.setLastModified(source.lastModified()-1000);

    ExmlConfigClassGenerator exmlConfigClassGenerator = new ExmlConfigClassGenerator(registry.getConfig());
    exmlConfigClassGenerator.generateConfigClass(source);

    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/TestComponent.as")), FileUtils.readFileToString(result));
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlToConfigClassParserTest.class.getResource(path).toURI());
  }
}
