package net.jangaroo.extxml.json;

import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.ComponentSuiteRegistry;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObject implements Json {
  private Map<String, Object> properties = new LinkedHashMap<String, Object>();

  public boolean isEmpty() {
    return properties.isEmpty();
  }

  @Override
  public String toString() {
    return toString(0);
  }

  /**
   * Make a prettyprinted JSON text of this JSONObject.
   * <p/>
   * Warning: This method assumes that the data structure is acyclical.
   *
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @return a printable, displayable, portable, transmittable
   *         representation of the object, beginning
   *         with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *         with <code>}</code>&nbsp;<small>(right brace)</small>.
   */
  public String toString(int indentFactor) {
    return toString(indentFactor, 0);
  }


  /**
   * Produce a string in double quotes with backslash sequences in all the
   * right places. A backslash will be inserted within </, allowing JSON
   * text to be delivered in HTML. In JSON text, a string cannot contain a
   * control character or an unescaped quote or backslash.
   *
   * @param string A String
   * @return A String correctly formatted for insertion in a JSON text.
   */
  public static String quote(String string) {
    if (string == null || string.length() == 0) {
      return "\"\"";
    }

    char b;
    char c = 0;
    int i;
    int len = string.length();
    StringBuilder sb = new StringBuilder(len + 4);
    String t;

    sb.append('"');
    for (i = 0; i < len; i += 1) {
      b = c;
      c = string.charAt(i);
      switch (c) {
        case '\\':
        case '"':
          sb.append('\\');
          sb.append(c);
          break;
        case '/':
          if (b == '<') {
            sb.append('\\');
          }
          sb.append(c);
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\r':
          sb.append("\\r");
          break;
        default:
          if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
              (c >= '\u2000' && c < '\u2100')) {
            t = "000" + Integer.toHexString(c);
            sb.append("\\u").append(t.substring(t.length() - 4));
          } else {
            sb.append(c);
          }
      }
    }
    sb.append('"');
    return sb.toString();
  }

  
  /**
   * Make a prettyprinted JSON text of an object value.
   * <p/>
   * Warning: This method assumes that the data structure is acyclical.
   *
   * @param key          The key of the value.
   * @param value        The value to be serialized.
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @param indent       The indentation of the top level.
   * @return a printable, displayable, transmittable
   *         representation of the object, beginning
   *         with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *         with <code>}</code>&nbsp;<small>(right brace)</small>.
   */
  static String valueToString(String key, Object value, int indentFactor, int indent) {
    if (value == null) {
      return "null";
    }
    if (value instanceof Number
        || value instanceof Boolean) {
      return (value.toString());
    } else if (value instanceof JsonObject) {
      return ((JsonObject) value).toString(indentFactor, indent);
    } else if (value instanceof JsonArray) {
      return ((JsonArray) value).toString(indentFactor, indent);
    } else if ("xtype".equals(key) || "ptype".equals(key)) {
      String xtype = (String) value;
      ComponentClass compClazz = ComponentSuiteRegistry.getInstance().findComponentClassByXtype(xtype);
      if (compClazz != null) {
        //Log.getErrorHandler().info(String.format("Using compontent class '%s' for xtype '%s'", compClazz, value));
        return (compClazz.getFullClassName())+ (".") + (key);
      } else {
        return ("\"") + (xtype) + ("\"");
      }
    } else if (((String) value).startsWith("{") && ((String) value).endsWith("}")) {
      return (((String) value).substring(1, ((String) value).lastIndexOf("}")));
    }
    return quote(value.toString());

  }


  /**
   * Make a prettyprinted JSON text of this JSONObject.
   * <p/>
   * Warning: This method assumes that the data structure is acyclical.
   *
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @param indent       The indentation of the top level.
   * @return a printable, displayable, transmittable
   *         representation of the object, beginning
   *         with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *         with <code>}</code>&nbsp;<small>(right brace)</small>.
   */
  public String toString(int indentFactor, int indent) {
    int j;
    int n = properties.size();
    if (n == 0) {
      return "{}";
    }
    Iterator<String> keys = this.properties.keySet().iterator();
    StringBuffer sb = new StringBuffer("{");
    int newindent = indent + indentFactor;
    String key;
    if (n == 1) {
      key = keys.next();
      sb.append(key);
      sb.append(": ");
      sb.append(valueToString(key, this.properties.get(key), indentFactor,
          indent));
    } else {
      while (keys.hasNext()) {
        key = keys.next();
        if (sb.length() > 1) {
          sb.append(",\n");
        } else {
          sb.append('\n');
        }
        for (j = 0; j < newindent; j += 1) {
          sb.append(' ');
        }
        sb.append(key);
        sb.append(": ");
        sb.append(valueToString(key, this.properties.get(key), indentFactor,
            newindent));
      }
      if (sb.length() > 1) {
        sb.append('\n');
        for (j = 0; j < indent; j += 1) {
          sb.append(' ');
        }
      }
    }
    sb.append('}');
    return sb.toString();
  }


  public Object get(String property) {
    return properties.get(property);
  }

  public void set(String property, Object value) {
    this.properties.put(property, value);
  }

  public Object remove(String property) {
    return this.properties.remove(property);
  }
}