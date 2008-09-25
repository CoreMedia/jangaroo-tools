package joox.unit.extensions.test {

import joox.unit.extensions.ExceptionTestCase;
import joox.unit.framework.TestCase;

public class MyExceptionTestCase extends ExceptionTestCase {

public function MyExceptionTestCase( name )
{ 
	super( name, TestCase );
}

public function testClass() { throw new TestCase(); }
public function testDerived() { throw new MyExceptionTestCase(); }
public function testOther() { throw new Error(); }
public function testNone() {}

}
}
