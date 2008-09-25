package joox.unit.extensions {

import joox.unit.framework.Assert;
import joox.unit.framework.Test;
import joox.unit.framework.TestResult;
import joox.unit.util.JooUtil;

/**
 * A Decorator for Tests. Use TestDecorator as the base class
 * for defining new test decorators. Test decorator subclasses
 * can be introduced to add behaviour before or after a test
 * is run.
 * @see joox.unit.framework.Test
 */
public class TestDecorator extends Assert implements Test {

  public var mTest : Test;

  /**
   * The constructore saves the test.
   * @param test The test to decorate.
   */
  public function TestDecorator(test : Test) {
    super( );
    this.mTest = test;
  }

  /**
   * The basic run behaviour. The function calls the run method of the decorated
   * test.
   * @param result The test result.
   */
  public function basicRun(result : TestResult) : void {
    this.mTest.run(result);
  }

  /**
   * Returns the number of the test cases.
   * @return Number the number of the test cases.
   */
  public function countTestCases() : Number {
    return this.mTest.countTestCases();
  }

  /**
   * Returns the test if it matches the name.
   * @param name The searched test name.
   * @return Test the test if it matches the name.
   */
  public function findTest(name : String) : Test {
    return this.mTest.findTest(name);
  }

  /**
   * Returns name of the test.
   * @note This is an enhancement to JUnit 3.8
   * @return String the name of the test.
   */
  public function getName() : String {
    return this.mTest.getName();
  }

  /**
   * Returns the decorated test.
   * @note This is an enhancement to JUnit 3.8
   * @return Test the decorated test.
   */
  public function getTest() : Test {
    return this.mTest;
  }

  /**
   * Run the test.
   * @param result the test result.
   */
  public function run(result : TestResult) : void {
    this.basicRun(result);
  }

  /**
   * Sets name of the test.
   * @param name the new name of the test.
   */
  public function setName(name : String) : void {
    this.mTest.setName(name);
  }

  /**
   * Returns the test as string.
   * @note This is an enhancement to JUnit 3.8
   * @return String the test as string.
   */
  public function toString() : String {
    return this.mTest.toString();
  }

}
}
