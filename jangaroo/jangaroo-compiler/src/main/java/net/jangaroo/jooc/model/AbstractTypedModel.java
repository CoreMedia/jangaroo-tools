package net.jangaroo.jooc.model;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 15.05.12 Time: 00:16 To change this template use File | Settings |
 * File Templates.
 */
public abstract class AbstractTypedModel extends DocumentedModel implements TypedModel {
  private String type;

  protected AbstractTypedModel() {
  }

  protected AbstractTypedModel(String name) {
    super(name);
  }

  protected AbstractTypedModel(String name, String type) {
    super(name);
    this.type = type;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }
}
