package net.jangaroo.extxml;

import java.util.Set;
import java.util.HashSet;
import java.io.File;

/**
 * The meta-model of an Ext JS component class.
 */
public class ComponentClass extends DescriptionHolder {

  public ComponentClass(ComponentSuite suite, String relativeSrcFilePath) {
    this.suite = suite;
    this.relativeSrcFilePath = relativeSrcFilePath;
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
      .append(  "file    ").append(relativeSrcFilePath)
      .append("\nclass   ").append(className)
      .append("\nxtype   ").append(xtype)
      .append("\nextends ").append(superClassName);
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
