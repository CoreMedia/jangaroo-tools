package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;

import java.io.File;

public class MavenModule {
  private ModuleType moduleType;
  private File directory;
  private Model data;

  public MavenModule(String moduleDir, Model model) {
    this.directory = new File(moduleDir);
    this.moduleType = calculateModuleType(model.getPackaging());
    this.data = model;
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

  public MavenModule(ModuleType moduleType, File directory, Model data) {
    this.moduleType = moduleType;
    this.directory = directory;
    this.data = data;
  }

  public ModuleType getModuleType() {
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
}
