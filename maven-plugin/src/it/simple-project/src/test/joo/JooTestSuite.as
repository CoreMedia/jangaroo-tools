package {
import net.jangaroo.example.HelloWorldTest;

import flexunit.framework.TestSuite;

public class JooTestSuite {

  public function JooTestSuite() {
  }

  public static function suite():TestSuite {
    var theSuite:TestSuite = new TestSuite(HelloWorldTest);
    return theSuite;
  }
}
}