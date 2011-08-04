package net.jangaroo.exml.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public final class ConfigAttribute extends DescriptionHolder {
  private static final Set<String> SIMPLE_TYPES = new HashSet<String>(Arrays.asList(
      "Boolean",
      "Number",
      "Date",
      "String"
    ));

  private static final Collection<String> SEQUENCE_TYPES = new HashSet<String>(Arrays.asList("Array", "MixedCollection", "Mixed"));


  private String name;
  private String type;

  public ConfigAttribute(String name, String type, String description) {
    super(description);
    if(type == null) {
      throw new IllegalArgumentException("The type should not be null for config attribute: '" + name + "'");
    }
    this.name = name;
    this.type = type;

  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  /**
   * The type used by xsd generation
   * @return the xs type
   */
  public String getXsType() {
    return isSimple() ? type : "String";
  }

  public boolean isSimple() {
    return SIMPLE_TYPES.contains(type);
  }

  public boolean isSequence() {
    return SEQUENCE_TYPES.contains(type);
  }

  public boolean isObject() {
    return !SIMPLE_TYPES.contains(type) && !SEQUENCE_TYPES.contains(type);
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
