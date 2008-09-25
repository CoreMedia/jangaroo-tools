package joox.unit.framework {

import joox.unit.framework.AssertionFailedError;
import joox.unit.util.CallStack;

/**
 * Thrown when a test assert comparing equal strings fail.
 */
public class ComparisonFailure extends AssertionFailedError {

  public var mExpected : String;
  public var mActual : String;

  /**
   * An AssertionFailedMessage needs a message and a call stack for construction.
   * @param msg Failure message (optional).
   * @param expected The expected string value.
   * @param actual The actual string value.
   * @param stack The call stack of the assertion.
   */
  public function ComparisonFailure(msg : String, expected : String, actual : String, stack : CallStack) {
    super(( msg ? msg + " " : "" ) + "expected", stack);
    this.name = "ComparisonFailure";
    this.mExpected = new String(expected);
    this.mActual = new String(actual);
  }

  /**
   * Returns the error message.
   * @return String Returns the formatted error message.
   * Returns "..." in place of common prefix and "..." in
   * place of common suffix between expected and actual.
   */
  public function toString() : String {
    var str : String = super.toString();

    var end : Number = Math.min(this.mExpected.length, this.mActual.length);
    var i : Number = 0;
    while (i < end) {
      if (this.mExpected.charAt(i) != this.mActual.charAt(i))
        break;
      ++i;
    }
    var j : Number = this.mExpected.length - 1;
    var k : Number = this.mActual.length - 1;
    while (k >= i && j >= i) {
      if (this.mExpected.charAt(j) != this.mActual.charAt(k))
        break;
      --k;
      --j;
    }
    var expected : String;
    var actual : String;

    if (j < i && k < i) {
      expected = this.mExpected;
      actual = this.mActual;
    } else {
      expected = this.mExpected.substring(i, j + 1);
      actual = this.mActual.substring(i, k + 1);
      if (i <= end && i > 0) {
        expected = "..." + expected;
        actual = "..." + actual;
      }
      if (j < this.mExpected.length - 1)
        expected += "...";
      if (k < this.mActual.length - 1)
        actual += "...";
    }

    return str + ":<" + expected + ">, but was:<" + actual + ">";
  }

{
  ComparisonFailure.prototype["name"] = "ComparisonFailure";
}

}
}
