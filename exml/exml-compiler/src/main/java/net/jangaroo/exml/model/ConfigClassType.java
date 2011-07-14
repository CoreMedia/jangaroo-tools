package net.jangaroo.exml.model;

/**
 * An enumeration of the types of ExtJS objects: components, plugins, and actions.
 */
public enum ConfigClassType {
  COMPONENT("xtype"), PLUGIN("ptype"), ACTION("atype"), LAYOUT("type");

  ConfigClassType(String extTypeAttribute) {
    this.extTypeAttribute = extTypeAttribute;
  }

  /**
   * The name of the attribute in a JSON object description that determines the object type:
   * xtype, ptype, atype.
   */
  public String extTypeAttribute;
}
