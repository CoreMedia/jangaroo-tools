package {
import net.jangaroo.example.*;

import flexunit.framework.TestSuite;

public class JooTestSuite {

  public function JooTestSuite() {
  }

  public static function suite():TestSuite {
    trace("creating TestSuite...");
    var theSuite:TestSuite = new TestSuite(HelloWorldTest);
    return theSuite;
  }
}
}