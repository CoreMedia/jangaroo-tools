package net.jangaroo.exml.model;

import net.jangaroo.exml.json.JsonObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExmlModel extends DescriptionHolder {
  private String packageName;
  private String className;
  private String superClassName;
  private Set<String> imports = new LinkedHashSet<String>();
  private JsonObject jsonObject = new JsonObject();
  private ConfigClass configClass;

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getFullClassName() {
    return packageName.length() > 0 ? packageName + "." + className : className;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public Set<String> getImports() {
    return imports;
  }

  public JsonObject getJsonObject() {
    return jsonObject;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setSuperClassName(String superClassName) {
    this.superClassName = superClassName;
  }

  public void addImport(String importedClassName) {
    imports.add(importedClassName);
  }

  public ConfigClass getConfigClass() {
    return configClass;
  }

  public void setConfigClass(ConfigClass configClass) {
    this.configClass = configClass;
    addImport(configClass.getFullName());
  }

  /**
   * Create a ComponentClass name from the given name. By convention all ComponentClass names are capitalized.
   *
   * @param name the name
   * @return return the new config-class name, matching the conventions.
   */
  public static String createComponentClassName(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    return new StringBuilder(name.length())
            .append(Character.toUpperCase(name.charAt(0)))
            .append(name.substring(1))
            .toString();
  }
}
