package ext.config {

[ExtConfig(target="ext.Button", xtype="button")]
public class button extends component {

  public function button(config:Object = null) {
  }

  /**
   * The text of the label
   */
  public native function get text():String;
  /**
   * @private
   */
  public native function set text(value:String):void;

  public native function get handler():Function;
  /**
   * @private
   */
  public native function set handler(value:Function):void;
}
}
