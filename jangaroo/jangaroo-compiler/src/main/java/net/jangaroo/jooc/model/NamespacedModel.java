package net.jangaroo.jooc.model;

/**
 * A model with a namespace. In ActionScript, there are some predefined namespaces (see enum
 * constants) and custom namespaces, which can be any identifier that has been
 * declared as a namespace.
 */
public interface NamespacedModel extends ActionScriptModel {

  public static final String PUBLIC = "public";
  public static final String INTERNAL = "internal";
  public static final String PROTECTED = "protected";
  public static final String PRIVATE = "private";

  String getNamespace();

  void setNamespace(String namespace);
}
