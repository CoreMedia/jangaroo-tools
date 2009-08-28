package net.jangaroo.extxml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The meta-model of an Ext JS component class.
 */
public class ComponentClass extends DescriptionHolder {

  public ComponentClass(String xtype, String className) {
    this.xtype = xtype;
    this.className = className;
  }

  public ComponentClass(File srcFile) {
    this.srcFile = srcFile;
  }

  public void setSuite(ComponentSuite suite) {
    this.suite = suite;
  }

  public ComponentSuite getSuite() {
    return suite;
  }

  public String getRelativeSrcFilePath() {
    if (srcFile != null) {
      int rootDirPathLength = getSuite().getRootDir().getPath().length();
      return srcFile.getPath().substring(rootDirPathLength);
    }
    return null;
  }

  public File getSrcFile() {
    return srcFile;
  }

  public void setXtype(String xtype) {
    this.xtype = xtype;
  }

  public String getXtype() {
    return xtype;
  }

  public String getElementName() {
    return suite.getPrefix() + xtype;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public String getXsType() {
    return suite.getPrefix() + className;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public ComponentClass getSuperClass() {
    return suite.getComponentClassByClassName(superClassName);
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public Collection<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
      .append("class   ").append(className)
      .append("\nxtype   ").append(xtype);
    if (superClassName != null) {
      builder.append("\nextends ").append(superClassName);
    }
    if (srcFile != null) {
      builder.append("\nfile    ").append(getRelativeSrcFilePath());
    }
    for (ConfigAttribute cfg : cfgs) {
      builder.append("\n  ").append(cfg);
    }
    return builder.toString();
  }

  private ComponentSuite suite;
  private File srcFile;
  private String xtype;
  private String className;
  private String superClassName;
  private Collection<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
}
