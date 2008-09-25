package joox.unit.runner {

import joox.unit.framework.Test;
import joox.unit.runner.TestRunListener;
import joox.unit.framework.AssertionFailedError;
import joox.unit.textui.TestRunner;
//import logging.Logger;
import joox.unit.framework.TestSuite;
import joox.unit.util.JooUtil;
import joox.unit.framework.TestListener;

/**
 * General base class for an application running test suites.
 */
public class BaseTestRunner implements TestListener {

  public var mElapsedTime : Number = 0;

  /**
   * Implementation of TestListener.
   * @param test The test that had an error.
   * @param except The thrown error.
   */
  public function addError(test : Test, except : Error) : void {
    this.testFailed(TestRunListener.STATUS_ERROR,
      test.toString(), except.toString());
  }
  /**
   * Implementation of TestListener.
   * @param test The test that had a failure.
   * @param afe The thrown failure.
   */
  public function addFailure(test : Test, afe : AssertionFailedError) : void {
    this.testFailed(TestRunListener.STATUS_ERROR,
      test.toString(), afe.toString());
  }
  /**
   * Implementation of TestListener.
   * @param test The ended test.
   */
  public function endTest(test : Test) : void {
    this.testEnded(test.toString());
  }
  /**
   * Retrieve the value of a global preference key.
   * @param key The key of the preference.
   * @param value The default value.
   * @return Object The value of the key or the default value.
   */
  public static function getPreference(key : String, value : Object) : Object {
    return getPreferences()[key] || value;
  }

  /**
   * Retrieves the Object with the global preferences of any runner.
   * @return Object Returns the runner's global preferences.
   */
  public static function getPreferences() : Object {
    if (!mPreferences) {
      mPreferences = {
        filterStack: true,
        maxMessageLength: 500
      };
    }
    return mPreferences;
  }
  /**
   * Returns the Test corresponding to the given suite.
   * This is a template method, subclasses override runFailed(),
   * clearStatus().
   * @param name The name of the test.
   * @return Test the Test corresponding to the given suite.
   */
  public function getTest(name : String) : Test {
    if (typeof name != "string") {
      this.clearStatus();
      return null;
    }
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"creating test: "+name);
    var test : Test;
    try {
      var testFunc : Function = eval(name);
      if (typeof testFunc == "function" && testFunc.prototype) {
        //TestRunner.log.log(Logger.LEVEL_DEBUG,"found test constructor: "+testFunc);
        if (testFunc.suite && typeof testFunc.suite == "function")
          test = testFunc.suite();
        else if (name.match(/Test$/))
          test = new TestSuite(name);
      }
    } catch(ex : Error) {
      //TestRunner.log.log(Logger.LEVEL_ERROR,"could not instantiate test: "+ex.message);
      name += ", error: " + ex.message;
    }
    if (test === undefined || !(test instanceof TestSuite)) {
      this.runFailed("Test not found \"" + name + "\"");
      return null;
    } else {
      this.clearStatus();
      return test;
    }
  }
  /**
   * Set a global preference.
   * @param key The key of the preference.
   * @param value The value of the preference.
   */
  public static function setPreference(key : String, value : Object) : void {
    getPreferences()[key] = value;
  }
  /**
   * Set any runner's global preferences.
   * @param prefs The new preferences.
   */
  public static function setPreferences(prefs : Object) : void {
    mPreferences = prefs;
  }
  /**
   * Retrieve the flag for raw stack output.
   * @return Boolean Flag for an unfiltered stack output.
   */
  public static function showStackRaw() : Boolean {
    return !getPreference("filterStack", false);
  }
  /**
   * Implementation of TestListener.
   * @param test The started test.
   */
  public function startTest(test : Test) : void {
    this.testStarted(test.toString());
  }
  /**
   * Truncates string to maximum length.
   * @param str The string to trancate.
   * @return String The truncated string.
   */
  public static function truncate(str : String) : String {
    var max : Number = getPreference("maxMessageLength");
    if (max < str.length)
      str = str.substring(0, max) + "...";
    return str;
  }

  public function clearStatus() : void {
  }

  public function runFailed(msg : String) : void {
  }

  public function testEnded(test : Test) : void {
  }

  public function testFailed(test : Test) : void {
  }

  public function testStarted(test : Test) : void {
  }

  public static var mPreferences : Object;


}
}
