package net.jangaroo.jooc.model;

/**
 * Represents the return type and description of a method.
 */
public class ReturnModel extends DocumentedModel implements TypedModel {

  private MethodModel method;

  public ReturnModel(MethodModel method) {
    this.method = method;
  }

  public String getType() {
    return method.getType();
  }

  public void setType(String type) {
    method.setType(type);
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitReturn(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReturnModel that = (ReturnModel)o;
    String type = getType();
    return type == null ? that.getType() == null : type.equals(that.getType());
  }

  @Override
  public int hashCode() {
    return getType() == null ? 0 : getType().hashCode();
  }
}
