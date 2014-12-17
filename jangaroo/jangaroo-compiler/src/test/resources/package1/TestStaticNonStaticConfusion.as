package package1 {
public class TestStaticNonStaticConfusion {

  public function TestStaticNonStaticConfusion() {
    foo();
    this.foo();
    TestStaticNonStaticConfusion.foo();
  }

  public static function foo():String {
    return "static foo";
  }

  private function foo():String {
    return "foo";
  }
}
}