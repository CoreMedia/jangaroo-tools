package net.jangaroo.exml.model;

import java.util.Locale;

/**
 * An enumeration of the types of ExtJS objects: components, plugins, and actions.
 */
public enum ConfigClassType {
  XTYPE("xtype"),
  PTYPE("ptype"),
  @SuppressWarnings({"UnusedDeclaration"})
  TYPE("type"),
  @SuppressWarnings({"UnusedDeclaration"})
  GCTYPE("xtype");

  public static ConfigClassType fromExtConfigAttribute(String parameterName) {
    String enumValue = parameterName.toUpperCase(Locale.ROOT);
    return ConfigClassType.valueOf(enumValue);
  }

  private String extTypeAttribute;

  ConfigClassType(String extTypeAttribute) {
    this.extTypeAttribute = extTypeAttribute;
  }

  /**
   * The name of the attribute in a JSON object description that determines the object type:
   * xtype, ptype, type.
   * @return the type
   */
  public String getExtTypeAttribute() {
    return extTypeAttribute;
  }
}
