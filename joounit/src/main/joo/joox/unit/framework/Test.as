package joox.unit.framework {

/**
 * A test can be run and collect its results.
 * @note Additional to JUnit 3.8 the test has always a name. The interface
 * requires a getter and a setter and a method to search for tests.
 */
public interface Test {

  /**
   * Counts the number of test cases that will be run by this test.
   * @return Number The number of test cases.
   */
  function countTestCases() : Number;

  /**
   * Search a test by name.
   * The function compares the given name with the name of the test and
   * returns its own instance if the name is equal.
   * @note This is an enhancement to JUnit 3.8
   * @param testName The name of the searched test.
   * @return Test The test instance itself of null.
   */
  function findTest(testName : String) : Test;

  /**
   * Retrieves the name of the test.
   * @note This is an enhancement to JUnit 3.8
   * @return String The name of test.
   */
  function getName() : String;

  /**
   * Runs the test.
   * @param result The result to fill.
   */
  function run(result : TestResult) : void;

  /**
   * Sets the name of the test.
   * @note This is an enhancement to JUnit 3.8
   * @param testName The new name of the test.
   */
  function setName(testName : String) : void;

}
}
