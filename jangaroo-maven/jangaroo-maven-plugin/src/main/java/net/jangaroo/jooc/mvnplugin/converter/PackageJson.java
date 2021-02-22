package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageJson {
  private String name;
  private String author;
  private String description;
  private String version;
  private String license;
  @JsonProperty("private")
  private boolean privat;
  private Map<String, String> dependencies;
  private Map<String, String> devDependencies;
  private Map<String, String> scripts;
  private List<String> workspaces;
  private Map<String, Object> typesVersions;

  public PackageJson(String name, String author, String description, String version, String license, boolean privat, Map<String, String> dependencies, Map<String, String> devDependencies, Map<String, String> scripts, List<String> workspaces, Map<String, Object> typesVersions) {
    this.name = name;
    this.author = author;
    this.description = description;
    this.version = version;
    this.license = license;
    this.privat = privat;
    this.dependencies = dependencies;
    this.devDependencies = devDependencies;
    this.scripts = scripts;
    this.workspaces = workspaces;
    this.typesVersions = typesVersions;
  }

  public PackageJson(AdditionalPackageJsonEntries additionalPackageJsonEntries) {
    this.author = additionalPackageJsonEntries.getAuthor();
    this.dependencies = additionalPackageJsonEntries.getDependencies();
    this.devDependencies = additionalPackageJsonEntries.getDevDependencies();
    this.scripts = additionalPackageJsonEntries.getScripts();
    this.typesVersions = additionalPackageJsonEntries.getTypesVersions();
    this.workspaces = new ArrayList<>();
  }

  public PackageJson() {
    this.dependencies = new HashMap<>();
    this.devDependencies = new HashMap<>();
    this.scripts = new HashMap<>();
    this.workspaces = new ArrayList<>();
    this.typesVersions = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public boolean isPrivat() {
    return privat;
  }

  public void setPrivat(boolean privat) {
    this.privat = privat;
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

  public List<String> getWorkspaces() {
    return workspaces;
  }

  public void addWorkspace(String workspace) {
    this.workspaces.add(workspace);
  }

  public void setWorkspaces(List<String> workspaces) {
    this.workspaces = workspaces;
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
