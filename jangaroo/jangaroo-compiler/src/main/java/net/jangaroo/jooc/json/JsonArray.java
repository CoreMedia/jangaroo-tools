package net.jangaroo.jooc.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonArray extends JsonBase {
  private List<Object> items = new ArrayList<Object>();
  private String id;

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

  protected String valueToString(int indentFactor, int indent){
    int len = items.size();
    if (len == 0) {
      return "[]";
    }
    int i;
    StringBuilder sb = new StringBuilder();
    sb.append("[");
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