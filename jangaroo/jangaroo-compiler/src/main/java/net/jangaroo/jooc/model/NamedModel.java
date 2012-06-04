package net.jangaroo.jooc.model;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 15.05.12 Time: 00:12 To change this template use File | Settings |
 * File Templates.
 */
public abstract class NamedModel implements ActionScriptModel {
  private String name;

  public NamedModel() {
  }

  public NamedModel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NamedModel that = (NamedModel)o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
