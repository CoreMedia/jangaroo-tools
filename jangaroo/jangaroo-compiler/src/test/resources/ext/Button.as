package ext {
import ext.Component;

[Native("Ext.Button", require)]
public class Button extends Component {

  public function Button(config:Button = null) {
  }

  /**
   * The tool tip of the button
   */
  [Bindable]
  public var toolTip:String;

  /**
   * The text of the button
   */
  [Bindable]
  public native function get text():String;
  /**
   * @private
   */
  [Bindable]
  public native function set text(value:String):void;

  public native function get handler():Function;
  /**
   * @private
   */
  public native function set handler(value:Function):void;
}
}
