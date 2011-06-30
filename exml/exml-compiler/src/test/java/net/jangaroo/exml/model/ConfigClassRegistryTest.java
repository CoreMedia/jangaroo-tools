package net.jangaroo.exml.model;

import net.jangaroo.jooc.input.FileInputSource;
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
  public void testGetConfigClassByName() throws Exception {
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));

    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());

    ConfigClass configClass = registry.getConfigClassByName("testNamespace.config.TestLabel");
    Assert.assertNotNull(configClass);
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testPackage.TestLabel", configClass.getComponentName());
    Assert.assertEquals(1, configClass.getCfgs().size());
  }
}
