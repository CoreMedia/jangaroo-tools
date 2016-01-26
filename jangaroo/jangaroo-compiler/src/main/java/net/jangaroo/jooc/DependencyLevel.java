package net.jangaroo.jooc;

enum DependencyLevel {
  /**
   * Dependency ensuring that instances of a class can be created.
   */
  DYNAMIC(""),
  /**
   * Dependency ensuring that all static methods may be called.
   */
  STATIC(".static"),
  /**
   * Dependency ensuring that the static initializers have run.
   */
  INIT(".init");

  String suffix;

  DependencyLevel(String suffix) {
    this.suffix = suffix;
  }
}
