package joox.unit.framework.test {

import joox.unit.framework.TestSuite;

public class MyTestSuite extends TestSuite {

public function MyTestSuite( name )
{
	super( name );
}

public function suite() 
{ 
	return new TestSuite( "MyTestSuite's Name" );
}

}

}
