package net.jangaroo.extxml.json;

import net.jangaroo.extxml.ComponentClass;
import net.jangaroo.extxml.ComponentSuiteRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObject implements Json {
  private Map<String, Object> properties = new LinkedHashMap<String, Object>();

  public boolean isEmpty() {
    return properties.isEmpty();
  }

  @Override
  public String toString() {
    return toJsonString("", null);
  }

  public String toJsonString(String spaces, String ignoreProperty) {
    StringBuffer bf = new StringBuffer();
    bf.append(spaces);
    bf.append("{");
    boolean first = true;
    for (Map.Entry<String, Object> prop : this.properties.entrySet()) {
      String key = prop.getKey();
      Object value = prop.getValue();
      if (ignoreProperty == null || !key.equals(ignoreProperty)) {
        if (!first) {
          bf.append(",\n");
          bf.append("  ");
          bf.append(spaces);
        }
        bf.append(key);
        bf.append(": ");
        if (value instanceof Number
            || value instanceof Boolean) {
          bf.append(value.toString());
        } else if (value instanceof JsonObject) {
          bf.append(((JsonObject) value).toJsonString("  " + spaces, null));
        } else if (value instanceof JsonArray) {
          bf.append(((JsonArray) value).toJsonString("  " + spaces));
        } else if (key.equals("xtype") || key.equals("ptype")) {
          String xtype = (String) value;
          ComponentClass compClazz = ComponentSuiteRegistry.getInstance().findComponentClassByXtype(xtype);
          if (compClazz != null) {
            bf.append(compClazz.getFullClassName()).append(".").append(key);
          } else {
            bf.append("\"").append(xtype).append("\"");
          }
        } else if (((String) value).startsWith("{") && ((String) value).endsWith("}")) {
          bf.append(((String) value).substring(1, ((String) value).lastIndexOf("}")));
        } else {
          bf.append("\"").append(value.toString()).append("\"");
        }
        first = false;
      }
    }
    bf.append("}");
    return bf.toString();
  }


  public Object get(String property) {
    return properties.get(property);
  }

  public void set(String property, Object value) {
    this.properties.put(property, value);

  }
}