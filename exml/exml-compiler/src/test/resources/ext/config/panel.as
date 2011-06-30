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

   /**
   * List of child items
   */
  public native function get menu():Array;
  /**
   * @private
   */
  public native function set menu(value:Array):void;
}
}