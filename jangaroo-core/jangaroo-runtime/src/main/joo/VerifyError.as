package {


/**
 * The VerifyError class represents an error that occurs when a malformed or corrupted SWF file is encountered.
 * @see flash.display.Loader
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
public dynamic class VerifyError extends Error {
  /**
   * Creates a new VerifyError object.
   * @param message Contains the message associated with the VerifyError object.
   *
   */
  public function VerifyError(message:String = "") {
    super(message);
  }
}
}