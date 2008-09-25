package joox.unit.framework.test {

import joox.unit.framework.TestListener;
import joox.unit.framework.Test;
import joox.unit.framework.AssertionFailedError;

public class TestResultTestListener implements TestListener {

  var mErrors : Number;
  var mFailures : Number;
  var mStarted : Number;
  var mEnded : Number;

  public function addError(test : Test, except : Error) : void {
    this.mErrors++;
  }
  public function addFailure(test : Test, afe : AssertionFailedError) : void {
    this.mFailures++;
  }
  public function startTest(test : Test) : void {
    this.mStarted++;
  }
  public function endTest(test : Test) : void {
    this.mEnded++;
  }
}
}
