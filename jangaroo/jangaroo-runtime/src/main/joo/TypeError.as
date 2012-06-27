package {

/**
 * A TypeError exception is thrown when the actual type of an operand is different from the expected type.
 * <p>In addition, this exception is thrown when:</p>
 * <ul>
 * <li>An actual parameter to a function or method could not be coerced to the formal parameter type.</li>
 * <li>A value is assigned to a variable and cannot be coerced to the variable's type.</li>
 * <li>The right side of the <code>is</code> or <code>instanceof</code> operator is not a valid type.</li>
 * <li>The <code>super</code> keyword is used illegally.</li>
 * <li>A property lookup results in more than one binding, and is therefore ambiguous.</li>
 * <li>A method is invoked on an incompatible object. For example, a TypeError exception is thrown if a RegExp class method is "grafted" onto a generic object and then invoked.</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./TypeError.html#includeExamplesSummary">View the examples</a></p>
 * @see operators#is
 * @see operators#instanceof
 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/statements.html#super super statement
 * @see RegExp
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class TypeError extends Error {
  /**
   * Creates a new TypeError object.
   * @param message Contains the message associated with the TypeError object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
   *
   */
  public function TypeError(message:String = "") {
    super(message);
  }
}
}
