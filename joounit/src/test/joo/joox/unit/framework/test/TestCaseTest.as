package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.test.MyTestCase;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class TestCaseTest extends TestCase {

  private var mTestCase : MyTestCase;

  public function TestCaseTest(name : String) {
    super(name);
  }

  public override function setUp() : void {
    this.mTestCase = new MyTestCase();
  }

  public function testCountTestCases() : void {
    Assert.assertEquals(1, this.mTestCase.countTestCases());
  }

  public function testCreateResult() : void {
    Assert.assertTrue(this.mTestCase.createResult() instanceof TestResult);
  }

  public function testFindTest() : void {
    Assert.assertEquals("testMe", this.mTestCase.findTest("testMe"));
    Assert.assertNull(this.mTestCase.findTest("Any"));
  }

  public function testGetName() : void {
    Assert.assertEquals("testMe", this.mTestCase.getName());
  }

  public function testRun() : void {
    var result : TestResult = new TestResult();
    this.mTestCase.run(result);
    Assert.assertTrue(result.wasSuccessful());
  }

  public function testRunTest() : void {
    try {
      this.mTestCase.runTest();
    } catch(ex : Error) {
      Assert.fail("runTest throwed unexpected exception.");
    }
    try {
      this.mTestCase.setName("noMember");
      this.mTestCase.runTest();
      Assert.fail("runTest did not throw expected exception.");
    } catch(ex : Error) {
    }
  }

  public function testSetName() : void {
    this.mTestCase.setName("newName");
    Assert.assertEquals("newName", this.mTestCase.getName());
  }

  public function testSetUp() : void {
    this.mTestCase.run(new TestResult());
    Assert.assertTrue(this.mTestCase.mSetUp);
  }

  public function testTearDown() : void {
    this.mTestCase.run(new TestResult());
    Assert.assertTrue(this.mTestCase.mTearDown);
  }

  public function testToString() : void {
    Assert.assertEquals("testMe", this.mTestCase.toString());
    Assert.assertEquals("testMe", this.mTestCase);
  }

}
}
