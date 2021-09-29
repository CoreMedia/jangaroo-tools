package package1.someOtherPackage {
import net.jangaroo.ext.FlExtEvent;

import package1.ConfigClass;
import package1.IncludedClass;

public class SomeEvent extends FlExtEvent {

  /**
   * @eventType onClick
   */
  public static const CLICK:String = "onClick";

  /**
   * @eventType onClickClack
   */
  public static const CLICK_CLACK:String = "onClickClack";

  public static const __PARAMETER_SEQUENCE__:Array = ["source", "stanger", "eOpts"];

  public function SomeEvent(type: String, arguments:Array) {
    super(type, arguments);
  }

  /**
   * The config event source.
   */
  public native function get source():ConfigClass;

  public native function get stranger():IncludedClass;
}
}