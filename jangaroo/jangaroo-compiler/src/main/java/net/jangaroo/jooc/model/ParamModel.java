package net.jangaroo.jooc.model;

import net.jangaroo.utils.AS3Type;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 14.05.12 Time: 16:49 To change this template use File | Settings |
 * File Templates.
 */
public class ParamModel extends AbstractTypedModel implements ValuedModel {
  private String value;
  private boolean rest;

  public ParamModel() {
  }

  public ParamModel(String name, String type) {
    super(name, type);
  }

  public ParamModel(String name, String type, String defaultValue) {
    this(name, type);
    this.value = defaultValue;
  }

  public ParamModel(String name, String type, String defaultValue, boolean rest) {
    this(name, type, defaultValue);
    this.rest = rest;
  }

  public ParamModel(String name, String type, String defaultValue, String asdoc) {
    this(name, type, defaultValue);
    setAsdoc(asdoc);
  }

  public ParamModel(String name, String type, String defaultValue, boolean rest, String asdoc) {
    this(name, type, defaultValue, rest);
    setAsdoc(asdoc);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }

  public boolean isRest() {
    return rest;
  }

  public void setRest(boolean rest) {
    this.rest = rest;
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitParam(this);
  }

  public boolean isOptional() {
    return getValue() != null && getValue().length() > 0;
  }

  public void setOptional(boolean optional) {
    if (optional != isOptional()) {
      setValue(optional ? AS3Type.getDefaultValue(getType()) : null);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    // ignore parameter name, so don't call super!
    ParamModel that = (ParamModel)o;
    return rest == that.rest &&
      !(getType() != null ? !getType().equals(that.getType()) : that.getType() != null) &&
      !(value != null ? !value.equals(that.value) : that.value != null);
  }

  @Override
  public int hashCode() {
    // ignore parameter name, so don't call super!
    int result = getType() != null ? getType().hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (rest ? 1 : 0);
    return result;
  }

  public ParamModel duplicate() {
    return new ParamModel(getName(), getType(), value, rest, getAsdoc());
  }
}
