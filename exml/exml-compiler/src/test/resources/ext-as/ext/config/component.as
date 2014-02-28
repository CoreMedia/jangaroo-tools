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

  /**
   * The action
   * 
   * @see Action
   */
  public native function get baseAction():Action;

  /**
   * @private
   */
  public native function set baseAction(value:Action):void;

  public native function get margins():Object;

  /**
   * @private
   */
  public native function set margins(value:Object):void;
}
}
