package package2 {

/**
 * Irrelevant implementation comment.
 */
public class TestMixin implements ITestMixin {

  /** @inheritDoc */
  [Bindable]
  public function get foo():Number {
    return 42;
  }

  /** @private */
  [Bindable]
  public function set foo(value:Number):void {
    // do nothing. 42 is perfect.
  }

  /**
   * @inheritDoc
   */
  public function mix(thing:String):String {
    return "Mixed " + thing + "!";
  }
}
}