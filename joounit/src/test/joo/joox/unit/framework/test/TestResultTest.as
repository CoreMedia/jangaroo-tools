package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.TestListener;
import joox.unit.framework.TestResult;
import joox.unit.framework.Test;
import joox.unit.framework.AssertionFailedError;
import joox.unit.framework.Assert;
import joox.unit.framework.test.TestResultTestListener;

public class TestResultTest extends TestCase {

  private var mListener : TestResultTestListener;

  public function TestResultTest(name : String) {
    super(name);
    this.mListener = new TestResultTestListener();
  }

  public override function setUp() : void {
    this.mListener.mErrors = 0;
    this.mListener.mFailures = 0;
    this.mListener.mStarted = 0;
    this.mListener.mEnded = 0;
  }

  public function testAddError() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.addError(new TestCase("Test"), new Object());
    Assert.assertEquals(1, result.errorCount());
    Assert.assertEquals(1, this.mListener.mErrors);
  }

  public function testAddFailure() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.addFailure(new TestCase("Test"), new Object());
    Assert.assertEquals(1, result.failureCount());
    Assert.assertEquals(1, this.mListener.mFailures);
  }

  public function testAddListener() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.run(new TestResultTest("testAddError"));
    Assert.assertEquals(1, this.mListener.mStarted);
    Assert.assertEquals(1, this.mListener.mEnded);
    Assert.assertEquals(0, this.mListener.mErrors);
    Assert.assertEquals(0, this.mListener.mFailures);
  }

  public function testCloneListeners() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    var listeners : Array = result.cloneListeners();
    Assert.assertEquals(1, listeners.length);
    Assert.assertEquals(0, this.mListener.mStarted);
    Assert.assertEquals(0, this.mListener.mEnded);
    Assert.assertEquals(0, listeners[0].mStarted);
    Assert.assertEquals(0, listeners[0].mEnded);
    result.run(new TestResultTest("testAddError"));
    Assert.assertEquals(1, this.mListener.mStarted);
    Assert.assertEquals(1, this.mListener.mEnded);
    Assert.assertEquals(1, listeners[0].mStarted);
    Assert.assertEquals(1, listeners[0].mEnded);
    result.removeListener(this.mListener);
    Assert.assertEquals(1, listeners.length);
    Assert.assertEquals(0, result.cloneListeners().length);
  }
  public function testEndTest() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.endTest(new TestCase("Test"));
    Assert.assertEquals(1, this.mListener.mEnded);
  }
  public function testErrorCount() : void {
    var result : TestResult = new TestResult();
    result.addError(new TestCase("Test"), new Object());
    Assert.assertEquals(1, result.errorCount());
  }
  public function testFailureCount() : void {
    var result : TestResult = new TestResult();
    result.addFailure(new TestCase("Test"), new Object());
    Assert.assertEquals(1, result.failureCount());
  }
  public function testRemoveListener() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.run(new TestResultTest("testAddError"));
    Assert.assertEquals(1, this.mListener.mStarted);
    Assert.assertEquals(1, this.mListener.mEnded);
    this.setUp();
    result.removeListener(this.mListener);
    result.run(new TestResultTest("testAddError"));
    Assert.assertEquals(0, this.mListener.mStarted);
    Assert.assertEquals(0, this.mListener.mEnded);
  }
  public function testRun() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    var test : TestResultTest = new TestResultTest("testAddError");
    result.run(test);
    Assert.assertEquals(1, this.mListener.mStarted);
    Assert.assertEquals(1, this.mListener.mEnded);
  }
  public function testRunCount() : void {
    var result : TestResult = new TestResult();
    var test : TestResultTest = new TestResultTest("testAddError");
    result.run(test);
    test.testAddError = function() : void {
      throw new AssertionFailedError("Message", null);
    };
    result.run(test);
    Assert.assertEquals(2, result.runCount());
  }
  public function testRunProtected() : void {
    var result : TestResult = new TestResult();
    var test : TestResultTest = new TestResultTest("testAddError");
    Assert.assertEquals(0, result.errorCount());
    Assert.assertEquals(0, result.failureCount());
    var mThrown : Error = null;
    var protectable : Function = function() : void {
      try {
        test.runBare();
      } catch(ex : Error) {
        mThrown = ex;
        throw ex;
      }
    };
    result.runProtected(test, protectable);
    Assert.assertEquals(0, result.errorCount());
    Assert.assertEquals(0, result.failureCount());
    Assert.assertNull(mThrown);
    this.setUp();
    test.testAddError = function() : void {
      throw new AssertionFailedError("Message", null);
    };
    mThrown = null;
    result.runProtected(test, protectable);
    Assert.assertEquals(0, result.errorCount());
    Assert.assertEquals(1, result.failureCount());
    Assert.assertNotNull(mThrown);
    this.setUp();
    test.testAddError = function() : void {
      throw new Object();
    };
    mThrown = null;
    result.runProtected(test, protectable);
    Assert.assertEquals(1, result.errorCount());
    Assert.assertEquals(1, result.failureCount());
    Assert.assertNotNull(mThrown);
  }
  public function testShouldStop() : void {
    var result : TestResult = new TestResult();
    result.stop();
    Assert.assertEquals(1, result.shouldStop());
  }
  public function testStartTest() : void {
    var result : TestResult = new TestResult();
    result.addListener(this.mListener);
    result.startTest(new TestCase("Test"));
    Assert.assertEquals(1, this.mListener.mStarted);
  }
  public function testStop() : void {
    var result : TestResult = new TestResult();
    result.stop();
    Assert.assertEquals(1, result.shouldStop());
  }
  public function testWasSuccessful() : void {
    var result : TestResult = new TestResult();
    var test : TestResultTest = new TestResultTest("testAddError");
    result.run(test);
    Assert.assertTrue(result.wasSuccessful());
    test.testAddError = function() : void {
      throw new AssertionFailedError("Message", null);
    };
    result.run(test);
    Assert.assertFalse(result.wasSuccessful());
    result = new TestResult();
    test.testAddError = function() : void {
      throw new Object();
    };
    result.run(test);
    Assert.assertFalse(result.wasSuccessful());
  }

}
}
