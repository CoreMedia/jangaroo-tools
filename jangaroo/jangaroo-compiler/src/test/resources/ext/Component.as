package ext {
import ext.mixin.Observable;

[Native("Ext.Component", require)]
[ExtConfig]
public class Component extends Observable {

  public function Component(config:Object = null) {
  }

  /**
   * Id of the component
   */
  public native function get id():String;
  /**
   * @private
   */
  public native function set id(value:String):void;

  /**
   * Alias for id to make it accessible from MXML
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

  public native function set mixins(value:Array):void;

  public native function get plugins():Array;

  public native function set plugins(value:Array):void;
}
}
