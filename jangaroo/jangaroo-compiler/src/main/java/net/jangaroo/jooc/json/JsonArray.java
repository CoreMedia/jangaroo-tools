package net.jangaroo.jooc.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonArray implements Json {
  private List<Object> items = new ArrayList<Object>();

  public JsonArray(Object ... items) {
    this.items = new ArrayList<Object>(Arrays.asList(items));
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < items.size(); ++i) {
      if (i > 0) {
        sb.append(", ");
  }
      sb.append(JsonObject.valueToString(this.items.get(i), 0, -1));
    }
    sb.append(']');
    return sb.toString();
  }

  /**
   * Make a pretty-printed JSON text of this JSONArray.
   * Warning: This method assumes that the data structure is acyclical.
   *
   * @param indentFactor The number of spaces to add to each level of
   *                     indentation.
   * @param indent       The indention of the top level.
   * @return a printable, displayable, transmittable
   *         representation of the array.
   */
  public String toString(int indentFactor, int indent){
    int len = items.size();
    if (len == 0) {
      return "[]";
    }
    int i;
    StringBuilder sb = new StringBuilder("[");
    if (len == 1) {
      sb.append(JsonObject.valueToString(this.items.get(0), indentFactor, indent));
    } else {
      int newindent = indent + indentFactor;
      sb.append(JsonObject.LINE_SEPARATOR);
      for (i = 0; i < len; i += 1) {
        if (i > 0) {
          sb.append(",").append(JsonObject.LINE_SEPARATOR);
        }
        for (int j = 0; j < newindent; j += 1) {
          sb.append(' ');
        }
        sb.append(JsonObject.valueToString(this.items.get(i),indentFactor, newindent));
      }
      sb.append(JsonObject.LINE_SEPARATOR);
      for (i = 0; i < indent; i += 1) {
        sb.append(' ');
      }
    }
    sb.append(']');
    return sb.toString();
  }

  public List<Object> getItems() {
    return new ArrayList<Object>(items);
  }

  public Object get(String property) {
    return items.get(Integer.parseInt(property));
  }

  public void set(String property, Object value) {
    items.set(Integer.parseInt(property), value);
  }
}