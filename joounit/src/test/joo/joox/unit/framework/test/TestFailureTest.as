package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.AssertionFailedError;
import joox.unit.framework.TestFailure;
import joox.unit.framework.Assert;

public class TestFailureTest extends TestCase {

public function TestFailureTest( name )
{
	super( name );
	this.mException = new AssertionFailedError( "Message", null );
	this.mTest = "testFunction";
}

public function testExceptionMessage()
{
	var ft = new TestFailure( this.mTest, this.mException );
	Assert.assertEquals( "AssertionFailedError: Message", ft.exceptionMessage());
}
public function testFailedTest()
{
	var ft = new TestFailure( this.mTest, this.mException );
	Assert.assertEquals( "testFunction", ft.failedTest());
}
public function testIsFailure()
{
	var ft = new TestFailure( this.mTest, this.mException );
	Assert.assertTrue( ft.isFailure());
	ft = new TestFailure( this.mTest, new Error( "Error" ));
	Assert.assertFalse( ft.isFailure());
}
public function testThrownException()
{
	var ft = new TestFailure( this.mTest, this.mException );
	Assert.assertEquals( this.mException, ft.thrownException());
}
public function testToString()
{
	var ft = new TestFailure( this.mTest, this.mException );
	Assert.assertEquals(
		"Test testFunction failed: AssertionFailedError: Message", ft );
}
public function testTrace()
{
	var ft = new TestFailure( this.mTest, 
		new AssertionFailedError( "Message", "Trace" ));
	Assert.assertEquals( "Trace", ft.trace());
}

}
}
