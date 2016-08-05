package net.jangaroo.utils;

/**
 * All built-in ActionScript 3 types.
 */
public enum AS3Type {
  BOOLEAN("Boolean", "false"),
  NUMBER("Number", "NaN"),
  INT("int", "0"),
  UINT("uint", "0"),
  STRING("String", "null"),
  OBJECT("Object", "null"),
  ARRAY("Array", "null"),
  FUNCTION("Function", "null"),
  CLASS("Class", "null"),
  VECTOR("Vector", "null"),
  REG_EXP("RegExp", "null"),
  DATE("Date", "null"),
  ANY("*", "undefined"),
  VOID("void", "undefined");

  public final String name;
  public final String defaultValue;

  AS3Type(String name, String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  public static AS3Type typeByName(String name) {
    if (name != null) {
      // special case: to handle different Vector types, the internal name is different:
      if ("Vector$object".equals(name)) {
        return VECTOR;
      }
      for (AS3Type value : values()) {
        if (value.toString().equals(name)) {
          return value;
        }
      }
    }
    return null;
  }

  public static String getDefaultValue(String typeName) {
    AS3Type type = typeName != null ? AS3Type.typeByName(typeName) : AS3Type.ANY;
    return type == null ? "null" : type.defaultValue;
  }

  @Override
  public String toString() {
    return name;
  }
}
