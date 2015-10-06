package ext.config {

[ExtConfig(target="ext.Component", xtype="component")]
public class component {

  /**
   * Id of the component
   */
  public native function get id():String;
  /**
   * @private
   */
  public native function set id(value:String):void;

   /**
   * the x value
   */
  public native function get x():Number;
  /**
   * @private
   */
  public native function set x(value:Number):void;

  public native function get margins():Object;

  /**
   * @private
   */
  public native function set margins(value:Object):void;

  public native function get mixins():Array;

  public native function set mixins(value:Array):void;

  public native function get plugins():Array;

  public native function set plugins(value:Array):void;
}
}
