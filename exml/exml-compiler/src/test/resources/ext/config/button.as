package ext.config {

[ExtConfig(target="ext.Button")]
public class button {

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