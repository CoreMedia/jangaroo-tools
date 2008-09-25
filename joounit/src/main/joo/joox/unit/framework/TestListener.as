package joox.unit.framework {

import joox.unit.framework.Test;
import joox.unit.framework.AssertionFailedError;

/**
 * A listener for test progress.
 */
public interface TestListener {

  /**
   * An occured error was added.
   * @param test The failed test.
   * @param except The thrown error.
   */
  function addError(test : Test, except : Error) : void;

  /**
   * An occured failure was added.
   * @param test The failed test.
   * @param afe The thrown assertion failure.
   */
  function addFailure(test : Test, afe : AssertionFailedError) : void;

  /**
   * A test ended.
   * @param test The ended test.
   */
  function endTest(test : Test) : void;

  /**
   * A test started
   * @param test The started test.
   */
  function startTest(test : Test) : void;

}
}
