package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PackageJson {
  @JsonProperty("name")
  private String name;
  @JsonProperty("author")
  private String author;
  @JsonProperty("description")
  private String description;
  @JsonProperty("version")
  private String version;
  @JsonProperty("license")
  private String license;
  // needs to get annotated, because private is a keyword in java. All the other fields also need to get annotated,
  // for the order of fields to be maintained.
  @JsonProperty("private")
  private boolean privat;
  @JsonProperty("dependencies")
  private Map<String, String> dependencies;
  @JsonProperty("devDependencies")
  private Map<String, String> devDependencies;
  @JsonProperty("scripts")
  private Map<String, String> scripts;
  @JsonProperty("workspaces")
  private List<String> workspaces;
  @JsonProperty("typesVersions")
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
  }

  public PackageJson() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name != null && !name.isEmpty()) {
      this.name = name;
    }
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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    if (version != null && !version.isEmpty()) {
      this.version = version;
    }
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    if (license != null && !license.isEmpty()) {
      this.license = license;
    }
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

  public List<String> getWorkspaces() {
    return workspaces;
  }

  public void addWorkspace(String workspace) {
    if (this.workspaces == null) {
      this.workspaces = new ArrayList<>();
    }
    if (!this.workspaces.contains(workspace)) {
      this.workspaces.add(workspace);
    }
  }

  public void setWorkspaces(List<String> workspaces) {
    if (workspaces != null && !workspaces.isEmpty()) {
      this.workspaces = workspaces;
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