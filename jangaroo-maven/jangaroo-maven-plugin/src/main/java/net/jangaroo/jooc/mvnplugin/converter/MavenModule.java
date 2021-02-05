package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;

import java.io.File;

public class MavenModule extends Module {
  public MavenModule(String moduleDir, Model model) {
    super(moduleDir, model);
  }

  public MavenModule(ModuleType moduleType, File directory, Model data) {
    super(moduleType, directory, data);
  }

  @Override
  public Model getData() {
    return (Model) super.getData();
  }

  @Override
  public String getVersion() {
    return getData().getVersion();
  }
}
