package joox.unit.extensions.test {

import joox.unit.framework.TestCase;
import joox.unit.extensions.TestSetup;
import joox.unit.framework.Test;
import joox.unit.framework.TestResult;
import joox.unit.framework.Assert;

public class TestSetupTest extends TestCase {

public function TestSetupTest( name )
{
	super( name );
}

public function testRun()
{
	var test = new TestSetup( new TestCase());
	test.setUp = function() { this.mSetUp = true; };
	test.tearDown = function() { this.mTearDown = true; };
	test.run( new TestResult());
	Assert.assertTrue( test.mSetUp );
	Assert.assertTrue( test.mTearDown );
}
}
}
