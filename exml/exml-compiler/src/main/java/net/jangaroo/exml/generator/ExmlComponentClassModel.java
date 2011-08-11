package net.jangaroo.exml.generator;

import net.jangaroo.exml.model.ExmlModel;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An EXML component to be rendered using Freemarker.
 */
public class ExmlComponentClassModel {
  private ExmlModel model;
  private String formattedConfig;
  private String configClassPackage;

  public ExmlComponentClassModel(ExmlModel model, String configClassPackage) {
    this.model = model;
    this.formattedConfig = model.getJsonObject().toString(2, 4).trim();
    this.configClassPackage = configClassPackage;
  }

  public ExmlModel getModel() {
    return model;
  }

  public String getFormattedConfig() {
    return formattedConfig;
  }

  public String getConfigClassPackage() {
    return configClassPackage;
  }

  public Set<String> getAllImports() {
    LinkedHashSet<String> result = new LinkedHashSet<String>();
    result.add("ext.Ext");
    result.add("ext.ComponentMgr");
    result.addAll(model.getImports());
    result.add(configClassPackage + "." + model.getClassName());
    return result;
  }
}
