package net.jangaroo.extxml.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements Json {
  private List<Object> items = new ArrayList<Object>();

  public String toString() {
    return items.toString();
  }

  public String toJsonString(String spaces) {
    StringBuffer bf = new StringBuffer();
    bf.append("[\n");
    bf.append(spaces);
    boolean first = true;
    for (Object item : items) {
      if (!first) {
        bf.append(",\n");
        bf.append(spaces);
      }
      if (item instanceof JsonObject) {
        bf.append(((JsonObject) item).toJsonString("  " + spaces, null));
      } else {
        bf.append(item.toString());
      }
      first = false;
    }
    bf.append("]");
    return bf.toString();
  }

  public Object get(String property) {
    return items.get(Integer.parseInt(property));
  }

  public void set(String property, Object value) {
    items.set(Integer.parseInt(property), value);
  }

  public void push(Object value) {
    this.items.add(value);
  }
}