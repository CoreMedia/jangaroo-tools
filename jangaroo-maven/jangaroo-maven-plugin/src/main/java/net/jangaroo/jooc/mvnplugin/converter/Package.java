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
  private List<Package> devDependencies;

  public Package(String name, String version, List<Package> dependencies, List<Package> devDependencies) {
    this.name = name;
    this.version = version;
    this.dependencies = dependencies;
    this.devDependencies = devDependencies;
  }

  public Package(String name, String version) {
    this.name = name;
    this.version = version;
    this.dependencies = new ArrayList<>();
    this.devDependencies = new ArrayList<>();
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

  public List<Package> getDevDependencies() {
    return devDependencies;
  }

  public void addAllDependencies(List<Package> newDependencies) {
    this.dependencies.addAll(newDependencies);
  }

  public void addDependency(Package newDependency) {
    dependencies.add(newDependency);
  }

  public void setDevDependencies(List<Package> devDependencies) {
    this.devDependencies = devDependencies;
  }

  public void addDevDependencie(Package devDependency) {
    this.dependencies.add(devDependency);
  }

  public Optional<Package> findDependency(String name, String version) {
    return dependencies.stream()
            .filter(aPackage -> name.equals(aPackage.getName()))
            .filter(aPackage -> version.equals(aPackage.getVersion()))
            .findFirst();
  }

  public Optional<Package> findDevDependency(String name, String version) {
    return devDependencies.stream()
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

  public void removeDevDependency(@Nonnull String name, @Nonnull String version) {
    List<Package> matchingDependencies = devDependencies.stream()
            .filter(aPackage -> name.equals(aPackage.getName()))
            .filter(aPackage -> version.equals(aPackage.getVersion()))
            .collect(Collectors.toList());
    matchingDependencies.forEach(dependencies::remove);
  }

  public boolean matches(String name, String version) {
    return this.name.equals(name) && this.version.equals(version);
  }
}
