package {
/**
 * A SyntaxError exception is thrown when a parsing error occurs, for one of the following reasons:.
 * <ul>
 * <li>An invalid regular expression is parsed by the RegExp class.</li>
 * <li>Invalid XML content is parsed by the XML class.</li>
 * </ul>
 * @see RegExp
 * @see XML
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 */
public class SyntaxError extends Error {

  /**
   * Creates a new SyntaxError object.
   * @param message A string associated with the error.
   */
  public function SyntaxError(message:String = "") {
    super(message);
  }
}
}