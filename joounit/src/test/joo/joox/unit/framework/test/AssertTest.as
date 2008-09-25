package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.AssertionFailedError;
import joox.unit.framework.ComparisonFailure;
import joox.unit.framework.Test;
import joox.unit.framework.Assert;

public class AssertTest extends TestCase {

public function AssertTest( name )
{
	super( name );
}

public function testAssertEquals()
{
	Assert.assertEquals( 1, 1 );
	Assert.assertEquals( "1 is 1", 1, 1 );
	try
	{
		Assert.assertEquals( 0, 1 );
		Assert.fail( "'assertEquals' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		Assert.assertEquals( "0 is not 1", 0, 1 );
		Assert.fail( "'assertEquals' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "0 is not 1" ) > 0 );
	}
	Assert.assertEquals( "This is 1", "This is 1" );
	try
	{
		Assert.assertEquals( "This is 1", "This is 0" );
		Assert.fail( "'assertEquals' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof ComparisonFailure );
		Assert.assertTrue( ex.toString().indexOf( "...1>" ) > 0 );
	}
	Assert.assertEquals( /.*1$/, "This is 1" );
	try
	{
		Assert.assertEquals( /.*1$/, "This is 0" );
		Assert.fail( "'assertEquals' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "RegExp" ) > 0 );
	}
}
public function testAssertFalse()
{
	Assert.assertFalse( "Should not throw!", false );
	Assert.assertFalse( false );
	try
	{
		Assert.assertFalse( "Have to throw!", true );
		Assert.fail( "'assertFalse' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Have to throw!" ) > 0 );
	}
	try
	{
		Assert.assertFalse( true );
		Assert.fail( "'assertFalse' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	Assert.assertFalse( "this instanceof joox.unit.framework.Test" );
	try
	{
		Assert.assertFalse( "this instanceof joox.unit.framework.Assert" );
		Assert.fail( "'assertFalse' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
}
public function testAssertNotNull()
{
	Assert.assertNotNull( "Is null!", 0 );
	Assert.assertNotNull( 0 );
	Assert.assertNotNull( "Is null!", 1 );
	Assert.assertNotNull( 1 );
	Assert.assertNotNull( "Is null!", "Hi!" );
	Assert.assertNotNull( "Hi!" );
	try
	{
		Assert.assertNotNull( null );
		Assert.fail( "'assertNotNull' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		Assert.assertNotNull( "Is null!", null );
		Assert.fail( "'assertNotNull' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Is null!" ) > 0 );
	}
}
public function testAssertNotSame()
{
	var one = new String( "1" );
	Assert.assertNotSame( "Should not throw!", one, new String( "1" ));
	Assert.assertNotSame( one, one, new String( "1" ));
	try
	{
		var me = this;
		Assert.assertNotSame( "Have to throw!", this, me );
		Assert.fail( "'assertNotSame' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Have to throw!" ) > 0 );
	}
	try
	{
		var me = this;
		Assert.assertNotSame( this, me );
		Assert.fail( "'assertNotSame' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
}
public function testAssertNotUndefined()
{
	Assert.assertNotUndefined( "Is undefined!", 0 );
	Assert.assertNotUndefined( 0 );
	Assert.assertNotUndefined( "Is undefined!", false );
	Assert.assertNotUndefined( false );
	Assert.assertNotUndefined( "Is undefined!", "Hi!" );
	Assert.assertNotUndefined( "Hi!" );
	try
	{
		var undefdVar;
		Assert.assertNotUndefined( undefdVar );
		Assert.fail( "'assertNotUndefined' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		var undefdVar;
		Assert.assertNotUndefined( "Is undefined!", undefdVar );
		Assert.fail( "'assertNotUndefined' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Is undefined!" ) > 0 );
	}
}
public function testAssertNull()
{
	Assert.assertNull( "Is not null!", null );
	Assert.assertNull( null );
	try
	{
		Assert.assertNull( 0 );
		Assert.fail( "'assertNull' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		Assert.assertNull( "Is not null!", 0 );
		Assert.fail( "'assertNull' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Is not null!" ) > 0 );
	}
}
public function testAssertSame()
{
	var me = this;
	Assert.assertSame( "Should not throw!", this, me );
	Assert.assertSame( this, me );
	try
	{
		var one = new String( "1" );
		Assert.assertSame( "Have to throw!", one, new String( "1" ));
		Assert.fail( "'assertSame' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Have to throw!" ) > 0 );
	}
	try
	{
		var one = new String( "1" );
		Assert.assertSame( one, new String( "1" ));
		Assert.fail( "'assertSame' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
}
public function testAssertTrue()
{
	Assert.assertTrue( "Should not throw!", true );
	Assert.assertTrue( true );
	try
	{
		Assert.assertTrue( "Have to throw!", false );
		Assert.fail( "'assertTrue' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Have to throw!" ) > 0 );
	}
	try
	{
		Assert.assertTrue( false );
		Assert.fail( "'assertTrue' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		Assert.assertTrue( "this instanceof joox.unit.framework.Test" );
		Assert.fail( "'assertTrue' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
}

private function fn() {}

public function testAssertUndefined()
{
	var x;
	Assert.assertUndefined( "Not undefined!", undefined );
	Assert.assertUndefined( undefined );
	Assert.assertUndefined( "Not undefined!", x );
	Assert.assertUndefined( x );
	Assert.assertUndefined( "Not undefined!", this.fn());
	Assert.assertUndefined( this.fn());
	try
	{
		Assert.assertUndefined( this );
		Assert.fail( "'assertUndefined' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
	}
	try
	{
		Assert.assertUndefined( "Not undefined!", this );
		Assert.fail( "'assertUndefined' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Not undefined!" ) > 0 );
	}
}
public function testFail()
{
	try
	{
		Assert.fail( "Have to throw!", null );
		Assert.fail( "'fail' should have thrown." );
	}
	catch( ex )
	{
		Assert.assertTrue( ex instanceof AssertionFailedError );
		Assert.assertTrue( ex.toString().indexOf( "Have to throw!" ) > 0 );
	}
}

}
}
