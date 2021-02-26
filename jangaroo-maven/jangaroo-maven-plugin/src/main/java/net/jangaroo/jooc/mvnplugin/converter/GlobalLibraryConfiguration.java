package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GlobalLibraryConfiguration {
  private Map<String, String> dependencies;
  private List<LibraryItem> globalLibraries;
  private List<String> additionalJsPaths;

  public GlobalLibraryConfiguration(Model mavenModel) {
    dependencies = new HashMap<>();
    globalLibraries = new ArrayList<>();
    additionalJsPaths = new ArrayList<>();
    getDownloadNpmPluginConfiguration(mavenModel).forEach(this::savePackage);
  }

  public Map<String, String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(Map<String, String> dependencies) {
    this.dependencies = dependencies;
  }

  public List<LibraryItem> getGlobalLibraries() {
    return globalLibraries;
  }

  public void setGlobalLibraries(List<LibraryItem> globalLibraries) {
    this.globalLibraries = globalLibraries;
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

    public LibraryItem(String packageName, String distPath, String globalVar) {
      this.packageName = packageName;
      this.distPath = distPath;
      this.globalVar = globalVar;
    }

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

  private List<Package> getDownloadNpmPluginConfiguration(Model mavenModel) {
    Optional<Plugin> optionalPlugin = mavenModel.getBuild().getPlugins().stream()
            .filter(plugin ->
                    "com.coremedia.internal.maven.download_npm_package_plugin".equals(plugin.getGroupId()) &&
                            "download-npm-package-plugin".equals(plugin.getArtifactId()))
            .findFirst();
    if (optionalPlugin.isPresent()) {
      Xpp3Dom configuration = (Xpp3Dom) optionalPlugin.get().getConfiguration();
      return new DownloadNpmPluginConfiguration(configuration).getPackages();
    } else {
      return new ArrayList<>();
    }
  }

  private void savePackage(Package aPackage) {
    String distPath = "";
    String globalVar = "";
    if ("rxjs".equals(aPackage.getName())) {
      globalVar = "rxjs";
      distPath = "bundles/rxjs.umd.min.js";
    } else if ("@coremedia/studio-apps-service-agent".equals(aPackage.getName())) {
      globalVar = "cmApps";
      distPath = "dist/lib-umd/js/cmApps.js";
    } else if ("moment".equals(aPackage.getName())) {
      globalVar = "moment";
      distPath = "min/moment-with-locales.min.js";
    } else {
      return;
    }
    dependencies.put(aPackage.getName(), aPackage.getVersion());
    globalLibraries.add(new LibraryItem(aPackage.getName(), distPath, globalVar));
    String lastPathSegment = distPath.substring(distPath.lastIndexOf('/') + 1);
    additionalJsPaths.add(String.format("resources/{}/{}/{}", aPackage.getName(), aPackage.getVersion(), lastPathSegment));
  }
}
