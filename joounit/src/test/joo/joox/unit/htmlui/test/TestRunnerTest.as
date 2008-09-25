package joox.unit.htmlui.test {

import joox.unit.framework.TestCase;
import joox.unit.htmlui.TestRunner;
import joox.unit.util.HTMLWriterFilter;
import joox.unit.util.StringWriter;
import joox.unit.framework.Assert;

public class TestRunnerTest extends TestCase {

  public function TestRunnerTest(name : String) {
    super(name);
  }

  public function testCtor() : void {
    var runner = new TestRunner();
    Assert.assertTrue(runner.mPrinter.getWriter() instanceof HTMLWriterFilter);
  }

  public function testSetPrinter() : void {
    var runner = new TestRunner();
    runner.setPrinter(new StringWriter());
    Assert.assertTrue(runner.mPrinter.getWriter() instanceof HTMLWriterFilter);
  }

}
}
