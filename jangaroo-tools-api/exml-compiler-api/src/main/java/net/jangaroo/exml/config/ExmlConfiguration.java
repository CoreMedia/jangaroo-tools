package net.jangaroo.exml.config;

import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.utils.FileLocations;
import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.utils.CompilerUtils;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;

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

  @SuppressWarnings({"UnusedDeclaration" })
  public File computeGeneratedConfigClassFile(File exmlFile) {
    return computeConfigClassTarget(CompilerUtils.uncapitalize(CompilerUtils.removeExtension(exmlFile.getName())));
  }

  @SuppressWarnings({"UnusedDeclaration" })
  public File computeGeneratedComponentClassFile(File exmlFile) throws IOException {
    File sourceDir = findSourceDir(exmlFile);
    String qName = CompilerUtils.qNameFromFile(sourceDir, exmlFile);
    String className = ExmlUtils.createComponentClassName(CompilerUtils.className(qName));
    String packageName = CompilerUtils.packageName(qName);
    // compute potential file location of component class in source directory:
    File classFile = CompilerUtils.fileFromQName(packageName, className, sourceDir, Jooc.AS_SUFFIX);
    // component class is only generated if it is not already present as source:
    return classFile.exists()
      ? null
      : CompilerUtils.fileFromQName(packageName, className, getOutputDirectory(), Jooc.AS_SUFFIX);
  }

  @Override
  public String toString() {
    return "ExmlConfiguration{" +
            "configClassPackage='" + configClassPackage + '\'' +
            '}' + super.toString();
  }
}
