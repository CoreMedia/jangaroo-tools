package net.jangaroo.extxml;

import java.util.List;

public class JooClass {
  String packageName;
  List<String> imports;
  String className;
  String extendsClass;
  String json;

  public JooClass(String packageName, List<String> imports, String className, String extendsClass, String json) {
    this.packageName = packageName;
    this.imports = imports;
    this.className = className;
    this.extendsClass = extendsClass;
    this.json = json;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public List<String> getImports() {
    return imports;
  }

  public void setImports(List<String> imports) {
    this.imports = imports;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getExtendsClass() {
    return extendsClass;
  }

  public void setExtendsClass(String extendsClass) {
    this.extendsClass = extendsClass;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }
}

