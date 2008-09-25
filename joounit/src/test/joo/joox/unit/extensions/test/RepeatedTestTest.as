package joox.unit.extensions.test {

import joox.unit.framework.Test;
import joox.unit.framework.TestCase;
import joox.unit.framework.TestSuite;
import joox.unit.framework.test.MyTest;
import joox.unit.extensions.RepeatedTest;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class RepeatedTestTest extends TestCase {

  public var mTest : TestSuite;

  public function RepeatedTestTest(name : String) {
    super(name);
  }

  public override function setUp() : void {
    this.mTest = new TestSuite(MyTest);
    this.mTest.runTest = function() : void {
      this.mCount++;
    };
    this.mTest.mCount = 0;
  }

  public function testCountTestCases() : void {
    var test : RepeatedTest = new RepeatedTest(this.mTest, 5);
    Assert.assertEquals(10, test.countTestCases());
    this.setUp();
    test = new RepeatedTest(this.mTest, 0);
    Assert.assertEquals(0, test.countTestCases());
  }

  public function testRun() : void {
    var test : RepeatedTest = new RepeatedTest(this.mTest, 5);
    test.run(new TestResult());
    Assert.assertEquals(10, this.mTest.mCount);
    this.setUp();
    test = new RepeatedTest(this.mTest, 0);
    test.run(new TestResult());
    Assert.assertEquals(0, this.mTest.mCount);
  }

  public function testToString() : void {
    var test : RepeatedTest = new RepeatedTest(this.mTest, 5);
    Assert.assertTrue(test.toString().indexOf("(repeated)") > 0);
  }

}
}
