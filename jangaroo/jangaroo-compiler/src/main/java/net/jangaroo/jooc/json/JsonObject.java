package net.jangaroo.jooc.json;

import net.jangaroo.utils.CompilerUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class JsonObject implements Json {
  static final String LINE_SEPARATOR = System.getProperty("line.separator");
  public static final String NET_JANGAROO_EXT_CREATE = "net.jangaroo.ext.create";
  private Map<String, Object> properties = new LinkedHashMap<>();
  private String wrapperClass;
  private String configClass;

  public JsonObject(Object ... namesAndValues) {
    if (namesAndValues.length % 2 != 0) {
      throw new IllegalArgumentException("argument vector must be of even length, but is " + Arrays.asList(namesAndValues));
    }
    for (int i = 0; i < namesAndValues.length; i += 2) {
      Object name = namesAndValues[i];
      if (!(name instanceof String)) {
        throw new IllegalArgumentException("property names must be strings, found '" + name + "'");
      }
      Object value = namesAndValues[i + 1];
      properties.put((String)name, value);
    }
  }

  public static Code code(final String code) {
    return new CodeImpl(code);
  }

  public static void apply(JsonObject o1, JsonObject o2) {
    o1.properties.putAll(o2.properties);
  }

  public static JsonObject merge(JsonObject ... toMerge) {
    JsonObject result = new JsonObject();

    for (JsonObject o : toMerge) {
      apply(result, o);
    }

    return result;
  }

  public String getWrapperClass() {
    return wrapperClass;
  }

  public JsonObject settingWrapperClass(String wrapperClass) {
    this.wrapperClass = wrapperClass;
    return this;
  }

  public boolean isEmpty() {
    return properties.isEmpty();
  }

  @Override
  public String toString() {
    return toString(0);
  }

  /**
   * Make a pretty-printed JSON text of this JSONObject.
   * <p>
   * Warning: This method assumes that the data structure is acyclic.
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
   * Make a prettyprinted JSON text of an object value.
   * <p>
   * Warning: This method assumes that the data structure is acyclic.
   *
   *
   * @param value        The value to be serialized.
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @param indent       The indentation of the top level.
   * @return a printable, displayable, transmittable
   *         representation of the object, beginning
   *         with <code>{</code>&nbsp;<small>(left brace)</small> and ending
   *         with <code>}</code>&nbsp;<small>(right brace)</small>.
   */
  public static String valueToString(Object value, int indentFactor, int indent) {
    return valueToString(value, indentFactor, indent, false);
  }

  public static String valueToString(Object value, int indentFactor, int indent, boolean alwaysQuoteKeys) {
    if (value == null) {
      return "null";
    }
    if (value instanceof Number
        || value instanceof Boolean) {
      return value.toString();
    } else if (value instanceof JsonObject) {
      return ((JsonObject)value).toString(indentFactor, indent, alwaysQuoteKeys);
    } else if (value instanceof JsonArray) {
      return ((JsonArray) value).toString(indentFactor, indent);
    } else if (value instanceof Code) {
      return ((Code)value).getCode().replaceAll("\n", LINE_SEPARATOR);
    }
    return CompilerUtils.quote(value.toString()).replaceAll("\n", LINE_SEPARATOR);

  }


  /**
   * Make a prettyprinted JSON text of this JSONObject.
   * <p>
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
    return toString(indentFactor, indent, false);
  }

  public String stringify() {
    return stringify(2);
  }

  public String stringify(int indentFactor) {
    return toString(indentFactor, 0, true);
  }

  public String toString(int indentFactor, int indent, boolean alwaysQuoteKeys) {
    Set<String> keySet = this.properties.keySet();

    StringBuilder sb = new StringBuilder();
    if (wrapperClass != null) {
      sb.append("new ").append(wrapperClass).append('(');
    } else if(configClass != null) {
      sb.append(NET_JANGAROO_EXT_CREATE).append('(').append(configClass).append(',');
    }
    sb.append("{");
    int newindent = indent + indentFactor;
    int n = keySet.size();
    Iterator<String> keys = keySet.iterator();
    if (n == 1) {
      writeKeyValue(keys.next(), indentFactor, indent, sb, alwaysQuoteKeys);
    } else if (n > 1) {
      boolean isFirstAttribute = true;
      while (keys.hasNext()) {
        if (isFirstAttribute) {
          isFirstAttribute = false;
        } else {
          sb.append(",");
        }
        newlineAndIndent(sb, newindent);
        writeKeyValue(keys.next(), indentFactor, newindent, sb, alwaysQuoteKeys);
      }
      if (sb.length() > 1) {
        newlineAndIndent(sb, indent);
      }
    }
    sb.append('}');
    if (wrapperClass != null || configClass != null) {
      sb.append(')');
    }
    return sb.toString();
  }

  private void newlineAndIndent(StringBuilder sb, int indent) {
    if (indent < 0) {
      return;
    }
    sb.append(LINE_SEPARATOR);
    for (int i = 0; i < indent; i++) {
      sb.append(' ');
    }
  }

  private static final Pattern AS3_IDENTIFIER_PATTERN = Pattern.compile("(\\p{Alpha}|[$_])(\\p{Alnum}|[$_])*");

  public static boolean isIdentifier(String str) {
    return AS3_IDENTIFIER_PATTERN.matcher(str).matches();
  }

  private void writeKeyValue(String key, int indentFactor, int indent, StringBuilder sb, boolean alwaysQuoteKeys) {
    if (alwaysQuoteKeys || !isIdentifier(key)) {
      sb.append(CompilerUtils.quote(key));
    } else {
      sb.append(key);
    }
    sb.append(": ");
    sb.append(valueToString(this.properties.get(key), indentFactor, indent, alwaysQuoteKeys));
  }

  public Object get(String property) {
    return properties.get(property);
  }

  public void set(String property, Object value) {
    this.properties.put(property, value);
  }

  public void add(JsonObject jsonObject) {
    for (Map.Entry<String, Object> property : jsonObject.properties.entrySet()) {
      set(property.getKey(), property.getValue());
    }
  }

  public Object remove(String property) {
    return this.properties.remove(property);
  }

  public void settingConfigClass(String fullName) {
    this.configClass = fullName;
  }

  private static class CodeImpl implements Code {
    private final String code;

    public CodeImpl(String code) {
      this.code = code;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      CodeImpl that = (CodeImpl) o;

      return code.equals(that.code);

    }

    @Override
    public int hashCode() {
      return code.hashCode();
    }
  }
}