package net.jangaroo.exml.model;

import net.jangaroo.exml.api.Exmlc;

import java.util.List;

/**
 * An EXML element, declaring config attributes and a super element.
 * The implementation delegates to a config class.
 */
public class ExmlElement {

  private ConfigClass configClass;
  private String ns;
  private ExmlElement superElement;

  public ExmlElement(ConfigClass configClass) {
    this.configClass = configClass;
  }

  public String getNamespace() {
    return Exmlc.EXML_CONFIG_URI_PREFIX + getPackage();
  }

  public void setNs(String shortNamespace) {
    ns = shortNamespace;
  }

  public String getNs() {
    return ns;
  }

  public String getFullTypeName() {
    return getNs() + ":" + getTypeName();
  }

  public String getDescription() {
    return configClass.getDescription();
  }

  /**
   * Returns only the config attributes that are not already defined in the super element.
   *
   * @return the list of config attributes
   */
  public List<ConfigAttribute> getDirectCfgs() {
    return configClass.getDirectCfgs();
  }

  /**
   * Return the EXML element this element inherits config attributes from, or null
   * if this is a top-level EXML element.
   * @return the super EXML element
   */
  public ExmlElement getSuperElement() {
    if (superElement == null) {
      if (configClass.getSuperClass() == null) {
        return null;
      }
      superElement = new ExmlElement(configClass.getSuperClass());
    }
    return superElement;
  }

  @Override
  public String toString() {
    return "<" + getNamespace() + ":" + getName() + ">";
  }

  public String getPackage() {
    return configClass.getPackageName();
  }

  public String getTypeName() {
    return configClass.getFullName();
  }

  public String getName() {
    return configClass.getName();
  }

}
