package net.jangaroo.extxml;

import net.jangaroo.extxml.json.Json;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The meta-model of an Ext JS component class.
 */
public final class ComponentClass extends DescriptionHolder {

  private ComponentSuite suite;
  private File srcFile;
  private String xtype;
  private List<String> imports;
  private String fullClassName;
  private String superClassName;
  private List<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
  private Json json;
  private ComponentType type;

  public ComponentClass(List<String> imports, String fullClassName, String superClassName, Json json) {
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
    return relativeSrcFilePath(getSuite(), getSrcFile());
  }

  public static String relativeSrcFilePath(ComponentSuite suite, File srcFile) {
    if (srcFile != null && suite != null && suite.getRootDir() != null) {
      int rootDirPathLength = suite.getRootDir().getPath().length();
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

  /**
   * Returns the XML element name, this is either just the xtype or, if the xtype is
   * the full qualified class name, the class name (without packages)
   * @return the xml element name
   */
  public String getElementName() {
    return xtype.substring(xtype.lastIndexOf('.')+1);
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
    return superClassName == null ? null : suite.findComponentClassByFullClassName(superClassName);
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public Collection<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public Collection<ConfigAttribute> getDirectCfgs() {
    ComponentClass cc = getSuperClass();
    if (cc != null) {
      ArrayList<ConfigAttribute> directCfgs = new ArrayList<ConfigAttribute>(cfgs);
      do {
        directCfgs.removeAll(cc.getCfgs());
        cc = cc.getSuperClass();
      } while (cc != null);
      //System.out.println("Removed "+(cfgs.size()-directCfgs.size())+" inherited configs.");
      return directCfgs;
    }
    return cfgs;
  }

  public Collection<ConfigAttribute> getAllCfgs() {
    ComponentClass cc = getSuperClass();
    if (cc != null) {
     Set<ConfigAttribute> allCfgs = new HashSet<ConfigAttribute>();
     allCfgs.addAll(cfgs);
      do {
        allCfgs.addAll(cc.getCfgs());
        cc = cc.getSuperClass();
      } while (cc != null);
      //System.out.println("Removed "+(cfgs.size()-allCfgs.size())+" inherited configs.");
      return allCfgs;
    }
    return new HashSet(cfgs);
  }

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
  }

  public void setCfgs(List<ConfigAttribute> cfgs) {
    this.cfgs = cfgs;
  }

  public Json getJson() {
    return json;
  }

  public void setJson(Json json) {
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
      .append("class: ").append(fullClassName)
      .append(" xtype: ").append(xtype);
    if (superClassName != null) {
      builder.append(" extends ").append(superClassName);
    }
    if (srcFile != null) {
      builder.append(" file ").append(getRelativeSrcFilePath());
    }
    for (ConfigAttribute cfg : cfgs) {
      builder.append(" ").append(cfg);
    }
    return builder.toString();
  }
}
