package net.jangaroo.jooc.model;

/**
 * A model of a field of an ActionScript class.
 */
public class FieldModel extends MemberModel implements ValuedModel {

  private String value = null;
  private boolean isConst = false;

  public FieldModel() {
  }

  public FieldModel(String name) {
    super(name);
  }

  public FieldModel(String name, String type) {
    super(name, type);
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return !isConst();
  }

  public FieldModel(String name, String type, String value) {
    super(name, type);
    this.value = value;
  }

  @Override
  public boolean isField() {
    return true;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isConst() {
    return isConst;
  }

  public void setConst(boolean aConst) {
    isConst = aConst;
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitField(this);
  }
}
