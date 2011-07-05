package net.jangaroo.exml.generator;

import net.jangaroo.exml.model.ExmlModel;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An EXML component to be rendered using Freemarker.
 */
public class ExmlComponentClassModel {
  private String formattedConfig;
  private ExmlModel model;

  public ExmlComponentClassModel(ExmlModel model) {
    this.formattedConfig = model.getJsonObject().toString(2, 4).trim();
    this.model = model;
  }

  public String getFormattedConfig() {
    return formattedConfig;
  }

  public ExmlModel getModel() {
    return model;
  }

  public Set<String> getAllImports() {
    LinkedHashSet<String> result = new LinkedHashSet<String>();
    result.add("ext.Ext");
    result.add("ext.ComponentMgr");
    result.addAll(model.getImports());
    return result;
  }
}
