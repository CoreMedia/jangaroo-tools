package net.jangaroo.extxml;

import java.util.Set;
import java.util.HashSet;
import java.io.File;

/**
 * The meta-model of an Ext JS component class.
 */
public class ComponentClass extends DescriptionHolder {

  public ComponentClass(String xtype, String className) {
    this.xtype = xtype;
    this.className = className;
  }

  public ComponentClass(String relativeSrcFilePath) {
    this.relativeSrcFilePath = relativeSrcFilePath;
  }

  public void setSuite(ComponentSuite suite) {
    this.suite = suite;
  }

  public ComponentSuite getSuite() {
    return suite;
  }

  public String getRelativeSrcFilePath() {
    return relativeSrcFilePath;
  }

  public File getSrcFile() {
    return new File(suite.getRootDir(), relativeSrcFilePath);
  }

  public String getXtype() {
    return xtype;
  }

  public void setXtype(String xtype) {
    this.xtype = xtype;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public ComponentClass getSuperClass() {
    // TODO: search for super class in _all_ suites!
    return suite.getComponentClassByClassName(superClassName);
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public Set<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
      .append("\nclass   ").append(className)
      .append("\nxtype   ").append(xtype);
   if (relativeSrcFilePath != null) {
     builder.append(  "file    ").append(relativeSrcFilePath);
   }
    if (superClassName != null) {
      builder.append("\nextends ").append(superClassName);
    }
    for (ConfigAttribute cfg : cfgs) {
      builder.append("\n  ").append(cfg);
    }
    return builder.toString();
  }

  private ComponentSuite suite;
  private String relativeSrcFilePath;
  private String xtype;
  private String className;
  private String superClassName;
  private Set<ConfigAttribute> cfgs = new HashSet<ConfigAttribute>();
}
