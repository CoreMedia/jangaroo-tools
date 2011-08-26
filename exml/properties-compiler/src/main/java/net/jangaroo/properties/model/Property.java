package net.jangaroo.properties.model;

import java.util.regex.Pattern;

/**
 * A POJO to store key, value, and comment of a single property read from a properties file.
 */
public class Property {

  private String comment;
  private String key;
  private boolean keyIsIdentifier;
  private String value;

  public Property(String comment, String key, boolean keyIsIdentifier, String value) {
    this.comment = comment;
    this.key = key;
    this.keyIsIdentifier = keyIsIdentifier;
    this.value = value;
  }

  public String getComment() {
    return comment;
  }

  public String getKey() {
    return key;
  }

  public boolean getKeyIsIdentifier() {
    return keyIsIdentifier;
  }

  public String getValue() {
    return value;
  }
}
