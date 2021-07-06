package net.jangaroo.jooc.mvnplugin.converter;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Package {
  private final String name;
  private final String version;
  private final String dependencyVersion;
  private final List<Package> dependencies;
  private final List<Package> devDependencies;

  public Package(String name, String version, String dependencyVersion, List<Package> dependencies, List<Package> devDependencies) {
    this.name = name;
    this.version = version;
    this.dependencyVersion = dependencyVersion;
    this.dependencies = dependencies;
    this.devDependencies = devDependencies;
  }

  public Package(String name, String version, String dependencyVersion) {
    this.name = name;
    this.version = version;
    this.dependencyVersion = dependencyVersion;
    this.dependencies = new ArrayList<>();
    this.devDependencies = new ArrayList<>();
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
}
