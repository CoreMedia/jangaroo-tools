package net.jangaroo.exml.model;

import net.jangaroo.exml.utils.ExmlUtils;
import net.jangaroo.utils.CompilerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
  private List<Declaration> constants = new ArrayList<Declaration>();

  private String name;
  private String packageName;
  private String superClassName;
  private ConfigClass superClass;
  private String componentClassName;
  private ConfigClassType type;
  private String typeValue;
  private boolean included = false;
  private Set<String> imports = new LinkedHashSet<String>();

  public ConfigClass() {
  }

  public void addCfg(ConfigAttribute cfg) {
    cfgs.add(cfg);
    cfgsByName.put(cfg.getName(), cfg);
  }

  public boolean contains(ConfigAttribute cfg) {
    return cfgsByName.containsKey(cfg.getName());
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

  public List<Declaration> getConstants() {
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

  public void addConstant(Declaration constant) {
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

  public boolean isIncluded() {
    return included;
  }

  public void setIncluded(boolean included) {
    this.included = included;
  }

  public void addImport(String importedClassName) {
    if (importedClassName.contains(".")) { // do not import top-level classes!
      imports.add(importedClassName);
    }
  }

  public List<String> getImports() {
    Set<String> imports = new HashSet<String>();
    ExmlUtils.addImport(imports, getSuperClassName());
    for (ConfigAttribute cfg : cfgs) {
      ExmlUtils.addImport(imports, cfg.getType());
    }
    for (Declaration constant : constants) {
      ExmlUtils.addImport(imports, constant.getType());
    }
    imports.addAll(this.imports);
    ArrayList<String> results = new ArrayList<String>(imports);
    Collections.sort(results);
    return results;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConfigClass that = (ConfigClass)o;
    return name.equals(that.name) && packageName.equals(that.packageName);

  }

  @Override
  public int hashCode() {
    return 31 * name.hashCode() + packageName.hashCode();
  }
}
