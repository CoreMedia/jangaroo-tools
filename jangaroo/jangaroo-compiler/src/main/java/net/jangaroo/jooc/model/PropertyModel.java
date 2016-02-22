package net.jangaroo.jooc.model;

import java.util.List;

/**
 * A model of a field of an ActionScript class.
 */
public class PropertyModel extends MemberModel {

  private boolean isFinal = false;
  private MethodModel getter = null;
  private MethodModel setter = null;

  public PropertyModel() {
  }

  public PropertyModel(String name, String type) {
    super(name, type);
  }

  PropertyModel(MethodModel accessor, MethodModel counterpart) {
    super(accessor.getName(), accessor.isGetter() ? accessor.getType() : accessor.getParams().get(0).getType());
    this.isFinal = accessor.isFinal();
    this.setStatic(accessor.isStatic());
    this.getter = accessor.isGetter() ? accessor : counterpart;
    this.setter = accessor.isSetter() ? accessor : counterpart;
  }

  @Override
  public void setName(String name) {
    super.setName(name);
    if (getter != null) {
      getter.setName(name);
    }
    if (setter != null) {
      setter.setName(name);
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public boolean isFinal() {
    return isFinal;
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setFinal(boolean isFinal) {
    this.isFinal = isFinal;
    if (getter != null) {
      getter.setFinal(isFinal);
    }
    if (setter != null) {
      setter.setFinal(isFinal);
    }
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
    getter.setAnnotations(getAnnotations());
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

  @Override
  public boolean isReadable() {
    return getter != null;
  }

  @Override
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
  public void setStatic(boolean isStatic) {
    super.setStatic(isStatic);
    if (getter != null) {
      getter.setStatic(isStatic);
    }
    if (setter != null) {
      setter.setStatic(isStatic);
    }
  }

  @Override
  public void addAnnotation(AnnotationModel annotation) {
    super.addAnnotation(annotation);
    if (getter != null) {
      getter.addAnnotation(annotation);
    }
  }

  @Override
  public void setAnnotations(List<AnnotationModel> annotations) {
    super.setAnnotations(annotations);
    if (getter != null) {
      getter.setAnnotations(annotations);
    }
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitProperty(this);
  }

  public PropertyModel duplicate() {
    MethodModel accessor;
    MethodModel counterpart;
    if (getter == null) {
      accessor = setter;
      counterpart = null;
    } else {
      accessor = getter;
      counterpart = setter;
    }
    return new PropertyModel(accessor.duplicate(), counterpart == null ? null : counterpart.duplicate());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PropertyModel that = (PropertyModel) o;

    return isFinal == that.isFinal
            && !(getter != null ? !getter.equals(that.getter) : that.getter != null)
            && !(setter != null ? !setter.equals(that.setter) : that.setter != null);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (isFinal ? 1 : 0);
    result = 31 * result + (getter != null ? getter.hashCode() : 0);
    result = 31 * result + (setter != null ? setter.hashCode() : 0);
    return result;
  }
}
