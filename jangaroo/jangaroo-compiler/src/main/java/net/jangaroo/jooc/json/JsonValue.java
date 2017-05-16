package net.jangaroo.jooc.json;

import net.jangaroo.utils.CompilerUtils;

/**
 * A simple wrapper for literals or code to a JSON object.
 */
public class JsonValue extends JsonBase {
  private Object value;

  @Override
  public Object get(String property) {
    throw new RuntimeException("Json Value has no properties.");
  }

  @Override
  public void set(String property, Object value) {
    throw new RuntimeException("Json Value has no properties.");
  }

  public JsonValue(Object value) {
    this.value = value;
  }

  @Override
  protected String valueToString(int indentFactor, int indent) {
    return toString();
  }

  @Override
  public String toString() {
    return value == null ? "null" : value instanceof String ? CompilerUtils.quote((String) value) : value.toString();
  }
}
