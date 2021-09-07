package net.jangaroo.jooc.mvnplugin.converter;

import java.util.List;

public class PnpmWorkspace {

  private List<String> packages;

  public PnpmWorkspace(List<String> packages) {
    this.packages = packages;
  }

  public List<String> getPackages() {
    return packages;
  }

  public void setPackages(List<String> packages) {
    this.packages = packages;
  }
}
