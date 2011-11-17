package net.jangaroo.exml.model;

import java.util.Locale;

/**
 * An enumeration of the types of ExtJS objects: components, plugins, and actions.
 */
public enum ConfigClassType {
  XTYPE("xtype", "xtype"),
  PTYPE("ptype", "ptype"),
  @SuppressWarnings({"UnusedDeclaration"})
  TYPE("type", "type"),
  GCTYPE("gctype", "xtype");

  public static ConfigClassType fromExtConfigAttribute(String parameterName) {
    String enumValue = parameterName.toUpperCase(Locale.ROOT);
    return ConfigClassType.valueOf(enumValue);
  }

  private String type;
  private String extTypeAttribute;

  ConfigClassType(String type, String extTypeAttribute) {
    this.type = type;
    this.extTypeAttribute = extTypeAttribute;
  }

  /**
   * The type used by exml and ext including our own types like gctype
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * The name of the attribute in a JSON object description that determines the object type:
   * xtype, ptype, type;
   * @return the type
   */
  public String getExtTypeAttribute() {
    return extTypeAttribute;
  }


}
