package ext.config {

[ExtConfig(target="ext.Panel", xtype="panel")]
public class panel extends container {

  public function panel(config:Object = null) {
  }

  /**
   * List of tools
   */
  public native function get tools():Array;
  /**
   * @private
   */
  public native function set tools(value:Array):void;

   /**
   * List of child items
   */
  public native function get menu():Array;
  /**
   * @private
   */
  public native function set menu(value:Array):void;

  /**
   * visible
   */
  public native function get visible():Boolean;
  /**
   * @private
   */
  public native function set visible(value:Boolean):void;


  public native function get title():String;

  public native function set title(value:String):void;
}
}
