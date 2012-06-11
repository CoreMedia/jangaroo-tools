package net.jangaroo.jooc.model;

/**
 * A model of an ActionScript namespace declaration.
 */
public class NamespaceModel extends AbstractAnnotatedModel implements ModelWithVisibility, ValuedModel {

  private Visibility visibility = Visibility.PUBLIC;
  private String value = null;

  public NamespaceModel() {
  }

  public NamespaceModel(String name) {
    super(name);
  }

  public NamespaceModel(String name, String value) {
    super(name);
    this.value = value;
  }

  public Visibility getVisibility() {
    return visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitNamespace(this);
  }
}
