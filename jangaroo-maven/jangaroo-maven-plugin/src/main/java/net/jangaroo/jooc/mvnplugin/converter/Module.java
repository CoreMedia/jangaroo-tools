package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;

import java.io.File;

public abstract class Module {
  private ModuleType moduleType;
  private File directory;
  private Object data;

  public Module(String moduleDir, Model model) {
    this.directory = new File(moduleDir);
    switch (model.getPackaging()) {
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
      case "AGGREGATOR":
        moduleType = ModuleType.AGGREGATOR;
        break;
      default:
        moduleType = ModuleType.IGNORE;
    }
    this.data = model;
  }

  public Module(ModuleType moduleType, File directory, Object data) {
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

  public Object getData() {
    return data;
  }

  public abstract String getVersion();
}

