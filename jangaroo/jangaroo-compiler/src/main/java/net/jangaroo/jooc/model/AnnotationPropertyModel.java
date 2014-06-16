package net.jangaroo.jooc.model;

/**
 * A model of an ActionScript annotation property.
 */
public class AnnotationPropertyModel extends NamedModel implements ValuedModel {

  private String value = null;

  public AnnotationPropertyModel() {
  }

  public AnnotationPropertyModel(String name, String value) {
    super(name);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void visit(ModelVisitor visitor) {
    visitor.visitAnnotationProperty(this);
  }

  @Override
  public String toString() {
    return getName() + ": " + getValue() + " (" + super.toString() + ")";
  }
}
