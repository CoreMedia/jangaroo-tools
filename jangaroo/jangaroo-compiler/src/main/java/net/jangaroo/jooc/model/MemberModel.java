package net.jangaroo.jooc.model;

/**
 * An abstract base class for members of an ActionScript class or interface.
 */
public abstract class MemberModel extends AbstractAnnotatedModel implements NamespacedModel, TypedModel {

  private boolean isStatic = false;
  private String type = null;
  private String namespace = PUBLIC;

  protected MemberModel() {
  }

  protected MemberModel(String name) {
    super(name);
  }

  protected MemberModel(String name, String type) {
    super(name);
    this.type = type;
  }

  public boolean isStatic() {
    return isStatic;
  }

  public void setStatic(boolean aStatic) {
    isStatic = aStatic;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public void setType(String type) {
    this.type = type;
  }

  public boolean isMethod() {
    return false;
  }

  public boolean isAccessor() {
   return false;
  }

  public boolean isGetter() {
    return false;
  }

  public boolean isSetter() {
    return false;
  }

  public boolean isField() {
    return false;
  }

  public boolean isProperty() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    MemberModel that = (MemberModel)o;
    return isStatic == that.isStatic &&
      !(type != null ? !type.equals(that.type) : that.type != null) &&
      namespace.equals(that.namespace);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (isStatic ? 1 : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + namespace.hashCode();
    return result;
  }
}
