package package1.someOtherPackage {
import package1.ConfigClass;

public class SomeEvent {

  public static const CLICK:String = "click";
  public static const CLICK_CLACK:String = "clack";

  public function SomeEvent(arguments:Array) {
    this['source'] = arguments[0];
  }

  /**
   * The config event source.
   */
  public native function get source():ConfigClass;
}
}