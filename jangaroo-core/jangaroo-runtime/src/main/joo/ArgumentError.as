package {
/**
 * The ArgumentError class represents an error that occurs when the arguments supplied in a function do not match the
 * arguments defined for that function. This error occurs, for example, when a function is called with the wrong number
 * of arguments, an argument of the incorrect type, or an invalid argument.
 */
public class ArgumentError extends Error {

  /**
   * Creates a new ArgumentError object.
   * @param message A string associated with the error.
   */
  public function ArgumentError(message:String = "") {
    super(message);
  }
}
}