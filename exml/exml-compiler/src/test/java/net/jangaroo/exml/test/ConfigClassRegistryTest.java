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
    getConfigClassRegistry().scanAllExmlFiles();

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentClassName());
    Assert.assertEquals(1, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromExml() throws Exception {
    setUp("testNamespace.config");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentClassName());
    Assert.assertEquals(1, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestLabel"));

    // 3rd try after full scan
    getConfigClassRegistry().scanAllExmlFiles();
    Assert.assertEquals(configClass, getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestLabel"));
  }

  @Test
  public void testGenerateFromExmlWithPregeneratedActionScript() throws Exception {
    setUp("testNamespace.config");

    File destDir = new File(outputFolder.getRoot(), "testNamespace/config");
    destDir.mkdirs();
    FileUtils.copyFileToDirectory(new File(getConfigClassRegistry().getConfig().getSourcePath().get(0), "testNamespace/config/TestComponent.as"),
            destDir);

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(3, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestComponent"));
  }

  @Test
  public void testGenerateFromLocalActionScript() throws Exception {
    setUp("somewhere.else.config", "/", "/almostEmptyPackage");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromClassPathActionScript() throws Exception {
    setUp("somewhere.else.config", "/almostEmptyPackage", "/");

    ConfigClass configClass = getConfigClassRegistry().getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test
  public void testDoesNotExist() throws Exception {
    setUp("testNamespace.config");
    assertNull(getConfigClassRegistry().getConfigClassByName("does.not.Exist"));
  }
}
