package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;

import java.io.File;

public class ExtModule extends Module {

  public ExtModule(String moduleDir, Model model) {
    super(moduleDir, model);
  }

  public ExtModule(ModuleType moduleType, File directory, PackageJsonData data) {
    super(moduleType, directory, data);
  }

  @Override
  public PackageJsonData getData() {
    return (PackageJsonData) super.getData();
  }

  @Override
  public String getVersion() {
    return getData().getVersion();
  }
}
