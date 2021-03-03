package net.jangaroo.jooc.mvnplugin.converter;

import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadNpmPluginConfiguration {
  private List<Package> packages;

  public DownloadNpmPluginConfiguration(Xpp3Dom configuration) {
    Xpp3Dom configPackages = configuration.getChild("packages");
    if (configPackages != null) {
      this.packages = Arrays.stream(configPackages.getChildren())
              .map(somePackage -> new Package(getConfigString(somePackage, "name"), getConfigString(somePackage, "version")))
              .collect(Collectors.toList());
    } else {
      this.packages = new ArrayList<>();
    }
    //todo: is this correct? hjow to handle dependencies
  }

  private String getConfigString(Xpp3Dom config, String property) {
    Xpp3Dom child = config.getChild(property);
    if (child != null) {
      return child.getValue();
    } else {
      return "";
    }
  }


  public DownloadNpmPluginConfiguration(List<Package> packages) {
    this.packages = packages;
  }

  public List<Package> getPackages() {
    return packages;
  }

  public void setPackages(List<Package> packages) {
    this.packages = packages;
  }
}
