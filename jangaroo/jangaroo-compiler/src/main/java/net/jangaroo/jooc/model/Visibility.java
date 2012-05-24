package net.jangaroo.jooc.model;

/**
 * Constants for ActionScript visibility scopes.
 */
public enum Visibility {
  PUBLIC,
  INTERNAL,
  PROTECTED,
  PRIVATE;

  public String toString() {
    return this.name().toLowerCase();
  }
}
