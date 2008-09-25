package joox.unit.textui.test {

import joox.unit.framework.TestCase;
import joox.unit.textui.ResultPrinter;
import joox.unit.util.StringWriter;
import joox.unit.framework.AssertionFailedError;
import joox.unit.util.CallStack;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class ResultPrinterTest extends TestCase {

  public var mPrinter : ResultPrinter;

  public function ResultPrinterTest(name : String) {
    super(name);
  }

  public override function setUp() : void {
    this.mPrinter = new ResultPrinter(new StringWriter());
  }

  public function testAddError() : void {
    this.mPrinter.addError(null, null);
    Assert.assertEquals("E\n", this.mPrinter.getWriter().getString());
  }

  public function testAddFailure() : void {
    this.mPrinter.addFailure(null, null);
    Assert.assertEquals("F\n", this.mPrinter.getWriter().getString());
  }

  public function testElapsedTimeAsString() : void {
    Assert.assertEquals("1", this.mPrinter.elapsedTimeAsString(1000));
    Assert.assertEquals("0.01", this.mPrinter.elapsedTimeAsString(10));
    Assert.assertEquals("100", this.mPrinter.elapsedTimeAsString(1E5));
    Assert.assertEquals("0.0001", this.mPrinter.elapsedTimeAsString(.1));
  }

  public function testPrint() : void {
    var test = function (x) {
      this.getWriter().print(x);
    };
    this.mPrinter.printHeader = test;
    this.mPrinter.printErrors = test;
    this.mPrinter.printFailures = test;
    this.mPrinter.printFooter = test;
    this.mPrinter.print("0", "1");
    Assert.assertEquals("1000\n", this.mPrinter.getWriter().getString());
  }

  public function testPrintErrors() : void {
    var result = new Object();
    result.mErrors = new Array();
    result.mErrors.push(new Error("XXX"));
    result.mErrors.push(new Error("YYY"));
    this.mPrinter.printErrors(result);
    var str = this.mPrinter.getWriter().getString();
    Assert.assertEquals(/were 2 errors/, str);
    Assert.assertEquals(/1\) Error: XXX/, str);
    Assert.assertEquals(/2\) Error: YYY/, str);
  }

  public function testPrintFailures() : void {
    var result = new Object();
    result.mFailures = new Array();
    result.mFailures.push(new AssertionFailedError("AFE", new CallStack()));
    this.mPrinter.printFailures(result);
    var str = this.mPrinter.getWriter().getString();
    Assert.assertEquals(/was 1 failure/, str);
    Assert.assertEquals(/1\) AssertionFailedError: AFE/, str);
  }

  public function testPrintFooter() : void {
    var result = new TestResult();
    this.mPrinter.printFooter(result);
    result.addError("Test", new Error("XXX"));
    this.mPrinter.printFooter(result);
    var str = this.mPrinter.getWriter().getString();
    Assert.assertEquals(/OK \(0 tests\)/, str);
    Assert.assertEquals(/FAILURES!!!\nTests run: 0, Failures: 0, Errors: 1/m,
      str);
  }

  public function testPrintHeader() : void {
    this.mPrinter.printHeader(10000);
    Assert.assertEquals("\nTime: 10\n", this.mPrinter.getWriter().getString());
  }

  public function testSetWriter() : void {
    Assert.assertNotSame(
      joox.unit.util.JooUtil.getSystemWriter(), this.mPrinter.getWriter());
    this.mPrinter.setWriter();
    Assert.assertSame(
      joox.unit.util.JooUtil.getSystemWriter(), this.mPrinter.getWriter());
  }

  public function testStartTest() : void {
    for (var i = 0; i < 42; ++i)
      this.mPrinter.startTest("test");
    Assert.assertEquals(/^\.{40}\n\.\.$/m, this.mPrinter.getWriter().getString());
  }

}
}
