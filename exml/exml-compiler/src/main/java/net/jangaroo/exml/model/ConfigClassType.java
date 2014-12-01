package net.jangaroo.exml.model;

import net.jangaroo.exml.api.Exmlc;

/**
 * An enumeration of the types of ExtJS objects: components, plugins, and actions.
 */
public enum ConfigClassType {
  CLASS(Exmlc.EXML_CLASS_NODE_NAME, null, null, false),
  COMPONENT(Exmlc.EXML_COMPONENT_NODE_NAME, "xtype", "xtype", true),
  PLUGIN(Exmlc.EXML_PLUGIN_NODE_NAME, "ptype", "ptype", true),
  LAYOUT(Exmlc.EXML_LAYOUT_NODE_NAME, "type", "type", true),
  GRID_COLUMN(Exmlc.EXML_GRID_COLUMN_NODE_NAME, "gctype", "xtype", false);

  public static ConfigClassType fromExtConfigAttribute(String parameterName) {
    for (ConfigClassType configClassType : ConfigClassType.values()) {
      if (parameterName.equalsIgnoreCase(configClassType.getType())) {
        return configClassType;
      }
    }
    throw new IllegalArgumentException(String.format("ConfigClassType for Ext config attribute '%s' not found.", parameterName));
  }

  public static ConfigClassType fromExmlRootNodeName(String exmlRootNodeName) {
    for (ConfigClassType configClassType : ConfigClassType.values()) {
      if (exmlRootNodeName.equals(configClassType.getExmlRootNodeName())) {
        return configClassType;
      }
    }
    throw new IllegalArgumentException(String.format("ConfigClassType for EXML root node name '%s' not found.", exmlRootNodeName));
  }

  private String type;
  private String extTypeAttribute;
  private boolean createdViaConfigObject;
  private String exmlRootNodeName;

  ConfigClassType(String exmlRootNodeName, String type, String extTypeAttribute, boolean createdViaConfigObject) {
    this.type = type;
    this.extTypeAttribute = extTypeAttribute;
    this.createdViaConfigObject = createdViaConfigObject;
    this.exmlRootNodeName = exmlRootNodeName;
  }

  /**
   * The name of the EXML root node that leads to a config class of this type to be generated.
   * @return the EXML root node name
   */
  public String getExmlRootNodeName() {
    return exmlRootNodeName;
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

  public boolean isCreatedViaConfigObject() {
    return createdViaConfigObject;
  }

}
