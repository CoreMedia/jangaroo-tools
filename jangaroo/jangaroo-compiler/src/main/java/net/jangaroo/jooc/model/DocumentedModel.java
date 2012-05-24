package net.jangaroo.jooc.model;

/**
 * Base class of all jooc ActionScript model classes.
 */
public abstract class DocumentedModel extends NamedModel {
  private String asdoc;

  protected DocumentedModel() {
  }

  protected DocumentedModel(String name) {
    super(name);
  }

  public DocumentedModel(String name, String asdoc) {
    super(name);
    this.asdoc = asdoc;
  }

  public String getAsdoc() {
    return asdoc;
  }

  public void setAsdoc(String asdoc) {
    this.asdoc = asdoc;
  }

}
