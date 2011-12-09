package net.jangaroo.exml.model;


import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.parser.ExmlToModelParser;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

public class Declaration extends TypedField {
  private String value;

  public Declaration(String name, String value, String type) {
    super(name, type == null || type.length() == 0 ? guessType(value) : type);
    this.value = value;
  }

  private static String guessType(String value) {
    AS3Type as3Type = CompilerUtils.guessType(value);
    return as3Type == null ? AS3Type.STRING.toString() : as3Type.toString();
  }

  public String getValue() {
    return JsonObject.valueToString(ExmlToModelParser.getAttributeValue(value, getType()), 4, 2);
  }

}
