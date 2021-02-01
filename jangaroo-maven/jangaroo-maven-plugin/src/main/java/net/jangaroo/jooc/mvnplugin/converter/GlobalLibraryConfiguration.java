package net.jangaroo.jooc.mvnplugin.converter;

import java.util.List;
import java.util.Map;

public class GlobalLibraryConfiguration {
  private Map<String, String> dependencies;
  private List<String> additionalJsPaths;

  public Map<String, String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(Map<String, String> dependencies) {
    this.dependencies = dependencies;
  }

  public List<String> getAdditionalJsPaths() {
    return additionalJsPaths;
  }

  public void setAdditionalJsPaths(List<String> additionalJsPaths) {
    this.additionalJsPaths = additionalJsPaths;
  }

  public class LibraryItem {
    private String packageName;
    private String distPath;
    private String globalVar;

    public String getPackageName() {
      return packageName;
    }

    public void setPackageName(String packageName) {
      this.packageName = packageName;
    }

    public String getDistPath() {
      return distPath;
    }

    public void setDistPath(String distPath) {
      this.distPath = distPath;
    }

    public String getGlobalVar() {
      return globalVar;
    }

    public void setGlobalVar(String globalVar) {
      this.globalVar = globalVar;
    }
  }
}
