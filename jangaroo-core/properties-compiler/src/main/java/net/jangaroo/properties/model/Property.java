package net.jangaroo.properties.model;

/**
 * A POJO to store key, value, and comment of a single property read from a properties file.
 */
public class Property {

  private String comment;
  private String key;
  private String value;

  public Property(String comment, String key, String value) {
    this.comment = comment;
    this.key = key;
    this.value = value;
  }

  public String getComment() {
    return comment;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
