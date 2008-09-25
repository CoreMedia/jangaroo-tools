package joox.unit.test {

import joox.unit.framework.TestSuite;
import joox.unit.framework.test.AssertionFailedErrorTest;
import joox.unit.framework.test.ComparisonFailureTest;
import joox.unit.framework.test.TestFailureTest;
import joox.unit.framework.test.TestResultTest;
import joox.unit.framework.test.AssertTest;
import joox.unit.framework.test.TestCaseTest;
import joox.unit.framework.test.TestSuiteTest;
import joox.unit.extensions.test.TestDecoratorTest;
import joox.unit.extensions.test.TestSetupTest;
import joox.unit.extensions.test.RepeatedTestTest;
import joox.unit.extensions.test.ExceptionTestCaseTest;
import joox.unit.runner.test.BaseTestRunnerTest;
import joox.unit.textui.test.ResultPrinterTest;
import joox.unit.textui.test.TestRunnerTest;
import joox.unit.htmlui.test.TestRunnerTest;

public class JooUnitTestSuite extends TestSuite {

public function JooUnitTestSuite()
{
	super( "JooUnitTestSuite" );
	this.addTestSuite( AssertionFailedErrorTest );
	this.addTestSuite( ComparisonFailureTest );
	this.addTestSuite( TestFailureTest );
	this.addTestSuite( TestResultTest );
	this.addTestSuite( AssertTest );
	this.addTestSuite( TestCaseTest );
	this.addTestSuite( TestSuiteTest );
	this.addTestSuite( TestDecoratorTest );
	this.addTestSuite( TestSetupTest );
	this.addTestSuite( RepeatedTestTest );
	this.addTestSuite( ExceptionTestCaseTest );
	this.addTestSuite( BaseTestRunnerTest );
	this.addTestSuite( ResultPrinterTest );
	this.addTestSuite( joox.unit.textui.test.TestRunnerTest );
	this.addTestSuite( joox.unit.htmlui.test.TestRunnerTest );
}

public static function suite() { return new JooUnitTestSuite(); }

}
}
