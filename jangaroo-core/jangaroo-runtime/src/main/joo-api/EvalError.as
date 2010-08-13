package {
/**
 * The EvalError class represents an error that occurs when user code calls the <code>eval()</code> function or attempts
 * to use the <code>new</code> operator with the Function object. Calling <code>eval()</code> and calling
 * <code>new</code> with the Function object are not supported.
 */
public class EvalError extends Error {

  /**
   * Creates a new EvalError object.
   * @param message A string associated with the error.
   */
  public function EvalError(message:String = "") {
    super(message);
  }
}
}