package net.jangaroo.extxml;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public class ConfigAttribute extends DescriptionHolder {

  private String name;
  private String jsType;
  private String xsType;

  public ConfigAttribute(String name, String jsType, String description) {
    super(description);
    this.name = name;
    this.jsType = jsType;
    xsType =
        jsType.equals("Boolean") ? "boolean"
            : jsType.equals("Number") ? "int"
            : jsType.equals("Float") ? "float"
            : jsType.equals("Date") ? "date"
            : "string";
  }
  
  public ConfigAttribute(String name, String jsType) {
    this(name, jsType, null);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ConfigAttribute)) return false;

    ConfigAttribute that = (ConfigAttribute) o;

    if (jsType != null ? !jsType.equals(that.jsType) : that.jsType != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (xsType != null ? !xsType.equals(that.xsType) : that.xsType != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (jsType != null ? jsType.hashCode() : 0);
    result = 31 * result + (xsType != null ? xsType.hashCode() : 0);
    return result;
  }
}
