package joox.unit.framework {

import joox.unit.framework.Test;
import joox.unit.framework.AssertionFailedError;

/**
 * A TestFailure collects a failed test together with the caught exception.
 */
public class TestFailure {

  public var mException : Error;
  public var mTest : Test;

  /**
   * @tparam joox.unit.framework.Test test The failed test.
   * @param except The thrown error of the exception
   * @see joox.unit.framework.TestResult
   */
  public function TestFailure(test : Test, except : Error) {
    this.mException = except;
    this.mTest = test;
  }

  /**
   * Retrieve the exception message.
   * @return String Returns the exception message.
   */
  public function exceptionMessage() : String {
    var ex : Error = this.thrownException();
    return ex ? ex.toString() : "";
  }

  /**
   * Retrieve the failed test.
   * @return Test Returns the failed test.
   */
  public function failedTest() : Test {
    return this.mTest;
  }

  /**
   * Test for a JooUnit failure.
   * @return Boolean Returns true if the exception is a failure.
   */
  public function isFailure() : Boolean {
    return this.thrownException() instanceof AssertionFailedError;
  }

  /**
   * Retrieve the thrown exception.
   * @return Test Returns the thrown exception.
   */
  public function thrownException() : Error {
    return this.mException;
  }

  /**
   * Retrieve failure as string.
   * Slightly enhanced message format compared to JUnit 3.7.
   * @return String Returns the error message.
   */
  public function toString() : String {
    return "Test " + this.mTest + " failed: " + this.thrownException();
  }

  /**
   * Retrieve the stack trace.
   * @return String Returns stack trace (if available).
   */
  public function trace() : String {
    var ex : Error = this.thrownException();
    if (ex && "mCallStack" in ex)
      return ex["mCallStack"].toString();
    else
      return "";
  }

}
}
