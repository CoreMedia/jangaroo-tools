package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.AssertionFailedError;
import joox.unit.framework.Assert;

public class AssertionFailedErrorTest extends TestCase {

public function AssertionFailedErrorTest( name )
{
        super( name );
}

public function testToString()
{
	var afe = new AssertionFailedError( "The Message", null );
	Assert.assertEquals( "AssertionFailedError: The Message", afe );
}

}
}
