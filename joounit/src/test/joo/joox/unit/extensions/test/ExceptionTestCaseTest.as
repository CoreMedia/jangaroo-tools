package joox.unit.extensions.test {
import joox.unit.framework.TestResult;

import joox.unit.framework.TestCase;
import joox.unit.extensions.test.MyExceptionTestCase;
import joox.unit.framework.Assert;

public class ExceptionTestCaseTest extends TestCase {

  public function ExceptionTestCaseTest(name : String) {
    super(name);
  }

  public function testRunTest() : void {
    var test : MyExceptionTestCase = new MyExceptionTestCase("testClass");
    var result : TestResult = new TestResult();
    test.run(result);
    Assert.assertTrue(result.wasSuccessful());
    test = new MyExceptionTestCase("testDerived");
    test.run(result);
    Assert.assertTrue(result.wasSuccessful());
    test = new MyExceptionTestCase("testOther");
    test.run(result);
    Assert.assertEquals(1, result.errorCount());
    test = new MyExceptionTestCase("testNone");
    test.run(result);
    Assert.assertEquals(1, result.failureCount());
  }

}
}
