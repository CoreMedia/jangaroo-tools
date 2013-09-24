package net.jangaroo.exml.model;

/**
 * A typed field of the EXML class, either a config attribute or a constant.
 */
public abstract class TypedField extends DescriptionHolder {
  private String name;
  private String type;

  protected TypedField(String name, String type) {
    this.name = name;
    this.type = type;
  }

  protected TypedField(String name, String type, String description) {
    super(description);
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TypedField that = (TypedField) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
