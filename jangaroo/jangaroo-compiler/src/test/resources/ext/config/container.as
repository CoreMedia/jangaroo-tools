package ext.config {

[ExtConfig(target="ext.Container", xtype="container")]
public class container extends component {

  /**
   * defaults for children
   */
  public native function get defaults():component;
  /**
   * @private
   */
  public native function set defaults(value:component):void;

  /**
   * this container's children
   */
  public native function get items():Array;
  /**
   * @private
   */
  public native function set items(value:Array):void;

  public native function get layout():String;

  public native function set layout(value:String):void;

  public native function get layoutConfig():Object;

  public native function set layoutConfig(value:Object):void;
}
}
