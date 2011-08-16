package net.jangaroo.exml.model;

import java.util.Locale;

/**
 * An enumeration of the types of ExtJS objects: components, plugins, and actions.
 */
public enum ConfigClassType {
  XTYPE,
  PTYPE,
  @SuppressWarnings({"UnusedDeclaration"})
  TYPE;

  public static ConfigClassType fromExtTypeAttribute(String parameterName) {
    String enumValue = parameterName.toUpperCase(Locale.ROOT);
    return ConfigClassType.valueOf(enumValue);
  }

  /**
   * The name of the attribute in a JSON object description that determines the object type:
   * xtype, ptype, atype.
   * @return the type
   */
  public String getExtTypeAttribute() {
    return toString().toLowerCase(Locale.ROOT);
  }
}
