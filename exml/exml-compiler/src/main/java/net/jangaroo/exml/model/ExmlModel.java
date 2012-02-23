package net.jangaroo.exml.model;

import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.utils.ExmlUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExmlModel extends DescriptionHolder {
  private String packageName;
  private String className;
  private String superClassName;
  private Set<String> imports = new LinkedHashSet<String>();
  private List<Declaration> vars = new ArrayList<Declaration>();
  private JsonObject jsonObject = new JsonObject();
  private ConfigClass configClass;
  private boolean included = false;

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

  public List<Declaration> getVars() {
    return vars;
  }

  public void addVar(Declaration var) {
    vars.add(var);
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
    ExmlUtils.addImport(imports, importedClassName);
  }

  public ConfigClass getConfigClass() {
    return configClass;
  }

  public void setConfigClass(ConfigClass configClass) {
    this.configClass = configClass;
    addImport(configClass.getFullName());
  }

  public boolean isIncluded() {
    return included;
  }

  public void setIncludeClassMode(IncludeClassMode includeClassMode) {
    included = includeClassMode == IncludeClassMode.TRUE;
    configClass.setIncluded(includeClassMode != IncludeClassMode.FALSE);
  }
}
