package net.jangaroo.exml.config;

import net.jangaroo.utils.FileLocations;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.utils.CompilerUtils;
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

  public File computeConfigClassTarget(String configClassName) {
    return CompilerUtils.fileFromQName(getConfigClassPackage(), configClassName, getOutputDirectory(), Jooc.AS_SUFFIX);
  }
  
  @Override
  public String toString() {
    return "ExmlConfiguration{" +
            "configClassPackage='" + configClassPackage + '\'' +
            '}' + super.toString();
  }
}
