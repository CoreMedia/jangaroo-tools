package net.jangaroo.exml.model;

import net.jangaroo.utils.AS3Type;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public final class ConfigAttribute extends TypedField {
  private static final Set<String> SIMPLE_TYPES = new HashSet<String>(Arrays.asList(
      AS3Type.BOOLEAN.toString(),
      AS3Type.NUMBER.toString(),
      AS3Type.INT.toString(),
      AS3Type.UINT.toString(),
      AS3Type.DATE.toString(),
      AS3Type.STRING.toString()
    ));

  private static final Collection<String> SEQUENCE_TYPES = new HashSet<String>(Arrays.asList(
    AS3Type.ARRAY.toString(),
    "MixedCollection",
    "Mixed"));


  public ConfigAttribute(String name, String type, String description) {
    super(name, type, description);
    if(type == null) {
      throw new IllegalArgumentException("The type should not be null for config attribute: '" + name + "'");
    }
  }

  /**
   * The type used by xsd generation
   * @return the xs type
   */
  public String getXsType() {
    return isSimple() ? getType() : "String";
  }

  public boolean isSimple() {
    return SIMPLE_TYPES.contains(getType());
  }

  public boolean isSequence() {
    return SEQUENCE_TYPES.contains(getType());
  }

  public boolean isObject() {
    return !SIMPLE_TYPES.contains(getType()) && !SEQUENCE_TYPES.contains(getType());
  }

  @Override
  public String toString() {
    return getName() + " : " + getType();
  }

}
