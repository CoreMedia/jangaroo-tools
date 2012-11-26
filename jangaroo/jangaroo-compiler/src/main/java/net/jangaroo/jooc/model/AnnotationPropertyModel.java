package net.jangaroo.jooc.model;

import net.jangaroo.utils.CompilerUtils;

/**
 * A model of a field of an ActionScript class.
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

  public String getStringValue() {
    return CompilerUtils.unquote(value);
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void visit(ModelVisitor visitor) {
    visitor.visitAnnotationProperty(this);
  }
}
