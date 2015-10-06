package ext.config {

[ExtConfig(target="ext.Label", xtype="label")]
public class label {

  /**
   * The text of the label
   */
  public native function get text():String;
  /**
   * @private
   */
  public native function set items(text:String):void;
}
}
