package net.jangaroo.exml.model;


public class Constant extends DescriptionHolder {
  private String name;
  private String value;

  public Constant(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Constant constant = (Constant) o;
    return !(name != null ? !name.equals(constant.name) : constant.name != null);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
