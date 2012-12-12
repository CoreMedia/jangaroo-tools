package package1.someOtherPackage {
import package1.ConfigClass;

public class SomeEvent {

  public function SomeEvent(arguments:Array) {
    this['source'] = arguments[0];
  }

  public native function get source():ConfigClass;
}
}