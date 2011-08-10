package net.jangaroo.exml.config;

import net.jangaroo.jooc.config.FileLocations;

import java.io.File;

public class ExmlConfiguration extends FileLocations {
  private String configClassPackage;
  // the directory into which resource (xsds) files are generated
  private File resourceOutputDirectory;

  public String getConfigClassPackage() {
    return configClassPackage;
  }

  public void setConfigClassPackage(String configClassPackage) {
    this.configClassPackage = configClassPackage;
  }

  public File getResourceOutputDirectory() {
    return resourceOutputDirectory;
  }

  public void setResourceOutputDirectory(File resourceOutputDirectory) {
    this.resourceOutputDirectory = resourceOutputDirectory;
  }

  @Override
  public String toString() {
    return "ExmlConfiguration{" +
            "configClassPackage='" + configClassPackage + '\'' +
            '}' + super.toString();
  }
}
