package joox.unit.textui.test {

import joox.unit.framework.TestCase;
import joox.unit.textui.TestRunner;
import joox.unit.framework.TestResult;
import joox.unit.util.StringWriter;
import joox.unit.framework.TestSuite;
import joox.unit.runner.BaseTestRunner;
import joox.unit.textui.test.DummyRunner;
import joox.unit.framework.Assert;
import joox.unit.textui.ResultPrinter;

public class TestRunnerTest extends TestCase {

  public function TestRunnerTest(name : String) {
    super(name);
  }
  public override function setUp() : void {
    this.mRunner = new TestRunner();
  }
  public function testCreateTestResult() : void {
    Assert.assertTrue(this.mRunner.createTestResult() instanceof TestResult);
  }
  public function testDoRun() : void {
    this.mRunner.setPrinter(new StringWriter());
    var result = this.mRunner.doRun(new TestSuite("Suite"));
    Assert.assertTrue(result instanceof TestResult);
  }
  public function testMain() : void {
    var orig = BaseTestRunner.getPreference("TestRunner");

    BaseTestRunner.setPreference("TestRunner", DummyRunner);

    var args = new Array();
    //	args.push( "--classic" );
    args.push("test");
    TestRunner.main(args);
    Assert.assertEquals("1", DummyRunner.msg);

    BaseTestRunner.setPreference("TestRunner", orig);
  }
  public function testSetPrinter() : void {
    var printer = new StringWriter();
    this.mRunner.setPrinter(printer);
    Assert.assertSame(printer, this.mRunner.mPrinter.getWriter());
    //printer = new ClassicResultPrinter();
    //this.mRunner.setPrinter( printer );
    //Assert.assertSame( printer, this.mRunner.mPrinter );
    this.mRunner.setPrinter(null);
    Assert.assertTrue(this.mRunner.mPrinter instanceof ResultPrinter);
  }
  public function testStart() : void {
    this.mRunner.doRun = function(suite) {
      this.mName = suite.getName();
    };
    this.mRunner.runFailed = function(msg) {
      this.mFailed = msg;
    };
    try {
      this.mRunner.start("-x");
      Assert.fail("'joox.unit.textui.TestRunner.start' should have thrown.");
    } catch(ex) {
      Assert.assertEquals("Usage", ex.name);
    }
    try {
      this.mRunner.start("-?");
      Assert.fail("'joox.unit.textui.TestRunner.start' should have thrown.");
    } catch(ex) {
      Assert.assertEquals("Usage", ex.name);
    }
    this.mRunner.start("joox.unit.textui.test.TestRunnerTest");
    Assert.assertEquals("joox.unit.textui.test.TestRunnerTest", this.mRunner.mName);
    this.mRunner.start("-- -TestNotFound-");
    Assert.assertEquals(/-TestNotFound-/, this.mRunner.mFailed);
  }

}
}
