package net.jangaroo.extxml;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public class ConfigAttribute extends DescriptionHolder {
  public ConfigAttribute(String name, String jsType) {
    this.name = name;
    this.jsType = jsType;
    xsType =
          jsType.equals("Boolean") ? "boolean"
        : jsType.equals("Number")  ? "int"
        : jsType.equals("Float")   ? "float"
        : jsType.equals("Date")    ? "date"
        :                            "string";
  }

  public String getName() {
    return name;
  }

  public String getJsType() {
    return jsType;
  }

  public String getXsType() {
    return xsType;
  }

  @Override
  public String toString() {
    return name + " : " + jsType;
  }

  private String name;
  private String jsType;
  private String xsType;
}
