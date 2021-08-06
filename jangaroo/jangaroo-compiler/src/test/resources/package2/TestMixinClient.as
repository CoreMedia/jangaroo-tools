package package2 {

public class TestMixinClient implements ITestMixin {

  [ExtConfig]
  public var thing: String;

  public function TestMixinClient(config: TestMixinClient = null) {
    mix(config.thing);
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