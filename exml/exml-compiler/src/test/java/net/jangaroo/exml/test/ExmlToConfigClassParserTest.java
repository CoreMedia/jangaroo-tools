package net.jangaroo.exml.test;

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
    File result = new File(outputFolder.getRoot(), "testNamespace/config/testComponent.as");
    File source = getFile("/testPackage/TestComponent.exml");

    File outputFile = getExmlc().generateConfigClass(source);

    assertNotNull(outputFile);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testComponent.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateConfigExcludeClassFalse() throws Exception {
    setUp("testNamespace.config");
    File result = new File(outputFolder.getRoot(), "testNamespace/config/testComponentPublicApiFalse.as");
    File source = getFile("/testPackage/TestComponentPublicApiFalse.exml");

    File outputFile = getExmlc().generateConfigClass(source);

    assertNotNull(outputFile);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testComponentPublicApiFalse.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateConfigExcludeClassTrue() throws Exception {
    setUp("testNamespace.config");
    File result = new File(outputFolder.getRoot(), "testNamespace/config/testComponentPublicApiTrue.as");
    File source = getFile("/testPackage/TestComponentPublicApiTrue.exml");

    File outputFile = getExmlc().generateConfigClass(source);

    assertNotNull(outputFile);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testComponentPublicApiTrue.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateWithExisitingNewOutputFile() throws Exception {
    setUp("testNamespace.config");
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "TestComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");

    getExmlc().generateConfigClass(source);

    assertFalse("The files should differ because it was not written!", FileUtils.readFileToString(getFile("/testNamespace/config/testComponent.as")).equals(FileUtils.readFileToString(result)));
  }

  @Test
  public void testGenerateWithExisitingOldOutputFile() throws Exception {
    setUp("testNamespace.config");
    File packageFolder = new File(outputFolder.getRoot(), "testNamespace/config/");
    packageFolder.mkdirs();

    File result = new File(packageFolder, "testComponent.as");
    result.createNewFile();

    File source = getFile("/testPackage/TestComponent.exml");

    //change modification date to 'old'
    result.setLastModified(source.lastModified()-1000);

    getExmlc().generateConfigClass(source);

    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testComponent.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGenerateActionConfig() throws Exception {
    setUp("testNamespace.config");
    File result = new File(outputFolder.getRoot(), "testNamespace/config/testAction.as");
    File source = getFile("/testPackage/TestAction.exml");

    File outputFile = getExmlc().generateConfigClass(source);

    assertNotNull(outputFile);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testAction.as")), FileUtils.readFileToString(result));
  }

  @Test
  public void testGeneratePluginConfig() throws Exception {
    setUp("testNamespace.config");
    File result = new File(outputFolder.getRoot(), "testNamespace/config/testPlugin.as");
    File source = getFile("/testPackage/TestPlugin.exml");

    File outputFile = getExmlc().generateConfigClass(source);

    assertNotNull(outputFile);
    assertTrue("Exml config file does not exist", result.exists());
    assertEquals("The files differ!", FileUtils.readFileToString(getFile("/testNamespace/config/testPlugin.as")), FileUtils.readFileToString(result));
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(ExmlToConfigClassParserTest.class.getResource("/test-module" + path).toURI());
  }
}
