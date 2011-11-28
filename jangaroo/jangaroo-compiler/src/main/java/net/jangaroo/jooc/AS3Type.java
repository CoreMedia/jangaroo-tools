package net.jangaroo.jooc;

/**
 * All built-in ActionScript 3 types.
 */
public enum AS3Type {
  BOOLEAN("Boolean"),
  NUMBER("Number"),
  INT("int"),
  UINT("uint"),
  STRING("String"),
  ARRAY("Array"),
  REG_EXP("RegExp"),
  DATE("Date"),
  ANY("*"),
  VOID("void");

  public final String name;

  AS3Type(String name) {
    this.name = name;
  }

  public static AS3Type typeByName(String name) {
    for (AS3Type value : values()) {
      if (value.toString().equals(name)) {
        return value;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return name;
  }
}
