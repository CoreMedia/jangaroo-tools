package joox.unit.runner {


/**
 * A listener interface for observing the execution of a test run.
 * @note This class is an &quot;initial version&quot; in JUnit 3.8.1
 * and might replace TestListener some day.
 */
public class TestRunListener {

  public function TestRunListener() {
  }

  /**
   * Status for an error.
   * @type Number
   */
  public static var STATUS_ERROR : Number = 1;

  /**
   * Status for a failure.
   * @type Number
   */
  public static var STATUS_FAILURE : Number = 2;

  /**
   * A test run was started.
   * @param suiteName The name of the test suite.
   * @param testCount The number of tests in the suite.
   */
  public function testRunStarted(suiteName : String, testCount : Number) : void {
  }

  /**
   * A test run was ended.
   * @param elapsedTime The number of elapsed milliseconds.
   */
  public function testRunEnded(elapsedTime : Number) : void {
  }

  /**
   * A test run was stopped.
   * @param elapsedTime The number of elapsed milliseconds.
   */
  public function testRunStopped(elapsedTime : Number) : void {
  }

  /**
   * A test started.
   * @param testName The name of the started test.
   */
  public function testStarted(testName : String) : void {
  }

  /**
   * A test ended.
   * @param testName The name of the ended test.
   */
  public function testEnded(testName : String) : void {
  }

  /**
   * A test failed.
   * @param status The status of the test.
   * @param testName The name of the failed test.
   * @param trace The stack trace as String.
   */
  public function testFailed(status : Number, testName : String, trace : String) : void {
  }

}
}
