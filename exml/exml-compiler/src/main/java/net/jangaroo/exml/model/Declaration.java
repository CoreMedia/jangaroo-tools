package net.jangaroo.exml.model;


import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

public class Declaration extends TypedField {
  private String value;

  public Declaration(String name, String value, String type) {
    super(name, type == null || type.length() == 0 ? guessType(value) : type);
    this.value = value;
  }

  private static String guessType(String value) {
    if (value == null) {
      return "Object";
    }
    AS3Type as3Type = CompilerUtils.guessType(value);
    return as3Type == null ? AS3Type.STRING.toString() : as3Type.toString();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
