package package2 {

public class TestMixinClient implements ITestMixin {

  public function TestMixinClient(thing:String) {
    mix(thing);
  }

  /** @inheritDoc */
  public native function mix(thing:String):String;
}
}