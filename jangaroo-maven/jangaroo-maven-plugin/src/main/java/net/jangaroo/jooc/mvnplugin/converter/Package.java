package net.jangaroo.jooc.mvnplugin.converter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Package {
  private String name;
  private String version;
  private List<Package> dependencies;

  public Package(String name, String version, List<Package> dependencies) {
    this.name = name;
    this.version = version;
    this.dependencies = dependencies;
  }

  public Package(String name, String version) {
    this.name = name;
    this.version = version;
    this.dependencies = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public List<Package> getDependencies() {
    return dependencies;
  }

  public void addAllDependencies(List<Package> newDependencies) {
    this.dependencies.addAll(newDependencies);
  }

  public void addDependency(Package newDependency) {
    dependencies.add(newDependency);
  }

  public Optional<Package> findDependency(@Nonnull String name, @Nonnull String version) {
    return dependencies.stream()
            .filter(aPackage -> name.equals(aPackage.getName()))
            .filter(aPackage -> version.equals(aPackage.getVersion()))
            .findFirst();
  }

  public void removeDependency(@Nonnull String name,@Nonnull String version) {
    List<Package> matchingDependencies = dependencies.stream()
            .filter(aPackage -> name.equals(aPackage.getName()))
            .filter(aPackage -> version.equals(aPackage.getVersion()))
            .collect(Collectors.toList());
    matchingDependencies.forEach(dependencies::remove);
  }
}
