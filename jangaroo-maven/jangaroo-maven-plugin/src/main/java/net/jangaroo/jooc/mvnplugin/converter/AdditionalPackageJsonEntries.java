package net.jangaroo.jooc.mvnplugin.converter;

import java.util.HashMap;
import java.util.Map;

public class AdditionalPackageJsonEntries {
  private String author;
  private String description;
  private Map<String, String> dependencies;
  private Map<String, String> devDependencies;
  private Map<String, String> scripts;
  private Map<String, Object> typesVersions;

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
      this.dependencies = new HashMap<>();
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
      this.devDependencies = new HashMap<>();
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
      this.scripts = new HashMap<>();
    }
    this.scripts.put(name, command);
  }

  public void setScripts(Map<String, String> scripts) {
    if (scripts != null && !scripts.isEmpty()) {
      this.scripts = scripts;
    }
  }

  public Map<String, Object> getTypesVersions() {
    return typesVersions;
  }

  public void addTypesVersion(String name, Object version) {
    if (this.typesVersions == null) {
      this.typesVersions = new HashMap<>();
    }
    this.typesVersions.put(name, version);
  }

  public void setTypesVersions(Map<String, Object> typesVersions) {
    if (typesVersions != null && !typesVersions.isEmpty()) {
      this.typesVersions = typesVersions;
    }
  }
}
