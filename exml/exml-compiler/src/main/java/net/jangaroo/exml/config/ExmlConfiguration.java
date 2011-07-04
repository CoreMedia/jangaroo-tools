package net.jangaroo.exml.config;

import net.jangaroo.jooc.config.FileLocations;

public class ExmlConfiguration extends FileLocations {
  private String configClassPackage;

  public String getConfigClassPackage() {
    return configClassPackage;
  }

  public void setConfigClassPackage(String configClassPackage) {
    this.configClassPackage = configClassPackage;
  }
}
