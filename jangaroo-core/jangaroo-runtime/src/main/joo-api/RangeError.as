package {
/**
 * A RangeError exception is thrown when a numeric value is outside the acceptable range. When working with arrays,
 * referring to an index position of an array item that does not exist will throw a RangeError exception.
 * Using <code>Number.toExponential()</code>, <code>Number.toPrecision()</code>, and <code>Number.toFixed()</code>
 * methods will throw a RangeError exception in cases where the arguments are outside the acceptable range of numbers.
 * You can extend <code>Number.toExponential()</code>, <code>Number.toPrecision()</code>, and
 * <code>Number.toFixed()</code> to avoid throwing a RangeError.
 *
 * <p>Other situations that cause this exception to be thrown include the following:
 * <ul>
 * <li>Any Flash runtime API that expects a depth number is invoked with an invalid depth number.
 * <li>Any Flash runtime API that expects a frame number is invoked with an invalid frame number.
 * <li>Any Flash runtime API that expects a layer number is invoked with an invalid layer number.
 * </li>
 * @see Number.toExponential()
 * @see Number.toPrecision()
 * @see Number.toFixed()
 */
public class RangeError extends Error {

  /**
   * Creates a new RangeError object.
   * @param message A string associated with the error.
   */
  public function RangeError(message:String = "") {
    super(message);
  }
}
}