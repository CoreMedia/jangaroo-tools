package {

/**
 * A RangeError exception is thrown when a numeric value is outside the acceptable range. When working with arrays, referring to an index position of an array item that does not exist will throw a RangeError exception. Using <code>Number.toExponential()</code>, <code>Number.toPrecision()</code>, and <code>Number.toFixed()</code> methods will throw a RangeError exception in cases where the arguments are outside the acceptable range of numbers. You can extend <code>Number.toExponential()</code>, <code>Number.toPrecision()</code>, and <code>Number.toFixed()</code> to avoid throwing a RangeError.
 * <p>Other situations that cause this exception to be thrown include the following:</p>
 * <ul>
 * <li>Any Flash runtime API that expects a depth number is invoked with an invalid depth number.</li>
 * <li>Any Flash runtime API that expects a frame number is invoked with an invalid frame number.</li>
 * <li>Any Flash runtime API that expects a layer number is invoked with an invalid layer number.</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/./RangeError.html#includeExamplesSummary">View the examples</a></p>
 * @see Number#toExponential()
 * @see Number#toPrecision()
 * @see Number#toFixed()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class RangeError extends Error {
  /**
   * Creates a new RangeError object.
   * @param message Contains the message associated with the RangeError object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ed2.html Error handling in ActionScript 3.0
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
   *
   */
  public function RangeError(message:String = "") {
    super(message);
  }
}
}
