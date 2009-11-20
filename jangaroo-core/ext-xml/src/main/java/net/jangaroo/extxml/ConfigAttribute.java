package net.jangaroo.extxml;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A meta model of an Ext JS component configuration attribute.
 */
public final class ConfigAttribute extends DescriptionHolder {

  private static final Map<String, String> XS_TYPE_BY_JS_TYPE = new HashMap<String, String>(5);

  static {
    XS_TYPE_BY_JS_TYPE.put("Boolean", "boolean");
    XS_TYPE_BY_JS_TYPE.put("Number", "int");
    XS_TYPE_BY_JS_TYPE.put("Float", "float");
    XS_TYPE_BY_JS_TYPE.put("Date", "date");
    XS_TYPE_BY_JS_TYPE.put("String", "string");
  }

  private static final Collection<String> SEQUENCE_JS_TYPES = new HashSet<String>(Arrays.asList("Array", "MixedCollection", "Mixed"));

  private String name;
  private Collection<String> jsTypes;
  private String xsType;

  public ConfigAttribute(String name, String jsType, String description) {
    super(description);
    this.name = name;
    this.jsTypes = new HashSet<String>(Arrays.asList(jsType.split("[^a-zA-Z0-9$_.]")));
    if (jsTypes.size() == 1) {
      xsType = XS_TYPE_BY_JS_TYPE.get(jsTypes.iterator().next());
    }
    if (xsType == null) {
      xsType = "string";
    }
  }

  public ConfigAttribute(String name, String jsType) {
    this(name, jsType, null);
  }

  public String getName() {
    return name;
  }

  public String getJsType() {
    return jsTypes.iterator().next();
  }

  public String getXsType() {
    return xsType;
  }

  public boolean isSimple() {
    for (String jsType : jsTypes) {
      if (!XS_TYPE_BY_JS_TYPE.containsKey(jsType)) {
        return false;
      }
    }
    return true;
  }

  public boolean isSequence() {
    for (String jsType : jsTypes) {
      if (SEQUENCE_JS_TYPES.contains(jsType)) {
        return true;
      }
    }
    return false;
  }

  public boolean isObject() {
    for (String jsType : jsTypes) {
      if (!XS_TYPE_BY_JS_TYPE.containsKey(jsType) && !SEQUENCE_JS_TYPES.contains(jsType)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return name + " : " + jsTypes;
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

    return !(name != null ? !name.equals(that.name) : that.name != null);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
