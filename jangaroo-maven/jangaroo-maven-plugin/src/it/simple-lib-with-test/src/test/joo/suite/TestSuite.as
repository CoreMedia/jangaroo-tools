package suite {
import net.jangaroo.example.HelloWorldTest;

//import flexunit.framework.TestSuite;

public class TestSuite {

  public function TestSuite() {
  }

  public static function suite():TestSuite {
    var theSuite:TestSuite = new TestSuite(HelloWorldTest);
    return theSuite;
  }
}
}