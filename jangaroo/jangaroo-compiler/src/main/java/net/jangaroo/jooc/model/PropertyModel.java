package net.jangaroo.jooc.model;

/**
 * A model of a field of an ActionScript class.
 */
public class PropertyModel extends MemberModel {

  private MethodModel getter = null;
  private MethodModel setter = null;

  public PropertyModel() {
  }

  public PropertyModel(String name, String type) {
    super(name, type);
  }

  @Override
  public boolean isProperty() {
    return true;
  }

  public MethodModel getGetter() {
    return getter;
  }

  public MethodModel addGetter() {
    getter = new MethodModel(MethodType.GET, getName(), getType());
    getter.setStatic(isStatic());
    getter.setAsdoc(getAsdoc());
    return getter;
  }

  public MethodModel getSetter() {
    return setter;
  }

  public MethodModel addSetter() {
    setter = new MethodModel(MethodType.SET, getName(), getType());
    setter.setStatic(isStatic());
    return setter;
  }

  public MethodModel getMethod(MethodType methodType) {
    return methodType == MethodType.GET ? getter : setter;
  }

  public boolean isReadable() {
    return getter != null;
  }

  public boolean isWritable() {
    return setter != null;
  }

  @Override
  public void setType(String type) {
    super.setType(type);
    if (getter != null) {
      getter.setType(type);
    }
    if (setter != null) {
      setter.getParams().get(0).setType(type);
    }
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitProperty(this);
  }
}
