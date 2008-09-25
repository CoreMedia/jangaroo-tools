package joox.unit.extensions.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.TestSuite;
import joox.unit.framework.test.MyTest;
import joox.unit.extensions.TestDecorator;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class TestDecoratorTest extends TestCase {

  public function TestDecoratorTest(name : String) {
    super(name);
  }

  public override function setUp() : void {
    this.mTest = new TestSuite(MyTest);
    this.mTest.runTest = function() {
      this.mCalled = true;
    };
  }

  public function testBasicRun() : void {
    var decorator = new TestDecorator(this.mTest);
    decorator.basicRun(new TestResult());
    Assert.assertTrue(this.mTest.mCalled);
  }

  public function testCountTestCases() : void {
    var decorator = new TestDecorator(this.mTest);
    Assert.assertEquals(2, decorator.countTestCases());
  }

  public function testFindTest() : void {
    var decorator = new TestDecorator(this.mTest);
    Assert.assertNotNull(decorator.findTest("joox.unit.framework.test.MyTest.testMyself"));
  }

  public function testGetName() : void {
    var decorator = new TestDecorator(this.mTest);
    Assert.assertEquals("joox.unit.framework.test.MyTest", decorator.getName());
  }

  public function testGetTest() : void {
    var decorator = new TestDecorator(this.mTest);
    Assert.assertSame(this.mTest, decorator.getTest());
  }

  public function testRun() : void {
    var decorator = new TestDecorator(this.mTest);
    decorator.run(new TestResult());
    Assert.assertTrue(this.mTest.mCalled);
  }

  public function testSetName() : void {
    var decorator = new TestDecorator(this.mTest);
    decorator.setName("FlyAlone");
    Assert.assertEquals("FlyAlone", this.mTest.getName());
  }

  public function testToString() : void {
    var decorator = new TestDecorator(this.mTest);
    Assert.assertTrue(decorator.toString().indexOf("'joox.unit.framework.test.MyTest'") > 0);
  }

}
}
