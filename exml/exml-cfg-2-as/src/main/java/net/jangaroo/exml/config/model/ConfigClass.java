package net.jangaroo.exml.config.model;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConfigClass extends DescriptionHolder {

  private List<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();

  private String packageName;

  private String superClassName;
  private String superClassPackage;

  private final File sourceFile;
  private final File outputFile;

  private final File rootFolder;

  public ConfigClass(File source, File outputFile, File rootFolder) {
    this.sourceFile = source;
    this.outputFile = outputFile;
    this.rootFolder = rootFolder;
  }

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
  }

  public List<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public File getOutputFile() {
    return outputFile;
  }

  public String getName() {
    return FilenameUtils.getBaseName(sourceFile.getName());
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public String getSuperClassPackage() {
    return superClassPackage;
  }

  public void setSuperClassPackage(String superClassPackage) {
    this.superClassPackage = superClassPackage;
  }

  public String getComponentFullQualifiedName() {
    int rootDirPathLength = rootFolder.getPath().length()+1;
    String subpath = FilenameUtils.removeExtension(sourceFile.getPath().substring(rootDirPathLength));
    return subpath.replaceAll(File.separator,".");
  }
}
