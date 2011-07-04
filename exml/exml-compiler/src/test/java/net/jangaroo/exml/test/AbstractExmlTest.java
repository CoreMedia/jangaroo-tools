package net.jangaroo.exml.test;

import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.jooc.input.FileInputSource;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;

public abstract class AbstractExmlTest {
  FileInputSource sourcePathInputSource;
  FileInputSource classpathInputSource;
  ConfigClassRegistry registry;

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  protected void setUp(String configClassPackage) throws Exception {
    setUp(configClassPackage, "/", "/");
  }

  protected void setUp(String configClassPackage, String sourcePathFileName, String classPathFileName) throws Exception {
    File sourcePathFile = new File(getClass().getResource(sourcePathFileName).toURI());
    sourcePathInputSource = new FileInputSource(sourcePathFile);
    File classPathFile = new File(getClass().getResource(classPathFileName).toURI());
    classpathInputSource = new FileInputSource(classPathFile);

    ExmlConfiguration config = new ExmlConfiguration();
    config.setSourcePath(Arrays.asList(sourcePathFile));
    config.setClassPath(Arrays.asList(classPathFile));
    config.setOutputDirectory(outputFolder.getRoot());
    config.setConfigClassPackage(configClassPackage);
    registry = new ConfigClassRegistry(config, sourcePathInputSource, classpathInputSource);
  }
}
