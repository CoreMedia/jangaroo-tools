package net.jangaroo.exml.test;

import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.jooc.input.FileInputSource;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

/**
 *
 */
public class ConfigClassRegistryTest extends AbstractExmlTest {
  @Test
  public void testScanInitially() throws Exception {
    setUp("testNamespace.config");
    registry.scanAllExmlFiles();

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentName());
    Assert.assertEquals(1, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromExml() throws Exception {
    setUp("testNamespace.config");

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentName());
    Assert.assertEquals(1, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, registry.getConfigClassByName("testNamespace.config.TestLabel"));

    // 3rd try after full scan
    registry.scanAllExmlFiles();
    Assert.assertEquals(configClass, registry.getConfigClassByName("testNamespace.config.TestLabel"));
  }

  @Test
  public void testGenerateFromExmlWithPregeneratedActionScript() throws Exception {
    setUp("testNamespace.config");

    File destDir = new File(outputFolder.getRoot(), "testNamespace/config");
    destDir.mkdirs();
    FileUtils.copyFileToDirectory(sourcePathInputSource.getChild("testNamespace/config/TestComponent.as").getFile(),
            destDir);

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals(3, configClass.getCfgs().size());

    // 2nd try should return the same object
    Assert.assertEquals(configClass, registry.getConfigClassByName("testNamespace.config.TestComponent"));
  }

  @Test
  public void testGenerateFromLocalActionScript() throws Exception {
    setUp("somewhere.else.config", "/", "/almostEmptyPackage");

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromClassPathActionScript() throws Exception {
    setUp("somewhere.else.config", "/almostEmptyPackage", "/");

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test(expected = Exception.class)
  public void testDoesNotExist() throws Exception {
    setUp("testNamespace.config");
    registry.getConfigClassByName("does.not.Exist");
  }
}
