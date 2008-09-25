package joox.unit.framework {

import joox.unit.util.CallStack;
import joox.unit.util.JooError;

/**
 * Thrown when a test assertion fails.
 */
public class AssertionFailedError extends JooError {

  /**
   * The call stack for the message.
   */
  public var mCallStack : CallStack;

  /**
   * An AssertionFailedMessage needs a message and a call stack for construction.
   * @param msg Failure message.
   * @param stack The call stack of the assertion.
   */
  public function AssertionFailedError(msg : String, stack : CallStack) {
    super("AssertionFailedError", msg);
    this.mCallStack = stack;
  }

}
}
