package package2 {

public class TestMixinClient implements ITestMixin {

  public function TestMixinClient(thing:String) {
    mix(thing);
  }

  /** @inheritDoc */
  [Bindable]
  public native function get foo():Number;

  /** @private */
  [Bindable]
  public native function set foo(value:Number):void;

  /** @inheritDoc */
  public native function mix(thing:String):String;
}
}