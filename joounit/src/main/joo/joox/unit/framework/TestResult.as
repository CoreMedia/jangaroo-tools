package joox.unit.framework {

import joox.unit.framework.Test;
import joox.unit.framework.TestFailure;
import joox.unit.framework.AssertionFailedError;
import joox.unit.framework.TestListener;
import joox.unit.textui.TestRunner;
//import logging.Logger;
import joox.unit.util.JooUtil;

/**
 * A TestResult collects the results of executing a test case.
 * The test framework distinguishes between <i>failures</i> and <i>errors</i>.
 * A failure is anticipated and checked for with assertions. Errors are
 * unanticipated problems like a JavaScript run-time error.
 *
 * @see joox.unit.framework.Test
 */
public class TestResult implements TestListener {

  public var mErrors : Array;
  public var mFailures : Array;
  public var mListeners : Array;
  public var mRunTests : Number;
  public var mStop : Number;

  public function TestResult() {
    this.mErrors = new Array();
    this.mFailures = new Array();
    this.mListeners = new Array();
    this.mRunTests = 0;
    this.mStop = 0;
  }

  /**
   * Add an occured error.
   * Add an occured error and call the registered listeners.
   * @param test The failed test.
   * @param except The thrown error.
   */
  public function addError(test : Test, except : Error) : void {
    this.mErrors.push(new TestFailure(test, except));
    for (var i : Number = 0; i < this.mListeners.length; ++i) {
      var testListener : TestListener = this.mListeners[i];
      testListener.addError(test, except);
    }
  }

  /**
   * Add an occured failure.
   * Add an occured failure and call the registered listeners.
   * @param test The failed test.
   * @param afe The thrown assertion failure.
   */
  public function addFailure(test : Test, afe : AssertionFailedError) : void {
    this.mFailures.push(new TestFailure(test, afe));
    for (var i : Number = 0; i < this.mListeners.length; ++i) {
      var testListener : TestListener = this.mListeners[i];
      testListener.addFailure(test, afe);
    }
  }

  /**
   * Add a listener.
   * @param listener The listener.
   */
  public function addListener(listener : TestListener) : void {
    this.mListeners.push(listener);
  }

  /**
   * Returns a copy of the listeners.
   * @return Array A copy of the listeners.
   */
  public function cloneListeners() : Array {
    var listeners : Array = new Array();
    for (var i : Number = 0; i < this.mListeners.length; ++i)
      listeners.push(this.mListeners[i]);
    return listeners;
  }

  /**
   * A test ended.
   * A test ended, inform the listeners.
   * @param test The ended test.
   */
  public function endTest(test : Test) : void {
    for (var i : Number = 0; i < this.mListeners.length; ++i) {
      var testListener : TestListener = this.mListeners[i];
      testListener.endTest(test);
    }
  }

  /**
   * Retrieve the number of occured errors.
   * @return Number
   */
  public function errorCount() : Number {
    return this.mErrors.length;
  }

  /**
   * Retrieve the number of occured failures.
   * @return Number
   */
  public function failureCount() : Number {
    return this.mFailures.length;
  }

  /**
   * Remove a listener.
   * @param listener The listener.
   */
  public function removeListener(listener : TestListener) : void {
    for (var i : Number = 0; i < this.mListeners.length; ++i) {
      if (this.mListeners[i] == listener) {
        this.mListeners.splice(i, 1);
        break;
      }
    }
  }

  /**
   * Runs a test case.
   * @param test The test case to run.
   */
  public function run(test : Test) : void {
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult: running test "+test.getName());
    this.startTest(test);

    //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult: started test "+test.getName());
    function protectable() : void {
      test.runBare();
    };
    this.runProtected(test, protectable);
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult: ran protected test "+test.getName());
    this.endTest(test);
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult: finished test "+test.getName());
  }

  /**
   * Retrieve the number of run tests.
   * @return Number the number of run tests.
   */
  public function runCount() : Number {
    return this.mRunTests;
  }

  /**
   * Runs a test case protected.
   * @param test The test case to run.
   * @param p The Function running the test.
   * To implement your own protected block that logs thrown exceptions,
   * pass a Function that stores an exception in its catch block to TestResult.runProtected().
   */
  public function runProtected(test : Test, p : Function) : void {
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult#runProtected("+test.getName()+")");
    try {
      p();
    } catch(ex : Error) {
      if (ex instanceof AssertionFailedError) {
        this.addFailure(test, ex);
      } else {
        //TestRunner.log.log(Logger.LEVEL_DEBUG,"TestResult: error during protected test, message='"+ex.message+"' ("+ex+")");
        this.addError(test, ex);
      }
    }
  }

  /**
   * Checks whether the test run should stop.
   * @return Boolean whether the test run should stop.
   */
  public function shouldStop() : Boolean {
    return this.mStop;
  }

  /**
   * A test starts.
   * A test starts, inform the listeners.
   * @param test The test to start.
   */
  public function startTest(test : Test) : void {
    ++this.mRunTests;

    for (var i : Number = 0; i < this.mListeners.length; ++i) {
      var testListener : TestListener = this.mListeners[i];
      testListener.startTest(test);
    }
  }

  /**
   * Marks that the test run should stop.
   */
  public function stop() : void {
    this.mStop = 1;
  }

  /**
   * Returns whether the entire test was successful or not.
   * @return Boolean
   */
  public function wasSuccessful() : Boolean {
    return this.mErrors.length + this.mFailures.length == 0;
  }

}
}
