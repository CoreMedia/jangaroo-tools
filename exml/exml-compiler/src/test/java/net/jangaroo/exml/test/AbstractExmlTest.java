package net.jangaroo.exml.test;

import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.jooc.config.FileLocations;
import net.jangaroo.jooc.input.FileInputSource;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;
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

    FileLocations fileLocations = new FileLocations();
    fileLocations.setSourcePath(Arrays.asList(sourcePathFile));
    fileLocations.setClassPath(Arrays.asList(classPathFile));
    fileLocations.setOutputDirectory(outputFolder.getRoot());
    registry = new ConfigClassRegistry(fileLocations, sourcePathInputSource, classpathInputSource, configClassPackage);
  }
}
