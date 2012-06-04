package net.jangaroo.exml.model;

import java.util.List;

/**
 * An EXML element, declaring config attributes and a super element.
 */
public interface ExmlElement {

  String getPackage();

  String getNamespace();

  void setNs(String shortNamespace);
  String getNs();

  String getName();

  String getTypeName();

  String getFullTypeName();

  String getDescription();

  /**
   * Returns only the config attributes that are not already defined in the super element.
   *
   * @return the list of config attributes
   */
  List<ConfigAttribute> getDirectCfgs();

  /**
   * Return the EXML element this element inherits config attributes from, or null
   * if this is a top-level EXML element.
   * @return the super EXML element
   */
  ExmlElement getSuperElement();
}
