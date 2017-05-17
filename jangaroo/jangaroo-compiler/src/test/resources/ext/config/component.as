package ext.config {
import ext.Action;

[ExtConfig(target="ext.Component", xtype="component")]
public class component {

  public function component(config:Object = null) {
  }

  /**
   * Id of the component
   */
  public native function get extId():String;

  [ExtConfig("id")]
  /**
   * @private
   */
  public native function set extId(value:String):void;

  public native function get baseAction():Action;
  public native function set baseAction(value:Action):void;

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

  [ExtConfig("__mixins__")]
  public native function set mixins(value:Array):void;

  public native function get plugins():Array;

  public native function set plugins(value:Array):void;
}
}
