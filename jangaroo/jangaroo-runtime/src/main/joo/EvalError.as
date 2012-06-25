package {


/**
 * The EvalError class represents an error that occurs when user code calls the <code>eval()</code> function or attempts to use the <code>new</code> operator with the Function object. Calling <code>eval()</code> and calling <code>new</code> with the Function object are not supported.
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class EvalError extends Error {
  /**
   * Creates a new EvalError object.
   * @param message A string associated with the error.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
   *
   */
  public function EvalError(message:String = "") {
    super(message);
  }
}
}
