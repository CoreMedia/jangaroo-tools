package exmlparser.config {

public class allElements {
  /**
   * This is my <b>constant</b>
   */
  public static const SOME_CONSTANT:int = 1234;

  /**
   * This is another <b>constant</b>
   */
  public static const ANOTHER_CONSTANT:String = "Lorem ipsum & Co.\n        Another line.";

  public static const CODE_CONSTANT:int = 1 + 1;

  /* describe the config properties of this component */
  public native function get myProperty():String;

  /**
   * @private
   */
  public native function set myProperty(value:String):void;

  /**
   * This is my <b>description</b>
   */
  public native function get myPropertyWithDescription():Boolean;

  /**
   * @private
   */
  public native function set myPropertyWithDescription(value:Boolean):void;
}
}