package net.jangaroo.exml.config;

import net.jangaroo.jooc.config.FileLocations;
import org.kohsuke.args4j.Option;

import java.io.File;

public class ExmlConfiguration extends FileLocations {
  private String configClassPackage;
  // the directory into which resource (xsds) files are generated
  private File resourceOutputDirectory;

  public String getConfigClassPackage() {
    return configClassPackage;
  }

  @Option(name = "-c", aliases = "--config-package", metaVar = "NAME", usage = "Name of the config class package", required = true)
  public void setConfigClassPackage(String configClassPackage) {
    this.configClassPackage = configClassPackage;
  }

  public File getResourceOutputDirectory() {
    return resourceOutputDirectory;
  }

  @Option(name="-r", metaVar = "RES_DIR", usage = "output directory for generated xsd files, default is DEST_DIR")
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
