package net.jangaroo.jooc.model;

/**
 * A model of an ActionScript namespace declaration.
 */
public class NamespaceModel extends AbstractAnnotatedModel implements NamespacedModel, ValuedModel {

  private String namespace = PUBLIC;
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

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
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
