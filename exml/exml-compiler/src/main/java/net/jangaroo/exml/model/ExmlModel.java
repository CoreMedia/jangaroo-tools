package net.jangaroo.exml.model;

import net.jangaroo.exml.json.JsonObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExmlModel {
  private String parentClassName;
  private Set<String> imports = new LinkedHashSet<String>();
  private JsonObject jsonObject = new JsonObject();

  public String getParentClassName() {
    return parentClassName;
  }

  public Set<String> getImports() {
    return imports;
  }

  public JsonObject getJsonObject() {
    return jsonObject;
  }

  private String formatClassName(String packageName, String className) {
    return packageName + "." +className;
  }

  public void setParentClassName(String packageName, String className) {
    this.parentClassName = formatClassName(packageName, className);
    addImport(packageName, className);
  }
  
  public void addImport(String packageName, String className) {
    imports.add(formatClassName(packageName, className));
  }
}
