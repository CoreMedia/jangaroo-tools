package {

/**
 * A ReferenceError exception is thrown when a reference to an undefined property is attempted on a sealed (nondynamic) object. References to undefined variables will result in ReferenceError exceptions to inform you of potential bugs and help you troubleshoot application code.
 * <p>However, you can refer to undefined properties of a dynamic class without causing a ReferenceError exception to be thrown. For more information, see the <code>dynamic</code> keyword.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./ReferenceError.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#dynamic dynamic keyword
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class ReferenceError extends Error {
  /**
   * Creates a new ReferenceError object.
   * @param message Contains the message associated with the ReferenceError object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
   *
   */
  public function ReferenceError(message:String = "") {
    super(message);
  }
}
}
