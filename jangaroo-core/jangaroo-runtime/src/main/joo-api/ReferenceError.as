package {
/**
 * A ReferenceError exception is thrown when a reference to an undefined property is attempted on a sealed (nondynamic)
 * object. References to undefined variables will result in ReferenceError exceptions to inform you of potential bugs
 * and help you troubleshoot application code.
 * <p>However, you can refer to undefined properties of a dynamic class without causing a ReferenceError exception to
 * be thrown. For more information, see the dynamic keyword.
 */
public class ReferenceError extends Error {

  /**
   * Creates a new ReferenceError object.
   * @param message A string associated with the error.
   */
  public function ReferenceError(message:String = "") {
    super(message);
  }
}
}