package net.jangaroo.jooc.json;

/**
 * Base class for JSON objects, array, and values.
 */
public abstract class JsonBase implements Json {
  private String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString(int indentFactor, int indent) {
    String value = valueToString(indentFactor, indent);
    return id != null ? id + " = " + value : value;
  }

  protected abstract String valueToString(int indentFactor, int indent);

}
