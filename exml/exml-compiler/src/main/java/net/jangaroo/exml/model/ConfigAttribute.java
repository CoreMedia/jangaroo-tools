package net.jangaroo.exml.model;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public final class ConfigAttribute extends DescriptionHolder {
  private String name;
  private String type;

  public ConfigAttribute(String name, String type, String description) {
    super(description);
    this.name = name;
    this.type = type;
  }

  public ConfigAttribute(String name, String type) {
    this(name, type, null);
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return name + " : " + type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigAttribute that = (ConfigAttribute) o;
    return name.equals(that.name) &&
            type.equals(that.type);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }
}
