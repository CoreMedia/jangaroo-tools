package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PnpmWorkspace {

  @JsonProperty("packages")
  private List<String> packages;

  public PnpmWorkspace(List<String> packages) {
    this.packages = packages;
  }

  // No-arg constructor to be used by Jackson's ObjectMapper
  @SuppressWarnings("unused")
  public PnpmWorkspace() {
  }

  public List<String> getPackages() {
    return packages;
  }

  public void setPackages(List<String> packages) {
    this.packages = packages;
  }
}
