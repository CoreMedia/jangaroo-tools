package net.jangaroo.properties.model;

/**
 * A POJO to store key, value, and comment of a single property read from a properties file.
 */
public class Property {

  private final String comment;
  private final String key;
  private final boolean keyIsIdentifier;
  private final String value;
  private final boolean valueIsReference;

  public Property(String comment, String key, boolean keyIsIdentifier, String value, boolean valueIsReference) {
    this.comment = comment;
    this.key = key;
    this.keyIsIdentifier = keyIsIdentifier;
    this.value = value;
    this.valueIsReference = valueIsReference;
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

  public boolean isValueIsReference() {
    return valueIsReference;
  }
}
