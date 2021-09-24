package package1.someOtherPackage {
import net.jangaroo.ext.FlExtEvent;

import package1.ConfigClass;

public class SomeEvent extends FlExtEvent {

  /**
   * @eventType onClick
   */
  public static const CLICK:String = "onClick";

  /**
   * @eventType onClickClack
   */
  public static const CLICK_CLACK:String = "onClickClack";

  public static const __PARAMETER_SEQUENCE__:Array = ["source", "eOpts"];

  public function SomeEvent(type: String, arguments:Array) {
    super(type, arguments);
  }

  /**
   * The config event source.
   */
  public native function get source():ConfigClass;
}
}