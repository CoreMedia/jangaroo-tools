package net.jangaroo.exml.model;

import net.jangaroo.utils.CompilerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public final class ConfigClass extends DescriptionHolder {

  private List<ConfigAttribute> cfgs = new ArrayList<ConfigAttribute>();
  private Map<String, ConfigAttribute> cfgsByName = new HashMap<String, ConfigAttribute>();
  private List<Constant> constants = new ArrayList<Constant>();

  private String name;
  private String packageName;
  private String superClassName;
  private ConfigClass superClass;
  private String componentClassName;
  private ConfigClassType type;
  private String typeValue;

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
    cfgsByName.put(cfg.getName(), cfg);
  }

  public ConfigClass getSuperClass() {
    return superClass;
  }

  public void setSuperClass(ConfigClass superClass) {
    this.superClass = superClass;
  }

  public List<ConfigAttribute> getCfgs() {
    return cfgs;
  }

  public List<Constant> getConstants() {
    return constants;
  }

  /**
   * Returns only the ConfigAttributes that are not already defined in the super class
   *
   * @return the list of ConfigAttributes
   */
  public List<ConfigAttribute> getDirectCfgs() {
    ConfigClass cc = getSuperClass();
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

  public ConfigAttribute getCfgByName(String name) {
    return cfgsByName.get(name);
  }

  public void addConstant(Constant constant) {
    constants.add(constant);
  }

  /**
   * Create a ConfigClass name from the given name. By convention all ConfigClass names are uncapitalized.
   *
   * @param name the name
   * @return return the new config-class name, matching the conventions.
   */
  public static String createConfigClassName(String name) {
    return CompilerUtils.uncapitalize(name);
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * the name
   * @return the name
   */
  public String getName() {
    return name;
  }

  public ConfigClassType getType() {
    return type;
  }

  public void setType(ConfigClassType type) {
    this.type = type;
  }

  public String getTypeValue() {
    return typeValue;
  }

  public void setTypeValue(String typeValue) {
    this.typeValue = typeValue;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getNs() {
    String[] parts = packageName.split("\\.");
    StringBuilder ns = new StringBuilder();
    for (String part : parts) {
      ns.append(part.charAt(0));
    }
    return ns.toString();
  }

  /**
   * The full qualified name
   * @return the fqn
   */
  public String getFullName() {
    return packageName + "." + name;
  }

  public void setComponentClassName(String componentClassName) {
    this.componentClassName = componentClassName;
  }

  public String getComponentClassName() {
    return componentClassName;
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public String getSuperClassName() {
    return superClassName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigClass that = (ConfigClass) o;

    if (cfgs != null ? !cfgs.equals(that.cfgs) : that.cfgs != null) {
      return false;
    }
    if (componentClassName != null ? !componentClassName.equals(that.componentClassName) : that.componentClassName != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) {
      return false;
    }
    if (superClassName != null ? !superClassName.equals(that.superClassName) : that.superClassName != null) {
      return false;
    }
    return type == that.type;

  }

  @Override
  public int hashCode() {
    int result = cfgs != null ? cfgs.hashCode() : 0;
    result = 31 * result + (cfgsByName != null ? cfgsByName.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
    result = 31 * result + (superClassName != null ? superClassName.hashCode() : 0);
    result = 31 * result + (componentClassName != null ? componentClassName.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }
}
