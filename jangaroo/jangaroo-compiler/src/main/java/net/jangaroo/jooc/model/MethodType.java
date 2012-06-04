package net.jangaroo.jooc.model;

/**
 * Special method type "getter" or "setter".
 */
public enum MethodType {
  GET,
  SET;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
