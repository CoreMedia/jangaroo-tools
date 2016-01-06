package net.jangaroo.exml.test;

import net.jangaroo.exml.model.ConfigClass;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNull;

/**
 *
 */
public class ConfigClassRegistryTest extends AbstractExmlTest {
  @Test
  public void testScanInitially() throws Exception {
    setUp("testNamespace.config");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.testLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentClassName());
    ConfigClass labelConfigClass = getConfigClassRegistry().getConfigClassByName("ext.config.label");
    Assert.assertNotNull(labelConfigClass);
    Assert.assertEquals("ext.config.label", labelConfigClass.getFullName());
    Assert.assertEquals(labelConfigClass, configClass.getSuperClass());
    Assert.assertEquals(1, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromExml() throws Exception {
    setUp("testNamespace.config");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.testLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentClassName());
    Assert.assertEquals(1, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, getConfigClassRegistry().getConfigClassByName("testNamespace.config.testLabel"));
  }

  @Test
  public void testGenerateFromExmlWithPregeneratedActionScript() throws Exception {
    setUp("testNamespace.config", "/expected", "/ext-as");

    File destDir = new File(outputFolder.getRoot(), "testNamespace/config");
    destDir.mkdirs();
    FileUtils.copyFileToDirectory(new File(getConfigClassRegistry().getConfig().getSourcePath().get(0), "testNamespace/config/testComponent.as"),
            destDir);

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.testComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(9, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, getConfigClassRegistry().getConfigClassByName("testNamespace.config.testComponent"));
  }

  @Test
  public void testGenerateFromLocalActionScript() throws Exception {
    setUp("somewhere.else.config", "/expected", "/ext-as");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.testComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(9, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromClassPathActionScript() throws Exception {
    setUp("somewhere.else.config", "/expected", "/ext-as");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.testComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(9, configClass.getCfgs().size());
  }

  @Test
  public void testDoesNotExist() throws Exception {
    setUp("testNamespace.config");
    assertNull(getConfigClassRegistry().getConfigClassByName("does.not.Exist"));
  }
}
