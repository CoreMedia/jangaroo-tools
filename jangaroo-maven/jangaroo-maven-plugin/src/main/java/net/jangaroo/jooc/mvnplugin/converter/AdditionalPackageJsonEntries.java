package net.jangaroo.jooc.mvnplugin.converter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class AdditionalPackageJsonEntries {
  private String author;
  private String description;
  private Map<String, String> dependencies;
  private Map<String, String> devDependencies;
  private Map<String, String> scripts;
  private Map<String, Object> exports;
  private Map<String, Object> typesVersions;
  private String types;
  private Map<String, Object> coremedia;
  private Map<String, Object> publishOverrides;

  public AdditionalPackageJsonEntries() {
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    if (author != null && !author.isEmpty()) {
      this.author = author;
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    if (description != null && !description.isEmpty()) {
      this.description = description;
    }
  }

  public Map<String, String> getDependencies() {
    return dependencies;
  }

  public void addDependency(String name, String version) {
    if (this.dependencies == null) {
      this.dependencies = new TreeMap<>();
    }
    this.dependencies.put(name, version);
  }

  public void setDependencies(Map<String, String> dependencies) {
    if (dependencies != null && !dependencies.isEmpty()) {
      this.dependencies = dependencies;
    }
  }

  public Map<String, String> getDevDependencies() {
    return devDependencies;
  }

  public void addDevDependency(String name, String version) {
    if (this.devDependencies == null) {
      this.devDependencies = new TreeMap<>();
    }
    this.devDependencies.put(name, version);
  }

  public void setDevDependencies(Map<String, String> devDependencies) {
    if (devDependencies != null && !devDependencies.isEmpty()) {
      this.devDependencies = devDependencies;
    }
  }

  public Map<String, String> getScripts() {
    return scripts;
  }

  public void addScript(String name, String command) {
    if (this.scripts == null) {
      this.scripts = new LinkedHashMap<>();
    }
    this.scripts.put(name, command);
  }

  public void setScripts(Map<String, String> scripts) {
    if (scripts != null && !scripts.isEmpty()) {
      this.scripts = scripts;
    }
  }

  public Map<String, Object> getExports() {
    return exports;
  }

  public void setExports(Map<String, Object> exports) {
    if (exports != null && !exports.isEmpty()) {
      this.exports = exports;
    }
  }

  public Map<String, Object> getTypesVersions() {
    return typesVersions;
  }

  public void addTypesVersion(String name, Object version) {
    if (this.typesVersions == null) {
      this.typesVersions = new LinkedHashMap<>();
    }
    this.typesVersions.put(name, version);
  }

  public void setTypesVersions(Map<String, Object> typesVersions) {
    if (typesVersions != null && !typesVersions.isEmpty()) {
      this.typesVersions = typesVersions;
    }
  }

  public String getTypes() {
    return types;
  }

  public void setTypes(String types) {
    if (types != null && !types.isEmpty()) {
      this.types = types;
    }
  }

  public Map<String, Object> getCoremedia() {
    return coremedia;
  }

  public void setCoremedia(Map<String, Object> coremedia) {
    this.coremedia = coremedia;
  }

  public Map<String, Object> getPublishOverrides() {
    return publishOverrides;
  }

  public void addPublishOverride(String key, Object value) {
    if (this.publishOverrides == null) {
      this.publishOverrides = new LinkedHashMap<>();
    }
    this.publishOverrides.put(key, value);
  }

  public void setPublishOverrides(Map<String, Object> publishOverrides) {
    if (publishOverrides != null && !publishOverrides.isEmpty()) {
      this.publishOverrides = publishOverrides;
    }
  }
}
