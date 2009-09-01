package net.jangaroo.extxml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The meta-model of an Ext JS component class.
 */
public class ComponentClass extends DescriptionHolder {

  private ComponentSuite suite;
  private File srcFile;
  private String xtype;
  private List<String> imports;
  private String fullClassName;
  private String superClassName;
  private Collection<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
  private String json;
  private ComponentType type;

  public ComponentClass(List<String> imports, String fullClassName, String superClassName, String json) {
    this(fullClassName, fullClassName);
    this.imports = imports;
    this.superClassName = superClassName;
    this.json = json;
  }

  public ComponentClass(String xtype, String fullClassName) {
    this.xtype = xtype;
    this.fullClassName = fullClassName;
    this.imports = new ArrayList<String>();
  }

  public ComponentClass(File srcFile) {
    this.srcFile = srcFile;
    this.imports = new ArrayList<String>();
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

  public List<String> getImports() {
    return imports;
  }

  public void addImport(String className) {
    imports.add(className);
  }

  public void setImports(List<String> imports) {
    this.imports = imports;
  }

  public void setFullClassName(String fullClassName) {
    this.fullClassName = fullClassName;
  }

  public String getFullClassName() {
    return fullClassName;
  }

  public String getClassName() {
    return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
  }

  public String getPackageName() {
    int lastDotPos = fullClassName.lastIndexOf('.');
    return lastDotPos==-1 ? "" : fullClassName.substring(0, lastDotPos);
  }

  public String getXsType() {
    return suite.getPrefix() + fullClassName;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public ComponentClass getSuperClass() {
    return suite.getComponentClassByFullClassName(superClassName);
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

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public ComponentType getType() {
    return type;
  }

  public void setType(ComponentType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
      .append("class   ").append(fullClassName)
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
}
