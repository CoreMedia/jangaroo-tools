package package2 {

/**
 * Test mixin event.
 */
[Event("firedByMixin")]

[Mixin("package2.TestMixin")]
/**
 * This is a test mixin.
 */
public interface ITestMixin {
  /**
   * An accessor.
   */
  [Bindable]
  function get foo():Number;

  /**
   * The setter.
   */
  [Bindable]
  function set foo(value:Number):void;

  /**
   * This method is mixed in.
   * @param thing from the swamp
   * @return mud
   */
  function mix(thing:String):String;
}
}
