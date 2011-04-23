package net.jangaroo.properties.model;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 23.04.11 Time: 22:24 To change this template use File | Settings |
 * File Templates.
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
