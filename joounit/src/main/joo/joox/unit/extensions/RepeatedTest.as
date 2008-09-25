package joox.unit.extensions {

import joox.unit.framework.Test;
import joox.unit.extensions.TestDecorator;
import joox.unit.framework.TestResult;

/**
 * A Decorator that runs a test repeatedly.
 */
public class RepeatedTest extends TestDecorator {

  public var mTimesRepeat : Number;

  /**
   * @param test test The test to repeat.
   * @param repeat The number of repeats.
   */
  public function RepeatedTest(test : Test, repeat : Number) {
    super( test );
    this.mTimesRepeat = repeat;
  }

  override public function countTestCases() : Number {
    var tests : Number = super.countTestCases( );
    return tests * this.mTimesRepeat;
  }

  /**
   * Runs a test case with additional set up and tear down.
   * @param result The result set.
   */
  override public function run(result : TestResult) : void {
    for( var i : Number = 0; i < this.mTimesRepeat; i++ ) {
      if (result.shouldStop())
        break;
      super.run(result);
    }
  }

  override public function toString() : String {
    return super.toString( ) + " (repeated)";
  }

}
}
