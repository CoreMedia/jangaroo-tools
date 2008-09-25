package joox.unit.extensions {

import joox.unit.extensions.TestDecorator;
import joox.unit.framework.Test;
import joox.unit.framework.TestResult;

/**
 * A Decorator to set up and tear down additional fixture state.
 * Subclass TestSetup and insert it into your tests when you want
 * to set up additional state once before the tests are run.
 * @see joox.unit.framework.TestCase
 */
public class TestSetup extends TestDecorator {

  /**
   * The constructore saves the test.
   * @param test the test to decorate.
   */
  public function TestSetup(test : Test) {
    super(test);
  }

  /**
   * Runs a test case with additional set up and tear down.
   * @param result the result set.
   */
  public override function run(result : TestResult) : void {
    var protectable : Function = (function() : void {
      this.setUp();
      this.basicRun( result );
      this.tearDown();
    }).bind(this);
    result.runProtected(this.mTest, protectable);
  }

  /**
   * Sets up the fixture. Override to set up additional fixture
   * state.
   */
  public function setUp() : void { }

  /**
   * Tears down the fixture. Override to tear down the additional
   * fixture state.
   */
  public function tearDown() : void { }

}
}
