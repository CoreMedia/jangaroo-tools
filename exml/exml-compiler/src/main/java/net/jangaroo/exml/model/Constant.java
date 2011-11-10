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
}
