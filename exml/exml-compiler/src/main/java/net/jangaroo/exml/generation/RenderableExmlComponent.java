package net.jangaroo.exml.generation;

import net.jangaroo.exml.model.ExmlModel;

/**
 * An EXML component to be rendered using Freemarker.
 */
public class RenderableExmlComponent {
  private String packageName;
  private String formattedConfig;
  private ExmlModel model;

  public RenderableExmlComponent(String packageName, String formattedConfig, ExmlModel model) {
    this.packageName = packageName;
    this.formattedConfig = formattedConfig;
    this.model = model;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getFormattedConfig() {
    return formattedConfig;
  }

  public ExmlModel getModel() {
    return model;
  }
}
