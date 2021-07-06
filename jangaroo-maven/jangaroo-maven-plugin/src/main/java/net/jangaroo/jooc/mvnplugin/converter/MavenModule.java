package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Model;

import java.io.File;

public class MavenModule {
  private File directory;
  private Model data;
  private Artifact artifact;

  public MavenModule(String moduleDir, Model model, Artifact artifact) {
    this.directory = new File(moduleDir);
    this.data = model;
    this.artifact = artifact;
  }

  public static ModuleType calculateModuleType(String packaging) {
    ModuleType moduleType;
    switch (packaging) {
      case "swc":
        moduleType = ModuleType.SWC;
        break;
      case "jangaroo-app":
        moduleType = ModuleType.JANGAROO_APP;
        break;
      case "jangaroo-app-overlay":
        moduleType = ModuleType.JANGAROO_APP_OVERLAY;
        break;
      case "jangaroo-apps":
        moduleType = ModuleType.JANGAROO_APPS;
        break;
      case "pom":
        moduleType = ModuleType.AGGREGATOR;
        break;
      default:
        moduleType = ModuleType.IGNORE;
    }
    return moduleType;
  }

  public File getDirectory() {
    return directory;
  }

  public Model getData() {
    return this.data;
  }

  public String getVersion() {
    return getData().getVersion();
  }

  public Artifact getArtifact() {
    return this.artifact;
  }
}
