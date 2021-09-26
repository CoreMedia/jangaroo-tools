package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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
  @JsonProperty("engines")
  private Map<String, String> engines;
  @JsonProperty("dependencies")
  private Map<String, String> dependencies;
  @JsonProperty("devDependencies")
  private Map<String, String> devDependencies;
  @JsonProperty("scripts")
  private Map<String, String> scripts;
  @JsonProperty("exports")
  private Map<String, Object> exports;
  @JsonProperty("typesVersions")
  private Map<String, Object> typesVersions;
  @JsonProperty("types")
  private String types;
  @JsonProperty("coremedia")
  private Map<String, Object> coremedia;
  @JsonProperty("publishConfig")
  private Map<String, Object> publishConfig;

  public PackageJson(String name, String author, String description, String version, String license, boolean privat, Map<String, String> engines, Map<String, String> dependencies, Map<String, String> devDependencies, Map<String, String> scripts, Map<String, Object> exports, Map<String, Object> typesVersions) {
    this.name = name;
    this.author = author;
    this.description = description;
    this.version = version;
    this.license = license;
    this.privat = privat;
    this.engines = engines;
    this.dependencies = dependencies;
    this.devDependencies = devDependencies;
    this.scripts = scripts;
    this.exports = exports;
    this.typesVersions = typesVersions;
  }

  public PackageJson(AdditionalPackageJsonEntries additionalPackageJsonEntries) {
    this.author = additionalPackageJsonEntries.getAuthor();
    this.description = additionalPackageJsonEntries.getDescription();
    this.dependencies = additionalPackageJsonEntries.getDependencies();
    this.devDependencies = additionalPackageJsonEntries.getDevDependencies();
    this.scripts = additionalPackageJsonEntries.getScripts();
    this.exports = additionalPackageJsonEntries.getExports();
    this.typesVersions = additionalPackageJsonEntries.getTypesVersions();
    this.types = additionalPackageJsonEntries.getTypes();
    this.coremedia = additionalPackageJsonEntries.getCoremedia();
    this.publishConfig = additionalPackageJsonEntries.getPublishConfig();
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

  public Map<String, String> getEngines() {
    return engines;
  }

  public void setEngines(Map<String, String> engines) {
    this.engines = engines;
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

  public Map<String, Object> getPublishConfig() {
    return publishConfig;
  }

  public void addPublishConfig(String key, Object value) {
    if (this.publishConfig == null) {
      this.publishConfig = new LinkedHashMap<>();
    }
    this.publishConfig.put(key, value);
  }

  public void setPublishConfig(Map<String, Object> publishConfig) {
    if (publishConfig != null && !publishConfig.isEmpty()) {
      this.publishConfig = publishConfig;
    }
  }
}
