package net.jangaroo.exml.model;

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
public class ConfigClassRegistryTest {

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  @Test
  public void testScanInitially() throws Exception {
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());
    registry.scanAllExmlFiles();

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentName());
    Assert.assertEquals(1, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromExml() throws Exception {
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());

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
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));

    File destDir = new File(outputFolder.getRoot(), "testNamespace/config");
    destDir.mkdirs();
    FileUtils.copyFileToDirectory(sourcePathInputSource.getChild("testNamespace/config/TestComponent.as").getFile(),
            destDir);

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());

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
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/almostEmptyPackage").toURI()));

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "somewhere.else.config", outputFolder.getRoot());

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test
  public void testGenerateFromClassPathActionScript() throws Exception {
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/almostEmptyPackage").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "somewhere.else.config", outputFolder.getRoot());

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestComponent");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals(3, configClass.getCfgs().size());
  }

  @Test(expected = Exception.class)
  public void testDoesNotExist() throws Exception {
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());
    registry.getConfigClassByName("does.not.Exist");
  }
}
