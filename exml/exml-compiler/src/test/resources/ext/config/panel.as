package ext.config {

[ExtConfig(target="Ext.Panel")]
public class panel {

  /**
   * List of child items
   */
  public native function get items():Array;
  /**
   * @private
   */
  public native function set items(value:Array):void;
}
}