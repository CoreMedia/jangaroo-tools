package net.jangaroo.jooc.mvnplugin.converter;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Package {
  private final String name;
  private final String version;
  private final String dependencyVersion;
  private final List<Package> dependencies;
  private final List<Package> devDependencies;
  private final Map<String, String> classMapping;

  public Package(String name, String version, String dependencyVersion, List<Package> dependencies, List<Package> devDependencies, Map<String, String> classMapping) {
    this.name = name;
    this.version = version;
    this.dependencyVersion = dependencyVersion;
    this.dependencies = dependencies;
    this.devDependencies = devDependencies;
    this.classMapping = classMapping;
  }

  public Package(String name, String version, String dependencyVersion) {
    this(name, version, dependencyVersion, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getDependencyVersion() {
    return dependencyVersion;
  }

  public List<Package> getDependencies() {
    return ImmutableList.copyOf(dependencies);
  }

  public List<Package> getDevDependencies() {
    return ImmutableList.copyOf(devDependencies);
  }

  public Map<String, String> getClassMapping() {
    return classMapping;
  }
}
