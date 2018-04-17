package package2 {
public class TestMixinConfig {

  [MixinHook(before="foo")]
  private function doSomethingBeforeFoo() {
    // gotcha before foo!
  }

  [MixinHook(after="foo")]
  protected function doSomethingAfterFoo() {
    // gotcha after foo!
  }

  [MixinHook(before="bar")]
  public function doSomethingBeforeBar() {
    // gotcha before bar!
  }

  [MixinHook(on="bar")]
  public function doSomethingOnBar() {
    // gotcha on bar!
  }

  [MixinHook("baz")]
  public function doSomethingOnBaz() {
    // gotcha on baz!
  }

  [MixinHook(before="one", before="two", after="three", on="four", "five")]
  public function doSomethingOnMany() {
    // gotcha on many!
  }

  [MixinHook(before="six")]
  [MixinHook(after="seven")]
  public function doSomethingOnBoth() {
    // gotcha on both!
  }

  [MixinHook(extended)]
  private static function extendClass(baseClass:Class, derivedClass:Class, classBody: Object):void {
    // gotcha extended!
  }
}
}