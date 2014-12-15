package {

/**
 * The ArgumentError class represents an error that occurs when the arguments supplied in a function do not match the arguments defined for that function. This error occurs, for example, when a function is called with the wrong number of arguments, an argument of the incorrect type, or an invalid argument.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./ArgumentError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
public dynamic class ArgumentError extends Error {
  /**
   * Creates an ArgumentError object.
   * @param message A string associated with the error.
   *
   */
  public function ArgumentError(message:String = "") {
    super(message);
  }
}
}
