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
    this.dependencies = new HashMap<>();
    this.devDependencies = new HashMap<>();
    this.scripts = new HashMap<>();
    this.typesVersions = new HashMap<>();
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, String> getDependencies() {
    return dependencies;
  }

  public void addDependency(String name, String version) {
    this.dependencies.put(name, version);
  }

  public void setDependencies(Map<String, String> dependencies) {
    this.dependencies = dependencies;
  }

  public Map<String, String> getDevDependencies() {
    return devDependencies;
  }

  public void addDevDependency(String name, String version) {
    this.devDependencies.put(name, version);
  }

  public void setDevDependencies(Map<String, String> devDependencies) {
    this.devDependencies = devDependencies;
  }

  public Map<String, String> getScripts() {
    return scripts;
  }

  public void addScript(String name, String command) {
    this.scripts.put(name, command);
  }

  public void setScripts(Map<String, String> scripts) {
    this.scripts = scripts;
  }

  public Map<String, Object> getTypesVersions() {
    return typesVersions;
  }

  public void addTypesVersion(String name, Object version) {
    this.typesVersions.put(name, version);
  }

  public void setTypesVersions(Map<String, Object> typesVersions) {
    this.typesVersions = typesVersions;
  }
}
