package joox.unit.framework {

import joox.unit.framework.Assert;
import joox.unit.framework.TestResult;
import joox.unit.framework.Test;
import joox.unit.textui.TestRunner;
//import logging.Logger;
import joox.unit.util.JooUtil;

/**
 * A test case defines the fixture to run multiple tests.
 * To define a test case
 * -# implement a subclass of joox.unit.framework.TestCase
 * -# define instance variables that store the state of the fixture
 * -# initialize the fixture state by overriding <code>setUp</code>
 * -# clean-up after a test by overriding <code>tearDown</code>.
 * Each test runs in its own fixture so there can be no side effects among
 * test runs.
 *
 * For each test implement a method which interacts
 * with the fixture. Verify the expected results with assertions specified
 * by calling <code>assertTrue</code> with a boolean or one of the other assert
 * functions.
 *
 * Once the methods are defined you can run them. The framework supports
 * both a static and more generic way to run a test.
 * In the static way you override the runTest method and define the method to
 * be invoked.
 * The generic way uses the JavaScript functionality to enumerate a function's
 * methods to implement <code>runTest</code>. In this case the name of the case
 * has to correspond to the test method to be run.
 *
 * The tests to be run can be collected into a TestSuite. JooUnit provides
 * several <i>test runners</i> which can run a test suite and collect the
 * results.
 * A test runner expects a function <code><i>FileName</i>Suite</code> as the
 * entry point to get a test to run.
 *
 * @see joox.unit.framework.TestResult
 * @see joox.unit.framework.TestSuite
 */
public class TestCase implements Test {

  public var mName : String;

  /**
   * Constructs a test case with the given name.
   * @param name The name of the test case.
   */
  function TestCase(name : String) {
    super();
    this.mName = name;
  }


  /**
   * Counts the number of test cases that will be run by this test.
   * @return Number Returns 1.
   */
  public function countTestCases() : Number {
    return 1;
  }

  /**
   * Creates a default TestResult object.
   * @return TestResult Returns the new object.
   */
  public function createResult() : TestResult {
    return new TestResult();
  }

  /**
   * Find a test by name.
   * @note This is an enhancement to JUnit 3.8
   * @param testName The name of the searched test.
   * @return Test Returns this if the test's name matches or null.
   */
  public function findTest(testName : String) : Test {
    return testName == this.mName ? this : null;
  }

  /**
   * Retrieves the name of the test.
   * @return String The name of test cases.
   */
  public function getName() : String {
    return this.mName;
  }

  /**
   * Runs a test and collects its result in a TestResult instance.
   * The function can be called with or without argument. If no argument is
   * given, the function will create a default result set and return it.
   * Otherwise the return value can be omitted.
   * @param result The test result to fill.
   */
  public function run(result : TestResult) : void {
    //TestRunner.log.log(Logger.LEVEL_DEBUG,"Running Test "+this.getName());
    if (!result) {
      //TestRunner.log.log(Logger.LEVEL_DEBUG,"Creating result...");
      result = this.createResult();
      //TestRunner.log.log(Logger.LEVEL_DEBUG,"Created result.");
    }
    result.run(this);
  }

  /**
   * \internal
   */
  public function runBare() : void {
    this.setUp();
    try {
      this.runTest();
      this.tearDown();
    } catch(ex : Error) {
      this.tearDown();
      throw ex;
    }
  }

  /**
   * Override to run the test and assert its state.
   */
  public function runTest() : void {
    var methodName : String = this.getName();
    Assert.assertNotNull(methodName);
    methodName = methodName.substring(methodName.lastIndexOf(".") + 1);
    var method : Function = this[methodName];
    if (method)
      method.call(this);
    else
      Assert.fail("Method '" + this.getName() + "' not found!");
  }

  /**
   * Sets the name of the test case.
   * @param name The new name of test cases.
   */
  public function setName(name : String) : void {
    this.mName = name;
  }

  /**
   * Retrieve the test case as string.
   * @return String Returns the name of the test case.
   */
  public function toString() : String {
    /*
     var className = new String( this.constructor );
     var regex = /function (\w+)/;
     regex.exec( className );
     className = new String( RegExp.$1 );
     */
    return this.mName; // + "(" + className + ")";
  }

  /**
   * Set up the environment of the fixture.
   */
  public function setUp() : void {
  }

  /**
   * Clear up the environment of the fixture.
   */
  public function tearDown() : void {
  }

}
}
