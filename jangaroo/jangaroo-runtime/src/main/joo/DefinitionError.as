package {

/**
 * The DefinitionError class represents an error that occurs when user code attempts to define an identifier that is already defined. This error commonly occurs in redefining classes, interfaces, and functions.
 */
public dynamic class DefinitionError extends Error {
  /**
   * Creates a new DefinitionError object.
   * @param message A string associated with the error.
   */
  public function DefinitionError(message:String = "") {
    super(message);
  }
}
}