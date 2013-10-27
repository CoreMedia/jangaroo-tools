package net.jangaroo.exml.model;

import net.jangaroo.exml.api.Exmlc;

import java.util.List;

/**
 * An adapter from config class to EXML element of the old format.
 */
public class ConfigClassToExmlElementAdapter implements ExmlElement {

  protected ConfigClass configClass;
  private String ns;
  private ExmlElement superElement;

  public ConfigClassToExmlElementAdapter(ConfigClass configClass) {
    this.configClass = configClass;
  }

  @Override
  public String getNamespace() {
    return Exmlc.EXML_CONFIG_URI_PREFIX + getPackage();
  }

  @Override
  public void setNs(String shortNamespace) {
    ns = shortNamespace;
  }

  @Override
  public String getNs() {
    return ns;
  }

  @Override
  public String getFullTypeName() {
    return getNs() + ":" + getTypeName();
  }

  @Override
  public String getDescription() {
    return configClass.getDescription();
  }

  @Override
  public List<ConfigAttribute> getDirectCfgs() {
    return configClass.getDirectCfgs();
  }

  @Override
  public ExmlElement getSuperElement() {
    if (superElement == null) {
      if (configClass.getSuperClass() == null) {
        return null;
      }
      superElement = createAdapter(configClass.getSuperClass());
    }
    return superElement;
  }

  @Override
  public String toString() {
    return "<" + getNamespace() + ":" + getName() + ">";
  }

  @Override
  public String getPackage() {
    return configClass.getPackageName();
  }

  @Override
  public String getTypeName() {
    return configClass.getFullName();
  }

  @Override
  public String getName() {
    return configClass.getName();
  }

  protected ExmlElement createAdapter(ConfigClass configClass) {
    return new ConfigClassToExmlElementAdapter(configClass);
  }

}
