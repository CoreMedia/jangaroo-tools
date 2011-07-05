package net.jangaroo.exml.test;

import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.model.ConfigClassRegistry;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;

public abstract class AbstractExmlTest {
  private Exmlc exmlc;

  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  protected void setUp(String configClassPackage) throws Exception {
    setUp(configClassPackage, "/", "/");
  }

  protected void setUp(String configClassPackage, String sourcePathFileName, String classPathFileName) throws Exception {
    File sourcePathFile = new File(getClass().getResource(sourcePathFileName).toURI());
    File classPathFile = new File(getClass().getResource(classPathFileName).toURI());

    ExmlConfiguration config = new ExmlConfiguration();
    config.setSourcePath(Arrays.asList(sourcePathFile));
    config.setClassPath(Arrays.asList(classPathFile));
    config.setOutputDirectory(outputFolder.getRoot());
    config.setConfigClassPackage(configClassPackage);
    exmlc = new Exmlc(config);
  }

  public Exmlc getExmlc() {
    return exmlc;
  }

  public ConfigClassRegistry getConfigClassRegistry() {
    return exmlc.getConfigClassRegistry();
  }
}
