package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.TestSuite;
import joox.unit.framework.test.MyTest;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class TestSuiteTest extends TestCase {

public function TestSuiteTest( name )
{
	super( name );
}
public function testCtor()
{
	var undef;
	var suite = new TestSuite();
	Assert.assertEquals( 0, suite.countTestCases());
	Assert.assertSame( "", suite.getName());
	suite = new TestSuite( null );
	Assert.assertEquals( 0, suite.countTestCases());
	Assert.assertSame( "", suite.getName());
	suite = new TestSuite( undef );
	Assert.assertEquals( 0, suite.countTestCases());
	Assert.assertSame( "", suite.getName());
	suite = new TestSuite( "name" );
	Assert.assertEquals( 0, suite.countTestCases());
	Assert.assertEquals( "name", suite.getName());
	suite = new TestSuite( MyTest );
	Assert.assertEquals( 2, suite.countTestCases());
	Assert.assertEquals( "joox.unit.framework.test.MyTest", suite.getName());
	suite = new TestSuite( new MyTest() );
	Assert.assertEquals( 1, suite.countTestCases());
	Assert.assertSame( "", suite.getName());
	suite = new TestSuite( new MyTest( "name" ));
	Assert.assertEquals( 1, suite.countTestCases());
	Assert.assertSame( "", suite.getName());
}
public function testAddTest()
{
	var suite = new TestSuite();
	Assert.assertEquals( 0, suite.countTestCases());
	suite.addTest( new MyTest( "testMe" ));
	Assert.assertEquals( 1, suite.countTestCases());
}
public function testAddTestSuite()
{
	var suite = new TestSuite();
	Assert.assertEquals( 0, suite.countTestCases());
	suite.addTestSuite( MyTest );
	Assert.assertEquals( 2, suite.countTestCases());
}
public function testCountTestCases()
{
	var suite = new TestSuite();
	Assert.assertEquals( 0, suite.countTestCases());
	suite.addTest( new MyTest( "testMe" ));
	Assert.assertEquals( 1, suite.countTestCases());
	suite.addTest( new MyTest( "testMyself" ));
	Assert.assertEquals( 2, suite.countTestCases());
	suite.addTest( new TestSuite( MyTest ));
	Assert.assertEquals( 4, suite.countTestCases());
}
public function testFindTest()
{
	var suite = new TestSuite( MyTest );
	var test = suite.findTest( "joox.unit.framework.test.MyTest.testMe" );
	Assert.assertEquals( "joox.unit.framework.test.MyTest.testMe", test ? test.getName() : null );
	Assert.assertNotNull( suite.findTest( "joox.unit.framework.test.MyTest.testMyself" ));
	Assert.assertNotNull( suite.findTest( "joox.unit.framework.test.MyTest" ));
	Assert.assertNull( suite.findTest( "you" ));
	Assert.assertNull( suite.findTest());
}
public function testGetName()
{
	var suite = new TestSuite( "name" );
	Assert.assertEquals( "name", suite.getName());
}
public function testRun()
{
	var suite = new TestSuite();
	var result = new TestResult();
	suite.run( result );
	Assert.assertEquals( 1, result.failureCount());
	Assert.assertEquals( 0, result.runCount());
	result = new TestResult();
	result.addFailure = function() { this.stop(); };
	suite.addTest( new TestSuite( MyTest ));
	suite.addTest( new TestSuite());
	suite.addTest( new TestSuite( MyTest ));
	suite.run( result );
	Assert.assertEquals( 2, result.runCount());
	Assert.assertEquals( 4, suite.countTestCases());
}
public function testRunTest()
{
	var suite = new TestSuite();
	var result = new TestResult();
	suite.runTest( new MyTest( "name" ), result );
	Assert.assertEquals( 1, result.runCount());
}
public function testSetName()
{
	var suite = new TestSuite();
	Assert.assertSame( "", suite.getName());
	suite.setName( "name" );
	Assert.assertEquals( "name", suite.getName());
}
public function testTestAt()
{
	var suite = new TestSuite();
	suite.addTest( new TestSuite( MyTest ));
	suite.addTest( new MyTest( "testMyself" ));
	Assert.assertEquals( "joox.unit.framework.test.MyTest", suite.testAt( 0 ).getName());
	Assert.assertEquals( ".testMyself", suite.testAt( 1 ).getName());
	Assert.assertUndefined( suite.testAt( 2 ));
}
public function testTestCount()
{
	var suite = new TestSuite();
	Assert.assertEquals( 0, suite.testCount());
	suite.addTest( new TestSuite( MyTest ));
	Assert.assertEquals( 1, suite.testCount());
	suite.addTest( new MyTest( "testMyself" ));
	Assert.assertEquals( 2, suite.testCount());
}
public function testToString()
{
	var suite = new TestSuite( "name" );
	Assert.assertEquals( "Suite 'name'", suite.toString());
	Assert.assertEquals( "Suite 'name'", suite );
}
public function testWarning()
{
	var suite = new TestSuite();
	var test = suite.warning( "This is a warning!" );
	Assert.assertEquals( "warning", test.getName());
	var result = new TestResult();
	suite.runTest( test, result );
	Assert.assertEquals( 1, result.failureCount());
	Assert.assertTrue( result.mFailures[0].toString().indexOf(
		"This is a warning!" ) > 0 );
}

}
}
