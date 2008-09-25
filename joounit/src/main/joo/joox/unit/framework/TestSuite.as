package joox.unit.framework {

import joox.unit.textui.TestRunner;
  //import logging.Logger;
import joox.unit.framework.Test;
import joox.unit.framework.TestCase;
import joox.unit.framework.TestResult;
import joox.unit.framework.AssertionFailedError;
import joox.unit.util.CallStack;
import joox.unit.framework.WarningTestCase;
import joox.unit.util.JooUtil;

/**
 * A TestSuite is a composition of Tests.
 * It runs a collection of test cases.
 * In despite of the JUnit implementation, this class has also functionality of
 * TestSetup of the extended JUnit framework. This is because of &quot;recursion
 * limits&quot; of the JavaScript implementation of BroadVision's One-to-one
 * Server (an OEM version of Netscape Enterprise Edition).
 * @see joox.unit.framework.Test
 */
public class TestSuite implements Test {

  public var mTests : Array;
  public var mName : String;

  /**
   * The constructor collects all test methods of the given object and adds them
   * to the array of tests.
   * @param obj if obj is an instance of a TestCase, the suite is filled
   * with the fixtures automatically. Otherwise obj's string value is treated as
   * name.
   */
  public function TestSuite(obj : Object) {
    this.mTests = new Array();

    var name : String;
    //noinspection FallthroughInSwitchStatementJS
    switch (typeof obj) {
      case "function":
        name = obj.getName();
        break;
      case "string": name = obj; break;
      case "object":
        if (obj !== null) {
          if (obj.getName && typeof( obj.getName ) == "function") {
            var tname : String = obj.getName();
            if (tname) {
              var idx : Number = tname.indexOf(".");
              if (idx == tname.lastIndexOf("."))
                obj = eval(name = tname.substring(0, idx));
            }
          }
          if (typeof( obj ) != "function") {
            this.addTest(
              this.warning("Cannot instantiate test class for "
                + "object '" + obj + "'"));
          }
        }
      // fall through
      case "undefined":
      // fall through
      default:
        if (typeof( name ) == "undefined")
          name = null;
        break;
    }

    this.setName(name);

    // collect all testXXX methods
    if (typeof( obj ) == "function" && obj.prototype) {
      for (var member : String in obj.prototype) {
        if (member.indexOf("test") == 0
          && typeof( obj.prototype[member] ) == "function") {
          //TestRunner.log.log(Logger.LEVEL_DEBUG,"found test method "+member);
          this.addTest(new obj(member));
        }
      }
    }
  }

  /**
   * Add a test to the suite.
   * @param test The test to add.
   * The test suite will add the given \a test to the suite and prepends the
   * name of a TestCase with the name of the suite.
   */
  public function addTest(test : Test) : void {
    if (test instanceof TestCase)
    {
      var name : String = test.getName();
      test.setName(this.getName() + "." + name);
    }
    this.mTests.push(test);
  }

  /**
   * Add a test suite to the current suite.
   * All fixtures of the test case will be collected in a suite which
   * will be added.
   * @param testCase The TestCase object to add.
   */
  public function addTestSuite(testCase : TestCase) : void {
    this.addTest(new TestSuite(testCase));
  }

  /**
   * Counts the number of test cases that will be run by this test suite.
   * @return Number The number of test cases.
   */
  public function countTestCases() : Number {
    var tests : Number = 0;
    for (var i : Number = 0; i < this.testCount(); ++i) {
      var test : Test = this.mTests[i];
      tests += test.countTestCases();
    }
    return tests;
  }

  /**
   * Search a test by name.
   * @note This is an enhancement to JUnit 3.8
   * The function compares the given name with the name of the test and
   * returns its own instance if the name is equal.
   * @param name The name of the searched test.
   * @return Test The instance itself or null.
   */
  public function findTest(name : String) : Test {
    if (name == this.mName)
      return this;

    for (var i : Number = 0; i < this.testCount(); ++i) {
      var test : Test = this.mTests[i];
      test = test.findTest(name);
      if (test != null)
        return test;
    }
    return null;
  }

  /**
   * Retrieves the name of the test suite.
   * @return String The name of test suite.
   */
  public function getName() : String {
    return this.mName ? this.mName : "";
  }

  /**
   * Runs the tests and collects their result in a TestResult instance.
   * @note As an enhancement to JUnit 3.8 the method calls also startTest
   * and endTest of the TestResult.
   * @param result The test result to fill.
   */
  public function run(result : TestResult) : void {
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"Running TestSuite '"+this.getName()+"'");
    --result.mRunTests;
    result.startTest(this);

    for (var i : Number = 0; i < this.testCount(); ++i) {
      if (result.shouldStop())
        break;
      var test : Test = this.mTests[i];
      this.runTest(test, result);
    }

    if (i == 0) {
      var ex : AssertionFailedError = new AssertionFailedError(
        "Test suite with no tests.", new CallStack());
      result.addFailure(this, ex);
    }

    result.endTest(this);
  }

  /**
   * Runs a single test test and collect its result in a TestResult instance.
   * @param test The test to run.
   * @param result The test result to fill.
   */
  public function runTest(test : Test, result : TestResult) : void {
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"Running Test "+test.getName());
    test.run(result);
  }

  /**
   * Sets the name of the suite.
   * @param name The name to set.
   */
  public function setName(name : String) : void {
    this.mName = name;
  }

  /**
   * Runs the test at the given index.
   * @param index The index.
   * @return Test the test at the given index.
   */
  public function testAt(index : Number) : Test {
    return this.mTests[index];
  }

  /**
   * Returns the number of tests in this suite.
   * @return Number the number of tests in this suite.
   */
  public function testCount() : Number {
    return this.mTests.length;
  }

  /**
   * Retrieve the test suite as string.
   * @return String Returns the name of the test case.
   */
  public function toString() : String {
    return "Suite '" + this.mName + "'";
  }

  /**
   * Returns a test which will fail and log a warning message.
   * @param message The warning message.
   * @return Test a test which will fail and log a warning message.
   */
  public function warning(message : String) : Test {
    return new WarningTestCase(message);
  }

}
}
